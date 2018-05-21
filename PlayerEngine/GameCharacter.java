package byog.PlayerEngine;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import byog.WorldEngine.Coordinate;
import byog.WorldEngine.MapGenerator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;


/**
 * A general class for randomly generated game characters
 */
public class GameCharacter implements Serializable {
    TETile figure;
    Coordinate position;
    int WORLD_HEIGHT;
    int WORLD_WIDTH;
    String msg;
    boolean movable;
    Random RANDOM;
    boolean isVillain;
    boolean justChangeToVillain;
    boolean isHostage;
    boolean isPaused;
    int timer;


    /**
     * A constructor of the game characters
     * @param random random object
     * @param pos position of the character
     * @param fig tile representation of the figure
     * @param worldHeight height of the world
     * @param worldWidth width of the world
     * @param message message said by the character
     * @param isMovable boolean value indicating whether it could move or not
     */
    public GameCharacter(Random random, Coordinate pos, TETile fig, int worldHeight, int worldWidth,
                     String message, boolean isMovable) {
        RANDOM = random;
        position = pos;
        figure = fig;
        WORLD_HEIGHT = worldHeight;
        WORLD_WIDTH = worldWidth;
        msg = message;
        movable = isMovable;
        isVillain = false;
        justChangeToVillain = false;
        isPaused = false;
        timer = 0;
        isHostage = false;
    }

    /**
     * Return the message it is speaking
     * @return string message
     */
    public String speak() {
        return msg;
    }

    /**
     * returns whether the character is a villain
     * @return true if the character is a villain
     *         false otherwise
     */
    public boolean isVillain() {
        return isVillain && !justChangeToVillain;
    }

    /**
     * Returns the position of the character
     * @return a coordinate object
     */
    public Coordinate getPosition() {
        return position;
    }

    /**
     * Renders the character to the world grid
     * @param world 2D world grid
     */
    public void render(TETile[][] world) {
        if (!world[position.getX()][position.getY()].equals(Tileset.PLAYER)) {
            world[position.getX()][position.getY()] = figure;
        }
    }

    /**
     * Updates the character's movements
     * @param p Player, controlled by the user
     * @param characters all the randomly generated characters
     * @param maxVillains max number of villains in the world
     * @param world 2D world grid
     * @return the string message the character says
     */
    public String update(Player p, ArrayList<GameCharacter> characters,
                         int maxVillains, TETile[][] world) {
        if (!meetPlayer(p, world)) {
            move(world);
        }
        return speak();
    }

    /**
     * Indicates whether the character has encountered the player
     * @param p player controlled by the user
     * @param world world grid
     * @return true if the character encounters with the player
     *         false otherwise
     */
    public boolean meetPlayer(Player p, TETile[][] world) {
        int xPos = position.getX();
        int yPos = position.getY();
        TETile playerFigure = p.getFigure();
        // check same position
        if (world[xPos][yPos].equals(playerFigure)) {
            isPaused = true;
            return true;
        }

        // check up
        if (yPos < WORLD_HEIGHT - 1 && world[xPos][yPos + 1].equals(playerFigure)) {
            isPaused = true;
            return true;
        }

        // check down
        if (yPos > 0 && world[xPos][yPos - 1].equals(playerFigure)) {
            isPaused = true;
            return true;
        }

        // check left
        if (xPos > 0 && world[xPos - 1][yPos].equals(playerFigure)) {
            isPaused = true;
            return true;
        }

        // check right
        if (xPos < WORLD_WIDTH - 1 && world[xPos + 1][yPos].equals(playerFigure)) {
            isPaused = true;
            return true;
        }
        return false;
    }

    /**
     * Moves the character around in the world
     * @param world 2D world grid
     */
    public void move(TETile[][] world) {
        if (!movable || isPaused) {
            return;
        }

        // if the character is movable, find a random direction to move
        int direction = RANDOM.nextInt(4);

        if (MapGenerator.isValidMove(position, direction, world, WORLD_WIDTH, WORLD_HEIGHT)) {
            int xPos = this.position.getX();
            int yPos = this.position.getY();
            if (!world[xPos][yPos].equals(Tileset.PLAYER)) {
                world[xPos][yPos] = Tileset.FLOOR;
            }
            switch (direction) {
                case 0: //moving left
                    world[xPos - 1][yPos] = this.figure;
                    this.position.setX(xPos - 1);
                    break;
                case 1: //moving up
                    world[xPos][yPos + 1] = this.figure;
                    this.position.setY(yPos + 1);
                    break;
                case 2: //moving right
                    world[xPos + 1][yPos] = this.figure;
                    this.position.setX(xPos + 1);
                    break;
                case 3: //moving down
                    world[xPos][yPos - 1] = this.figure;
                    this.position.setY(yPos - 1);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Indicates whether the character is a hostage
     * @return true if the character is a hostage
     *         false otherwise
     */
    public boolean isHostage() {
        return isHostage;
    }
}
