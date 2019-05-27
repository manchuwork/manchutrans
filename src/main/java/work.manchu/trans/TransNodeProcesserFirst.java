package work.manchu.trans;

import lombok.extern.slf4j.Slf4j;
import work.manchu.trans.node.TransFirstReplace;
import work.manchu.trans.node.TransNode;
import work.manchu.trans.node.TransSimbolReplace;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author heshen
 */
@Slf4j
public class TransNodeProcesserFirst implements TransNode {

    List<TransNode> transNodes;

    public TransNodeProcesserFirst() throws IOException {
        transNodes = List.of(new TransSimbolReplace(),new TransFirstReplace()
               );
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
