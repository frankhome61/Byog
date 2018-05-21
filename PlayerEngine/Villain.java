package byog.PlayerEngine;
import byog.TileEngine.TETile;
import byog.WorldEngine.Coordinate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Villain extends GameCharacter implements Serializable {
    static final int MAX_ATTACK = 5;
    int attack;
    boolean lastMeet;


    /**
     * A villain character constructor
     * A hostage may only appear in the final level of the maze
     * @param random random object
     * @param pos position of the character
     * @param fig tile representation of the figure
     * @param worldHeight height of the world
     * @param worldWidth width of the world
     * @param message message said by the character
     * @param isMoveable boolean value indicating whether it could move or not
     */
    public Villain(Random random, Coordinate pos, TETile fig, int worldHeight,
                   int worldWidth, String message, boolean isMoveable) {

        super(random, pos, fig, worldHeight, worldWidth, message, isMoveable);
        attack = RANDOM.nextInt(MAX_ATTACK) + 1;
        isVillain = true;
        lastMeet = false;
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
        timer++;
        if (meetPlayer(p, world)) {
            if (!lastMeet) {
                timer = 0;
                isPaused = true;
                lastMeet = true;
                fight(p);
            }
            return speak();
        }
        if (timer % 5 == 0) {
            isPaused = false;
        }
        lastMeet = false;
        move(world);
        return "";
    }

    /**
     * Fights with the player, reduces the player's health
     * @param p
     */
    public void fight(Player p) {
        p.reduceHealth(attack);
    }

    /**
     * Returns the string representation of the character
     * @return
     */
    public String toString() {
        return "Villain";
    }
}
