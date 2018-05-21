package byog.Core;
import byog.PlayerEngine.GameCharacter;
import byog.PlayerEngine.NPC;
import byog.PlayerEngine.Hostage;
import byog.PlayerEngine.Villain;
import byog.PlayerEngine.Player;
import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import byog.UI.UserInterface;
import byog.UI.Hud;
import byog.WorldEngine.Coordinate;
import byog.WorldEngine.MapGenerator;
import edu.princeton.cs.introcs.StdDraw;
import edu.princeton.cs.introcs.StdAudio;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 40;
    private static final int MAX_NUMBER_OF_ROOMS = 40;
    private static final int MAX_ROOM_DIM = 6;
    private static final int MIN_ROOM_DIM = 3;
    private static int MAX_LEVEL;
    private String quitSequence = "";
    private long SEED;
    private Random RANDOM;
    private MapGenerator map;
    private Player player;
    private ArrayList<GameCharacter> characters;
    private static int MAX_GAMECHARACTERS;
    private static int MAX_VILLAINS;
    private int numOfVallains;
    private String characterMsg;

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        UserInterface ui = new UserInterface();
        Hud hud = new Hud();
        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
        ui.startMenu();
        StdAudio.play("/byog/resources/ingame.wav");
        char selection = ui.getKey();
        if (selection == 'n') {
            //New game
            finalWorldFrame = new TETile[WIDTH][HEIGHT];
            ui.introLore();
            ui.inputMenu();
            String input = "N" + ui.getSeed();
            ter.initialize(WIDTH, HEIGHT, 0, 0);
            newGame(input, finalWorldFrame);
            Coordinate start = this.map.getRandomStart(finalWorldFrame);
            player = new Player(start, 100, Tileset.PLAYER, WIDTH, HEIGHT, finalWorldFrame);
            ter.renderFrame(finalWorldFrame);
            initialGameCharacters(finalWorldFrame);
            renderCharacters(finalWorldFrame);

        } else if (selection == 'l') {
            // Load game from Save Log
            this.ter.initialize(WIDTH, HEIGHT, 0, 0);
            finalWorldFrame = loadGame();
            ter.renderFrame(finalWorldFrame);

        } else if (selection == 'q') {
            //Quit the game
            System.exit(0);
        }
        if (selection == 'l' || selection == 'n') {
            while (true) {
                if (hasKeyboardQuit()) {
                    quit();
                }
                playerKeyControl(player, finalWorldFrame);
                updateCharacters(finalWorldFrame);
                ter.renderFrame(finalWorldFrame, player.getScore(), map.getLevel(),
                        characterMsg, player.getHealth(), hud);

                StdDraw.show();
                StdDraw.pause(75);

                if (!characterMsg.equals("")) {
                    hud.displayCharacterMsg(WIDTH, HEIGHT, characterMsg);
                    StdDraw.pause(500);
                    characterMsg = "";
                }
                player.fightVillains(characters, finalWorldFrame, hud);
                if (!this.map.isLastLevel()) {
                    this.map.renderStair(finalWorldFrame);
                }

                if (player.getHealth() <= 0) {
                    ui.gameOverLostMenu();
                    break;
                }
                if (player.getWinState()) {
                    ui.gameOverWinMenu();
                    break;
                }
            }
        }

    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().
        char gameState = input.charAt(0);
        TETile[][] finalWorldFrame  = new TETile[WIDTH][HEIGHT];

        if (gameState == 'L' || gameState == 'l') {
            finalWorldFrame = loadGame();
        } else if (gameState == 'N' || gameState == 'n') {
            newGame(input, finalWorldFrame);
            Coordinate start = this.map.getRandomStart(finalWorldFrame);
            player = new Player(start, 100, Tileset.PLAYER, WIDTH, HEIGHT, finalWorldFrame);
            initialGameCharacters(finalWorldFrame);
            renderCharacters(finalWorldFrame);
        }
        if (gameState == 'L' || gameState == 'l' || gameState == 'N' || gameState == 'n') {
            String controls = getControlSequence(input);
            for (int i = 0; i < controls.length(); i++) {
                playerStringControl(player, controls.charAt(i), finalWorldFrame);
                this.map.findRandomStair();
                this.map.renderStair(finalWorldFrame);
                player.render(finalWorldFrame);
            }
        }
        if (hasStringQuit(input)) {
            saveGame();
        }

        return finalWorldFrame;
    }



    /////////////////////////////////////
    /////     Helper Methods       //////
    /////////////////////////////////////

    /**
     * Given an input string, initializes the game
     * @param input Input string from user
     */
    private void initialize(String input) {
        SEED = getSeed(input);
        RANDOM = new Random(SEED);
    }

    /**
     * Given a command line string input,
     * parse the string and returns the number
     * contained in the string as a seed for the game
     * @param input a string input from user
     * @return an integer (long)
     */
    private long getSeed(String input) {
        String seedString = "";
        for (int i = 1; i < input.length(); i++) {
            char a = input.charAt(i);
            if (Character.isDigit(a)) {
                seedString += a;
            } else {
                break;
            }
        }

        long seed = Long.parseLong(seedString);
        return seed;
    }

    /**
     * Returns a string containing only the control sequence consists
     * "w" or "a" or "s" or "d"
     * @param input user input
     * @return
     */
    private String getControlSequence(String input) {
        int startOfSqe = 0;
        String seq = "";
        if (input.charAt(0) == 'l' || input.charAt(0) == 'L') {
            startOfSqe = 1;
        } else {
            for (int i = 0; i < input.length(); i++) {
                startOfSqe++;
                if (input.charAt(i) == 's' || input.charAt(i) == 'S') {
                    break;
                }
            }
        }
        for (int i = startOfSqe; i < input.length(); i++) {
            if (input.charAt(i) == ':') {
                return seq;
            }
            seq += input.charAt(i);
        }
        return seq;
    }


    ////////////////////////////////////
    ////// Random Character Methods ////
    ////////////////////////////////////

    /**
     * Returns the total number of villains in the world
     * @param characters the character array list
     * @return the total number of villains
     */
    public static int findNumOfVillains(ArrayList<GameCharacter> characters) {
        int numOfVillains = 0;
        for (int i = 0; i < characters.size(); i++) {
            GameCharacter character = characters.get(i);
            numOfVillains += character.isVillain() ? 1 : 0;
        }
        return numOfVillains;
    }

    /**
     * Initializes the random characters (NPC, Villain, Hostage)
     * @param world 2D world grid
     */
    private void initialGameCharacters(TETile[][] world) {
        characters = new ArrayList<>();
        characterMsg = "";

        TETile npcFigure = Tileset.NPC;
        TETile villainFigure = Tileset.VILLAIN;
        String npcMsg = "I am a NPC!";
        String villainMsg = "I am a villain!";


        if (map.isLastLevel()) {
            addHostage(world);
        }

        int numOfCharacterToCreate = RANDOM.nextInt(MAX_GAMECHARACTERS - characters.size()) + 3;

        for (int i = 0; i < numOfCharacterToCreate; i++) {
            int couldHeal = RANDOM.nextInt(2);
            numOfVallains = findNumOfVillains(characters);

            if (numOfVallains >= MAX_VILLAINS) {
                NPC c = new NPC(RANDOM, this.map.getRandomStart(world), npcFigure,
                        HEIGHT, WIDTH, npcMsg, true, couldHeal);
                characters.add(c);
            } else {
                int gameCharacterType = RANDOM.nextInt(2); // 0 == NPC, 1 == VILLAIN
                switch (gameCharacterType) {
                    case 0:
                        // NPC
                        NPC c = new NPC(RANDOM, this.map.getRandomStart(world),
                                npcFigure, HEIGHT, WIDTH, npcMsg, true, couldHeal);
                        characters.add(c);
                        break;
                    case 1:
                        // VILLAIN
                        Villain v = new Villain(RANDOM, this.map.getRandomStart(world),
                                villainFigure, HEIGHT, WIDTH, villainMsg, true);
                        characters.add(v);
                        break;
                    default:
                        System.out.println("Game::initialGameCharacters, switch default");
                        break;
                }
            }
        }
        numOfVallains = findNumOfVillains(characters);
    }

    /**
     * Render all the random characters
     * @param world world grid
     */
    private void renderCharacters(TETile[][] world) {
        for (int i = 0; i < characters.size(); i++) {
            characters.get(i).render(world);
        }
    }

    /**
     * Update all the characters to the next position
     * @param world world grid
     */
    private void updateCharacters(TETile[][] world) {
        for (int i = 0; i < characters.size(); i++) {
            String msg = characters.get(i).update(player, characters, MAX_VILLAINS, world);
            if (!msg.equals("")) {
                // this character meets the player and says something
                characterMsg = msg;
            }
        }
        numOfVallains = findNumOfVillains(characters);
    }

    /**
     * Add hostage and surrounding villains to the final level of the map
     * @param world world grid
     */
    private void addHostage(TETile[][] world) {
        TETile hostageFigure = Tileset.HOSTAGE;
        String hostageMsg = "I am hostage.";
        String villainMsg = "I am a villain!";
        Coordinate hostagePos = this.map.getRandomStart(world);
        int hostagePosX = hostagePos.getX();
        int hostagePosY = hostagePos.getY();
        Hostage princess = new Hostage(RANDOM, hostagePos, hostageFigure,
                HEIGHT, WIDTH, hostageMsg, false);
        characters.add(princess);
        // add villains

        for (int i = 0; i < 4; i++) {
            if (MapGenerator.isValidMove(hostagePos, i, world, WIDTH, HEIGHT)) {
                int xPos = (i % 2 == 0) ? ((i == 0)
                        ? hostagePosX - 1 : hostagePosX + 1) : hostagePosX;
                int yPos = (i % 2 == 1) ? ((i == 3)
                        ? hostagePosY - 1 : hostagePosY + 1) : hostagePosY;
                Villain v = new Villain(RANDOM, new Coordinate(xPos, yPos),
                        Tileset.VILLAIN, HEIGHT, WIDTH, villainMsg, false);
                characters.add(v);
            }
        }
    }
    //////////////////////////////////
    //////// Game State Change  //////
    //////////////////////////////////

    /**
     * Loads previously saved game from local file
     * Loads the map information
     * Loads the player information
     * @return previously saved 2D array
     */
    private TETile[][] loadGame() {
        try {
            this.map = MapGenerator.loadState();
            this.player = Player.loadState();
            this.SEED = map.getSEED();
            this.RANDOM = map.getRANDOM();
            FileInputStream inFile = new FileInputStream("characters.txt");
            ObjectInputStream in = new ObjectInputStream(inFile);
            this.characters = (ArrayList) in.readObject();
            this.MAX_VILLAINS = (Integer) in.readObject();
            this.numOfVallains = (Integer) in.readObject();
            this.MAX_LEVEL = (Integer) in.readObject();
            this.characterMsg = "";
            in.close();
            return map.getWorld();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Failed");
        }
        return null;
    }

    /**
     * Starts a new game.
     * @param input input string from user
     * @param world 2D world grid
     */
    private void newGame(String input, TETile[][] world) {
        initialize(input);
        MAX_LEVEL = RANDOM.nextInt(3) + 2;
        MapGenerator newMap = new MapGenerator(WIDTH, HEIGHT, RANDOM,
                MAX_NUMBER_OF_ROOMS, MIN_ROOM_DIM, MAX_ROOM_DIM, SEED, world, 1, MAX_LEVEL);
        this.map = newMap;
        map.render();
        MAX_GAMECHARACTERS = this.map.getNumOfRooms() / 2;
        MAX_VILLAINS = MAX_GAMECHARACTERS / 2;
    }

    /**
     * Saves the current state of the game
     * Saves the state of the map
     * Saves the state of the player
     */
    private void saveGame() {
        try {
            player.saveState();
            map.saveState();
            FileOutputStream outFile = new FileOutputStream("characters.txt");
            ObjectOutputStream out = new ObjectOutputStream(outFile);
            out.writeObject(this.characters);
            out.writeObject(MAX_VILLAINS);
            out.writeObject(numOfVallains);
            out.writeObject(MAX_LEVEL);
            out.close();
        } catch (IOException e) {
            System.out.println("Sorry, failed to save game state! ");
        }
    }




    //////////////////////////////////
    /////      User Controls    //////
    //////////////////////////////////
    /**
     * Controls the player using arrow keys or "WASD" keys
     * Used in playWithKeyboard
     * @param playerOb player object that could be controlled by user
     * @param world world grid
     */
    private void playerKeyControl(Player playerOb, TETile[][] world) {
        boolean hitStair = false;
        if (StdDraw.isKeyPressed(KeyEvent.VK_UP) || StdDraw.isKeyPressed(KeyEvent.VK_W)) {
            hitStair = playerOb.isStair(1, world);
            playerOb.movePlayer(1, world);
            //updateCharacters(world);
        } else if (StdDraw.isKeyPressed(KeyEvent.VK_DOWN) || StdDraw.isKeyPressed(KeyEvent.VK_S)) {
            hitStair = playerOb.isStair(3, world);
            playerOb.movePlayer(3, world);
            //updateCharacters(world);
        } else if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT) || StdDraw.isKeyPressed(KeyEvent.VK_A)) {
            hitStair = playerOb.isStair(0, world);
            playerOb.movePlayer(0, world);
            //updateCharacters(world);
        } else if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT) || StdDraw.isKeyPressed(KeyEvent.VK_D)) {
            hitStair = playerOb.isStair(2, world);
            playerOb.movePlayer(2, world);
            //updateCharacters(world);
        }

        if (hitStair) {
            // initialize a new map as the second level after hitting the stair
            MapGenerator newMap = new MapGenerator(WIDTH, HEIGHT, RANDOM,
                    MAX_NUMBER_OF_ROOMS, MIN_ROOM_DIM, MAX_ROOM_DIM, SEED,
                    world, map.getLevel() + 1, MAX_LEVEL);
            this.map = newMap;
            this.map.render();
            MAX_GAMECHARACTERS = this.map.getNumOfRooms() / 2;
            MAX_VILLAINS = MAX_GAMECHARACTERS / 2;
            playerOb.setPosition(this.map.getRandomStart(world), world);
            initialGameCharacters(world);
            renderCharacters(world);
        }
    }

    /**
     * Controls the player using input strings involving "wasd"
     * Used in playWithInputString(input)
     * @param playerOb player to be controlled
     * @param control control sequence returned by getControlSequence(input)
     * @param world world grid
     */
    private void playerStringControl(Player playerOb, char control, TETile[][] world) {
        boolean hitStair = false;
        switch (control) {
            case 'w':
                hitStair = playerOb.isStair(1, world);
                playerOb.movePlayer(1, world);
                break;
            case 'a':
                hitStair = playerOb.isStair(0, world);
                playerOb.movePlayer(0, world);
                break;
            case 's':
                hitStair = playerOb.isStair(3, world);
                playerOb.movePlayer(3, world);
                break;
            case 'd':
                hitStair = playerOb.isStair(2, world);
                playerOb.movePlayer(2, world);
                break;
            default:
                break;
        }
        if (hitStair) {
            // initialize a new map as the second level after hitting the stair
            MapGenerator newMap = new MapGenerator(WIDTH, HEIGHT, RANDOM,
                    MAX_NUMBER_OF_ROOMS, MIN_ROOM_DIM, MAX_ROOM_DIM,
                    SEED, world, map.getLevel() + 1, MAX_LEVEL);
            this.map = newMap;
            this.map.render();
            playerOb.setPosition(this.map.getRandomStart(world), world);
        }
    }


    /////////////////////////////////////
    ///////   Quitting the Game  ////////
    /////////////////////////////////////
    /**
     * Saves the quitSequence into the class to provide future access
     */
    private void generateQuitSequence() {
        if (StdDraw.hasNextKeyTyped()) {
            char nextKey = StdDraw.nextKeyTyped();
            if (nextKey == ':') {
                quitSequence += ':';
            } else if (nextKey == 'q' || nextKey == 'Q') {
                quitSequence += 'q';
            }
        }
    }

    /**
     * Returns true if the user input the quit sequence ":q"
     * returns false otherwise
     * @return
     */
    private boolean hasKeyboardQuit() {
        generateQuitSequence();
        return quitSequence.contains(":q");
    }

    /**
     * Save the current game state, and terminate the game
     */
    private void quit() {
        saveGame();
        System.exit(0);
    }

    /**
     * Returns true if the command line input string has quit sequence
     * Called in playWithInputString
     * @param input User input
     * @return true if ":Q" or ":q" is in the input
     *         false otherwise
     */
    private static boolean hasStringQuit(String input) {
        for (int i = 0; i < input.length() - 1; i++) {
            if (input.charAt(i) == ':') {
                char next = input.charAt(i + 1);
                if (next == 'q' || next == 'Q') {
                    return true;
                }
            }
        }
        return false;
    }



}
