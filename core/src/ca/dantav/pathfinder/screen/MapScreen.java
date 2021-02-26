package ca.dantav.pathfinder.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;

import org.lwjgl.Sys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.soap.Text;

import ca.dantav.pathfinder.BlockEntity;
import ca.dantav.pathfinder.Entity;
import ca.dantav.pathfinder.Location;
import ca.dantav.pathfinder.Pathfinder;
import ca.dantav.pathfinder.pathfinder.Djkstra;
import ca.dantav.pathfinder.pathfinder.Pathfinding;
import ca.dantav.pathfinder.pathfinder.PathfindingType;
import ca.dantav.pathfinder.sprite.SpriteActor;
import jdk.nashorn.internal.ir.Block;

public class MapScreen extends GameScreen {

    /*
     * The hovered entities
     */
    private Set<Entity> hoveredEntities = new HashSet<>();

    /*
    The floor map
     */
    private Map<Location, BlockEntity> floor = new HashMap<>();

    /*
    The last pathfinding route
     */
    private List<BlockEntity> lastRoute = new ArrayList<>();

    /*
    The blocks horizontal
     */
    private int blocksHorizontal;

    /*
    The blocks vertical
     */
    private int blocksVertical;

    /*
    The player block entity
     */
    private BlockEntity player;

    /*
    The exit block entity
     */
    private BlockEntity exit;

    /*
    The button type
     */
    private enum ButtonType {
        BLOCK,
        ENTER,
        EXIT,
        REMOVE;
    }

    /*
    The button type
     */
    private ButtonType buttonType;

    /*
    The ui skin
     */
    private Skin skin;

    /*
    The ui table
     */
    private Table table;

    /*
    If dialog box open
     */
    private boolean inDialog;

    /**
     * Default constructor
     * <p>
     * Calls start function
     *
     * @param pathfinder
     */
    public MapScreen(Pathfinder pathfinder) {

        super(pathfinder);

        this.skin = new Skin(Gdx.files.internal("uiskin.json"));
    }

    @Override
    public void start() {
        table = new Table();
        table.setWidth(getStage().getWidth());
        table.align(Align.center|Align.top);

        table.setPosition(0, getStage().getHeight());

        TextButton placePlayerButton = new TextButton("Place Player", skin);
        TextButton placeBlockButton = new TextButton("Place Block", skin);
        TextButton placeExitButton = new TextButton("Place Exit", skin);
        TextButton placeRemoveButton = new TextButton("Remove Block", skin);

        TextButton calculateButton = new TextButton("Calculate", skin);

        TextButton clearButton = new TextButton("Clear", skin);

        Map<ButtonType, TextButton> buttonMap = new HashMap<>();

        buttonMap.put(ButtonType.BLOCK, placeBlockButton);
        buttonMap.put(ButtonType.ENTER, placePlayerButton);
        buttonMap.put(ButtonType.EXIT, placeExitButton);
        buttonMap.put(ButtonType.REMOVE, placeRemoveButton);

        Dialog dialog = new Dialog("Cannot reach destination.", skin, "dialog");

        table.defaults().expandX().fill().space(5f);

        placeBlockButton.pad(10);

        clearButton.setColor(Color.LIGHT_GRAY);
        calculateButton.setColor(Color.LIGHT_GRAY);

        calculateButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                buildChoiceDialog();
            }
        });

        clearButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clear();
            }
        });

        for(Map.Entry<ButtonType, TextButton> entry : buttonMap.entrySet()) {
            entry.getValue().setColor(Color.LIGHT_GRAY);
            entry.getValue().addListener(new ClickListener() {

                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if(buttonType != null && buttonType.equals(entry.getKey())) {
                        buttonType = null;
                    } else {
                        buttonType = entry.getKey();
                    }
                    for(Map.Entry<ButtonType, TextButton> entry2 : buttonMap.entrySet()) {

                        if(buttonType != null && buttonType.equals(entry2.getKey())) {
                            entry2.getValue().setColor(Color.RED);
                        } else {
                            entry2.getValue().setColor(Color.LIGHT_GRAY);
                        }
                    }
                }

            });
        }
        table.add(placeBlockButton, placePlayerButton, placeExitButton, placeRemoveButton, clearButton, calculateButton);

        renderFloor();

        getStage().addActor(table);

    }

    private void buildChoiceDialog() {
        Dialog dialog = new Dialog("Choose Algorithm", skin, "dialog");
        setInDialog(true);
        for(PathfindingType type : PathfindingType.values()) {
            TextButton textButton = new TextButton(type.getName(), skin);
            textButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    startAlgorithm(type.getPathfinder(MapScreen.this, player, exit));
                    dialog.remove();
                }
            });
            dialog.add(textButton);
        }
        dialog.show(getStage());
    }

    public void startAlgorithm(Pathfinding pathfinding) {
        for(BlockEntity e : lastRoute) {
            if(e.getBlockType().equals(BlockEntity.BlockType.PATH)) {
                e.setBlockType(BlockEntity.BlockType.FLOOR);
            }
        }
        lastRoute.clear();
        pathfinding.start();
        if(pathfinding.getRoute() == null || pathfinding.getRoute().isEmpty())  {
            return;
        }

        setInDialog(true);

        Timer.schedule(new Timer.Task() {


            @Override
            public void run() {
                if(pathfinding.getRoute().isEmpty() || player == null) {
                    this.cancel();
                    setInDialog(false);

                    return;
                }

                BlockEntity entity = pathfinding.getRoute().poll();
                if(!entity.equals(exit) && !entity.equals(player)) {
                    lastRoute.add(entity);
                    entity.setBlockType(BlockEntity.BlockType.PATH);
                }

            }
        },0,0.1f);
    }


    @Override
    public Sprite getBackground() {
        return null;
    }

    public void clearFloor() {
        for(BlockEntity e : floor.values()) {
            e.setBlockType(BlockEntity.BlockType.FLOOR);
        }
    }

    public void renderFloor() {
        blocksHorizontal = (int) getStage().getWidth() / 32 - 1;
        blocksVertical = (int) (getStage().getHeight() - 60) / 32;

        int id = 0;
        for(int i = 0; i <= blocksHorizontal; i++) {
            for(int j = 0; j <= blocksVertical; j++) {
                Sprite spriteFloor = new Sprite(getPathfinder().getAssets().get("floor", Texture.class));

                BlockEntity entity = new BlockEntity(this, new SpriteActor(spriteFloor), BlockEntity.BlockType.FLOOR, id) {

                    @Override
                    public void update() {

                        if(inDialog) {
                            return;
                        }

                        Vector2 adjustedPos = getStage().screenToStageCoordinates(new Vector2(
                                Gdx.input.getX(), Gdx.input.getY()
                        ));

                        if (overlaps(adjustedPos.x, adjustedPos.y) && getPathfinder().getMouseWindowQuery().isMouseInsideWindow()) {
                            getSpriteActor().getSprite().setScale(1.4f);
                            hoveredEntities.add(this);
                        } else {
                            getSpriteActor().getSprite().setScale(1f);
                            hoveredEntities.remove(this);
                        }

                        if(Gdx.input.isTouched() && overlaps(adjustedPos.x, adjustedPos.y) && getPathfinder().getMouseWindowQuery().isMouseInsideWindow()) {
                            if(buttonType == null) {
                                System.out.println(new Location(getX(), getY()));
                                return;
                            }
                            switch(buttonType) {
                                case REMOVE:
                                    if(getBlockType().equals(BlockType.BARRICADE)) {
                                        setBlockType(BlockType.FLOOR);
                                    }
                                    break;
                                case BLOCK:
                                    if(getBlockType().equals(BlockType.FLOOR) || getBlockType().equals(BlockType.PATH)) {
                                        setBlockType(BlockType.BARRICADE);
                                    }
                                    break;
                                case EXIT:
                                    if(exit != null) {
                                        exit.setBlockType(BlockType.FLOOR);
                                    }

                                    exit = this;
                                    exit.setBlockType(BlockType.EXIT);
                                    break;
                                case ENTER:
                                    if(player != null) {
                                        player.setBlockType(BlockType.FLOOR);
                                    }

                                    player = this;
                                    player.setBlockType(BlockType.ENTER);
                                    break;
                            }
                        }
                     }
                };

                entity.getSpriteActor().addListener(new ClickListener() {

                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                        if(entity.getBlockType().equals(BlockEntity.BlockType.BARRICADE)) {
                            entity.setBlockType(BlockEntity.BlockType.FLOOR);
                        }
                        return true;
                    }

                });
                entity.getSpriteActor().setPosition(i * 32, j * 32);
                floor.put(new Location(entity.getX(), entity.getY()), entity);
                getPathfinder().getEntityManager().register(entity);
                id++;
            }
        }
    }

    public void clear() {
        player = null;
        exit = null;
        clearFloor();
    }

    @Override
    public void resize() {
        for(Entity e : floor.values()) {
            e.getSpriteActor().remove();
            getPathfinder().getEntityManager().unregister(e);
        }
        renderFloor();
        table.setPosition(0, getStage().getHeight());
        getStage().addActor(table);
    }

    @Override
    public void render(ShapeRenderer shapeRenderer) {
        super.render(shapeRenderer);

        for(Entity e : hoveredEntities) {
            getStage().getBatch().begin();
            e.getSpriteActor().draw(getStage().getBatch(), 1.0f);
            getStage().getBatch().end();
        }
    }

    @Override
    public void back() {

    }

    public Map<Location, BlockEntity> getFloor() {
        return floor;
    }

    public int getBlocksHorizontal() {
        return blocksHorizontal;
    }

    public int getBlocksVertical() {
        return blocksVertical;
    }

    public Skin getSkin() {
        return skin;
    }

    public boolean isInDialog() {
        return inDialog;
    }

    public void setInDialog(boolean inDialog) {
        this.inDialog = inDialog;
    }
}
