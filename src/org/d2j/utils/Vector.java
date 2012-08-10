package org.d2j.utils;

/**
 * User: Blackrush
 * Date: 23/12/11
 * Time: 11:15
 * IDE : IntelliJ IDEA
 */
public class Vector {
    public static Vector fromPoints(Point p1, Point p2){
        return new Vector(
                p2.getX() - p1.getX(),
                p2.getY() - p1.getY()
        );
    }

    private int x, y;

    public Vector() {

    }

    public Vector(int x, int y) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector vector = (Vector) o;

        return !(x != vector.x || y != vector.y);

    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public String toString() {
        return "(" + x + ";" + y + ")";
    }

    public boolean collinear(Vector vector){
        return (x * vector.y - y * vector.x) == 0;
    }

    private double getLambda(Vector that){
        if (this.x != 0 || that.x != 0){
            if (this.x != 0) return that.x / this.x;
            else if (that.x != 0) return this.x / that.x;
            else return 0.0;
        }
        else if (this.y != 0 || that.y != 0){
            if (this.y != 0) return that.y / this.y;
            else if (that.y != 0) return this.y / that.y;
            else return 0.0;
        }
        else{
            return 0.0;
        }
    }

    public boolean sameDirection(Vector vector){
        return collinear(vector) && getLambda(vector) > 0;
    }
}
