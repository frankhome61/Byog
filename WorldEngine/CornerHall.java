package byog.WorldEngine;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import java.util.ArrayList;

public class CornerHall extends Hallway {
    private int inDirection;
    private int outDirection;
    private ArrayList<Coordinate> walls;
    private ArrayList<Coordinate> floors;

    /**
     * A constructor for VerticalHall
     * @param start the coordinate of the starting point of the hallway
     * @param inDir direction indicator of the inbound direction
     * @param outDir direction indicator of the outbound
     *               0: Left
     *               1: Up
     *               2: Right
     *               3. Down
     */
    public CornerHall(Coordinate start, int inDir, int outDir) {
        this.startPos = start;
        length = 2;
        inDirection = inDir;
        outDirection = outDir;
        floors = new ArrayList<>();
        walls = new ArrayList<>();
    }

    /**
     * The method to set correct tiles of the hallway
     * @param world A 2D TETile Array representing the random generated world
     */
    @Override
    public void setHallway(TETile[][] world) {
        generateHallway();
        for (int i = 0; i < floors.size(); i++) {
            int xPos = floors.get(i).getX();
            int yPos = floors.get(i).getY();
            if (world[xPos][yPos] != Tileset.FLOOR) {
                world[xPos][yPos] = Tileset.FLOOR;
            }
        }
        for (int i = 0; i < walls.size(); i++) {
            int xPos = walls.get(i).getX();
            int yPos = walls.get(i).getY();
            if (world[xPos][yPos] != Tileset.FLOOR) {
                world[xPos][yPos] = Tileset.WALL;
            }
        }
    }

    /**
     * Setting correct tiles in the world grid
     *
     *                              out direction
     *                               ↑
     *    #####                     #·#
     *    #····  <-- in direction   #·###
     *    #·###                     #····  <-- in direction
     *    #·#                       #####
     *     ↓
     *     out direction
     *
     */
    public void generateHallway() {
        int xPos = startPos.getX();
        int yPos = startPos.getY();
        switch (inDirection) {
            case 0: //Incoming left
                leftInCorner(xPos, yPos);
                break;
            case 1: //Incoming Up
                upInCorner(xPos, yPos);
                break;
            case 2: //Incoming Right
                rightInCorner(xPos, yPos);
                break;
            case 3: //Incoming Down
                bottomInCorner(xPos, yPos);
                break;
            default:
                break;
        }
    }

    /**
     * Creates Corner hallways like shown in the below diagram
     * @param xPos The x coordinate of start position
     * @param yPos The y coordinate of start position
     *
     *                               ↑
     *    #####                     #·#
     *    #···· <-                  #·###
     *    #·###                     #···· <-
     *    #·#                       #####
     *     ↓
     */
    public void leftInCorner(int xPos, int yPos) {
        floors.add(new Coordinate(xPos, yPos));
        floors.add(new Coordinate(xPos - 1, yPos));
        walls.add(new Coordinate(xPos, yPos + 1));
        walls.add(new Coordinate(xPos, yPos - 1));
        walls.add(new Coordinate(xPos - 2, yPos));
        walls.add(new Coordinate(xPos - 2, yPos - 1));
        walls.add(new Coordinate(xPos - 2, yPos + 1));
        if (outDirection == 1) { //Outgoing up
            walls.add(new Coordinate(xPos - 1, yPos - 1));
            floors.add(new Coordinate(xPos - 1, yPos + 1));
            this.end = new Coordinate(xPos - 1, yPos + 1);
        } else if (outDirection == 3) { //outgoing down
            walls.add(new Coordinate(xPos - 1, yPos + 1));
            floors.add(new Coordinate(xPos - 1, yPos - 1));
            this.end = new Coordinate(xPos - 1, yPos - 1);
        }

    }

    /**
     * Creates Corner hallways like shown in the below diagram
     * @param xPos The x coordinate of start position
     * @param yPos The y coordinate of start position
     *
     *      #####             ↑
     *   -> ····#            #·#
     *      ###·#            #·#
     *        #·#          ###·#
     *        #·#       -> ····#
     *         ↑           #####
     */
    public void rightInCorner(int xPos, int yPos) {
        floors.add(new Coordinate(xPos, yPos)); 
        floors.add(new Coordinate(xPos + 1, yPos));
        walls.add(new Coordinate(xPos, yPos + 1));
        walls.add(new Coordinate(xPos, yPos - 1));
        walls.add(new Coordinate(xPos + 2, yPos)); 
        walls.add(new Coordinate(xPos + 2, yPos - 1)); 
        walls.add(new Coordinate(xPos + 2, yPos + 1)); 
        if (outDirection == 1) { //Outgoing up
            walls.add(new Coordinate(xPos + 1, yPos - 1)); 
            floors.add(new Coordinate(xPos + 1, yPos + 1)); 
            this.end = new Coordinate(xPos + 1, yPos + 1);
        } else if (outDirection == 3) { //Outgoing down
            walls.add(new Coordinate(xPos + 1, yPos + 1)); 
            floors.add(new Coordinate(xPos + 1, yPos - 1)); 
            this.end = new Coordinate(xPos + 1, yPos - 1);
        }
    }

    /**
     * Creates Corner hallways like shown in the below diagram
     * @param xPos The x coordinate of start position
     * @param yPos The y coordinate of start position
     *
     *       ↓              ↓
     *      #·#            #·#
     *      #·#            #·#
     *      #·###        ###·#
     *      #···· ->  <- ····#
     *      #####        #####
     */
    public void upInCorner(int xPos, int yPos) {
        floors.add(new Coordinate(xPos, yPos));
        floors.add(new Coordinate(xPos, yPos + 1));
        walls.add(new Coordinate(xPos + 1, yPos));
        walls.add(new Coordinate(xPos - 1, yPos));
        walls.add(new Coordinate(xPos, yPos + 2)); 
        walls.add(new Coordinate(xPos - 1, yPos + 2)); 
        walls.add(new Coordinate(xPos + 1, yPos + 2)); 
        if (outDirection == 0) { //Outgoing left
            walls.add(new Coordinate(xPos + 1, yPos + 1)); 
            floors.add(new Coordinate(xPos - 1, yPos + 1)); 
            this.end = new Coordinate(xPos - 1, yPos + 1);
        } else if (outDirection == 2) { //Outgoing right
            walls.add(new Coordinate(xPos - 1, yPos + 1)); 
            floors.add(new Coordinate(xPos + 1, yPos + 1)); 
            this.end = new Coordinate(xPos + 1, yPos + 1);
        }
    }


    /**
     * Creates Corner hallways like shown in the below diagram
     * @param xPos The x coordinate of start position
     * @param yPos The y coordinate of start position
     *
     *      #####         #####
     *   <- ····#         #····  ->
     *      ###·#         #·###
     *        #·#         #·#
     *        #·#         #·#
     *         ↑           ↑
     */
    public void bottomInCorner(int xPos, int yPos) {
        floors.add(new Coordinate(xPos, yPos - 1)); 
        floors.add(new Coordinate(xPos, yPos)); 
        walls.add(new Coordinate(xPos + 1, yPos)); 
        walls.add(new Coordinate(xPos - 1, yPos)); 
        walls.add(new Coordinate(xPos, yPos - 2)); 
        walls.add(new Coordinate(xPos - 1, yPos - 2)); 
        walls.add(new Coordinate(xPos + 1, yPos - 2)); 
        if (outDirection == 0) { //outgoing left
            walls.add(new Coordinate(xPos + 1, yPos - 1)); 
            floors.add(new Coordinate(xPos - 1, yPos - 1)); 
            this.end = new Coordinate(xPos - 1, yPos - 1);
        } else if (outDirection == 2) { //outgoing right
            walls.add(new Coordinate(xPos - 1, yPos - 1)); 
            floors.add(new Coordinate(xPos + 1, yPos - 1)); 
            this.end = new Coordinate(xPos + 1, yPos - 1);
        }
    }
}
