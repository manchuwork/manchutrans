package work.manchu.merge;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import work.manchu.util.LangUtil;
import work.manchu.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * @author heshen
 */
@Slf4j
public class MergeFiles {

    public static void main(String[] args) throws IOException {
        merge("input","output", "output-merge.txt");


    }

    public static void merge(String dir,String outputDir, String outputFile) throws IOException {

        Path p = Paths.get(dir);

        File f = p.toFile();
        if(!f.exists()){
            log.error("not exists file in dir:{}", dir);
            return;
        }

        Set<String> lines = new TreeSet<>();
        for(File file : f.listFiles()){

            if(!needOpt(file)){

                log.info("do not need opt file:{}", file);
            }
            if(!file.exists()){
                log.error("file:{} not exists file in dir:{}", file, dir);
                continue;
            }

            List<String> readLines = Files.readAllLines(Path.of(file.toURI()));

            List<String> readLinesNeedOpt = readLines.parallelStream().filter(o-> filters(o)).collect(Collectors.toList());



            lines.addAll(readLinesNeedOpt);

        }

        List<String> letterBeginLines = lines.parallelStream().filter(MergeFiles::isLetterBegin).collect(Collectors.toList());
        Files.write(Paths.get(outputDir,"letterbegin_"+ outputFile), letterBeginLines);

        List<String> notLetterLines = lines.parallelStream().filter(o->!MergeFiles.isLetterBegin(o)).collect(Collectors.toList());

        Files.write(Paths.get(outputDir,"notletterbegin"+ outputFile), notLetterLines);


        List<String> letterLowercaseBeginLines = letterBeginLines.parallelStream().filter(MergeFiles::isLowerCaseBegin).collect(Collectors.toList());
        Files.write(Paths.get(outputDir,"letterLowerCasebegin_"+ outputFile), letterLowercaseBeginLines);

        List<String> letterNotLowercaseBeginLines = letterBeginLines.parallelStream().filter(o->!MergeFiles.isLowerCaseBegin(o)).collect(Collectors.toList());

        Files.write(Paths.get(outputDir,"letterNotLowerCasebegin_"+ outputFile), letterNotLowercaseBeginLines);


    }

    private static boolean isLowerCaseBegin(String line){

        List<String> list = StringUtil.listOneByOne(line);

        if(list.size()< 0){
            log.error("line size <0 , line:{}", line);
        }

        if(LangUtil.isEnLowerCase(list.get(0))){
            return true;
        }
        log.info("not en first, line:{}",line);

        return false;
    }

    private static boolean isLetterBegin(String line){

        List<String> list = StringUtil.listOneByOne(line);

        if(list.size()< 0){
            log.error("line size <0 , line:{}", line);
        }

        if(LangUtil.isEn(list.get(0))){
            return true;
        }
        log.info("not en first, line:{}",line);

        return false;
    }

    private static boolean needOpt(File file) {
        if(file.getName().endsWith(".txt")){
            return true;
        }
        return false;
    }

    private static boolean filters(String line){

        return filterWords(line) && filterEmpty(line) && filterNotStartWithBracket(line) && filter共搜索到词条(line);
    }

    private static boolean filterEmpty(String line){
        if(StringUtils.isBlank(line)){
            log.info("ignore isBlank line :{}", line);
            return false;
        }

        return true;
    }

    private static boolean filterNotStartWithBracket(String line){
        if(line.startsWith("[")){
            log.info("ignore line :{}", line);
            return false;
        }

        return true;
    }

    static String[] filterWords = {"?部 ??\uE78F\uE49F滭??\uE4E0 hali de dabsun banjirak?ofi dabsun fuifure de jobo?ho 满文"};
    private static boolean filterWords(String line){

        for(String s : filterWords){
            if(line.contains(s)){
                log.info("ignore line filter word:{}", line);
                return false;
            }
        }
        return true;
    }

    private static boolean filter共搜索到词条(String line){
        if(line.startsWith("共搜索到词条")){
            log.info("ignore line 共搜索到词条:{}", line);
            return false;
        }

        return true;
    }
}
