package com.ilotterytech.spark.util;

import java.sql.*;

/**
 * Created by SJ on 2017-6-16.
 */
public class HiveUtil {

    private static HiveUtil instance = null;

    private String user = "hdfs";
    private String password = "";

    private static String url = "jdbc:hive2://192.168.200.81:10000/db_20170430";

    private HiveUtil() {
    }
    public static HiveUtil getInstance() {
        if (instance == null) {
            synchronized (HiveUtil.class) {
                if (instance == null) {
                    instance = new HiveUtil();
                }
            }
        }
        return instance;
    }

    static {
        try {
            Class.forName("org.apache.hive.jdbc.HiveDriver");
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

}
