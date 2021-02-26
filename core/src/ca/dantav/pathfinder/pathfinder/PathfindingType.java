package ca.dantav.pathfinder.pathfinder;

import ca.dantav.pathfinder.BlockEntity;
import ca.dantav.pathfinder.screen.MapScreen;
import jdk.nashorn.internal.ir.Block;

public enum PathfindingType {

    DJKSTRA("Djkstra") {
        @Override
        public Pathfinding getPathfinder(MapScreen screen, BlockEntity start, BlockEntity end) {
            return new Djkstra(screen, start, end);
        }
    },

    ASTAR("A-Star") {
        @Override
        public Pathfinding getPathfinder(MapScreen screen, BlockEntity start, BlockEntity end) {
            return new AStar(screen, start, end);
        }
    };

    String name;

    PathfindingType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract Pathfinding getPathfinder(MapScreen screen, BlockEntity start, BlockEntity end);
}
