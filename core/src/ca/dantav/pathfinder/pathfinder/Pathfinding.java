package ca.dantav.pathfinder.pathfinder;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

import ca.dantav.pathfinder.BlockEntity;
import ca.dantav.pathfinder.screen.MapScreen;
import jdk.nashorn.internal.ir.Block;

public abstract class Pathfinding {

    private MapScreen screen;

    private BlockEntity start;

    private BlockEntity end;

    private Queue<BlockEntity> route;

    public Pathfinding(MapScreen screen, BlockEntity start, BlockEntity end) {
        this.screen = screen;
        this.start = start;
        this.end = end;
    }

    public void start() {
        if(start == null) {
            showDialog("No starting point.");
            return;
        }

        if(end == null) {
            showDialog("No ending point.");
            return;
        }

        compute();

        if(route.isEmpty()) {
            showDialog("No Possible path.");
        }
    }

    public void showDialog(String errorMessage) {
        Dialog dialog = new Dialog(errorMessage, screen.getSkin(), "dialog");
        TextButton textButton = new TextButton("Okay", screen.getSkin());
        screen.setInDialog(true);

        textButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialog.remove();
                screen.setInDialog(false);
            }
        });

        dialog.add(textButton).align(Align.center);
        dialog.show(screen.getStage());
    }

    public abstract void compute();



    public BlockEntity getStart() {
        return start;
    }

    public BlockEntity getEnd() {
        return end;
    }

    public Queue<BlockEntity> getRoute() {
        return route;
    }

    public MapScreen getScreen() {
        return screen;
    }

    public void setRoute(List<BlockEntity> route) {
        this.route = new ArrayDeque<BlockEntity>();
        for(int i = route.size()-1; i >= 0; i--) {
            this.route.add(route.get(i));
        }
    }
}
