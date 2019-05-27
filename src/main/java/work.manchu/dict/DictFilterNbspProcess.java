package work.manchu.dict;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import lombok.extern.slf4j.Slf4j;
import work.manchu.trans.node.OldDictTransNode;
import work.manchu.util.CsvUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
public class DictFilterNbspProcess {

    public static void main(String[] args) throws IOException {
        ///Users/heshen/github/manchu/manchutrans/output_dict/output-first_zh_filter.txt_left
        opt("input_dict", "trans_nbsp.csv", "output_dict", "output-first_trans_vNbsp.txt");

//        opt("input_dict", "dicts.csv", "output_dict", "output-first.txt");


    }

    public static void opt(String dir, String file, String outputDir, String outputFile) throws IOException {

        Path p = Paths.get(dir, file);


        File f = p.toFile();
        if (!f.exists()) {
            log.error("not exists file in dir:{}", dir);
            return;
        }


//        if (!needOpt(f)) {
//
//            log.warn("do not need opt file:{}", file);
//
//            return;
//        }

        CSVReader csvReader = CsvUtil.createCSVReader(f);

        OldDictTransNode transNodeProcesser = new OldDictTransNode();

        CSVWriter csvWriter = CsvUtil.createCSVWriter(Path.of(outputDir ,outputFile).toFile());

        CSVWriter csvWriterError = CsvUtil.createCSVWriter(Path.of(outputDir ,"myerror_" + outputFile).toFile());

        List<String[]> errorList = new ArrayList<>();
        Iterator<String[]> iterator = csvReader.iterator();
        while (iterator.hasNext()) {

            String[] values = iterator.next();

//            String mnc = values[1];

            String mncTrans = values[2];
            String updated_at_str = values[6];
            String created_at_str = values[7];
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
            LocalDateTime updated_at = null;
            LocalDateTime created_at = null;

            Long id = Long.parseLong(values[0]);
            try{

                updated_at = LocalDateTime.parse(updated_at_str,dateTimeFormatter);

            }catch (Exception e){
                log.info("LocalDateTime.parse,id:{},updated_at_str:{},",id,updated_at_str,dateTimeFormatter );
            }

            try{
                created_at = LocalDateTime.parse(created_at_str,dateTimeFormatter);

            }catch (Exception e){
                log.info("LocalDateTime.parse,id:{}, created_at_str:{},",id,created_at_str,dateTimeFormatter );
            }



//            if(updated_at.isAfter(LocalDateTime.of(2019,1,1,0,0))){
//                csvWriterError.writeNext(values);
//                csvWriterError.flush();
//                continue;
//            }
            String mncTrans_v2 = mncTrans.replace("&nbsp;"," ");
            try {

                mncTrans_v2 = mncTrans_v2.trim();
                String[] valueSave = new String[values.length + 1];
                System.arraycopy(values, 0, valueSave, 0, values.length);
                if (!mncTrans_v2.equals(mncTrans)) {

                    valueSave[values.length] = mncTrans_v2;
                    log.error("updated_at:{},created_at:{}, mncTrans:{},mncTrans_v2:{}", updated_at, created_at , mncTrans , mncTrans_v2);

                    csvWriter.writeNext(valueSave);
                    csvWriter.flush();

//                    SaveDB.updateMncTransById(mncTrans_v2,Long.parseLong(values[0]));

                }


            } catch (Exception e) {
                log.error("encounter error DictFirstProcess, {}",values,e);
                errorList.add(values);
                //throw e;
            }
//            log.info("mnc:{}, mncTrans:{}", mnc, mncTrans);
//            log.info("values:{}", Arrays.toString(values));
        }
        csvWriter.close();
        csvWriterError.close();

        CSVWriter csvWriterErrorRow = CsvUtil.createCSVWriter(Path.of(outputDir ,"errorRow_" + outputFile).toFile());

        csvWriterErrorRow.writeAll(errorList);


    }

}
