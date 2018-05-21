package byog.WorldEngine;


import java.io.Serializable;

/**
 * A class to organize all coordinates of objects in
 * 2D Array-based world
 * A coordinate object contains the x coordinate and y coordinate
 */
public class Coordinate implements Comparable<Coordinate>, Serializable {
    private int x;
    private int y;

    /**
     * Default constructor of a coordinate object
     */
    public Coordinate() {
        x = 0;
        y = 0;
    }

    /**
     * Constructor of a coordinate object
     * @param x x coordinate
     * @param y y coordinate
     */
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Copy constructor of a coordinate
     * @param p coordinate to be copied
     */
    public Coordinate(Coordinate p) {
        this.x = p.x;
        this.y = p.y;
    }

    /**
     * Resets the x coordinate of the Coordinate object
     * @param x new x coordinate to be changed
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Resets the y coordinate of the Coordinate object
     * @param y new y coordinate to be changed
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * gets the x coordinate from a coordinate object
     * @return the x coordinate
     */
    public int getX() {
        return this.x;
    }

    /**
     * gets the y coordinate from a coordinate object
     * @return the y coordinate
     */
    public int getY() {
        return this.y;
    }

    /**
     * Compares two coordinates
     * @param otherPos the other coordinate to be compared
     * @return a positive number if THIS has a larger Manhattan distance
     *         a negative number if THIS has a smaller Manhattan distance
     *         0 if same
     */
    public int compareTo(Coordinate otherPos) {
        return (this.x - otherPos.x) + (this.y - otherPos.y);
    }

    /**
     * Compares if two coordinates represents the same point
     * @param otherPos
     * @return true only if x and y coordinates are same
     */
    public boolean equals(Coordinate otherPos) {
        return (this.x == otherPos.x) && (this.y == otherPos.y);
    }

    /**
     * Returns a string that represents a specific coordinate in the format: "(a, b)"
     * @return a string
     */
    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }

}
