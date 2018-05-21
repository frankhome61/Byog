package byog.Test;
import org.junit.Test;
import static org.junit.Assert.*;
public class TestStringControl {

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

    @Test
    public void testString() {
        String input = "N129839218SSDAWSDASW";
        String expected = "SDAWSDASW";
        assertEquals(expected, getControlSequence(input));
    }

    @Test
    public void testString2() {
        String input = "N129839218SSDAWSDASW:q";
        String expected = "SDAWSDASW";
        assertEquals(expected, getControlSequence(input));
    }

    @Test
    public void testString3() {
        String input = "ld";
        String expected = "d";
        assertEquals(expected, getControlSequence(input));
    }
    @Test
    public void testString4() {
        String input = "n2470236310810299812saawssdwd:q";
        String expected = "aawssdwd";
        assertEquals(expected, getControlSequence(input));
    }

}
