package work.manchu.parse;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author heshen
 */
@Slf4j
public class MergeFiles {

    public static void main(String[] args) throws IOException {
        merge("input","output.txt");
    }

    public static void merge(String dir, String outputFile) throws IOException {

        Path p = Paths.get(dir);

        File f = p.toFile();
        if(!f.exists()){
            log.error("not exists file in dir:{}", dir);
            return;
        }

        Set<String> lines = new TreeSet<>();
        for(File file : f.listFiles()){

            if(!file.exists()){
                log.error("file:{} not exists file in dir:{}", file, dir);
                continue;
            }

            List<String> readLines = Files.readAllLines(Path.of(file.toURI()));

            lines.addAll(readLines);

        }

        Files.write(Paths.get(outputFile), lines);
    }
}
