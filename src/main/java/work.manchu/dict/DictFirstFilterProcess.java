package work.manchu.dict;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import work.manchu.trans.node.DictFilterZhNode;
import work.manchu.util.LangUtil;
import work.manchu.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class DictFirstFilterProcess {

    static String[] filterWords = {"?部 ??\uE78F\uE49F滭??\uE4E0 hali de dabsun banjirak?ofi dabsun fuifure de jobo?ho 满文"};

    public static void main(String[] args) throws IOException {
        opt("input_dict", "dicts.csv", "output_dict", "output-first_zh_filter.txt");


    }

    public static void opt(String dir, String file, String outputDir, String outputFile) throws IOException {

        Path p = Paths.get(dir, file);


        File f = p.toFile();
        if (!f.exists()) {
            log.error("not exists file in dir:{}", dir);
            return;
        }


        if (!needOpt(f)) {

            log.warn("do not need opt file:{}", file);

            return;
        }

        CsvReader csvReader = new CsvReader(Path.of(f.toURI()).toFile().getPath(), ',', Charset.forName("utf-8"));

        DictFilterZhNode transNodeProcesser = new DictFilterZhNode();

        CsvWriter csvWriter = new CsvWriter(Path.of(outputDir ,outputFile).toFile().getPath(), ',', Charset.forName("utf-8"));


        List<String> errorList = new ArrayList<>();
        while (csvReader.readRecord()) {

            String[] values = csvReader.getValues();

//            String mnc = values[1];

            String mncTrans = values[2];
            String updated_at_str = values[6];
            String created_at_str = values[7];
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
            LocalDateTime updated_at = LocalDateTime.parse(updated_at_str,dateTimeFormatter);
            LocalDateTime created_at = LocalDateTime.parse(created_at_str,dateTimeFormatter);


            String mncTrans_v2 = mncTrans.replace("&nbsp;"," ");
            try {
                String mncTrans_v3 = transNodeProcesser.trans(mncTrans_v2);

                String[] valueSave = new String[values.length + 1];
                System.arraycopy(values, 0, valueSave, 0, values.length);
                if (!mncTrans_v2.equals(mncTrans_v3)) {

                    valueSave[values.length] = mncTrans_v3;
                    log.error("updated_at:{},created_at:{}, mncTrans:{},mncTrans_v2:{},  mncTrans_v3:{}", updated_at, created_at , mncTrans , mncTrans_v2, mncTrans_v3);

                    csvWriter.writeRecord(valueSave);
                    csvWriter.flush();
                }
            } catch (Exception e) {
                log.error("{}",values,e);
                errorList.add(Arrays.toString(values));
                //throw e;
            }
//            log.info("mnc:{}, mncTrans:{}", mnc, mncTrans);
//            log.info("values:{}", Arrays.toString(values));
        }
        csvWriter.close();
        Files.write(Paths.get(outputDir, "errorRow_" + outputFile), errorList);

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

    private static boolean filters(String line) {

        return filterWords(line) && filterEmpty(line) && filterNotStartWithBracket(line) && filter共搜索到词条(line);
    }

    private static boolean filterEmpty(String line) {
        if (StringUtils.isBlank(line)) {
            log.info("ignore isBlank line :{}", line);
            return false;
        }

        return true;
    }

    private static boolean filterNotStartWithBracket(String line) {
        if (line.startsWith("[")) {
            log.info("ignore line :{}", line);
            return false;
        }

        return true;
    }

    private static boolean filterWords(String line) {

        for (String s : filterWords) {
            if (line.contains(s)) {
                log.info("ignore line filter word:{}", line);
                return false;
            }
        }
        return true;
    }

    private static boolean filter共搜索到词条(String line) {
        if (line.startsWith("共搜索到词条")) {
            log.info("ignore line 共搜索到词条:{}", line);
            return false;
        }

        return true;
    }
}
