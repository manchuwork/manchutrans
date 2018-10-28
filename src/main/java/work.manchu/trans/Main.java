package work.manchu.trans;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class Main {

    public static void main(String[] args) throws IOException {

        TransNodeProcesser transNodeProcesser = new TransNodeProcesser();
        String ret = transNodeProcesser.trans("Sxd");
        log.info("ret3:{}",ret);

//        MergeFiles.merge("input","output.txt");
    }
}
