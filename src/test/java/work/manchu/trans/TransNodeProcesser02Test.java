package work.manchu.trans;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;


@Slf4j
class TransNodeProcesser02Test {


    @Test
    void trans() throws IOException {


        String mnc = t("r'e");

        log.info(mnc);

        mnc= t("malhvng’a");
        log.info(mnc);
    }

    private String t(String trans) throws IOException {
        return new TransNodeProcesser02().trans(trans);
    }

    @Test
    void trans__test() throws IOException {
        assertEquals("ᡰᠠ",t("r'a"));
        assertEquals("ᡰᠠ ",t("r'a "));



        assertEquals("ᡩᡠᠰᡟ",t("dusy"));

        assertEquals("ᠰᡟᠴᡠᠸᠠᠨ",t("sycuwan"));

        assertEquals("ᡷᡳ",t("jy"));

//        r'i\u0020=Zi
//        r'o\u0020=Zo
//        r'u\u0020=Zu
//
//        ža\u0020=Za
//        že\u0020=Ze
//        ži\u0020=Zi
//        žo\u0020=Zo
//        žu\u0020=Zu

        String m = "babuliye，babuliye，babuliye，bababa~";
        String mm = t(m);
        log.info("mm:"+ mm);

        String t = "[00:55.77]nomhon sain sarla yasa i minde alaha (lei),,";
        mm = t(t);
        log.info("mm:"+ mm);

        t = "[00:52.73]uhe hvwaliyasun i jugvn de muse be yarumbi。,";
        mm = t(t);
        log.info("mm:"+ mm);
    }


}