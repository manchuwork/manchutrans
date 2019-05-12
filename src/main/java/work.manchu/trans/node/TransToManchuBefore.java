package work.manchu.trans.node;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class TransToManchuBefore implements TransNode {


    private final Map<Pattern,String> map;

    public TransToManchuBefore() throws IOException {

        String text = Files.readString(Paths.get("src/main/resources/first_transmap.json"));
        Map<String,String> tmp = JSON.parseObject(text, LinkedHashMap.class);

        Map<Pattern,String> patterns = new LinkedHashMap();


        tmp.forEach((k,v)->
                patterns.put(Pattern.compile(k), v)
        );
        map = Map.copyOf(patterns);
    }
    @Override
    public String trans(String text) {

        for(Map.Entry<Pattern,String> entry: map.entrySet()){
            Pattern k = entry.getKey();
            String v = entry.getValue();
            Matcher matcher = k.matcher(text);

            text = matcher.replaceAll(v);
        }
        return text;
    }
}
