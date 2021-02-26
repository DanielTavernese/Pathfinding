package ca.dantav.pathfinder;

import com.badlogic.gdx.assets.AssetManager;

import java.util.HashMap;
import java.util.Map;

public final class Assets {

    private AssetManager assetManager;

    private Map<String, String> nameMap = new HashMap<String, String>();

    public Assets() {
        assetManager = new AssetManager();
    }

    public <T> T get(String name, Class<T> type) {
        String path = nameMap.get(name);
        return assetManager.get(path, type);
    }

    public void addToNameMap(String name, String path) {
        nameMap.put(name, path);
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }
}
