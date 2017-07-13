package com.ilotterytech.sqoop;

import java.io.IOException;
import java.sql.*;

/**
 * Created by liush on 17-6-5.
 */
public class TestMysqlConnection {
    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        String url = "jdbc:mysql://192.168.200.2/azkaban";
        String name = "com.mysql.jdbc.Driver";
        String user = "azkaban";
        String password = "czkjAzkaban1234";
        String sql = "select * from execution_flows";//SQL语句
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet ret = null;
        //指定连接类型
        final Class<?> aClass = Class.forName(name);
        conn = DriverManager.getConnection(url, user, password);//获取连接
        pst=conn.prepareStatement(sql);//
        ret=pst.executeQuery();
        while (ret.next()) {
            String uid = ret.getString(1);
            System.out.println("===="+uid);

        }
    }
}
