package byog.Test;
import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import byog.WorldEngine.Coordinate;
import byog.WorldEngine.Path;
import byog.WorldEngine.Room;

import java.util.Random;

public class RoomLauncher {
    private static TERenderer ter = new TERenderer();
    private static final int WIDTH = 80;
    private static final int HEIGHT = 40;
    private static Random RANDOM;


    public static TETile[][] generateWorld(long seed) {
        RANDOM = new Random(seed);
        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
        ter.initialize(WIDTH, HEIGHT);
        initializeWorld(finalWorldFrame);


        drawRoom(finalWorldFrame);


        ter.renderFrame(finalWorldFrame);
        return finalWorldFrame;
    }


    private static void initializeWorld(TETile[][] world) {
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }


    public static void drawRoom(TETile[][] world) {
        Room room1 = new Room(10, 10, new Coordinate(5, 10), RANDOM);
        Room room2 = new Room(10, 10, new Coordinate(35, 20), RANDOM);
        Coordinate pt1 = room1.getRandomSpot();
        Coordinate pt2 = room2.getRandomSpot();
        Path path = new Path(RANDOM, pt1, pt2);
        room1.setRoom(world);
        room2.setRoom(world);
        path.setPath(world);

    }


    public static void main(String[] args) {
        generateWorld(123);
    }

}
