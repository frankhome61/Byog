package byog.WorldEngine;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class MapGenerator implements Serializable {
    private static int MAXLEVEL;
    private int WIDTH;
    private int HEIGHT;
    private Random RANDOM;
    private long SEED;
    private int MAX_NUMBER_OF_ROOMS;
    private int MAX_ROOM_DIM;
    private int MIN_ROOM_DIM;
    private ArrayList<Room> allRooms;
    private ArrayList<Hallway> allHallways;
    private ArrayList<Exit> allExits;
    private ArrayList<Coordinate> allConnectPt;
    private TETile[][] finalWorldFrame;
    private Coordinate stairPos;
    private int level;
    /**
     * A generator of world map
     * @param wid width of the world
     * @param hei height of the world
     * @param ran random object
     * @param maxRoom the max number of rooms
     * @param maxRoomDim  max value of room dimension
     * @param minRoomDim  minimun value of room dimension
     * @param seed random seed
     * @param world 2D world grid
     */
    public MapGenerator(int wid, int hei, Random ran, int maxRoom, int maxRoomDim,
                        int minRoomDim, long seed, TETile[][] world, int lev, int maxLevel) {
        WIDTH = wid;
        HEIGHT = hei;
        RANDOM = ran;
        MAX_NUMBER_OF_ROOMS = maxRoom;
        MIN_ROOM_DIM = minRoomDim;
        MAX_ROOM_DIM = maxRoomDim;
        SEED = seed;
        finalWorldFrame = world;
        this.level = lev;
        MAXLEVEL = maxLevel;
    }

    /**
     * Renders the entire map in the world grid
     */
    public void render() {
        initializeWorld(this.finalWorldFrame);
        renderRoom(this.finalWorldFrame);
        renderHallway(this.finalWorldFrame);
        renderExit(this.finalWorldFrame);
        if (level < MAXLEVEL) {
            findRandomStair();
            renderStair(this.finalWorldFrame);
        }
    }

    public void findRandomStair() {
        int roomNum = RANDOM.nextInt(allRooms.size());
        stairPos = allRooms.get(roomNum).getRandomSpot();
    }

    public void renderStair(TETile[][] world) {
        if (stairPos != null) {
            world[stairPos.getX()][stairPos.getY()] = Tileset.PORT;
        }
    }

    /**
     * Method that renders all the rooms randomly
     * @param world 2D array grid
     */
    private void renderRoom(TETile[][] world) {
        int numOfRooms = RANDOM.nextInt(MAX_NUMBER_OF_ROOMS) + 12;
        //int numOfRooms = 2;
        for (int i = 0; i < numOfRooms; i++) {
            int roomWidth = RANDOM.nextInt(MAX_ROOM_DIM) +  MIN_ROOM_DIM;
            int roomHeight = RANDOM.nextInt(MAX_ROOM_DIM) + MIN_ROOM_DIM;
            int xPos = RANDOM.nextInt(WIDTH - roomWidth - 5) + 1;
            int yPos = RANDOM.nextInt(HEIGHT - roomHeight - 5) + 1;
            Coordinate pos = new Coordinate(xPos, yPos);
            Room room = new Room(roomWidth, roomHeight, pos, RANDOM);
            for (int j = 0; j < allRooms.size(); j++) {
                if (room.isOverlapping(allRooms.get(j))) {
                    allRooms.get(j).seperateRoom(room, RANDOM, WIDTH, HEIGHT);
                }
            }
            allRooms.add(room);
            allRooms.sort(new Room.RoomComparator());
            room.setRoom(world);
        }
    }

    /**
     * Renders all the hallways that connect all the rooms
     * @param world 2D array grid
     */
    private void renderHallway(TETile[][] world) {
        for (int i = 0; i < allRooms.size(); i++) {
            Coordinate pt = allRooms.get(i).getRandomSpot();
            allConnectPt.add(pt);
        }

        for (int i = 0; i < allConnectPt.size() - 1; i++) {
            Coordinate start = allConnectPt.get(i);
            Coordinate end = allConnectPt.get(i + 1);
            Path path = new Path(RANDOM, start, end);
            path.setPath(world);
        }
    }

    private void renderExit(TETile[][] world) {

    }

    /**
     * Initialize the TETile grid
     * @param world 2D world array grid
     */
    private void initializeWorld(TETile[][] world) {
        TETile tile = typeOfSpace();
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = tile;
            }
        }
        allExits = new ArrayList<>();
        allRooms = new ArrayList<>();
        allHallways = new ArrayList<>();
        allConnectPt = new ArrayList<>();
    }

    /**
     * Randomly select whether the empty space is WATER or NOTHING
     * @return a tile
     */
    private TETile typeOfSpace() {
        Random random2 = new Random(SEED);
        int type = random2.nextInt(2);
        if (type == 1) {
            return Tileset.WATER;
        } else {
            return Tileset.NOTHING;
        }
    }

    /**
     * Saves the current state to local file
     * @throws IOException
     */
    public void saveState() throws IOException {
        FileOutputStream outFile = new FileOutputStream("map.txt");
        ObjectOutputStream out = new ObjectOutputStream(outFile);
        out.writeObject(this);
        out.close();
    }

    /**
     * Loads previously saved state from local file
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static MapGenerator loadState() throws IOException, ClassNotFoundException {
        FileInputStream inFile = new FileInputStream("map.txt");
        ObjectInputStream in = new ObjectInputStream(inFile);
        MapGenerator map = (MapGenerator) in.readObject();
        in.close();
        return map;
    }

    /**
     * Returns the world grid to the Game interface
     * @return
     */
    public TETile[][] getWorld() {
        return this.finalWorldFrame;
    }

    /**
     * Returns the current Random object to Game interface
     * @return
     */
    public Random getRANDOM() {
        return RANDOM;
    }

    public boolean isLastLevel() {
        return level == MAXLEVEL;
    }

    /**
     * returns the seed to the Game interface
     * @return
     */
    public long getSEED() {
        return SEED;
    }

    /**
     * Generates a random valid on the map
     * @return
     */
    public Coordinate getRandomStart(TETile[][] world) {
        int roomIndex = RANDOM.nextInt(allRooms.size());
        Coordinate randomStart = allRooms.get(roomIndex).getRandomSpot();
        int xPos = randomStart.getX();
        int yPos = randomStart.getY();
        while (stairPos != null && randomStart.equals(stairPos)
                && !world[xPos][yPos].equals(Tileset.PLAYER)
                && !world[xPos][yPos].equals(Tileset.HOSTAGE)
                && !world[xPos][yPos].equals(Tileset.NPC)) {
            randomStart = allRooms.get(roomIndex).getRandomSpot();
            roomIndex = RANDOM.nextInt(allRooms.size());
        }
        return randomStart;
    }

    /**
     * gets the current level in the maze
     * @return
     */
    public int getLevel() {
        return this.level;
    }

    public static boolean isValidMove(Coordinate pos, int direc, TETile[][] world,
                                      int worldWidth, int worldHeight) {
        int xPos = pos.getX();
        int yPos = pos.getY();

        switch (direc) {
            case 0: //moving left
                if (xPos - 1 < 0) {
                    return false;
                }
                if (world[xPos - 1][yPos].equals(Tileset.FLOOR)
                        && !world[xPos - 1][yPos].equals(Tileset.PLAYER)) {
                    return true;
                }
                return false;
            case 1: //moving up
                if (yPos + 1 > worldHeight) {
                    return false;
                }
                if (world[xPos][yPos + 1].equals(Tileset.FLOOR)
                        && !world[xPos - 1][yPos].equals(Tileset.PLAYER)) {
                    return true;
                }
                return false;
            case 2: //moving right
                if (xPos + 1 > worldWidth) {
                    return false;
                }
                if (world[xPos + 1][yPos].equals(Tileset.FLOOR)
                        && !world[xPos - 1][yPos].equals(Tileset.PLAYER)) {
                    return true;
                }
                return false;
            case 3: //moving down
                if (yPos - 1 < 0) {
                    return false;
                }
                if (world[xPos][yPos - 1].equals(Tileset.FLOOR)
                        && !world[xPos - 1][yPos].equals(Tileset.PLAYER)) {
                    return true;
                }
                return false;
            default:
                return false;
        }

    }

    public int getNumOfRooms() {
        return allRooms.size();
    }
}
