package work.manchu.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author heshen
 */
public class StringUtil {

    public static final char CH_SPACE = (char)12288;
    public static final char EN_SPACE = ' ' ;
    public static List<String> listOneByOne(String text){

        List<String> list = new ArrayList<>();
        for(int i = 0 ; i< text.length();i ++){
            String c = text.substring(i, i+1);
            list.add(c);
        }

        return list;

    }

    public static List<String> split(String value){

        value = value.replaceAll("["+ EN_SPACE+CH_SPACE+"]{1,}"," ");

        value = value.trim();
        String[] a = value.split("[,|，]");

        List<String> r = Arrays.stream(a).map(o-> o.trim()).collect(Collectors.toList());
        return r;
    }

    public static String formatSpace(String value){

        value = value.replace("&nbsp;"," ");

        return value.replaceAll("["+ EN_SPACE+CH_SPACE+"]{1,}"," ");
    }
}
