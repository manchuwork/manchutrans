package work.manchu.trans;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author heshen
 */
@Slf4j
public class TransNodeProcesser implements TransNode{

    List<TransNode> transNodes;

    public TransNodeProcesser() throws IOException {
        transNodes = List.of(new TransFirstReplace(), new TransToManchu());
    }
    @Override
    public String trans(String text) throws UnsupportedEncodingException {

        String tmp = text;
        for(TransNode transNode : transNodes){
            String tmpret= transNode.trans(tmp);
            log.info("param:{}, ret:{}, transNode:{}, ",tmp, tmpret, transNode);
            tmp =  tmpret;

        }
        return tmp;
    }
}
