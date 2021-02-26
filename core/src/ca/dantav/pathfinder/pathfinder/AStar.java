package ca.dantav.pathfinder.pathfinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.dantav.pathfinder.BlockEntity;
import ca.dantav.pathfinder.screen.MapScreen;

public class AStar extends Pathfinding {

    public class Node {

        private BlockEntity entity;

        private Node parent;

        private float f;

        private float h;

        private float g;

        public Node(BlockEntity entity) {
            this.entity = entity;
        }

        @Override
        public boolean equals(Object other) {
            if(!(other instanceof Node)) {
                return false;
            }

            Node otherNode = (Node) other;
            return entity.equals(otherNode.getEntity());
        }

        public float getF() {
            return f;
        }

        public void setF(float f) {
            this.f = f;
        }

        public float getH() {
            return h;
        }

        public void setH(float h) {
            this.h = h;
        }

        public float getG() {
            return g;
        }

        public void setG(float g) {
            this.g = g;
        }

        public BlockEntity getEntity() {
            return entity;
        }

        public Node getParent() {
            return parent;
        }

        public void setParent(Node parent) {
            this.parent = parent;
        }
    }

    private Map<Integer, Node> openList = new HashMap<>();

    private Map<Integer, Node> closedList = new HashMap<>();

    private Node start;

    private Node end;

    public AStar(MapScreen screen, BlockEntity start, BlockEntity end) {
        super(screen, start, end);
        this.start = new Node(start);
        this.end = new Node(end);
    }

    @Override
    public void compute() {
        openList.put(start.getEntity().getId(), start);

        while(!openList.isEmpty()) {
            Node curr = findLowestF();

            openList.remove(curr.getEntity().getId());
            closedList.put(curr.getEntity().getId(), curr);

            if(curr.getEntity().equals(getEnd())) {
                List<BlockEntity> route = new ArrayList<>();
                Node back = curr;
                while(back != null) {
                    route.add(back.getEntity());
                    back = back.getParent();
                }
                setRoute(route);
                return;
            }

            List<Node> children = new ArrayList<>();
            List<BlockEntity> neighbours = curr.getEntity().getNeighbours(getScreen());

            for(BlockEntity neighbour : neighbours) {

                if(neighbour.getBlockType().equals(BlockEntity.BlockType.BARRICADE)) {
                    continue;
                }

                Node newNode = new Node(neighbour);
                newNode.setParent(curr);
                children.add(newNode);
            }

            for(Node childNode : children) {

                if(closedList.containsKey(childNode.getEntity().getId())) {
                    continue;
                }

                childNode.setG(curr.getG() + getDistance(childNode, curr));
                childNode.setH(getDistance(childNode, end));
                childNode.setF(childNode.getG() + childNode.getH());

                if(openList.containsKey(childNode.getEntity().getId())) {
                    Node c = openList.get(childNode.getEntity().getId());
                    if(childNode.getG() > c.getG()) {
                        continue;
                    }
                }

                openList.put(childNode.getEntity().getId(), childNode);


            }
        }
    }

    public Node findLowestF() {
        Node lowest = null;
        for(Node n : openList.values()) {
            if(lowest == null || n.getF() < lowest.getF()) {
                lowest = n;
            }
        }
        return lowest;
    }

    public float getDistance(Node one, Node two) {
        float distX = Math.abs(one.getEntity().getX() - two.getEntity().getX());
        float distY = Math.abs(one.getEntity().getY() - two.getEntity().getY());
        return distX*distX + distY*distY;
    }
}
