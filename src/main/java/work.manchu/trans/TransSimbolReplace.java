package work.manchu.trans;

import com.alibaba.fastjson.util.IOUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
public class TransSimbolReplace implements TransNode {

    private Map<String,String> map;

    public TransSimbolReplace() throws IOException {

        Path path = Paths.get("src/main/resources/mnc_symbol_trans.properties") ;//TransToManchu.class.getResource("mulinder_first_trans.properties").getPath();
        log.info("TransSimbolReplace, path"+path);
        BufferedReader in = Files.newBufferedReader(path);
        Properties tmp = new Properties();

        tmp.load(in);

        IOUtils.close(in);

        Map tmpMap = new HashMap();

        for (String name :
                tmp.stringPropertyNames()) {
            tmpMap.put(name, tmp.getProperty(name));
        }

        map = Map.copyOf(tmpMap);
    }

    @Override
    public String trans(String text) {

        String tmp = text;
//        log.info("trans map:{}",map);
        for(String key : map.keySet()){
            tmp = text.replace(key, map.get(key));
            text = tmp;
        }
        return tmp;
    }
}
