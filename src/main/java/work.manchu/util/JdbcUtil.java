package work.manchu.util;

import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Jdbc工具类
 * 数据库连接管理类<p>
 * 1. getConnection() 从连接缓冲池中获取一个数据库连接<p>
 * 2. closeConnection( connection ) 把一个数据库连接归还到缓冲池
 */
@Slf4j
public class JdbcUtil {
    /**
     * 是否使用事务
     */
    private static ThreadLocal tlIsAutoCommit = new ThreadLocal();
    /***保存数据库的连接*/
    private static ThreadLocal tlconn = new ThreadLocal();

    /***保存内存数据库的连接*/
    private static ThreadLocal tlNemConn = new ThreadLocal();


    public static final  String DRIVERNAME = "com.mysql.jdbc.Driver";
    //?useUnicode=true&characterEncoding=utf-8&useSSL=false&allowMultiQueries=true
    public static final  String URL = "jdbc:mysql://localhost:3306/manchu?useUnicode=true&characterEncoding=utf-8&useSSL=false&allowMultiQueries=true";
    public static final  String USER = "root";
    public static final  String PASSWORD = "heshen";
    /**
     * 单例模式工作
     */
    private JdbcUtil() {

    }

    static {
        try {
            Class.forName(DRIVERNAME);
        } catch (ClassNotFoundException e) {
            log.error("init database drive error. DRIVERNAME:{}", DRIVERNAME, e);
        }
    }

    /**
     * 从连接缓冲池中返回一个可用的数据库连接
     *
     * @return 数据库连接对象, 如果没有可用数据库连接/无法建立新的数据库连接, 返回null
     */
    public static synchronized Connection getConnection() {

        try {

            // 2. 获得连接


            if (tlIsAutoCommit.get() != null) {
                if (tlconn.get() == null) {

                    Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
                    con.setAutoCommit(false);
                    tlconn.set(con);
                }
                return (Connection) tlconn.get();
            }
            // 返回连接
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            log.error(e.getMessage());
            return null;
        }
    }



    public static int getCountBySql(String sql) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            rs.next();
            return rs.getBigDecimal(1).intValue();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw e;
        } finally {
            close(ps, rs, con);
        }
    }

    public static int getCountBySql(Connection con, String sql) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            rs.next();
            return rs.getBigDecimal(1).intValue();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw e;
        } finally {
            close(ps, rs, con);
        }
    }

    /**
     * 返回resultSet中的数据List
     *
     * @return  List&lt;Object[]>
     */
    public static List<Object[]> getListFromResultSet(ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        List<Object[]> list = new ArrayList<Object[]>();
        while (rs.next()) {
            Object[] result = new Object[columnCount];
            for (int i = 0; i < columnCount; i++) {
                result[i] = rs.getObject(i + 1);
            }
            list.add(result);
        }
        return list;
    }

    /**
     * 返回resultSet中的数据List,传入的可滚动的 ResultSet 对象
     *
     * @param rs 可滚动的 ResultSet 对象
     * @return list<Object   [   ]>
     */
    public static List getListFromResultSet(ResultSet rs, int offset, int size) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        List list = new ArrayList();
        if (offset > 0) {
            rs.absolute(offset);
        }

        int r = 0;
        while (rs.next() && r < size) {
            Object[] result = new Object[columnCount];
            for (int i = 0; i < columnCount; i++) {
                result[i] = rs.getObject(i + 1);
            }
            list.add(result);
            r++;
        }
        return list;
    }

    /**
     * 从oracle数据库执行查询语句,采用oracle分页
     */
    public static List getListOraclePageBySql(String sql, int offset, int size) throws SQLException {
        StringBuffer sqlBufferPage = new StringBuffer();
        sqlBufferPage.append("select /*+ FIRST_ROWS */ * from (select row_.*, rownum rownum_ from ( ");
        sqlBufferPage.append(sql);
        sqlBufferPage.append(" ) row_ where rownum <=" + (offset + size) + ") where rownum_ >" + offset);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List list = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(sqlBufferPage.toString());
            rs = ps.executeQuery();
            list = getListFromResultSet(rs);
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw e;
        } finally {
            close(ps, rs, con);
        }
        return list;

    }

    /**
     * 采用jdbc分页
     */
    public static List getListBySql(String sql, int offset, int size) throws SQLException {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List list = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = ps.executeQuery();
            list = getListFromResultSet(rs, offset, size);
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw e;
        } finally {
            close(ps, rs, con);
        }
        return list;

    }

    /**
     * 默认的数据源Oracle
     */
    public static List getListBySql(String sql) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List list = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            list = getListFromResultSet(rs);
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw e;
        } finally {
            close(ps, rs, con);
        }
        return list;
    }

    public static List getListBySql(Connection con, String sql) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List list = null;
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            list = getListFromResultSet(rs);
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw e;
        } finally {
            close(ps, rs, con);
        }
        return list;
    }

    /**
     * 从oracle数据库执行查询语句,采用oracle分页
     */
    public static List getListOraclePageBySql(Connection con, String sql, int offset, int size) throws SQLException {
        StringBuffer sqlBufferPage = new StringBuffer();
        sqlBufferPage.append("select /*+ FIRST_ROWS */ * from (select row_.*, rownum rownum_ from ( ");
        sqlBufferPage.append(sql);
        sqlBufferPage.append(" ) row_ where rownum <=" + (offset + size) + ") where rownum_ >" + offset);

        PreparedStatement ps = null;
        ResultSet rs = null;
        List list = null;
        try {
            ps = con.prepareStatement(sqlBufferPage.toString());
            rs = ps.executeQuery();
            list = getListFromResultSet(rs);
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw e;
        } finally {
            close(ps, rs, con);
        }
        return list;

    }

    /**
     * 采用jdbc分页
     */
    public static List getListBySql(Connection con, String sql, int offset, int size) throws SQLException {

        PreparedStatement ps = null;
        ResultSet rs = null;
        List list = null;
        try {
            ps = con.prepareStatement(sql);
            ps = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = ps.executeQuery();
            list = getListFromResultSet(rs, offset, size);
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw e;
        } finally {
            close(ps, rs, con);
        }
        return list;

    }

    public static int executeUpdate(String sql, Object... params) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(sql);
            int i = 1;
            for(Object object : params){
                if(object instanceof Long){
                    ps.setLong(i, (Long)object);
                }else if(object instanceof Integer){
                    ps.setInt(i, (Integer)object);
                }else if(object instanceof String){
                    ps.setString(i, (String)object);
                }else if(object instanceof Double){
                    ps.setDouble(i, (Double)object);
                }else if(object instanceof Float){
                    ps.setFloat(i, (Float)object);
                }else if(object instanceof Short){
                    ps.setShort(i, (Short)object);
                }else if(object instanceof Date){
                    ps.setDate(i, new java.sql.Date(((Date) object).getTime()));
                }else if(object instanceof java.sql.Date){
                    ps.setDate(i, (java.sql.Date) object);
                }else{
                    throw new UnsupportedOperationException("type: object:"+object);
                }

                i++;
            }
            return ps.executeUpdate();
        } catch (SQLException e) {
            log.error("execute sql:{}, params:{}",sql, params, e);
            throw e;
        } finally {
            close(ps, null, con);
        }
    }

    public static int executeUpdate(Connection con, String sql) throws SQLException {


        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            return ps.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw e;
        } finally {
            close(ps, null, con);
        }

    }

    public static int executeUpdate(String sql) throws SQLException {

        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(sql);
            return ps.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw e;
        } finally {
            close(ps, null, con);
        }

    }

    /**
     * 返回一个'数据库连接'到缓冲池
     *
     * @param conn 数据库连接对象
     */
    public static synchronized void close(Connection conn) {
        if (conn != null && tlIsAutoCommit.get() == null) {
            try {
                conn.close();
            } catch (SQLException e) {
                log.error(e.getMessage());
            }
            conn = null;
        }
    }

    /**
     * 关闭连接
     */
    public static void close(PreparedStatement ps, ResultSet rs, Connection conn) {

        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {

            }
            rs = null;
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {

            }
            ps = null;
        }
        if (conn != null && tlIsAutoCommit.get() == null) {
            try {
                conn.close();
            } catch (SQLException e) {
                log.error(e.getMessage());
            }
            conn = null;
        }
    }

    /**
     * 关闭连接
     */
    public static void close(Statement sta, ResultSet rs, Connection conn) {

        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {

            }
            rs = null;
        }
        if (sta != null) {
            try {
                sta.close();
            } catch (SQLException e) {

            }
            sta = null;
        }
        if (conn != null && tlIsAutoCommit.get() == null) {
            try {
                conn.close();
            } catch (SQLException e) {
                log.error(e.getMessage());
            }
            conn = null;
        }
    }

    public static void close(PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {

            }
            ps = null;
        }
    }

    public static void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {

            }
            rs = null;
        }
    }

    /**
     * 是否启用事务
     *
     * @bool false 启用事务
     */
    public static void setAutoCommit(boolean bool) {
        if (bool == false) {
            tlIsAutoCommit.set(false);
        }
    }

    /**
     * 提交事务
     */
    public static void commit() {
        if (tlconn.get() != null) {
            Connection con = (Connection) tlconn.get();
            try {
                con.commit();
            } catch (SQLException e) {
                log.error("commit failed", e);
            }

            try {
                con.close();
            } catch (SQLException e) {
                log.error("close connection failed", e);
            }
            tlIsAutoCommit.remove();
            tlconn.remove();
        }

        if (tlNemConn.get() != null) {
            Connection con = (Connection) tlNemConn.get();
            try {
                con.commit();
            } catch (SQLException e) {
                log.error("commit failed", e);
            }

            try {
                con.close();
            } catch (SQLException e) {
                log.error("close connection failed", e);
            }
            tlIsAutoCommit.remove();
            tlNemConn.remove();
        }
    }

    /**
     * 回滚事务
     */
    public static void rollback() {
        if (tlconn.get() != null) {
            Connection con = (Connection) tlconn.get();
            try {
                con.rollback();
            } catch (SQLException e) {
                log.error("commit failed", e);
            }
            tlIsAutoCommit.remove();
            tlconn.remove();
            try {
                con.close();
            } catch (SQLException e) {
                log.error("close connection failed", e);
            }
        } else {
            log.error("rollback error,the connection is null");
        }

        if (tlNemConn.get() != null) {
            Connection con = (Connection) tlNemConn.get();
            try {
                con.rollback();
            } catch (SQLException e) {
                log.error("commit failed", e);
            }
            tlIsAutoCommit.remove();
            tlNemConn.remove();
            try {
                con.close();
            } catch (SQLException e) {
                log.error("close connection failed", e);
            }
        }
    }


}