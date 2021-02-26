package ca.dantav.pathfinder.sprite;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import java.util.concurrent.atomic.AtomicBoolean;

public class SpriteActor extends Actor {

    Sprite sprite;

    private float boundsWidth;

    private float boundsHeight;

    public SpriteActor(Sprite sprite) {
        this.sprite = sprite;
        this.boundsWidth = sprite.getWidth();
        this.boundsHeight = sprite.getHeight();

        //setDebug(true);
        setPosition(sprite.getX(), sprite.getY());
        setTouchable(Touchable.enabled);
    }

    public void setPosition(float x, float y){
        sprite.setPosition(x, y);
        setBounds(sprite.getX(), sprite.getY(), boundsWidth, boundsHeight);
    }

    public void setBounds(float boundsWidth, float boundsHeight) {
        this.boundsHeight = boundsHeight;
        this.boundsWidth = boundsWidth;
    }

    public boolean overlaps() {

        AtomicBoolean hit = new AtomicBoolean(false);

        for(Actor other : getStage().getActors()) {
            if(getX() < other.getX() + other
                    .getWidth() && getX() + getWidth() > other.getX() &&
                    getY() < other.getY() + other.getHeight() && getY() + getHeight() > other.getY()) {
                hit.set(true);
                break;
            }
        }

        return hit.get();
    }


    public void onHover() {
        System.out.println("yes");
    }
    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        sprite.draw(batch);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        sprite.setPosition(this.sprite.getX(), this.sprite.getY());
        this.sprite = sprite;
    }
}

