package sqlManager.manager;

import abstractSqlManager.AbstractSqlManager;

import java.sql.*;

public class SqlManagerUtility extends AbstractSqlManager {


    public SqlManagerUtility() {
        try {
            connection = DriverManager.getConnection(jdbcUrl, username, password);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int getMaxUnitCode() {
        int ret = -1;
        try {
            String sql = "SELECT max(code) FROM units";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next())
                ret = resultSet.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }
}
