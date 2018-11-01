package work.manchu.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class SymbolUtil {
    static final String std_quote = "'";
    static final List<String> list;

    static final Set<String> set;
    static {
        try {
            list =
                    Files.readAllLines(Paths.get("src/main/resources/mnc_symbol_trans.properties"));

            set = list.stream().filter(
                    o->!StringUtils.isBlank(o)).map(o->o.trim())
                    .collect(Collectors.toSet());

        } catch (IOException e) {
            log.error("init Symbol error", e);
            throw new RuntimeException();
        }
    }
    public static Set<String> getQuoteSymboleAll(){
        return set;
    }

    public static String replaceQuoteSymbole(String text){
        for(String key : set){
            text = text.replace(key, std_quote);
        }
        return text;
    }

    public static void main(String[] args) throws IOException {

        System.out.println(getQuoteSymboleAll());
    }
}
