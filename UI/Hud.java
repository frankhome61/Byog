package byog.UI;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;
import java.awt.Color;
import java.awt.Font;
import java.io.Serializable;

public class Hud implements Serializable {

    /**
     * Displays the current score of the player on top of the frame
     * @param worldWidth width of the frame
     * @param worldHeight height of the frame
     * @param score player's score
     */
    private void displayCurrentScore(int worldWidth, int worldHeight, int score) {
        String info = "Current Score: " + score;
        StdDraw.setPenColor(Color.CYAN);
        Font font = new Font("Break", Font.BOLD, 20);
        StdDraw.setFont(font);
        StdDraw.text(worldWidth - info.length() / 2, worldHeight - 1.2, info);
    }

    /**
     * Prints the information of the tile that the mouse is currently pointing to
     * @param worldWidth width of the frame
     * @param worldHeight height of the frame
     * @param world world grid
     */
    private void displayTileInfo(int worldWidth, int worldHeight, TETile[][] world) {
        StdDraw.setPenColor(Color.CYAN);
        Font font = new Font("Monaco", Font.BOLD, 15);
        StdDraw.setFont(font);
        int xPos = (int) Math.floor(StdDraw.mouseX());
        int yPos = (int) Math.floor(StdDraw.mouseY());
        String message = "";
        if (xPos < worldWidth && yPos < worldHeight && xPos >= 0 && yPos >= 0) {
            if (world[xPos][yPos].equals(Tileset.WALL)) {
                message = "Un-penetrable Cyber Wall:";
            } else if (world[xPos][yPos].equals(Tileset.FLOOR)) {
                message = "Floor. Seems to have nothing here.";
            } else if (world[xPos][yPos].equals(Tileset.PLAYER)) {
                message = "You are stuck in this maze.";
            } else if (world[xPos][yPos].equals(Tileset.PORT)) {
                message = "Wormhole. Transport to the next map.";
            } else if (world[xPos][yPos].equals(Tileset.VILLAIN)) {
                message = "This is an evil villain. He could hurt you!!";
            } else if (world[xPos][yPos].equals(Tileset.NPC)) {
                message = "A stranger. He might have something to give you.";
            }
            StdDraw.text(message.length() / 3.0, worldHeight - 1.2, message);
        }
    }

    /**
     * Displayer text box
     * @param worldWidth width of the frame
     * @param worldHeight height of the frame
     */
    private void displayBar(int worldWidth, int worldHeight) {
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.filledRectangle((worldWidth) / 2, worldHeight - 1, (worldWidth - 0.6) / 2.0, 1);
        StdDraw.setPenColor(Color.CYAN);
        StdDraw.rectangle((worldWidth) / 2, worldHeight - 1, (worldWidth - 0.6) / 2, 1);
    }

    /**
     * Displays the heads up display
     * @param worldWidth width of the frame
     * @param worldHeight height of the frame
     * @param score player score
     * @param lev current level of the maze
     * @param characterMsg character message
     * @param playerHealth health of the player
     * @param world world grid
     */
    public void displayHUD(int worldWidth, int worldHeight, int score, int lev,
                           String characterMsg, int playerHealth, TETile[][] world) {
        displayBar(worldWidth, worldHeight);
        displayCurrentScore(worldWidth, worldHeight, score);
        displayTileInfo(worldWidth, worldHeight, world);
        displayLevel(worldWidth, worldHeight, lev);
        displayPlayerHealth(worldWidth, worldHeight, playerHealth);
        StdDraw.show();
        StdDraw.pause(20);
        Font font = new Font("Monaco", Font.BOLD, 14 - 2);
        StdDraw.setFont(font);

    }

    /**
     * Displays the current level of the maze
     * @param worldWidth width of frame
     * @param worldHeight height of frame
     * @param lev level of the maze
     */
    public void displayLevel(int worldWidth, int worldHeight, int lev) {
        StdDraw.setPenColor(Color.CYAN);
        Font font = new Font("Break", Font.BOLD, 18);
        StdDraw.setFont(font);
        String message = "Current level: " + lev;
        StdDraw.text(worldWidth / 2, worldHeight - 1.2, message);
    }

    /**
     * Displays the message of the character
     * @param worldWidth width of frame
     * @param worldHeight height of frame
     * @param msg message of the character
     */
    public void displayCharacterMsg(int worldWidth, int worldHeight, String msg) {
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.filledRectangle((worldWidth) / 2, 5, (worldWidth) / 4.0, 2);
        StdDraw.setPenColor(Color.CYAN);
        StdDraw.rectangle((worldWidth) / 2, 5, (worldWidth) / 4, 2);

        Font font = new Font("Break", Font.BOLD, 18);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.CYAN);
        StdDraw.text(worldWidth / 2, 5, msg);
        StdDraw.show();
        font = new Font("Monaco", Font.BOLD, 14 - 2);
        StdDraw.setFont(font);
        StdDraw.pause(1000);

    }

    /**
     * Displays the health value of the player
     * @param worldWidth width of frame
     * @param worldHeight height of frame
     * @param playerHealth health of player
     */
    public void displayPlayerHealth(int worldWidth, int worldHeight, int playerHealth) {
        Font font = new Font("Break", Font.BOLD, 18);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.CYAN);
        String message = "Current Health: " + playerHealth;
        StdDraw.text(worldWidth * (3.0 / 4.0) - 2, worldHeight - 1.2, message);
    }
}
