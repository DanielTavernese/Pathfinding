package ca.dantav.pathfinder;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

import ca.dantav.pathfinder.screen.MapScreen;
import ca.dantav.pathfinder.sprite.SpriteActor;

public class Entity {

    private SpriteActor spriteActor;

    private MapScreen mapScreen;

    public Entity(MapScreen mapScreen, SpriteActor spriteActor) {
        this.mapScreen = mapScreen;
        this.spriteActor = spriteActor;
    }

    public void update() {

    }

    public void register() {
        mapScreen.getStage().addActor(spriteActor);
    }

    public void unregister() {
        spriteActor.remove();
    }

    public void draw(ShapeRenderer shapeRenderer, Camera camera) {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        getSpriteActor().drawDebug(shapeRenderer);
        shapeRenderer.end();

    }

    public boolean overlaps (Entity entity) {
        return overlaps(entity.getSpriteActor());
    }

    public boolean overlaps (SpriteActor other) {
        return getX() < other.getX() + other
                .getWidth() && getX() + spriteActor.getWidth() > other.getX() &&
                getY() < other.getY() + other.getHeight() && getY() + spriteActor.getHeight() > other.getY();
    }

    public boolean overlaps (float x, float y) {
        return getX() < x && getX() + spriteActor.getWidth() > x &&
                getY() < y && getY() + spriteActor.getHeight() > y;
    }

    public void transform(float x, float y) {
        setPosition(x + getX(), y + getY());
    }

    public SpriteActor getSpriteActor() {
        return spriteActor;
    }

    public void setPosition(float x, float y) {
        spriteActor.setPosition(x, y);
    }

    public MapScreen getMapScreen() {
        return mapScreen;
    }

    public void setSpriteActor(SpriteActor spriteActor) {
        this.spriteActor = spriteActor;
    }

    public float getX() {
        return spriteActor.getX();
    }

    public float getY() {
        return spriteActor.getY();
    }


}
