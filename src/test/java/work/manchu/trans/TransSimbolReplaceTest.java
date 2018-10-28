package work.manchu.trans;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class TransSimbolReplaceTest {

    @Test
    void trans() throws IOException {
        TransSimbolReplace transSimbolReplace = new TransSimbolReplace();

        String text = "n`";
        String ret = transSimbolReplace.trans(text);
        assertEquals("n'", ret);


        text = "r＇";
        ret = transSimbolReplace.trans(text);
        assertEquals("r'", ret);

    }
}