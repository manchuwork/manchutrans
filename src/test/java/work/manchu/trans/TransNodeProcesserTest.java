package work.manchu.trans;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class TransNodeProcesserTest {

    @Test
    void trans() throws IOException {

        TransNodeProcesser transSimbolReplace = new TransNodeProcesser();

        String text = "fer＇　fer＇　seme";
        String ret = transSimbolReplace.trans(text);

        log.info("ret:{}, text:{}", ret, text);
        assertEquals("ᡶᡝᡰ　ᡶᡝᡰ　ᠰᡝᠮᡝ", ret);


        text = "biyadar' seme";
        ret = transSimbolReplace.trans(text);
        log.info("ret:{}, text:{}", ret, text);

        assertEquals("ᠪᡳᠶᠠᡩᠠᡰ ᠰᡝᠮᡝ", ret);

        text = "baZ'ar";
        ret = transSimbolReplace.trans(text);
        log.info("ret:{}, text:{}", ret, text);

        assertEquals("ᠪᠠᡰᠠᡵ", ret);



        text = "baz'ar";
        ret = transSimbolReplace.trans(text);
        log.info("ret:{}, text:{}", ret, text);

        assertEquals("ᠪᠠᡰᠠᡵ", ret);
    }
}