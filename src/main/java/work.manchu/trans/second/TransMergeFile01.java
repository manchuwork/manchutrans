package work.manchu.trans.second;

import com.alibaba.fastjson.JSON;
import work.manchu.parse.vo.ManchuLineMutilDescVO;
import work.manchu.trans.TransNodeProcesser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TransMergeFile01 {

    public static void main(String[] args) throws IOException {



        List<ManchuLineMutilDescVO> list =
                read("output/trans/letterLowerCaseBeginMergeDesc_json.json");


        trans(list);

        List<String> list02 = list.stream().sorted(Comparator.comparing(ManchuLineMutilDescVO::getMnc)).map(JSON::toJSONString)
                .collect(Collectors.toList());
        Files.write(Paths.get("output/trans/trans_letterLowerCaseBeginMergeDesc_json.json"), list02);
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
