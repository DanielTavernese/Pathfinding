package ca.dantav.pathfinder;

import java.util.Objects;

public class Location {

    private float x;

    private float y;

    public Location(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object other) {
        if(!(other instanceof Location)) {
            return false;
        }

        Location otherLocation = (Location) other;
        return x == otherLocation.getX() && y == otherLocation.getY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
