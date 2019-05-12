package work.manchu.trans.node;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class TransSimbolReplaceTest {

    @Test
    void test_load() throws IOException {
        TransSimbolReplace transSimbolReplace = new TransSimbolReplace();

        //'='
        //＇='
        //`='
        //ʼ='
        //’='
        //‘='
        log.info("map:{}",transSimbolReplace.map);
    }
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