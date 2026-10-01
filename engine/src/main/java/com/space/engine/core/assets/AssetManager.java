package com.space.engine.core.assets;

import com.space.engine.core.Debug;
import com.space.engine.core.assets.data.AssetData;
import com.space.engine.core.exceptions.AssetException;
import com.space.engine.core.exceptions.AssetLoadFail;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

//in debug modes all assets are in text format, but when packing for release, pack everything into binary format.
public class AssetManager implements AssetContext {

    private final Map<String, RegisteredType> typesMap     = new HashMap<>();
    private final Set<AssetId>                loadingStack = new HashSet<>();

    private final AssetSource     source;
    private final AssetSerializer serializer;

    public AssetManager(Properties gameProperties) {
        source     = new FileAssetSource( //todo: pack asset source in release
                Path.of((String) gameProperties.getOrDefault("assets.root", "./assets"))
        );
        serializer = new JsonAssetSerializer(); //todo: binary asset serializer in release
    }

    public <D extends AssetData, O> void register(String type, Class<D> dataType, AssetFactory<D, O> factory) {
        typesMap.put(type.toLowerCase(), new RegisteredType(dataType, factory));
    }

    public <D extends AssetData, O> O load(AssetId id) throws AssetException {

        if (loadingStack.contains(id)){
            throw new AssetException("Circular dependency detected when loading asset: " + id);
        }

        String type = id.extension();
        try {
            byte[] bytes;
            try {
                bytes = source.read(id);
            } catch (IOException e) {
                String msg = String.format("Failed to load asset(%s): %s", type, id);
                Debug.err(msg);
                throw new AssetLoadFail(e.getMessage(), e);
            }

            var registered = typesMap.get(type);

            if (registered == null) {
                throw new AssetException("No factory registered for " + type);
            }

            loadingStack.add(id);

            @SuppressWarnings("unchecked")
            AssetFactory<D, O> factory = (AssetFactory<D, O>) registered.factory();
            @SuppressWarnings("unchecked")
            D decoded = (D) serializer.decode(bytes, registered.type, id);

            return factory.create(decoded, this);
        } catch (ClassCastException e) {
            throw new AssetException("Failed to cast factory types", e);
        }finally {
            loadingStack.remove(id);
        }
    }

    private record RegisteredType(
            Class<? extends AssetData> type,
            AssetFactory<? extends AssetData, ?> factory
    ) {

    }
}
