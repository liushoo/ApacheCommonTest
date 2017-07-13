package com.ilotterytech.spark.util;

import java.sql.*;
import java.util.HashMap;


/**
 * Created by SJ on 2017-6-16.
 */
public class JDBCUtil {

    private static JDBCUtil instance = null;

    private String user = "qgk";
    private String password = "lottery2017";

    private static String url = "jdbc:mysql://192.168.200.2:3306/stats";

    private JDBCUtil() {
    }

    public static JDBCUtil getInstance() {
        if (instance == null) {
            synchronized (JDBCUtil.class) {
                if (instance == null) {
                    instance = new JDBCUtil();
                }
            }
        }
        return instance;
    }

    static {
        try {
            System.out.println("loading driver");
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public void free(ResultSet rs, Statement st, Connection conn) {
        try {
            if (rs != null)
                rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (st != null)
                    st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (conn != null)
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
            }
        }
    }

    public static void main(String[] args) {
        String sql="select * from core_dictionary where dict_code='SSQWF' and use_flag=1";
        String key = "dict_code";
        String val = "id";

        JDBCUtil util = JDBCUtil.getInstance();

        try {
            Connection conn = util.getConnection();
            System.out.println("conn" + conn);
            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);
            HashMap<String, String> map = new HashMap<String, String>();

//            System.out.println(resultSet.next());
            while(resultSet.next()) {
                System.out.println("-------------"+key);
                StringBuffer keyBuf = new StringBuffer();

                for(String k: key.split(",", -1)){
                    System.out.println(resultSet.getString(k));
                    keyBuf.append(resultSet.getString(k)).append("\t");
                }
                String mapVal = resultSet.getString(val);
                map.put(keyBuf.toString(), mapVal.toString());

            }
            System.out.println(map.toString());

        }catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

    }

}
