package org.d2j.utils;

/**
 * User: Blackrush
 * Date: 23/11/11
 * Time: 16:58
 * IDE : IntelliJ IDEA
 */
public class Point {
    private int x, y;

    public Point() {
        this.x = 0;
        this.y = 0;
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Point)
            return equals((Point)obj);
        return false;
    }

    public boolean equals(Point point){
        if (point == null)
            return false;

        return point.x == this.x && point.y == this.y;
    }

    @Override
    public int hashCode() {
        return x ^ y;
    }

    @Override
    public String toString() {
        return "(" + x + ";" + y + ")";
    }
}
