package byog.WorldEngine;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class VerticalHall extends Hallway {

    /**
     * A constructor for VerticalHall
     * @param start the coordinate of the starting point of the hallway
     * @param len length of the hallway (random length)
     * @param dir direction indicator
     *            +1: hallway extending upwards
     *            -1: hallway extending downwards
     */
    public VerticalHall(Coordinate start, int len, int dir) {
        this.startPos = start;
        length = len;
        direction = dir;
        this.end = new Coordinate(start.getX(), start.getY() + (length - 1) * direction);
    }

    /**
     * Setting correct tiles in the world grid
     * @param world 2D TETile Array representing the random generated world
     */
    @Override
    public void setHallway(TETile[][] world) {
        int xPos = startPos.getX();
        int yPos = startPos.getY();
        for (int i = 0; i < length; i++) {
            world[xPos][yPos + i * direction] = Tileset.FLOOR;
            if (world[xPos + 1][yPos + i * direction] != Tileset.FLOOR) {
                world[xPos + 1][yPos + i * direction] = Tileset.WALL;
            }
            if (world[xPos - 1][yPos + i * direction] != Tileset.FLOOR) {
                world[xPos - 1][yPos + i * direction] = Tileset.WALL;
            }
        }
    }

}
