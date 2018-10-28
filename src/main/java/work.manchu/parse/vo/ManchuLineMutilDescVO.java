package work.manchu.parse.vo;

import lombok.Data;

import java.util.Set;

@Data
public class ManchuLineMutilDescVO {
    private String mnc;

    private String mncOld;

    private Set<String> zhDesc;
}
