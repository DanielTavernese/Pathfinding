package ca.dantav.pathfinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EntityManager {

    private List<Entity> entityList = new ArrayList<Entity>();

    public void register(Entity e) {
        entityList.add(e);
        e.register();
    }

    public void unregister(Entity e) {
        entityList.remove(e);
        e.unregister();
    }

    public List<Entity> getEntityList() {
        return Collections.unmodifiableList(new ArrayList<>(entityList));
    }

}
