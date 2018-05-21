package byog.UI;

import edu.princeton.cs.introcs.StdDraw;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;

public class UserInterface {
    private int width;
    private int height;
    private String inputSeedString = "";

    /**
     * UserInterface Constructor
     */
    public UserInterface() {
        this.width = 1244;
        this.height = 700;
    }

    public void introLore() {
        StdDraw.setCanvasSize(this.width, this.height);
        Font font = new Font("Break", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.setPenColor(Color.CYAN);
        StdDraw.clear(Color.BLACK);
        StdDraw.picture(this.width / 2.0, this.height / 2.0,
                "/byog/resources/lore.jpg", this.width, this.height);

        StdDraw.text(this.width / 2.0, 470, "Your princess is trapped in a digital maze");
        StdDraw.show();
        StdDraw.pause(4000);

        StdDraw.text(this.width / 2.0, 400, "She is kidnapped by the digital soldiers");
        StdDraw.show();
        StdDraw.pause(4000);

        StdDraw.text(this.width / 2.0, 330, "And you are going to save her. You are going to kill the soldiers.");
        StdDraw.show();
        StdDraw.pause(4000);

        Font font2 = new Font("TR2N", Font.BOLD, 20);
        StdDraw.setFont(font2);
        StdDraw.text(this.width / 2.0, 100, "Please enter a seed to enter the digital world to save her");
        StdDraw.show();
        StdDraw.pause(4000);
        StdDraw.enableDoubleBuffering();
    }

    /**
     * Generates a start menu of the game/
     */
    public void startMenu() {
        StdDraw.setCanvasSize(this.width, this.height);
        Font font = new Font("TR2N", Font.BOLD, 50);
        Font font2 = new Font("TR2N", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.setPenColor(Color.CYAN);
        StdDraw.clear(Color.BLACK);
        StdDraw.picture(this.width / 2.0, this.height / 2.0,
                "/byog/resources/maze.jpg", this.width, this.height);
        StdDraw.text(this.width / 2.0, 380, "CS 61B: The Whatsoever Game");
        StdDraw.setFont(font2);
        StdDraw.text(this.width / 2.0, 230, "New Game (N)");
        StdDraw.text(this.width / 2.0, 190, "Load Game (L)");
        StdDraw.text(this.width / 2.0, 150, "Quit (Q)");
        StdDraw.show();
        StdDraw.enableDoubleBuffering();
    }

    /**
     * Gets the start menu option key from the user
     * @return the corresponding char value
     */
    public char getKey() {
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char input = StdDraw.nextKeyTyped();
                if (input == 'q' || input == 'n' || input == 'l') {
                    return input;
                }
            }
        }
    }

    /**
     * Displays the input menu where the user could input the seed
     */
    public void inputMenu() {
        StdDraw.setCanvasSize(this.width, this.height);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.setPenColor(Color.CYAN);
        StdDraw.clear(Color.BLACK);
        setTitle();

        StdDraw.show();
        while (true) {
            setTitle();
            if (StdDraw.hasNextKeyTyped()) {
                if (StdDraw.isKeyPressed(KeyEvent.VK_ENTER)
                        || StdDraw.isKeyPressed(KeyEvent.VK_S)) {
                    break;
                } else {
                    char input = StdDraw.nextKeyTyped();
                    if (Character.isDigit(input)) {
                        inputSeedString += input;
                        StdDraw.text(this.width / 2.0, 160, inputSeedString);
                        StdDraw.show();
                    } else {
                        notIntError();
                        inputSeedString = "";
                    }
                }
            }
        }
        StdDraw.show();
        StdDraw.enableDoubleBuffering();
    }

    /**
     * Displays this menu when the user loses
     */
    public void gameOverLostMenu() {
        StdDraw.setCanvasSize(this.width, this.height);
        Font font = new Font("TR2N", Font.BOLD, 80);
        Font font2 = new Font("Break", Font.PLAIN, 40);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.setPenColor(Color.CYAN);
        StdDraw.clear(Color.BLACK);
        StdDraw.picture(this.width / 2.0, this.height / 2.0,
                "/byog/resources/over.jpg", this.width, this.height);
        StdDraw.text(this.width / 2.0, 380, "Game Over");
        StdDraw.setFont(font2);
        StdDraw.setPenColor(Color.RED);
        StdDraw.text(this.width / 2.0, 280, "You have died. You failed to rescue the hostage");
        StdDraw.show();
        StdDraw.enableDoubleBuffering();
    }

    /**
     * Displays this menu when the user wins
     */
    public void gameOverWinMenu() {
        StdDraw.setCanvasSize(this.width, this.height);
        Font font = new Font("TR2N", Font.BOLD, 80);
        Font font2 = new Font("Break", Font.PLAIN, 40);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.setPenColor(Color.CYAN);
        StdDraw.clear(Color.BLACK);
        StdDraw.picture(this.width / 2.0, this.height / 2.0,
                "/byog/resources/win.jpg", this.width, this.height);
        StdDraw.text(this.width / 3.2, 440, "Winner Winner");
        StdDraw.text(this.width / 3.2, 370, "Chicken Dinner");
        StdDraw.setFont(font2);
        StdDraw.setPenColor(Color.RED);
        StdDraw.text(this.width / 3.0, 250, "You have won the game!");
        StdDraw.text(this.width / 3.0, 200, "The hostage is rescued!");
        StdDraw.show();
        StdDraw.enableDoubleBuffering();
    }



    /**
     * helper method that sets correct title and style of the menu
     */
    private void setTitle() {
        Font font = new Font("TR2N", Font.BOLD, 50);
        Font font2 = new Font("TR2N", Font.BOLD, 30);
        Font font3 = new Font("Break", Font.PLAIN, 20);
        StdDraw.picture(this.width / 2.0, this.height / 2.0,
                "/byog/resources/maze.jpg", this.width, this.height);
        StdDraw.setFont(font);
        StdDraw.text(this.width / 2.0, 380, "CS 61B: The Whatsoever Game");
        StdDraw.setFont(font2);
        StdDraw.text(this.width / 2.0, 230, "Please enter your world generation seed: ");
        StdDraw.setFont(font3);
        StdDraw.text(this.width / 2.0, 200, "Your seed must be integers");
    }


    /**
     * Generates a string message if the user inserts invalid character
     */
    private void notIntError() {
        Font font = new Font("Break", Font.PLAIN, 20);
        StdDraw.setFont(font);
        StdDraw.text(this.width / 2.0, 120, "Sorry, please input an integer.");
        StdDraw.show();
    }

    /**
     * returns the seed input of the user
     * @return
     */
    public String getSeed() {
        return inputSeedString;
    }
}
