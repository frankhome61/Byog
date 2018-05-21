package byog.Test;
import byog.Core.Game;
import byog.TileEngine.TETile;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestSameWorld {
    @Test
    public void testSameSeed() {
        Game game = new Game();
        TETile[][] world1 = game.playWithInputString("N123421412");
        TETile[][] world2 = game.playWithInputString("N123421412");
        assertArrayEquals(world1, world2);
    }

    @Test
    public void testDifferentSeed() {
        Game game = new Game();
        TETile[][] world1 = game.playWithInputString("n2801844204828707540");
        TETile[][] world2 = game.playWithInputString("n4820759759604654444");
        TETile[][] world3 = game.playWithInputString("n4820759759604654444");
        boolean isSame = true;
        boolean isDifferent = true;
        for (int i = 0; i < world1.length; i++) {
            for (int j = 0; j < world1[0].length; j++) {
                if (!world1[i][j].equals(world2[i][j])) {
                    isSame = false;
                }
                if (world2[i][j].equals(world2[i][j])) {
                    isDifferent = false;
                }
            }
        }
        assertFalse(isSame);
        assertFalse(isDifferent);
    }

    @Test
    public void testSaveAndLoad() {
        Game game = new Game();
        TETile[][] world1 = game.playWithInputString("n2801844204828707540:q");
        TETile[][] world2 = game.playWithInputString("l");
        assertArrayEquals(world1, world2);
    }

    @Test
    public void testSameMovement() {
        Game game = new Game();
        TETile[][] world1 = game.playWithInputString("n2801844204828707540saswdwaswdasw");
        TETile[][] world2 = game.playWithInputString("n2801844204828707540saswdwaswdasw");
        assertArrayEquals(world1, world2);
    }

    @Test
    public void testSplitMoveBetweenSave() {
        Game game = new Game();
        TETile[][] world1 = game.playWithInputString("n2470236310810299812saawssdwdw");
        TETile[][] world2 = game.playWithInputString("n2470236310810299812saawssdwd:q");
        TETile[][] world3 = game.playWithInputString("lw");
        assertArrayEquals(world1, world3);
    }

    @Test
    public void testComplicatedSaveAndMove() {
        Game game = new Game();
        TETile[][] world1 = game.playWithInputString("N999SDDDWWWDDD");
        TETile[][] world2 = game.playWithInputString("N999SDDD:Q");
        TETile[][] world3 = game.playWithInputString("LWWWDDD");
        assertArrayEquals(world1, world3);
    }

    @Test
    public void testComplicatedSaveAndMove2() {
        Game game = new Game();
        TETile[][] world0 = game.playWithInputString("N999SDDDWWWDDD");
        TETile[][] world1 = game.playWithInputString("N999SDDD:Q");
        TETile[][] world2 = game.playWithInputString("LWWW:Q");
        TETile[][] world3 = game.playWithInputString("LDDD:Q");
        assertArrayEquals(world0, world3);
    }


}
