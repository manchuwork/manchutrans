package work.manchu.dict;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import lombok.extern.slf4j.Slf4j;
import work.manchu.db.SaveDB;
import work.manchu.trans.node.OldDictTransNode;
import work.manchu.util.CsvUtil;
import work.manchu.util.LangUtil;
import work.manchu.util.StringUtil;

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
public class DictFirstProcess {

    public static void main(String[] args) throws IOException {
        ///Users/heshen/github/manchu/manchutrans/output_dict/output-first_zh_filter.txt_left
        opt("input_dict", "dicts_v2.csv", "output_dict", "output-first_trans_v2.txt");

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



            if(updated_at.isAfter(LocalDateTime.of(2019,1,1,0,0))){
                csvWriterError.writeNext(values);
                csvWriterError.flush();
                continue;
            }
            String mncTrans_v2 = mncTrans.replace("&nbsp;"," ");
            try {
                String mncTrans_v3 = transNodeProcesser.trans(mncTrans_v2);

                String[] valueSave = new String[values.length + 1];
                System.arraycopy(values, 0, valueSave, 0, values.length);
                if (!mncTrans_v2.equals(mncTrans_v3)) {

                    valueSave[values.length] = mncTrans_v3;
                    log.error("updated_at:{},created_at:{}, mncTrans:{},mncTrans_v2:{},  mncTrans_v3:{}", updated_at, created_at , mncTrans , mncTrans_v2, mncTrans_v3);

                    csvWriter.writeNext(valueSave);
                    csvWriter.flush();

                    SaveDB.updateMncTransById(mncTrans_v3,Long.parseLong(values[0]));

                }

                if(mncTrans_v2.endsWith("k'") || mncTrans_v2.contains("k' ") || mncTrans_v2.contains("k' ")){
                    valueSave[values.length] = "k'_____"+mncTrans_v3;
                    log.error("updated_at:{},created_at:{}, mncTrans:{},mncTrans_v2:{},  mncTrans_v3:{}", updated_at, created_at , mncTrans , mncTrans_v2, mncTrans_v3);
                    errorList.add(values);

                    csvWriter.writeNext(valueSave);
                    csvWriter.flush();
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
//        Files.write(Paths.get(outputDir, "errorRow_" + outputFile), errorList);

//        csvReader.readHeaders();


//        CSVReader
//        CsvToBeanBuilder cl;
//
//        List<String> readLines = Files.readAllLines(Path.of(file.toURI()));
//
//        List<String> readLinesNeedOpt = readLines.parallelStream().filter(o-> filters(o)).collect(Collectors.toList());
//
//
//
//        lines.addAll(readLinesNeedOpt);
//
//        Set<String> lines = new TreeSet<>();
//        for(File file : f.listFiles()){
//
//
//
//        }
//
//        List<String> letterBeginLines = lines.parallelStream().filter(MergeFiles::isLetterBegin).collect(Collectors.toList());
//        Files.write(Paths.get(outputDir,"letterbegin_"+ outputFile), letterBeginLines);
//
//        List<String> notLetterLines = lines.parallelStream().filter(o->!MergeFiles.isLetterBegin(o)).collect(Collectors.toList());
//
//        Files.write(Paths.get(outputDir,"notletterbegin"+ outputFile), notLetterLines);
//
//
//        List<String> letterLowercaseBeginLines = letterBeginLines.parallelStream().filter(MergeFiles::isLowerCaseBegin).collect(Collectors.toList());
//        Files.write(Paths.get(outputDir,"letterLowerCasebegin_"+ outputFile), letterLowercaseBeginLines);
//
//        List<String> letterNotLowercaseBeginLines = letterBeginLines.parallelStream().filter(o->!MergeFiles.isLowerCaseBegin(o)).collect(Collectors.toList());
//
//        Files.write(Paths.get(outputDir,"letterNotLowerCasebegin_"+ outputFile), letterNotLowercaseBeginLines);


    }

    private static boolean isLowerCaseBegin(String line) {

        List<String> list = StringUtil.listOneByOne(line);

        if (list.size() < 0) {
            log.error("line size <0 , line:{}", line);
        }

        if (LangUtil.isEnLowerCase(list.get(0))) {
            return true;
        }
        log.info("not en first, line:{}", line);

        return false;
    }

    private static boolean isLetterBegin(String line) {

        List<String> list = StringUtil.listOneByOne(line);

        if (list.size() < 0) {
            log.error("line size <0 , line:{}", line);
        }

        if (LangUtil.isEn(list.get(0))) {
            return true;
        }
        log.info("not en first, line:{}", line);

        return false;
    }

    private static boolean needOpt(File file) {
        if (file.getName().endsWith(".csv")) {
            return true;
        }
        return false;
    }
}
