package byog.Test;
import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import byog.WorldEngine.Coordinate;
import byog.WorldEngine.Path;

import java.util.Random;

public class HallwayLauncher {
    private static TERenderer ter = new TERenderer();
    private static final int WIDTH = 80;
    private static final int HEIGHT = 40;
    private static Random RANDOM;


    public static TETile[][] generateWorld(long seed) {
        RANDOM = new Random(seed);
        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
        ter.initialize(WIDTH, HEIGHT);
        initializeWorld(finalWorldFrame);

        generatePath(finalWorldFrame);

        //generateCorner(finalWorldFrame);
        ter.renderFrame(finalWorldFrame);
        return finalWorldFrame;
    }

    public static void generatePath(TETile[][] world) {

        Coordinate end = new Coordinate(8, 28);
        Coordinate start = new Coordinate(28, 15);
        Path path = new Path(RANDOM, end, start);

        path.setPath(world);
    }



    private static void initializeWorld(TETile[][] world) {
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }


    public static void main(String[] args) {
        generateWorld(123);
    }

}
