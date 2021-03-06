package work.manchu.trans.node;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import work.manchu.util.LangUtil;
import work.manchu.util.StringUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;

/**
 * @author heshen
 */
@Slf4j

public class TransToManchu02 implements TransNode {


    private final LinkedHashMap<String,String> map;

    public TransToManchu02() throws IOException {

        String text = Files.readString(Paths.get("src/main/resources/transmap.json"));
        LinkedHashMap tmp = JSON.parseObject(text, LinkedHashMap.class);
        map = tmp;
    }
    @Override
    public String trans(String text) throws UnsupportedEncodingException {

        StringBuilder sb = new StringBuilder();
        for (String letter : StringUtil.listOneByOne( text)){

            if(LangUtil.isSimbol(letter)){
                sb.append(letter);
                continue;
            }

            if(LangUtil.isMnc(letter)){
                sb.append(letter);
                continue;
            }

            String tmp = map.get(letter);
            if(tmp == null){
                log.warn( "{}, not found,when opt text:{}",letter,text);
                throw new UnsupportedEncodingException(letter + ", not supported,when opt ,text:"+text);
            }

            sb.append(tmp);
        }
        return sb.toString();
    }
}
