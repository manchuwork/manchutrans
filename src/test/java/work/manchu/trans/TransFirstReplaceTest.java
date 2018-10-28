package work.manchu.trans;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class TransFirstReplaceTest {

    @Test
    void trans() throws IOException {

        TransFirstReplace transFirstReplace = new TransFirstReplace();
        String text = "n'";
        String ret = transFirstReplace.trans(text);
        log.info("ret:{}, text:{}", ret, text);
        assertEquals("n", ret);


        text = "r＇";
        ret = transFirstReplace.trans(text);
        assertEquals("R", ret);
    }
}