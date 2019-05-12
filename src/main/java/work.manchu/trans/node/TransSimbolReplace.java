package work.manchu.trans.node;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class TransSimbolReplace extends PropertiesNode {

    public TransSimbolReplace() throws IOException {

        super("mnc_symbol_trans.properties");
    }
}
