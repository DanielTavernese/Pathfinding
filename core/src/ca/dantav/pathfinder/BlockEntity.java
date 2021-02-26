package ca.dantav.pathfinder;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ca.dantav.pathfinder.screen.MapScreen;
import ca.dantav.pathfinder.sprite.SpriteActor;
import jdk.nashorn.internal.ir.Block;

public class BlockEntity extends Entity {

    private int id;

    public enum BlockType {
        BARRICADE,
        FLOOR,
        ENTER,
        EXIT,
        PATH;
    }

    private BlockType blockType;

    public BlockEntity(MapScreen mapScreen, SpriteActor spriteActor, BlockType blockType, int id) {
        super(mapScreen, spriteActor);
        this.blockType = blockType;
        this.id = id;
    }

    public void setBlockType(BlockType blockType) {
        Sprite sprite = null;
        switch(blockType) {
            case FLOOR:
                sprite = new Sprite(getMapScreen().getPathfinder().getAssets().get("floor", Texture.class));
                break;
            case BARRICADE:
                sprite = new Sprite(getMapScreen().getPathfinder().getAssets().get("barricade", Texture.class));
                break;
            case ENTER:
                sprite = new Sprite(getMapScreen().getPathfinder().getAssets().get("player", Texture.class));
                break;
            case EXIT:
                sprite = new Sprite(getMapScreen().getPathfinder().getAssets().get("exit", Texture.class));
                break;
            case PATH:
                sprite = new Sprite(getMapScreen().getPathfinder().getAssets().get("path", Texture.class));
                break;
        }
        this.blockType = blockType;
        getSpriteActor().setSprite(sprite);
    }

    @Override
    public boolean equals(Object other) {
        if(!(other instanceof BlockEntity)) {
            return false;
        }

        BlockEntity otherblock = (BlockEntity) other;
        return id == otherblock.getId();
    }

    public List<BlockEntity> getNeighbours(MapScreen screen) {
        List<BlockEntity> neighbours = new ArrayList<>();

        addNeighbour(neighbours, screen.getFloor(), new Location(getX() + 32, getY()));
        addNeighbour(neighbours, screen.getFloor(), new Location(getX() - 32, getY()));
        addNeighbour(neighbours, screen.getFloor(), new Location(getX(), getY() + 32));
        addNeighbour(neighbours, screen.getFloor(), new Location(getX(), getY() - 32));


        return neighbours;
    }

    public void addNeighbour(List<BlockEntity> neighbours, Map<Location, BlockEntity> map, Location location) {
        if(map.containsKey(location)) {
            neighbours.add(map.get(location));
        }
    }

    public BlockType getBlockType() {
        return blockType;
    }

    public int getId() {
        return id;
    }
}
