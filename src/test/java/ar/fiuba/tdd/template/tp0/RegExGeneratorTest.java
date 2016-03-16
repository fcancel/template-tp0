package ar.fiuba.tdd.template.tp0;

import org.junit.Test;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;



public class RegExGeneratorTest {

    static final int MAXIMUM_LENGTH_REGEX = 50;

    private boolean validate(String regEx, int numberOfResults) {
        RegExGenerator generator = new RegExGenerator(MAXIMUM_LENGTH_REGEX);
        List<String> results = generator.generate(regEx, numberOfResults);
        // force matching the beginning and the end of the strings
        Pattern pattern = Pattern.compile("^" + regEx + "$");
        return results
                .stream()
                .reduce(true,
                    (acc, item) -> {
                        Matcher matcher = pattern.matcher(item);
                        return acc && matcher.find();
                    },
                    (item1, item2) -> item1 && item2);
    }
/*
    @Test
    public void testACharacter() {
        assertTrue(validate("a", 1));
    }

    @Test
    public void testAStreamOfSimpleCharacters() {
        assertTrue(validate("Stream55", 1));
    }


    @Test
    public void testAnyCharacter() {
        assertTrue(validate(".", 1));
    }
*/
    @Test
    public void testLiteral() {
        assertTrue(validate("\\@", 1));
    }
/*
    @Test
    public void testLiteralDotCharacter() {
        assertTrue(validate("\\@..", 1));
    }

    @Test
    public void testMultipleCharacters() {
        assertTrue(validate("...", 1));
    }

    @Test
    public void testLiteralWithEndSet() {
        assertTrue(validate("\\[abc]",1));
    }

    @Test
    public void testZeroOrOneCharacter() {
        assertTrue(validate("\\@.h?", 1));
    }

    @Test
    public void testCharacterSet() {
        assertTrue(validate("[abc]", 1));
    }

    @Test
    public void testCharacterSetWithQuantifiers() {
        assertTrue(validate("[abc]+", 1));
    }
    */
}
