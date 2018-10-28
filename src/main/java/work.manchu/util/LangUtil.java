package work.manchu.util;

import java.util.Arrays;
import java.util.List;

/**
 * Created by heshen on 2018/10/22.
 */
public class LangUtil {

    public static boolean isCh(String c) {

        return "\u0800".compareTo(c) <= 0 && "\uFFFF".compareTo(c)
                >= 0;
    }

    public static boolean isEn(String c) {

        return "a".compareTo(c) <= 0 && "z".compareTo(c) >= 0 || "A".compareTo(c) <= 0 && "Z".compareTo(c) >= 0;
    }

    public static boolean isEnLowerCase(String c) {
        return "a".compareTo(c) <= 0 && "z".compareTo(c) >= 0;

    }

    public static boolean isChOther(String c){
        return Arrays.asList("1","2","(").contains(c);
    }

    private static List<String> chStopWords = Arrays.asList("＇","'", "　", "’");
    public static boolean isChStopWord(String text){
        return chStopWords.contains(text);
    }

    private static List<String> simbol = Arrays.asList(" ","/"," '","　","&","~","_","5","0","3","4","1","2","6","7","8","9");

    public static boolean isSimbol(String letter) {
        return simbol.contains(letter);
    }
}
