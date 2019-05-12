package work.manchu.trans;

import lombok.extern.slf4j.Slf4j;
import work.manchu.trans.node.TransFirstReplace;
import work.manchu.trans.node.TransNode;
import work.manchu.trans.node.TransSimbolReplace;
import work.manchu.trans.node.TransToManchu02;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author heshen
 */
@Slf4j
public class TransNodeProcesser02 implements TransNode {

    List<TransNode> transNodes;

    public TransNodeProcesser02() throws IOException {
        transNodes = List.of(new TransSimbolReplace(),new TransFirstReplace(),
                /*new TransToManchuBefore(), */
                new TransToManchu02());
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
