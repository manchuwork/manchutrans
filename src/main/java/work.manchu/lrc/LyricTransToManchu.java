package work.manchu.lrc;

import lombok.extern.slf4j.Slf4j;
import work.manchu.trans.TransNodeProcesser02;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by heshen on 2018/12/9.
 */
@Slf4j
public class LyricTransToManchu {


    public static void main(String[] args) throws IOException, UnsupportedEncodingException {


        Path pathInputs = Paths.get("lyric/input/");

        String ignore_file_name = LyricTransToManchu.class.getResource("/ignore_file_list.txt").getFile();

        List<String> ignoreFiles = Files.readAllLines(Paths.get(ignore_file_name));

        List<String> errorList = new ArrayList<>();
        Files.walkFileTree(pathInputs,new SimpleFileVisitor<Path>(){
            @Override
            public FileVisitResult visitFile(Path path, BasicFileAttributes attrs)
                    throws IOException {

                for(String ignoreFile : ignoreFiles){
                    if(path.toFile().toString().endsWith(ignoreFile)){
                        return super.visitFile(path, attrs);
                    }
                }

//                if(file.toString().endsWith(".pdf")){
//                    System.out.println(file.getFileName());
//                }


                String o = null;

                try {
                    log.info("opt path:" + path.getFileName());
                    List<String> list = Files.readAllLines(path);

                    List<String> list01 = new ArrayList<>();
                    for (String one : list) {
                        String str = null;

                        o = one;
                        str = trans(one);

                        if (str == null) {
                            continue;
                        }

                        list01.add(str);
                    }

                    Files.write(Paths.get("lyric/output/mnc_" + path.getFileName()+".lrc"), list01);
                } catch (UnsupportedEncodingException e) {
                    errorList.add("o:{} "+o+",fileName:"+ path.getFileName() + ",msg:"+e.getMessage());
                    log.error("UnsupportedEncodingException",e);
                } catch (IOException e) {
                    errorList.add("o:{} "+o+",fileName:"+ path.getFileName() + ",msg:"+e.getMessage());

                    log.error("IOException",e);

                }
                return super.visitFile(path, attrs);
            }
        });

        Files.write(Paths.get("lyric/output/" + "errorList.txt"), errorList);



    }


    private static String trans(String mnc) throws IOException {

        int idx = mnc.lastIndexOf(']');


        if(isContainChinese(mnc)){

            // 含有中文
            // 不处理
            return mnc;

        }else if(isContainMnc(mnc)){
            // 含有满文
            // 不处理
            return mnc;
        }


        String pre = "";
        String after = "";
        if(idx>0){
            pre = mnc.substring(0, idx+1);
            after = mnc.substring(idx+1);
        }else {
            return mnc;
        }


        TransNodeProcesser02 transNodeProcesser = new TransNodeProcesser02();
        return pre + transNodeProcesser.trans(after);
    }

    /**
     * 判断字符串中是否包含中文
     * @param str
     * 待校验字符串
     * @return 是否为中文
     * @warn 不能校验是否为中文标点符号
     */
    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * 判断字符串中是否包含含有满文
     * @param str
     * 待校验字符串
     * @return 是否为中文
     * @warn 不能校验是否为中文标点符号
     */
    public static boolean isContainMnc(String str) {
        //{1800}-x{18AF}
        Pattern p = Pattern.compile("[\u1800-\u18AF]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }



}
