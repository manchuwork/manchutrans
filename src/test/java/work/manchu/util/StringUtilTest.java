package work.manchu.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
@Slf4j
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


    @Test
    void testSplit(){
        String value = "器械 | 豹尾枪，器械之械";
        String[] a = value.split("[,|，]");

        List<String> r = Arrays.stream(a).map(o-> o.trim()).collect(Collectors.toList());
        log.info("split:"+ r);
    }

    @Test
    void test_formatSpace(){
        String value = "器械   |    　　豹尾枪，                &nbsp; 器械之械";

        String r = StringUtil.formatSpace(value);
        log.info("formatSpace:"+ r);

        assertEquals("器械 | 豹尾枪， 器械之械", r);

        value = "器械 |    豹尾枪，                 器械之械";

        r = StringUtil.formatSpace(value);

        assertEquals("器械 | 豹尾枪， 器械之械", r);

        log.info("formatSpace"+(char)12288+":"+ r+(StringUtil.CH_SPACE == StringUtil.EN_SPACE) + " ".equals(StringUtil.EN_SPACE+""));

        assertEquals("　", StringUtil.CH_SPACE+"");

        assertEquals("gaikame wajirakv",StringUtil.formatSpace("gaikame　wajirakv "));
    }
}