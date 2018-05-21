package byog.Test;

import edu.princeton.cs.introcs.StdDraw;

public class KeyStrokeTest {
    public static void main(String[] args) {
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                System.out.println(StdDraw.nextKeyTyped());
            }
        }
    }
}
