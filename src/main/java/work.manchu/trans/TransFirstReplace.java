package work.manchu.trans;

import com.alibaba.fastjson.util.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * @author heshen
 */
public class TransFirstReplace implements TransNode {

    Logger logger = Logger.getLogger(TransFirstReplace.class.getName());
    private Map<String,String> map;

    public TransFirstReplace() throws IOException {

        Path path = Paths.get("src/main/resources/mulinder_first_trans.properties") ;//TransToManchu.class.getResource("mulinder_first_trans.properties").getPath();
        logger.info("TransFirstReplace, path"+path);
        InputStream in = Files.newInputStream(path);
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
        for(String key : map.keySet()){
            tmp = text.replace(key, map.get(key));
        }
        return tmp;
    }
}
