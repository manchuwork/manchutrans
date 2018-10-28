package work.manchu.util;

import org.junit.jupiter.api.Test;
import work.manchu.parse.StringUtil;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringUtilTest {

    @Test
    void listOneByOne() {

        String text = "  中国人物 ent abd";
        List<String> list = StringUtil.listOneByOne(text);

        assertEquals(text.length(),list.size());
        assertEquals(14,list.size());
        assertEquals(" ",list.get(0));
        assertEquals("d",list.get(13));
        assertEquals("物",list.get(5));


    }
}