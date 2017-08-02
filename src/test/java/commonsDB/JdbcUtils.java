package commonsDB;

import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.commons.dbutils.DbUtils;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by liush on 17-7-28.
 */
public class JdbcUtils {
    private static DataSource ds = null;

    static {
        try {
            Properties prop = new Properties();
            InputStream in = JdbcUtils.class.getClassLoader().getResourceAsStream("dbcpconfig.properties");
            prop.load(in);
            BasicDataSourceFactory factory = new BasicDataSourceFactory();
            ds = factory.createDataSource(prop);
            System.out.println(ds.getConnection().getMetaData().getDatabaseMajorVersion()+"===="+ds);
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static DataSource getDataSource() {
        return ds;
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public int update(String sql) throws SQLException {
        Connection conn = this.prepareConnection();

        return this.update(conn, true, sql, (Object[]) null);
    }
    private int update(Connection conn, boolean closeConn, String sql, Object... params) throws SQLException {
        if (conn == null) {
            throw new SQLException("Null connection");
        }

        if (sql == null) {
            if (closeConn) {
                DbUtils.close(conn);
            }
            throw new SQLException("Null SQL statement");
        }

        PreparedStatement stmt = null;
        int rows = 0;

        try {
            stmt = this.prepareStatement(conn, sql);
            rows = stmt.executeUpdate();

        } catch (SQLException e) {
            throw new SQLException("Null SQL statement");

        } finally {
            DbUtils.close(stmt);
            if (closeConn) {
                DbUtils.close(conn);
            }
        }

        return rows;
    }

    protected PreparedStatement prepareStatement(Connection conn, String sql)
            throws SQLException {

        return conn.prepareStatement(sql);
    }

    protected Connection prepareConnection() throws SQLException {
        if (this.getDataSource() == null) {
            throw new SQLException(
                    "QueryRunner requires a DataSource to be "
                            + "invoked in this way, or a Connection should be passed in");
        }
        return this.getDataSource().getConnection();
    }
    /*
     * 工具类里面现在没有必要提供release()方法，因为我们是使用dbutils操作数据库，
     * 即调用dbutils的update()和query()方法操作数据库，他操作完数据库之后，会自动释放掉连接。
     */
}
