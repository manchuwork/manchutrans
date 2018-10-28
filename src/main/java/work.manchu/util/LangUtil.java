package work.manchu.util;

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
}
