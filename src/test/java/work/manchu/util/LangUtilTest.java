package work.manchu.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LangUtilTest {

    @Test
    void isCh() {

        assertTrue(LangUtil.isCh("中"));
        assertTrue(LangUtil.isCh("国"));
    }

    @Test
    void isEn() {
        assertTrue(LangUtil.isEn("A"));
        assertTrue(LangUtil.isEn("a"));
        assertFalse(LangUtil.isEn("#"));
    }
}