package work.manchu.trans.node;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class OldDictTransNode extends PropertiesNode {

    public OldDictTransNode() throws IOException {
        super("old_dict_trans.properties");
    }
}