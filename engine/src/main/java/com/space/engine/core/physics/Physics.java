package com.space.engine.core.physics;


import com.github.stephengold.joltjni.*;
import com.github.stephengold.joltjni.enumerate.EActivation;
import com.github.stephengold.joltjni.enumerate.EMotionQuality;
import com.github.stephengold.joltjni.enumerate.EMotionType;
import com.github.stephengold.joltjni.enumerate.EOverrideMassProperties;
import com.github.stephengold.joltjni.readonly.ConstShape;
import com.space.engine.core.Debug;
import com.space.engine.core.actors.components.RigidBodyComponent;
import com.space.engine.core.math.Transform;
import com.space.engine.core.physics.shapes.BoxShapeDefinition;
import com.space.engine.core.physics.shapes.ShapeDefinition;
import com.space.engine.core.util.JoltNativeLibrary;

public final class Physics {
    public static final int MAX_BODIES       = 1000;
    public static final int NUM_BODY_MUTEXES = 0;//use default value
    public static final int MAX_BODY_PAIRS   = 65536;
    public static final int MAX_CONTACTS     = 20480;

    public static final int NUM_BP_LAYERS = 1;
    public static final int NUM_OP_LAYERS = 2;

    public static final int OP_LAYER_STATIC  = 1;
    public static final int OP_LAYER_DYNAMIC = 0;

    private static final Physics INSTANCE = new Physics();

    private final int numWorkerThreads = Runtime.getRuntime().availableProcessors();

    private float gravity;

    private JobSystem     jobSystem;
    private TempAllocator tempAllocator;
    private PhysicsSystem physicsSystem;

    public static Physics get() {
        return INSTANCE;
    }

    public boolean init(float gravity) {
        this.gravity = Math.abs(gravity);

        try {
            JoltNativeLibrary.load();
        } catch (Exception e) {
            //todo log
            return false;
        }

        JoltPhysicsObject.startCleaner();
        Jolt.registerDefaultAllocator();
        Jolt.installDefaultAssertCallback();
        Jolt.installDefaultTraceCallback();
        Jolt.setTraceAllocations(true);

        if (!Jolt.newFactory()) {
            return false;
        }

        Jolt.registerTypes();

        tempAllocator = new TempAllocatorMalloc();
        jobSystem     = new JobSystemThreadPool(Jolt.cMaxPhysicsJobs, Jolt.cMaxPhysicsBarriers, numWorkerThreads);

        physicsSystem = new PhysicsSystem();

        ObjectLayerPairFilterTable         ovoFilter = new ObjectLayerPairFilterTable(NUM_OP_LAYERS);

        ovoFilter.enableCollision(OP_LAYER_DYNAMIC, OP_LAYER_DYNAMIC);
        ovoFilter.enableCollision(OP_LAYER_DYNAMIC, OP_LAYER_STATIC);
        ovoFilter.disableCollision(OP_LAYER_STATIC, OP_LAYER_STATIC);

        BroadPhaseLayerInterfaceTable      layerMap  = new BroadPhaseLayerInterfaceTable(NUM_OP_LAYERS, NUM_BP_LAYERS);

        layerMap.mapObjectToBroadPhaseLayer(OP_LAYER_DYNAMIC, 0);
        layerMap.mapObjectToBroadPhaseLayer(OP_LAYER_STATIC, 0);

        ObjectVsBroadPhaseLayerFilterTable ovbFilter = new ObjectVsBroadPhaseLayerFilterTable(layerMap, NUM_BP_LAYERS, ovoFilter, NUM_OP_LAYERS);

        physicsSystem.init(
                MAX_BODIES,
                NUM_BODY_MUTEXES,
                MAX_BODY_PAIRS,
                MAX_CONTACTS,
                layerMap,
                ovbFilter,
                ovoFilter
        );

        physicsSystem.setGravity(0, -this.gravity, 0);

        physicsSystem.setContactListener(new CustomContactListener() {
            @Override
            public void onContactAdded(long body1Va, long body2Va, long manifoldVa, long settingsVa) {
                super.onContactAdded(body1Va, body2Va, manifoldVa, settingsVa);

                Debug.log(String.format("Contact! %d <=> %d", body1Va, body2Va));
            }
        });

        return true;
    }

    public void step(double delta) {
        double deltaSeconds      = delta / 1000;
        int    numCollisionSteps = 1;
        physicsSystem.update((float) deltaSeconds, numCollisionSteps, tempAllocator, jobSystem);
    }

    public void CreateBody(RigidBodyComponent rigidBody, Transform transform) {
        BodyInterface bi = physicsSystem.getBodyInterface();
        try (BodyCreationSettings bcs = new BodyCreationSettings()) {
            bcs.setShape(createShape(rigidBody.getShape()));
            bcs.setRestitution(rigidBody.getRestitution());
            bcs.setFriction(rigidBody.getFriction());
            bcs.setLinearDamping(rigidBody.getLinearDamping());
            bcs.setAngularDamping(rigidBody.getAngularDamping());

            bcs.getMassPropertiesOverride().setMass(rigidBody.getMass());
            bcs.setOverrideMassProperties(EOverrideMassProperties.CalculateInertia);

            bcs.setPosition(new RVec3(
                    transform.position.x,
                    transform.position.y,
                    transform.position.z
            ));

            bcs.setRotation(Quat.sEulerAngles(
                    transform.rotation.x,
                    transform.rotation.y,
                    transform.rotation.z
            ));

            switch (rigidBody.getMotionType()) {
                case DYNAMIC -> {
                    bcs.setMotionType(EMotionType.Dynamic);
                    bcs.setObjectLayer(OP_LAYER_DYNAMIC);
                    bcs.setMotionQuality(EMotionQuality.LinearCast);
                }
                case STATIC -> {
                    bcs.setMotionType(EMotionType.Static);
                    bcs.setObjectLayer(OP_LAYER_STATIC);
                }
            }

            rigidBody.setBody(bi.createBody(bcs));
        }
    }

    public void AddRigidBody(RigidBodyComponent rigidBody) {
        BodyInterface bi = physicsSystem.getBodyInterface();

        rigidBody.setBodyActive(true);

        bi.addBody(rigidBody.getBody(), EActivation.Activate);
    }

    public void RemoveRigidBody(RigidBodyComponent rigidBody) {
        BodyInterface bi = physicsSystem.getBodyInterface();

        rigidBody.setBodyActive(false);

        bi.removeBody(rigidBody.getBody().getId());

        rigidBody.setBody(null);
    }

    public void synchronizeRigidBody(RigidBodyComponent rigidBody, Transform transform) {
        if (rigidBody.getBody() == null) return;

        transform.position.set(
                rigidBody.getBody().getPosition().x(),
                rigidBody.getBody().getPosition().y(),
                rigidBody.getBody().getPosition().z()
        );

        transform.rotation.set(
                (float)Math.toDegrees(rigidBody.getBody().getRotation().getRotationAngle(Vec3.sAxisX())),
                (float)Math.toDegrees(rigidBody.getBody().getRotation().getRotationAngle(Vec3.sAxisY())),
                (float)Math.toDegrees(rigidBody.getBody().getRotation().getRotationAngle(Vec3.sAxisZ()))
        );
    }

    private ConstShape createShape(ShapeDefinition shapeDefinition) {
        return switch (shapeDefinition) {
            case BoxShapeDefinition boxShapeDefinition -> {
                BoxShapeDefinition box = (BoxShapeDefinition) shapeDefinition;

                yield new BoxShape(box.halfExtents().x, box.halfExtents().y, box.halfExtents().z);
            }
            default -> throw new IllegalStateException("Unexpected value: " + shapeDefinition);
        };
    }

}
