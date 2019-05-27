package work.manchu.dict;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class V {
    Long id;
    String[] ori;
    String mncTrans;
    String ch;

    String transCh;


    public V(Long id, String mncTrans, String ch, String[] ori) {
        this.id = id;
        this.ori = ori;
        this.mncTrans = mncTrans;
        this.ch = ch;
        parseOri();
    }

    void parseOri(){

        this.transCh = ori[8];
//        log.info("parseOri, transCh:{}  ----, {}",transCh,toString());
    }
}