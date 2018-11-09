package work.manchu.trans.second;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import work.manchu.parse.vo.ManchuLineMutilDescVO;
import work.manchu.trans.TransNodeProcesser;
import work.manchu.util.SymbolUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class TransMergeFile01 {

    public static void main(String[] args) throws IOException {



        List<ManchuLineMutilDescVO> list =
                read("output/trans/trans_letterLowerCaseBeginMergeDesc_json.json");


        preTrans(list);
        list = toManchuLineMutilDescVO(list);
        trans(list);
        List<String> list02 = list.stream().sorted(Comparator.comparing(ManchuLineMutilDescVO::getMnc)).map(JSON::toJSONString)
                .collect(Collectors.toList());
        Files.write(Paths.get("output/trans/trans_OldMnc_letterLowerCaseBeginMergeDesc_json.json"), list02);
    }

    public static List<ManchuLineMutilDescVO> toManchuLineMutilDescVO(Collection<ManchuLineMutilDescVO> set){

        Map<String,List<ManchuLineMutilDescVO>> map = set.stream().collect(Collectors.groupingBy(o->o.getMnc()));

        List<ManchuLineMutilDescVO> ret = new ArrayList<>();

        for(Map.Entry<String,List<ManchuLineMutilDescVO>> entry : map.entrySet()){
            List<ManchuLineMutilDescVO> value = entry.getValue();


            Set<String> descsAll = new HashSet<String>();

            value.forEach(o-> descsAll.addAll(o.getZhDesc()));


            Set<List<String>> setOldLineDescs = descsAll.stream().map(o-> Arrays.asList(o.split(","))).collect(Collectors.toSet());
            Set<String> descsNewAll = new HashSet<>();
            setOldLineDescs.forEach(o-> descsNewAll.addAll(o));


            log.info("setOldLineDescs:{}, descsNewAll:{}", setOldLineDescs, descsAll);
            ret.add(toManchuLineMutilDescVO(entry.getKey(), descsNewAll));
        }
        return ret;
    }

    public static ManchuLineMutilDescVO toManchuLineMutilDescVO(String mnc, Set<String> descsNewAll){
        ManchuLineMutilDescVO vo = new ManchuLineMutilDescVO();

        vo.setMnc(mnc);
        vo.setZhDesc(descsNewAll);
        return vo;
    }

    public static Set<String> filterDupliate(Set<String> descs){
        return descs.stream().map(o-> o.replace("、","，")).map(o-> o.replace(",","，")).collect(Collectors.toSet());
    }

    private static void preTrans(List<ManchuLineMutilDescVO> list){
        for(ManchuLineMutilDescVO vo : list){
            String mnc = vo.getMnc();

            mnc = mnc.replace("\"","");

            int indexT= mnc.indexOf('\t');
            if(indexT> 0){
                mnc = mnc.substring(0, indexT);
            }

            mnc = SymbolUtil.replaceQuoteSymbole(mnc);

            vo.setMnc(mnc);
        }
    }
    private static void trans(List<ManchuLineMutilDescVO> list) throws IOException {
        for(ManchuLineMutilDescVO vo : list){
            String mnc = vo.getMnc();

            mnc = mnc.replace("\"","");

            int indexT= mnc.indexOf('\t');
            if(indexT> 0){
                mnc = mnc.substring(0, indexT);
            }

            mnc = SymbolUtil.replaceQuoteSymbole(mnc);
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
