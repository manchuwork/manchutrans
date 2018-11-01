package work.manchu.parse.second;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import work.manchu.util.StringUtil;
import work.manchu.parse.vo.ManchuLineMutilDescVO;
import work.manchu.parse.vo.ManchuLineVO;
import work.manchu.util.LangUtil;
import work.manchu.util.SymbolUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class ParseMergeFiles01 {

    public static void main(String[] args) throws IOException {


        Set<ManchuLineVO> set = parseFile("output/letterLowerCasebegin_output-merge.txt");

        set = transMncQuote(set);
        List<String> list = set.stream().sorted(Comparator.comparing(ManchuLineVO::getMnc)).map(JSON::toJSONString)
                .collect(Collectors.toList());
        Files.write(Paths.get("output/trans/letterLowerCaseBegin_json.json"), list);


        List<ManchuLineMutilDescVO> manchuLineMutilDescVOS = toManchuLineMutilDescVO(set);


        List<String> list02 = manchuLineMutilDescVOS.stream().sorted(Comparator.comparing(ManchuLineMutilDescVO::getMnc)).map(JSON::toJSONString)
                .collect(Collectors.toList());

        Files.write(Paths.get("output/trans/trans_letterLowerCaseBeginMergeDesc_json.json"), list02);

    }

    public static Set<ManchuLineVO> transMncQuote(Set<ManchuLineVO> set){

        for(ManchuLineVO vo : set){
            String mnc = SymbolUtil.replaceQuoteSymbole(vo.getMnc());
            vo.setMnc(mnc);
        }

        return set;
    }
    public static List<ManchuLineMutilDescVO> toManchuLineMutilDescVO(Set<ManchuLineVO> set){

        Map<String,List<ManchuLineVO>> map = set.stream().collect(Collectors.groupingBy(o->o.getMnc()));

        List<ManchuLineMutilDescVO> ret = new ArrayList<>();

        for(Map.Entry<String,List<ManchuLineVO>> entry : map.entrySet()){
            List<ManchuLineVO> value = entry.getValue();

            Set<String> descs = value.stream().map(o->o.getZhDesc()).collect(Collectors.toSet());



            ret.add(toManchuLineMutilDescVO(entry.getKey(), filterDupliate(descs)));
        }
        return ret;
    }


    public static Set<String> filterDupliate(Set<String> descs){
        return descs.stream().map(o-> o.replace("、","，")).map(o-> o.replace(",","，")).collect(Collectors.toSet());
    }
    public static ManchuLineMutilDescVO toManchuLineMutilDescVO(String mnc, Set<String> descs){
        ManchuLineMutilDescVO vo = new ManchuLineMutilDescVO();
        vo.setMnc(mnc);
        vo.setZhDesc(descs);
        return vo;
    }

    public static Set<ManchuLineVO> parseFile(String file) throws IOException {

        List<String> readLines = Files.readAllLines(Paths.get(file));

        Set<ManchuLineVO> lines = new TreeSet();

        for(String line : readLines){
            ManchuLineVO vo = parseLine(line);

            if(vo == null){
                continue;
            }
            lines.add(vo);
        }
        return lines;
    }


    public static ManchuLineVO parseLine(String line){
        int enIndex = -1;
        int zhIndex = -1;

        int i = 0;
        for(String c: StringUtil.listOneByOne(line)){
            try{
                //
                if((LangUtil.isCh(c) || LangUtil.isChOther(c)) && !LangUtil.isChStopWord(c)){
                    int index = i;
                    if(zhIndex < 0){
                        zhIndex = index;
                        log.info("zhIndex:{}, line:{}", zhIndex, line);
                    }
                }else if(LangUtil.isEn(c) ){
                    int index = i;

                    if(enIndex < 0 ){
                        enIndex = index;

                    }
                }
            }catch (Exception e){
                log.error("line:{}",line, e);
                throw e;
            }

            i++;
        }

        if(enIndex < 0){
            log.error("not found letter, line:{}", line);
            System.exit(-1);
        }

        if(zhIndex < 0){
            log.error("not found chinese, line:{}", line);

            return null;
        }

        String mnc;
        try{
            mnc = line.substring(enIndex, zhIndex);
            mnc = mnc.trim();

        }catch (Exception e){
            log.error("parse line en error,mncIndex:{}, zhIndex:{}, line:{}, ",
                    enIndex, zhIndex, line);
            throw e;
        }

        String zh;
        try{
            zh = line.substring(zhIndex, line.length());
            zh = zh.trim();;

        }catch (Exception e){
            log.error("parse line zh error,mncIndex:{}, zhIndex:{}, line:{}, ",
                    enIndex, zhIndex, line);

            throw e;
        }





        ManchuLineVO vo = new ManchuLineVO();

        vo.setMnc(mnc);
        vo.setZhDesc(zh);

        log.info("mnc:{}, zh:{}, line:{}", mnc, zh, line);
        return vo;
    }
}
