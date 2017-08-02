package commonsDB;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Created by liush on 17-7-28.
 */
public class dbDemo1 {
    // 使用dbutils完成数据库的CRUD
    @Test
    public void insert() throws SQLException {
        QueryRunner runner = new QueryRunner(JdbcUtils.getDataSource());
        String sql = "insert into users(id,name,password,email,birthday) values(?,?,?,?,?)";
        Object[] params = {2, "bbb", "123", "bbb@163.com", new Date()};
        runner.update(sql, params);
    }

    @Test
    public void update() throws SQLException {
        QueryRunner runner = new QueryRunner(JdbcUtils.getDataSource());
        String sql = "update users set email=? where id=?";
        Object[] params = {"yeyiyi@126.com", 1};
        runner.update(sql, params);
    }

    @Test
    public void delete() throws SQLException {
        QueryRunner runner = new QueryRunner(JdbcUtils.getDataSource());
        String sql = "delete from users where id=?";
        runner.update(sql, 2);
    }

    @Test
    public void find() throws SQLException {
        QueryRunner runner = new QueryRunner(JdbcUtils.getDataSource());
       // String sql = "select * from users where ds=?";
        String sql = "select * from base_lottery_station where id=?";

        User u = (User)runner.query(sql,new BeanHandler(User.class),1);
        System.out.println(u.getEmail());
        //User user = (User) runner.query(sql, new BeanHandler(User.class),1);
       // System.out.println(user.getEmail());
    }

    @Test
    public void getAll() throws SQLException {
        QueryRunner runner = new QueryRunner(JdbcUtils.getDataSource());
        String sql = "select * from users";
        List list = (List) runner.query(sql, new BeanListHandler(User.class));
        System.out.println(list);
    }

    @Test
    public void batch() throws SQLException {
        QueryRunner runner = new QueryRunner(JdbcUtils.getDataSource());
        String sql = "insert into users(id,name,password,email,birthday) values(?,?,?,?,?)";
        Object[][] params = new Object[3][5];
        for (int i = 0; i < params.length; i++) {
            params[i] = new Object[]{i+1, "aa"+i, "123", i+"@sian.com", new Date()};
        }
        runner.batch(sql, params);
    }
}

