package work.manchu.trans.node;

import com.alibaba.fastjson.util.IOUtils;
import lombok.extern.slf4j.Slf4j;
import work.manchu.util.OrderedProperties;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
public class PropertiesNode implements TransNode {

    protected Map<String, String> map;

    public PropertiesNode(String resource) throws IOException {

        Path path = Paths.get("src/main/resources/"+ resource);
        log.info("{}, path:{}" ,this.getClass().getName(), path);
        BufferedReader in = Files.newBufferedReader(path);
        Properties tmp = new OrderedProperties();

        tmp.load(in);

        IOUtils.close(in);

        LinkedHashMap tmpMap = new LinkedHashMap();

        for (String name :
                tmp.stringPropertyNames()) {
            tmpMap.put(name, tmp.getProperty(name));
        }

        map = tmpMap;
    }

    @Override
    public String trans(String text) {

        String tmp = text;

//        log.info("trans map:{}",map);
        for (String key : map.keySet()) {
            tmp = text.replace(key, map.get(key));
            text = tmp;
        }
        return tmp;
    }
}