package work.manchu.parse.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class ManchuLineVO implements Comparable<ManchuLineVO> {

    private String mnc;

    private String zhDesc;


    @Override
    public int compareTo(ManchuLineVO o) {
        int c = mnc.compareTo(o.mnc);

        if(c != 0){
            return c;
        }
        return zhDesc.compareTo(o.zhDesc) ;
    }
}
