package byog.PlayerEngine;
import byog.Core.Game;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import byog.WorldEngine.Coordinate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;


public class NPC extends Villain implements Serializable {
    int couldHeal;

    /**
     * A constructor of the NPC characters
     * @param random random object
     * @param pos position of the character
     * @param fig tile representation of the figure
     * @param worldHeight height of the world
     * @param worldWidth width of the world
     * @param message message said by the character
     * @param isMoveable boolean value indicating whether it could move or not
     */
    public NPC(Random random, Coordinate pos, TETile fig, int worldHeight, int worldWidth,
               String message, boolean isMoveable, int heal) {
        super(random, pos, fig, worldHeight, worldWidth, message, isMoveable);
        couldHeal = heal;
        isVillain = false;
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
        if (timer % 5 == 0) {
            isPaused = false;
        }

        if (isPaused) {
            return "";
        }

        if (!meetPlayer(p, world)) {
            move(world);
            return "";
        }

        // meet the player
        timer = 0;
        isPaused = true;
        if (couldHeal == 1) {
            heal(p);
            couldHeal = 0;
            return speak() + "I could heal you!";
        }

        if (isVillain) {
            justChangeToVillain = false;
            super.update(p, characters, maxVillains, world);
            this.figure = Tileset.VILLAIN;
            return "";
        }
        if (Game.findNumOfVillains(characters) < maxVillains) {
            // to randomly decide whether becoming villain
            int toBeVillains = RANDOM.nextInt(2); // 0 == GOOD, 1 == BAD
            if (toBeVillains == 1) {
                isVillain = true;
                justChangeToVillain = true;
                this.figure = Tileset.VILLAIN;
                super.update(p, characters, maxVillains, world);
                msg = "Change to Villain!";
                return msg;
            }
        }

        return speak();
    }

    /**
     * Returns the string representation of the character
     * @return
     */
    public String toString() {
        return "NPC";
    }

    /**
     * Fights with the player, reduces the player's health
     * @param p
     */
    public void heal(Player p) {
        p.recoverHealth(10);
    }
}
