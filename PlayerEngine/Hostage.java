package byog.PlayerEngine;
import byog.TileEngine.TETile;
import byog.WorldEngine.Coordinate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * A class for hostage. Rescuing the hostage is the main goal of the game
 */
public class Hostage extends GameCharacter implements Serializable {

    /**
     * A hostage character constructor
     * A hostage may only appear in the final level of the maze
     * @param random random object
     * @param pos position of the character
     * @param fig tile representation of the figure
     * @param worldHeight height of the world
     * @param worldWidth width of the world
     * @param message message said by the character
     * @param isMoveable boolean value indicating whether it could move or not
     */
    public Hostage(Random random, Coordinate pos, TETile fig, int worldHeight,
                   int worldWidth, String message, boolean isMoveable) {
        super(random, pos, fig, worldHeight, worldWidth, message, isMoveable);
        movable = false;
        isPaused = false;
        isHostage = true;
    }

    /**
     * Updates the character's movements
     * @param p Player, controlled by the user
     * @param characters all the randomly generated characters
     * @param maxVillains max number of villains in the world
     * @param world 2D world grid
     * @return the string message the character says
     */
    @Override
    public String update(Player p, ArrayList<GameCharacter> characters,
                         int maxVillains, TETile[][] world) {
        if (meetPlayer(p, world)) {
            return speak();
        }
        return "";
    }
}
