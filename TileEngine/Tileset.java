package byog.TileEngine;

import java.awt.Color;
import java.io.Serializable;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 *
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 *
 * Ex:
 *      world[x][y] = Tileset.FLOOR;
 *
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class Tileset implements Serializable {
    public static final TETile PLAYER = new TETile('@', Color.black, Color.green, "player",
            "/byog/resources/player.png");
    public static final TETile NPC = new TETile('N', Color.black, Color.blue, "NPC",
            "/byog/resources/npc.png");
    public static final TETile VILLAIN = new TETile('V', Color.black, Color.red, "Villain",
            "/byog/resources/villain.png");
    public static final TETile HOSTAGE = new TETile('H', Color.pink, Color.WHITE, "Hostage",
            "/byog/resources/hostage.png");
    public static final TETile PORT = new TETile('P', Color.pink, Color.WHITE, "Hostage",
            "/byog/resources/port.png");
    public static final TETile PLAYER2 = new TETile('@', Color.black, Color.green, "player");
    public static final TETile WALL = new TETile('#', Color.CYAN, Color.darkGray,
            "wall");
    public static final TETile FLOOR = new TETile('·', new Color(128, 192, 128), Color.black,
            "floor");
    public static final TETile NOTHING = new TETile(' ', Color.black, Color.black, "nothing");
    public static final TETile GRASS = new TETile('"', Color.green, Color.black, "grass");
    public static final TETile WATER = new TETile('≈', Color.blue, Color.black, "water");
    public static final TETile FLOWER = new TETile('❀', Color.magenta, Color.pink, "flower");
    public static final TETile LOCKED_DOOR = new TETile('█', Color.orange, Color.black,
            "locked door");
    public static final TETile UNLOCKED_DOOR = new TETile('▢', Color.orange, Color.black,
            "unlocked door");
    public static final TETile SAND = new TETile('▒', Color.yellow, Color.black, "sand");
    public static final TETile MOUNTAIN = new TETile('▲', Color.gray, Color.black, "mountain");
    public static final TETile TREE = new TETile('♠', Color.green, Color.black, "tree");
    public static final TETile NPC2 = new TETile('N', Color.black, Color.blue, "NPC");
    public static final TETile VILLAIN2 = new TETile('V', Color.black, Color.red, "Villain");
    public static final TETile HOSTAGE2 = new TETile('P', Color.pink, Color.WHITE, "Hostage");
}
