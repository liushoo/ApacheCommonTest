package commonsDB;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by liush on 17-7-28.
 */
public class Demo2 {

    @Test
    public void queryTest() throws SQLException {
        //ArrayHandler：把结果集中的第一行数据转成对象数组
        QueryRunner runner = new QueryRunner(JdbcUtils.getDataSource());


     List<Object[]> list=runner.query("select * from core_dictionary", new ArrayListHandler());
        for(Object[] os : list){
            for(Object o : os){
                System.out.println(o);
            }
        }


    }

    @Test
    public void countMysqlTest() throws SQLException {
        //ArrayHandler：把结果集中的第一行数据转成对象数组
        QueryRunner runner = new QueryRunner(JdbcUtils.getDataSource());
        int topicNum=0;
        Object[] params={"ZGZT"};
        String sql ="select count(*) from core_dictionary where dict_code=? order by id desc";
        topicNum=(int)(long) runner.query(sql,new ScalarHandler(),params);
        System.out.println("========"+topicNum);

    }

    @Test
    public void counHiveTest() throws SQLException {
        //ArrayHandler：把结果集中的第一行数据转成对象数组
        QueryRunner runner = new QueryRunner(JdbcUtils.getDataSource(),true);
        int topicNum=0;
        Object[] params={"2016127"};
        String sql ="select count(a.id) as count from QL730_ticket as a where a.ds=?";
        topicNum=(int)(long) runner.query(sql,new ScalarHandler(),params);
        System.out.println("========"+topicNum);

    }
    @Test
    public void find(){
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            conn = JdbcUtils.getConnection();
            String sql = "select count(a.id) as count from testdb.QL730_ticket as a where ds= ?";
            st = conn.prepareStatement(sql);
            st.setInt(1, 2016127);
            rs = st.executeQuery();
            if(rs.next()){
                System.out.println(rs.getInt("count"));
            }
        }catch (Exception e) {

        }finally{
           // JdbcUtils.release(conn, st, rs);
        }
    }

    @Test
    public void test1() throws SQLException {
        //ArrayHandler：把结果集中的第一行数据转成对象数组
        QueryRunner runner = new QueryRunner(JdbcUtils.getDataSource());
        String sql = "select * from ql730_selldata";
        Object[] result = runner.query(sql, new ArrayHandler());
        System.out.println(result[0]);
        System.out.println(result[1]);
    }
    @Test
    public void test2() throws SQLException {
        //ArrayListHandler：把结果集中的每一行数据都转成一个数组，再存放到List中

        QueryRunner runner = new QueryRunner(JdbcUtils.getDataSource(),true);
        String sqlSelldata="select  count(a.station) as count,sum(a.order_num) as sum from ql730_selldata as a where ds=2016127";
        String sql = "select * from ql730_selldata";
        List<Object[]> lista = runner.query(JdbcUtils.getConnection(), sqlSelldata, new ArrayListHandler());

       // List list = runner.query(sql, new ArrayListHandler());
       // System.out.println(lista.get(0));
       // System.out.println(lista.get(1));
        int selldataSum=-1;
        int selldataCount=-1;
        for(Object[] os : lista){
            selldataSum=Integer.valueOf(os[0].toString());
            selldataCount=Integer.valueOf(os[1].toString());
            System.out.println(os[0]+"==="+selldataSum);
            System.out.println(os[1]+"==="+selldataCount);
          /*  for(Object o : os){

                System.out.println(o);
            }*/
        }
        //System.out.println(lista);
    }

    @Test
    public void test3() throws SQLException {
        //ColumnListHandler：将结果集中某一列的数据存放到List中
        QueryRunner runner = new QueryRunner(JdbcUtils.getDataSource());
        String sql = "select * from ql730_selldata";
        List list = (List) runner.query(sql, new ColumnListHandler("station"));
        System.out.println(list);
    }
    @Test
    public void test5() throws SQLException {
        QueryRunner runner = new QueryRunner(JdbcUtils.getDataSource());
        String sql = "select count(*) from ql730_selldata";
        int totalrecord = ((Long)runner.query(sql, new ScalarHandler(13))).intValue();
        System.out.println(totalrecord);
    }
}
