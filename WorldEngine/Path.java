package byog.WorldEngine;
import byog.TileEngine.TETile;
import java.util.Random;
import java.util.ArrayList;

/**
 * A class that manages different paths and performs path finding
 */
public class Path {
    private ArrayList<Coordinate> keyPts;
    private ArrayList<Coordinate> allPts;
    private Random RANDOM;
    private Coordinate startPt;
    private Coordinate endPt;

    /**
     * A constructor that initializes a path as well as creating the path and store the
     * path information
     * @param random Random seed
     * @param start The starting point of the path
     * @param end the end point of the path
     */
    public Path(Random random, Coordinate start, Coordinate end) {
        this.RANDOM = random;
        this.keyPts = new ArrayList<>();
        this.allPts = new ArrayList<>();
        startPt = start;
        endPt = end;
        createPath(startPt, endPt);
    }

    /**
     * Generic path finding algorithm
     * @param start the starting point of the hallway
     * @param end the ending point of the hallway
     * @return returns the key points of the path
     */
    private  ArrayList<Coordinate> createPath(Coordinate start, Coordinate end) {
        int diffX = end.getX() - start.getX();
        int diffY = end.getY() - start.getY();
        Coordinate current = new Coordinate(start);
        keyPts.add(new Coordinate(current));
        while (!current.equals(end)) {
            int direction = RANDOM.nextInt(2);
            int movingLength = 0;
            if (direction == 0) {
                //x direction
                movingLength = RANDOM.nextInt(Math.abs(diffX) + 1);
            } else {
                // y direction
                movingLength = RANDOM.nextInt(Math.abs(diffY) + 1);
            }
            for (int i = 0; i < movingLength; ++i) {
                if (diffX != 0 || diffY != 0) {
                    if (direction == 0) {
                        current.setX(current.getX() + diffX / Math.abs(diffX));
                    } else {
                        current.setY(current.getY() + diffY / Math.abs(diffY));
                    }
                    Coordinate newCurrent = new Coordinate(current);
                    allPts.add(newCurrent);
                    if (i == movingLength - 1) {
                        keyPts.add(newCurrent);
                    }
                }
            }
            diffX = end.getX() - current.getX();
            diffY = end.getY() - current.getY();
        }
        return this.keyPts;
    }

    public ArrayList<Coordinate> getAllPts() {
        return this.allPts;
    }

    /**
     * Sets the tiles of the path in the world grid
     * @param world 2D world grid
     */
    public void setPath(TETile[][] world) {
        setVerticalPath(world);
        setHorizontalPath(world);
        setCorner(world);
    }

    /**
     * Sets the correct tiles of horizontal portion of the generated hallway
     * @param world 2D world grid
     */
    public void setHorizontalPath(TETile[][] world) {
        for (int i = 0; i < this.keyPts.size() - 1; i++) {
            Coordinate startPoint = this.keyPts.get(i);
            Coordinate endPoint = this.keyPts.get(i + 1);
            if (endPoint.getY() == startPoint.getY()) {
                int lenVector = endPoint.getX() - startPoint.getX();
                int length = Math.abs(lenVector);
                HorizontalHall hall = new HorizontalHall(startPoint, length, length / lenVector);
                hall.setHallway(world);
            }
        }
    }

    /**
     * Sets the correct tiles of vertical portion of the generated hallway
     * @param world  2D world grid
     */
    public void setVerticalPath(TETile[][] world) {
        for (int i = 0; i < this.keyPts.size() - 1; i++) {
            Coordinate startPoint = this.keyPts.get(i);
            Coordinate endPoint = this.keyPts.get(i + 1);
            if (endPoint.getX() == startPoint.getX()) {
                int lenVector = endPoint.getY() - startPoint.getY();
                int length = Math.abs(lenVector);
                VerticalHall hall = new VerticalHall(startPoint, length, length / lenVector);
                hall.setHallway(world);
            }
        }
    }

    /**
     * Sets the correct tiles for every corner of the hallway
     * @param world 2D world grid
     */
    public void setCorner(TETile[][] world) {
        for (int i = 1; i < this.keyPts.size() - 1; i++) {
            Coordinate startPoint = this.keyPts.get(i - 1);
            Coordinate midPoint = this.keyPts.get(i);
            Coordinate endPoint = this.keyPts.get(i + 1);
            int xPosStart = startPoint.getX(); int yPosStart = startPoint.getY();
            int xPosMid = midPoint.getX(); int yPosMid = midPoint.getY();
            int xPosEnd = endPoint.getX(); int yPosEnd = endPoint.getY();

            int inXVec = xPosMid - xPosStart;
            int inYVec = yPosMid - yPosStart;
            int outXVec = xPosEnd - xPosMid;
            int outYVec = yPosEnd - yPosMid;
            if (inXVec * outXVec == 0 && inYVec * outYVec == 0) {
                //There is a turn in the path
                if (inXVec == 0) {
                    //Vertical in
                    int inVec = inYVec / Math.abs(inYVec);
                    int outVec = outXVec / Math.abs(outXVec);
                    int inDirec = Math.floorMod(inVec, 4); // 1 for going up, 3 for going down
                    int outDirec = outVec + 1;
                    Coordinate cornerStartPt = new Coordinate(xPosMid, yPosMid - inVec);
                    CornerHall corner = new CornerHall(cornerStartPt, inDirec, outDirec);
                    corner.setHallway(world);

                } else if (inYVec == 0) {
                    //Horizontal in
                    int inVec = inXVec / Math.abs(inXVec);
                    int outVec = outYVec / Math.abs(outYVec);
                    int inDirec = inVec + 1;
                    int outDirec = Math.floorMod(outVec, 4);
                    Coordinate cornerStartPt = new Coordinate(xPosMid - inVec, yPosMid);
                    CornerHall corner = new CornerHall(cornerStartPt, inDirec, outDirec);
                    corner.setHallway(world);
                }
            }
        }
    }
}
