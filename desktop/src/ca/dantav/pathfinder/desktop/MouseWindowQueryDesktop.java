package ca.dantav.pathfinder.desktop;

import org.lwjgl.input.Mouse;

import ca.dantav.pathfinder.MouseWindowQuery;

public class MouseWindowQueryDesktop implements MouseWindowQuery {

    @Override
    public boolean isMouseInsideWindow() {
        return Mouse.isInsideWindow();
    }

}
