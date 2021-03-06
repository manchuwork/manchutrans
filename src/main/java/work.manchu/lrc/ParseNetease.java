package work.manchu.lrc;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import work.manchu.trans.TransNodeProcesser02;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public class ParseNetease {

    public static void main(String[] args) throws IOException, UnsupportedEncodingException {


        Path pathInputs = Paths.get("lyric/netease/");
        Files.walkFileTree(pathInputs,new SimpleFileVisitor<Path>(){
            @Override
            public FileVisitResult visitFile(Path path, BasicFileAttributes attrs)
                    throws IOException {
//                if(file.toString().endsWith(".pdf")){
//                    System.out.println(file.getFileName());
//                }

                try {
                    log.info("opt path:" + path.getFileName());
                    List<Map> oriList = read(path);
                    List<String> list = oriList.stream()
                            .map(o->(String)o.get("lyric")).collect(Collectors.toList());


                    List<String> list01 = new ArrayList<>();
                    for (String o : list) {
                        String str = null;

                        str = trans(o);

                        if (str == null) {
                            continue;
                        }

                        list01.add(str);
                    }

                    Files.write(Paths.get("lyric/input/netease_" + path.getFileName()), list01);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return super.visitFile(path, attrs);
            }
        });


    }

    private static List<Map> read(Path file) throws IOException {
        List<String> list =  Files.readAllLines(file);

        return list.stream().map(o-> JSON.parseObject(o, HashMap.class)).collect(Collectors.toList());
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

