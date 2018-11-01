package work.manchu.trans.second;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import work.manchu.parse.vo.ManchuLineMutilDescVO;
import work.manchu.trans.TransNodeProcesser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class MergeTransFile02 {

    public static void main(String[] args) throws IOException {



        List<ManchuLineMutilDescVO> list =
                read("output/trans/trans_letterLowerCaseBeginMergeDesc_json.json");


        //trans(list);

        Map<String, List<ManchuLineMutilDescVO>> map02 = list.stream().sorted(Comparator.comparing(ManchuLineMutilDescVO::getMncOld))
                .collect(Collectors.groupingBy(ManchuLineMutilDescVO::getMncOld));


        List<String> list01 = new ArrayList<>();
        map02.forEach((k,v)->{
            if(v.size() >1){
                log.info("v is duplicated k:{}, v:{}", k, v);
                list01.add(k+"|"+ JSON.toJSONString(JSON.toJSONString(v)));
            }


        });
        Files.write(Paths.get("output/trans/merge_mnc_trans_letterLowerCaseBeginMergeDesc_json.json"), list01);
    }

    private static void trans(List<ManchuLineMutilDescVO> list) throws IOException {
        for(ManchuLineMutilDescVO vo : list){
            String mnc = vo.getMnc();

            mnc = mnc.replace("\"","");

            int indexT= mnc.indexOf('\t');
            if(indexT> 0){
                mnc = mnc.substring(0, indexT);
            }
            String mncOld = trans(mnc);

            vo.setMnc(mnc);

            vo.setMncOld(mncOld);
        }
    }

    private static String trans(String mnc) throws IOException {

        TransNodeProcesser transNodeProcesser = new TransNodeProcesser();
        return transNodeProcesser.trans(mnc);
    }

    private static List<ManchuLineMutilDescVO> read(String file) throws IOException {
        List<String> list =  Files.readAllLines(Paths.get(file));

        return list.stream().map(o->JSON.parseObject(o, ManchuLineMutilDescVO.class)).collect(Collectors.toList());
    }
}
