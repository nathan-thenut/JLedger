package jledger.parser;

import java.util.List;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {
   
    @Test
    public void testReadingFile() {
        Parser parser = new Parser();
        List<String> test = parser.readFile("test.txt");
        assertFalse(test.contains("Hello World!"), "Test file contained " + test);
    }

}
