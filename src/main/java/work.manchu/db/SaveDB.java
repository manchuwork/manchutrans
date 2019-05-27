package work.manchu.db;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import work.manchu.parse.vo.ManchuLineMutilDescVO;
import work.manchu.util.JdbcUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class SaveDB {

    public static void main(String[] args) throws SQLException, IOException {

        Connection conn = JdbcUtil.getConnection();

        log.info("Save db, conn:{}, conn.isClosed:{}",conn, conn.isClosed());



        List<ManchuLineMutilDescVO> list =
                read("output/trans/trans_OldMnc_letterLowerCaseBeginMergeDesc_json.json");

        for(ManchuLineMutilDescVO vo :list){

            try{
                String manchu = vo.getMncOld();
                // # ' ` '  ' ʼ ' '。'，` ~ "'  ’ <,'‘’ ' ` '' "" ` ` '
                //# ` ' ʼ  ＇ '   ' ` ` b'
                manchu = manchu.replace("'","'")
                        .replace("`","'");
                String trans = vo.getMnc();
                String pic = "";
                String chinese = getDesc(vo);
                Long user_id = 2L;
                int row = insert(manchu, trans, pic, chinese, user_id);

                log.info("Save db,row:{} conn:{}, conn.isClosed:{}",row, conn, conn.isClosed());
            }catch (Exception e){
                log.error("Save db, encounter error vo:{}", vo, e);
            }


        }


        JdbcUtil.close(conn);

        log.info("Save db, conn:{}, conn.isClosed:{}", conn, conn.isClosed());
    }

    private static String getDesc(ManchuLineMutilDescVO vo){

        return String.join(" | ", vo.getZhDesc());
    }

    private static List<ManchuLineMutilDescVO> read(String file) throws IOException {
        List<String> list =  Files.readAllLines(Paths.get(file));

        return list.stream().map(o-> JSON.parseObject(o, ManchuLineMutilDescVO.class)).collect(Collectors.toList());
    }

    public static int insert(String manchu, String trans,String pic,String chinese, Long userId) throws SQLException {

        Object[] params = { manchu, trans, chinese, pic, userId};
        String sql = "insert into dicts(manchu, trans, chinese,pic, user_id, created_at, updated_at) values (?,?,?,?,?,now(),now())";

        int row = JdbcUtil.executeUpdate(sql, params);
        log.info("parms:{}, afectRow :{}", params, row);

        return row;
    }

    public static int deleteById(long id) throws SQLException {
        log.info("deleteById:{}", id);

        Object[] params = { id};
        String sql = "delete from dicts where id = ?";

        int row = JdbcUtil.executeUpdate(sql, params);
        log.info("deleteById parms:{}, afectRow :{}", params, row);

        return row;
    }

    public static void updateMncTransById(String mncTrans, long id) throws SQLException {
        log.info("updateMncTransById , trans:{}, id:{}",mncTrans , id);

        Object[] params = { mncTrans, id};
        String sql = "update dicts set trans=? where id = ?";

        int row = JdbcUtil.executeUpdate(sql, params);
        log.info("updateMncTransById parms:{}, afectRow :{}", params, row);

//        return row;
    }

    public static void updateChById(String joinCh, Long id) throws SQLException {
        log.info("updateMncTransById , joinCh:{}, id:{}",joinCh , id);

        Object[] params = { joinCh, id};
        String sql = "update dicts set chinese =? where id = ?";

        int row = JdbcUtil.executeUpdate(sql, params);
        log.info("updateMncTransById parms:{}, afectRow :{}", params, row);
    }

    public static void updateMncById(String mnc_2, Long id) throws SQLException {
        log.info("updateMncById , mnc_2:{}, id:{}",mnc_2 , id);

        Object[] params = { mnc_2, id};
        String sql = "update dicts set manchu =? where id = ?";

        int row = JdbcUtil.executeUpdate(sql, params);
        log.info("updateMncById params:{}, afectRow :{}", params, row);
    }
}
