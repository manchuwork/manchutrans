package work.manchu.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class SymbolUtilTest {

    @Test
    void getQuoteSymboleAll() {
    }

    @Test
    void replaceQuoteSymbole() {

        String text = "abc`b clib' clib' clib‘ clib’ c‘g’";
        log.info(text);

        String ret = SymbolUtil.replaceQuoteSymbole(text);
        log.info(ret);
    }

}