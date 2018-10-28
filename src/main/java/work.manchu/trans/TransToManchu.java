package work.manchu.trans;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import work.manchu.util.StringUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * @author heshen
 */
@Slf4j

public class TransToManchu implements TransNode{


    private final Map<String,String> map;

    public TransToManchu() throws IOException {

        String text = Files.readString(Paths.get("src/main/resources/transmap.json"));
        Map tmp = JSON.parseObject(text, HashMap.class);
        map = Map.copyOf(tmp);
    }
    @Override
    public String trans(String text) throws UnsupportedEncodingException {

        StringBuilder sb = new StringBuilder();
        for (String letter : StringUtil.listOneByOne( text)){
            String tmp = map.get(letter);
            if(tmp == null){
                log.error(letter + ", not found");
                throw new UnsupportedEncodingException(letter + ", not supported");
            }
            sb.append(tmp);
        }
        return sb.toString();
    }
}
