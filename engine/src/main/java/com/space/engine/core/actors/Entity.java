package com.space.engine.core.actors;

import com.space.engine.core.actors.components.ActorComponent;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public abstract class Entity {
    private UUID    uid      = UUID.randomUUID();
    @Setter
    @Getter
    private boolean isActive = true;

    private final HashMap<Class<?>, ActorComponent> components = new HashMap<>();

    public <T extends ActorComponent> T getComponent(Class<T> clazz) {
        return clazz.cast(components.get(clazz));
    }

    public Collection<ActorComponent> getComponents() {
        return components.values();
    }

    public boolean hasComponent(Class<? extends ActorComponent> clazz) {
        return components.containsKey(clazz);
    }

    public void addComponent(ActorComponent comp) {
        comp.setOwner(this);
        components.put(comp.getClass(), comp);
    }

    public void activate() {
        setActive(true);
    }

    public void deactivate() {
        setActive(false);
    }

}
