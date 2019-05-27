package work.manchu.dict;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import lombok.extern.slf4j.Slf4j;
import work.manchu.db.SaveDB;
import work.manchu.trans.TransNodeProcesser02;
import work.manchu.util.CsvUtil;
import work.manchu.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class DictTransMnc {


    public static void main(String[] args) throws IOException {
        opt("input_dict", "dicts_7.csv", "output_dict", "output-transmnc_v6.txt");


    }

    static List<V> toV(CSVReader csvReader){
        Iterator<String[]> iterator = csvReader.iterator();
        List<V> r = new ArrayList<>();
        while (iterator.hasNext()) {

            String[] values = iterator.next();

            String mnc = values[1];

            String mncTrans = values[2];
            String ch = values[3];

            Long id = Long.parseLong(values[0]);

            V v = new V(id, mncTrans,ch, values);
            r.add(v);
//            log.info("mnc:{}, mncTrans:{}", mnc, mncTrans);
//            log.info("values:{}", Arrays.toString(values));
        }

        return r;
    }

    public static void opt(String dir, String file, String outputDir, String outputFile) throws IOException {

        Path p = Paths.get(dir, file);


        File f = p.toFile();
        if (!f.exists()) {
            log.error("not exists file in dir:{}", dir);
            return;
        }


        CSVReader csvReader = CsvUtil.createCSVReader(f);

        CSVReader csvReaderDicts = CsvUtil.createCSVReader(Paths.get(dir,"trans.csv").toFile());

        List<V> transKeeps = toV(csvReaderDicts);

        List<Long> keepIds = transKeeps.stream().map(o->o.getId()).collect(Collectors.toList());

        TransNodeProcesser02 transNodeProcesser = new TransNodeProcesser02();

        CSVWriter csvWriter = CsvUtil.createCSVWriter(Path.of(outputDir ,outputFile).toFile());

        List<String> errorList = new ArrayList<>();



        TreeMap<String,List<V>> transMap = new TreeMap<>();


        Iterator<String[]> iterator = csvReader.iterator();
        while (iterator.hasNext()) {

            String[] values = iterator.next();

            String mnc = values[1];

            String mncTrans = values[2];
            String ch = values[3];

            Long id = Long.parseLong(values[0]);
            List<V> dupTransKeys = transMap.get(mncTrans);
            if(dupTransKeys == null){
                dupTransKeys = new ArrayList<>();
                transMap.put(mncTrans, dupTransKeys);
            }
            dupTransKeys.add(new V(id, mncTrans,ch, values));
            try {
                String mnc_2 = transNodeProcesser.trans(mncTrans);

                String[] valueSave = new String[values.length + 1];
                System.arraycopy(values, 0, valueSave, 0, values.length);

//                if (!mnc.replace("&nbsp;"," ").equals(mnc_2)) {
//
//                    valueSave[values.length] = mnc_2;
////                    log.error("mnc:{},  mnc_2:{}, mncTrans:{}", mnc, mnc_2, mncTrans);
//
//                    csvWriter.writeNext(valueSave);
//                    csvWriter.flush();
//                }else

                if(!mnc.equals(mnc_2)) {
                    try {
                        valueSave[values.length] = "NNNNNNNBBBBB___" + mnc_2;
                        log.error("mnc:{},  mnc_2:{}, mncTrans:{}", mnc, mnc_2, mncTrans);

                        SaveDB.updateMncById(mnc_2, id);
                        csvWriter.writeNext(valueSave);
                        csvWriter.flush();
                    }catch (Exception e){
                        log.error("SaveDB.updateMncById encounter error. id:{}, mnc:{},  mnc_2:{}, mncTrans:{}",id, mnc, mnc_2, mncTrans, e);

                    }
                }


            } catch (Exception e) {
                log.error("encounter error dict_trans_mnc ,{}",values,e);
                errorList.add(Arrays.toString(values)+"id:"+ id+", "+e.getMessage());
                //throw e;
            }
//            log.info("mnc:{}, mncTrans:{}", mnc, mncTrans);
//            log.info("values:{}", Arrays.toString(values));
        }
        csvWriter.close();
        Files.write(Paths.get(outputDir, "errorRow_" + outputFile), errorList);

        List<String> notSame = new ArrayList<>();
        List<String> idJoinCh = new ArrayList<>();

        List<CharSequence> dupList = new ArrayList<>();
        transMap.forEach((k,v)->
                {
                    if(v.size() >1){


                        Long id = v.get(0).getId();

                        List<Long> allIds = new ArrayList<>();
                        for(V vOne : v){
                            allIds.add(vOne.getId());

                            if(keepIds.contains(vOne.getId())){
                                id = vOne.getId();
                            }
                        }

                        Map<Long,List<String>> map = new HashMap();
                        for (V value:v
                             ) {
                            map.put(value.getId(), StringUtil.split(value.getCh()));


                        }

                        TreeSet<String> allValue = new TreeSet<>();


                        for(Map.Entry<Long,List<String>> e : map.entrySet()){

                            allValue.addAll(e.getValue());


                        }



                        String joinCh = String.join("，",allValue);


                        String msg = "id:"+id + ",joinCh:"+ joinCh+ "key:"+ k+",allIds:"+ allIds;
                        allIds.remove(id);
                        List<Long> removeIds = allIds;
                        idJoinCh.add(msg+",removeIds:"+ removeIds);
//
//                        try {
//                            SaveDB.updateChById(joinCh,id);
//                        } catch (SQLException e) {
//                            log.error("updateChById encounter error:id,{}",id,e);
//                            e.printStackTrace();
//                        }
//
//                        removeIds.forEach(o->{
//                            try {
//                                SaveDB.deleteById(o) ;
//                            } catch (SQLException e) {
//                                log.error("deleteById encounter error:id,{}",o,e);
//
//                                e.printStackTrace();
//                            }
//                        });


                        if(map.size() > 1){
                            boolean isSame = false;
                            Iterator<Map.Entry<Long,List<String>>> iteratorEntry= map.entrySet().iterator();
                            Map.Entry<Long,List<String>> entry = iteratorEntry.next();
                            List<String> first = entry.getValue();
                            while (iteratorEntry.hasNext()){


                                entry = iteratorEntry.next();
                                List<String> compareWords = entry.getValue();
                                for(String word: first){
                                    if(compareWords.contains(word)){
                                        isSame = true;
                                        break;
                                    }
                                }
                            }

                            if(!isSame){
                                notSame.add("key:"+ k + ", v:"+ v);
                            }
                        }


                        dupList.add("key:"+ k + ",v:"+ v);
                    }
                }

                );


        Files.write(Paths.get(outputDir, "idJoinCh_" + outputFile), idJoinCh);

        Files.write(Paths.get(outputDir, "dupMap_" + outputFile), dupList);

        Files.write(Paths.get(outputDir, "notSame_" + outputFile), notSame);


    }

}
