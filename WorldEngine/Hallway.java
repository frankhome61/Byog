package byog.WorldEngine;
import byog.TileEngine.TETile;

abstract class Hallway {
    protected Coordinate startPos;
    protected Coordinate end;
    protected int length;
    protected int direction;

    /**
     * Default constructor of the Hallway abstract class
     */
    public Hallway() {
        startPos = null;
        length = 0;
    }

    /**
     * A constructor for VerticalHall
     * @param start the coordinate of the starting point of the hallway
     * @param len length of the hallway (random length)
     * @param dir direction indicator
     *            +1: hallway extending upwards
     *            -1: hallway extending downwards
     */
    public Hallway(Coordinate start, int len, int dir) {
        this.startPos = start;
        length = len;
        direction = dir;
    }

    /**
     * Abstract method of generating hallways
     * To be overriden by subclasses
     * @param world A 2D TETile Array representing the random generated world
     */
    abstract void setHallway(TETile[][] world);

    /**
     * Returns the end point (exit) of the hallway
     */
    public Coordinate getEnd() {
        return this.end;
    }
}
