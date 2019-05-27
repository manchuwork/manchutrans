package work.manchu.trans;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

class TransNodeProcesserFirstTest {

    private String t(String trans) throws IOException {
        return new TransNodeProcesserFirst().trans(trans);
    }
    @Test
    void trans__test() throws IOException {


        assertEquals("duL",t("dusy"));


//        r'i\u0020=Zi
//        r'o\u0020=Zo
//        r'u\u0020=Zu
//
//        ža\u0020=Za
//        že\u0020=Ze
//        ži\u0020=Zi
//        žo\u0020=Zo
//        žu\u0020=Zu
    }
}