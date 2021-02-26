package ca.dantav.pathfinder.pathfinder;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import org.lwjgl.Sys;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ca.dantav.pathfinder.BlockEntity;
import ca.dantav.pathfinder.screen.MapScreen;

public class Djkstra extends Pathfinding {

    private List<BlockEntity> candidates;

    private boolean reached[];

    private int cost[];

    private int estimate[];

    private int size;

    private BlockEntity predecessor[];


    /**
     * @param start to start
     * @param end to end
     *
     * Sets up arrays depending on size of total paths
     */
    public Djkstra(MapScreen screen, BlockEntity start, BlockEntity end) {
        super(screen, start, end);
        this.size = screen.getFloor().size();
        this.candidates = new ArrayList<BlockEntity>(size);
        this.cost = new int[size];
        this.estimate = new int[size];
        this.reached = new boolean[size];
        this.predecessor = new BlockEntity[size];
    }

    /**
     * @return the vertex that has the largest path from starting point
     */
    @Override
    public void compute() {
        //results of start point being reached right from beginning
        cost[getStart().getId()] = 0;
        reached[getStart().getId()] = true;

        List<BlockEntity> edges = getStart().getNeighbours(getScreen());

        Iterator<BlockEntity> iterator = edges.iterator();

        //factor in estimates of neighbour vertices of start vertex
        while(iterator.hasNext()) {
            BlockEntity blockEntity = iterator.next();

            if(blockEntity.getBlockType().equals(BlockEntity.BlockType.BARRICADE)) {
                estimate[blockEntity.getId()] = 10000;
                continue;
            }

            estimate[blockEntity.getId()] = 1;
            candidates.add(blockEntity);
            predecessor[blockEntity.getId()] = getStart();
        }

        //If not neighbour of start vertex set estimate to very large for now
        for(int i = 0; i < estimate.length; i++) {
            if(estimate[i] == 0) {
                estimate[i] = 10000;
            }
        }

        //the estimate of start vertex should be 0 as cost 0
        estimate[getStart().getId()] = 0;

        //current vertex
        BlockEntity vertex = null;

        while(!candidates.isEmpty()) {

            //grab a vertex out of candidate list
            vertex = candidates.get(0);

            //pick which vertex to work on
            for (int i = 1; i < candidates.size(); i++) {
                if (estimate[candidates.get(i).getId()] < estimate[vertex.getId()]) {
                    vertex = candidates.get(i);
                }
            }

            cost[vertex.getId()] = estimate[vertex.getId()];

            //if vertex is the end vertex no need to calculate more
            if(vertex == getEnd()) {
                break;
            }

            //look at neighbours of current vertex
            List<BlockEntity> neighbours = vertex.getNeighbours(getScreen());

            if (!neighbours.isEmpty()) {
                iterator = neighbours.iterator();

                while(iterator.hasNext()) {
                    BlockEntity e = iterator.next();

                    if(e == null) {
                        continue;
                    }
                    //if vertex already reached continue on
                    if(reached[e.getId()]) {
                        continue;
                    }

                    if(e.getBlockType().equals(BlockEntity.BlockType.BARRICADE)) {
                        continue;
                    }

                    //if neigbour vertex not part of candidates add it in
                    if(!candidates.contains(e)) {
                        candidates.add(e);
                    }

                    //set the destination weight to a new better flight path
                    estimate[e.getId()] = 1;
                    predecessor[e.getId()] = vertex;
                }
            }

            //remove vertex from candidate list as its been dealt with
            for(int i = 0; i < candidates.size(); i++) {
                if(candidates.get(i) == vertex) {
                    candidates.remove(i);
                }
            }

            reached[vertex.getId()] = true;
        }

        getRoute(vertex);
    }


    private void getRoute(BlockEntity vertex) {

        List<BlockEntity> route = new ArrayList<>();

        if(vertex == getEnd()) {

            while(vertex != getStart()) {

                List<BlockEntity> neighbours = predecessor[vertex.getId()].getNeighbours(getScreen());

                Iterator<BlockEntity> iterator = neighbours.iterator();

                while(iterator.hasNext()) {
                    BlockEntity edge = iterator.next();

                    if(edge.equals(vertex) && cost[edge.getId()] == 1) {
                        route.add(edge);
                        break;
                    }
                }
                vertex = predecessor[vertex.getId()];
            }
        }

        setRoute(route);



    }



    public int[] getCost() {
        return cost;
    }



}
