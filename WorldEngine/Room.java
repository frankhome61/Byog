package byog.WorldEngine;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Random;

/**
 * A Room class that manage all room objects
 */
public class Room implements Comparable<Room>, Serializable {
    private static int EXITLIMIT = 4;
    private int height;      // y axis
    private int width;     // x axis
    private Coordinate leftBottomPos;
    private static Random RANDOM;

    /**
     * Default constructor of the room
     */
    public Room() {
        this.height = 0;
        this.width = 0;
        this.leftBottomPos = null;
    }

    /**
     * Constructor of the room object
     * @param width width of the room
     * @param height height of the room
     * @param pos coordinate of the left bottom corner of the room
     * @param random random seed
     */
    public Room(int width, int height, Coordinate pos, Random random) {
        //this.id = id;
        this.width = width;
        this.height = height;
        this.leftBottomPos = pos;
        this.RANDOM = random;
    }


    /**
     * Returns true if ROOM is overlapping with THIS room
     * returns false otherwise
     * @param room a room instance to be determined whether
     *             is overlapping with THIS roomor not
     */
    public boolean isOverlapping(Room room) {
        Coordinate roomBottomLeft1 = this.leftBottomPos;
        Coordinate roomBottomLeft2 = room.leftBottomPos;
        int leftBotX1 = roomBottomLeft1.getX();
        int leftBotX2 = roomBottomLeft2.getX();
        int leftBotY1 = roomBottomLeft1.getY();
        int leftBotY2 = roomBottomLeft2.getY();

        int roomHeight1 = this.height;
        int roomHeight2 = room.height;
        int roomWidth1 = this.width;
        int roomWidth2 = room.width;

        int xLeftBound = leftBotX1 - roomWidth2;
        int xRightBound = leftBotX1 + roomWidth1;
        int yLowerBound = leftBotY1 - roomHeight2;
        int yUpperBound = leftBotY1 + roomHeight1;
        return leftBotX2 > xLeftBound && leftBotX2 < xRightBound
                && leftBotY2 > yLowerBound && leftBotY2 < yUpperBound;
    }

    /**
     * Seperated two rooms if they are overlapping with each other
     * @param room a room instance to be determined whether
     *             is overlapping with THIS room or not
     * @param random A Random instance
     * @param worldWidth width of the world
     * @param worldHeight height of the world
     */
    public void seperateRoom(Room room, Random random, int worldWidth, int worldHeight) {
        while (this.isOverlapping(room)) {
            int xPos = random.nextInt(worldWidth - room.width - 5) + 1;
            int yPos = random.nextInt(worldHeight - room.height - 5) + 1;
            room.moveTo(xPos, yPos);
        }
    }

    /**
     * Moves a room to the specified coordinate (x,y)
     * @param x target x coordinate
     * @param y target y coordinate
     */
    public void moveTo(int x, int y) {
        this.leftBottomPos.setX(x);
        this.leftBottomPos.setY(y);
    }

    /**
     * Sets correct tiles for entire room in the world grid
     * @param world 2D world array
     */
    public void setRoom(TETile[][] world) {
        setFloor(world);
        setWall(world);
    }

    /**
     * Sets correct tiles for room floors in the world grid
     * @param world 2D world array
     */
    public void setFloor(TETile[][] world) {
        for (int x = 0; x < width - 2; ++x) {
            for (int y = 0; y < height - 2; ++y) {
                world[leftBottomPos.getX() + x + 1][leftBottomPos.getY() + y + 1] = Tileset.FLOOR;
            }
        }
    }

    /**
     * Sets the correct tiles of the walls of the room in the world grid
     * @param world A 2D TETile Array representing the random generated world
     */
    public void setWall(TETile[][] world) {
        for (int y = 0; y < height - 2; ++y) {
            int xPosRoom = leftBottomPos.getX();
            int yPosRoom = leftBottomPos.getY();
            TETile leftTile = world[xPosRoom][yPosRoom + 1 + y];
            TETile rightTile = world[xPosRoom + width - 1][yPosRoom + 1 + y];

            if (leftTile.equals(Tileset.NOTHING) || leftTile.equals(Tileset.WATER)) {
                world[xPosRoom][yPosRoom + 1 + y] = Tileset.WALL;
            }
            if (rightTile.equals(Tileset.NOTHING) || rightTile.equals(Tileset.WATER)) {
                world[xPosRoom + width - 1][yPosRoom + 1 + y] = Tileset.WALL;
            }
        }

        for (int x = 0; x < width; ++x) {
            int xPosRoom = leftBottomPos.getX();
            int yPosRoom = leftBottomPos.getY();
            TETile upperTile = world[xPosRoom + x][yPosRoom];
            TETile lowerTile = world[xPosRoom + x][yPosRoom + height - 1];
            if (upperTile.equals(Tileset.NOTHING) || upperTile.equals(Tileset.WATER)) {
                world[xPosRoom + x][yPosRoom] = Tileset.WALL;
            }
            if (lowerTile.equals(Tileset.NOTHING) || lowerTile.equals(Tileset.WATER)) {
                world[xPosRoom + x][yPosRoom + height - 1] = Tileset.WALL;
            }
        }
    }

    /**
     * compareTo method of the Comparator class
     * @param r room to be compared with THIS room
     * @return a positive number if the Manhattan distance to the origin (0,0)
     *            of THIS room is larger than R's Manhattan distance
     *         zero if the distances are same
     *         a negative number if THIS's Manhattan distance is smaller
     */
    public int compareTo(Room r) {
        int thisX = this.leftBottomPos.getX();
        int thisY = this.leftBottomPos.getY();
        int otherX = r.leftBottomPos.getX();
        int otherY = r.leftBottomPos.getY();
        return (thisX + thisY) - (otherX + otherY);
    }

    /**
     * Returns a comparator instance of class Room
     */
    public static class RoomComparator implements Comparator<Room> {
        public int compare(Room r1, Room r2) {
            return r1.compareTo(r2);
        }
    }

    /**
     * Returns a Room instance of height H, width W originated at location (x,y)
     * @param x x coordinate of the left bottom corner
     * @param y y coordinate of the left bottom corner
     * @param h height of the room (randomly generated in Game.java)
     * @param w width of the room (randomly generated in Game.java)
     * @param random a Random instance
     * @return
     */
    public static Room generateRoom(int x, int y, int h, int w, Random random) {
        int roomWidth = h;
        int roomHeight = w;
        Coordinate pos = new Coordinate(x, y);
        Room room = new Room(roomWidth, roomHeight, pos, random);
        return room;
    }

    /**
     * Get the width of the room
     * @return width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get the height of the room
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Get the coordinate of a random spot chosen in the room
     * To be used to construct hallways
     * @return a coordinate of the random point
     */
    public Coordinate getRandomSpot() {
        int xLeftBottom = leftBottomPos.getX();
        int yLeftBottom = leftBottomPos.getY();
        int xLimit = this.width;
        int yLimit = this.height;
        int xPos = xLeftBottom + this.width / 2;
        int yPos = yLeftBottom + this.height / 2;
        return new Coordinate(xPos, yPos);
    }

}
