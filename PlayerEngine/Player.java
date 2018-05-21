package byog.PlayerEngine;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import byog.UI.Hud;
import byog.WorldEngine.Coordinate;
import edu.princeton.cs.introcs.StdDraw;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable {
    private Coordinate position;
    private int health;
    private int score;
    private TETile figure;
    private int WORLD_HEIGHT;
    private int WORLD_WIDTH;
    private boolean isGameOver;
    private boolean isWin;

    public Player() {
        this.position = null;
        health = 0;
        figure = null;
    }

    public Player(Coordinate pos, int heal, TETile fig,
                  int worldWidth, int worldHeight, TETile[][] world) {
        this.position = pos;
        this.health = heal;
        this.score = 0;
        this.figure = fig;
        this.WORLD_HEIGHT = worldHeight;
        this.WORLD_WIDTH = worldWidth;
        isGameOver = false;
        world[position.getX()][position.getY()] = this.figure;
    }

    /**
     * Reduces the player's health
     * @param amount
     */
    public void reduceHealth(int amount) {
        this.health -= amount;
    }

    /**
     * Reduces the player's health
     * @param amount
     */
    public void recoverHealth(int amount) {
        this.health += amount;
    }


    /**
     * Returns the health of the player
     * @return the current health
     */
    public int getHealth() {
        return health;
    }


    /**
     * Sets the player's position to the new position
     * @param newPos new position
     * @param world world grid
     */
    public void setPosition(Coordinate newPos, TETile[][] world) {
        this.position = newPos;
        world[position.getX()][position.getY()] = this.figure;
    }

    /**
     * Get's the tile representation of the figure
     * @return
     */
    public TETile getFigure() {
        return figure;
    }

    /**
     * Fights the villain if the character approaches it
     * @param characters the characters arrayList
     * @param world world grid
     * @param hud heads up display object
     */
    public void fightVillains(ArrayList<GameCharacter> characters, TETile[][] world, Hud hud) {
        ArrayList<GameCharacter> deleteCharacter = new ArrayList<>();
        for (int i = 0; i < characters.size(); i++) {
            GameCharacter c = characters.get(i);
            if (c.meetPlayer(this, world)) {
                if (c.isVillain()) {
                    Coordinate cPos = c.getPosition();
                    world[cPos.getX()][cPos.getY()] = Tileset.FLOOR;
                    this.health -= 10;
                    this.score += 10;
                    hud.displayCharacterMsg(WORLD_WIDTH, WORLD_HEIGHT,
                            "Ahhhh, I am beaten by you, but you are hurt!");
                    StdDraw.pause(1000);
                    deleteCharacter.add(c);
                } else if (c.isHostage()) {
                    isWin = true;
                    isGameOver = true;
                }
            }
        }

        for (int i = 0; i < deleteCharacter.size(); i++) {
            characters.remove(deleteCharacter.get(i));
        }
    }

    /**
     * Moves player to the desired position if the move is valid
     * @param direc Direction index
     *              0: left
     *              1: up
     *              2: right
     *              3: down
     * @param world world grid
     */
    public void movePlayer(int direc, TETile[][] world) {
        if (isValidMove(direc, world)) {
            makeMove(direc, world);
        }
    }

    /**
     * Returns true if moving 1 step ahead in the direction
     * specified by DIREC is a valid move (i.e. No bumping in to walls)
     * @param direc Direction index
     *              0: left
     *              1: up
     *              2: right
     *              3: down
     * @param world the world grid
     * @return
     */
    private boolean isValidMove(int direc, TETile[][] world) {
        int xPos = this.position.getX();
        int yPos = this.position.getY();
        switch (direc) {
            case 0: //moving left
                if (xPos - 1 < 0) {
                    return false;
                }
                if (world[xPos - 1][yPos].equals(Tileset.WALL)) {
                    return false;
                }
                return true;
            case 1: //moving up
                if (yPos + 1 > WORLD_HEIGHT) {
                    return false;
                }
                if (world[xPos][yPos + 1].equals(Tileset.WALL)) {
                    return false;
                }
                return true;
            case 2: //moving right
                if (xPos + 1 > WORLD_WIDTH) {
                    return false;
                }
                if (world[xPos + 1][yPos].equals(Tileset.WALL)) {
                    return false;
                }
                return true;
            case 3: //moving down
                if (yPos - 1 < 0) {
                    return false;
                }
                if (world[xPos][yPos - 1].equals(Tileset.WALL)) {
                    return false;
                }
                return true;
            default:
                return false;
        }
    }

    /**
     * Moves the player 1 step ahead in the desired direction specified by DIREC
     * @param direc Direction index
     *              0: left
     *              1: up
     *              2: right
     *              3: down
     * @param world world grid
     */
    private void makeMove(int direc, TETile[][] world) {
        int xPos = this.position.getX();
        int yPos = this.position.getY();
        world[xPos][yPos] = Tileset.FLOOR;
        switch (direc) {
            case 0: //moving left
                this.position.setX(xPos - 1);
                break;
            case 1: //moving up
                this.position.setY(yPos + 1);
                break;
            case 2: //moving right
                this.position.setX(xPos + 1);
                break;
            case 3: //moving down
                this.position.setY(yPos - 1);
                break;
            default:
                break;
        }
        render(world);
    }

    public void render(TETile[][] world) {
        world[position.getX()][position.getY()] = this.figure;
    }

    /**
     * Tells whether the player has reached a stair(Wormhole)
     * @param direc direction of the player going
     * @param world world grid
     * @return
     */
    public boolean isStair(int direc, TETile[][] world) {
        int xPos = this.position.getX();
        int yPos = this.position.getY();
        switch (direc) {
            case 0: //moving left
                xPos = xPos - 1;
                break;
            case 1: //moving up
                yPos += 1;
                break;
            case 2: //moving right
                xPos += 1;
                break;
            case 3: //moving down
                yPos -= 1;
                break;
            default:
                return false;
        }
        if (world[xPos][yPos].equals(Tileset.PORT)) {
            return true;
        }
        return false;
    }


    /**
     * Saves the current state to local file
     * @throws IOException
     */
    public void saveState() throws IOException {
        FileOutputStream outFile = new FileOutputStream("player.txt");
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
    public static Player loadState() throws IOException, ClassNotFoundException {
        FileInputStream inFile = new FileInputStream("player.txt");
        ObjectInputStream in = new ObjectInputStream(inFile);
        Player player = (Player) in.readObject();
        in.close();
        return player;
    }

    /**
     * Returns whether the player is win or not
     * @return
     */
    public boolean getWinState() {
        return isGameOver;
    }

    /**
     * Gets the current score of the player
     * @return
     */
    public int getScore() {
        return this.score;
    }

    public Coordinate getPosition() {
        return position;
    }
}
