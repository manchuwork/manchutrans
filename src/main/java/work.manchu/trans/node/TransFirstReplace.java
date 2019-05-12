package work.manchu.trans.node;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author heshen
 */
@Slf4j
public class TransFirstReplace extends PropertiesNode {

    public TransFirstReplace() throws IOException {

        super("mulinder_first_trans.properties");
    }
}
