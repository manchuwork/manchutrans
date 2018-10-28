package work.manchu.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author heshen
 */
public class StringUtil {

    public static List<String> listOneByOne(String text){

        List<String> list = new ArrayList<>();
        for(int i = 0 ; i< text.length();i ++){
            String c = text.substring(i, i+1);
            list.add(c);
        }

        return list;

    }
}
