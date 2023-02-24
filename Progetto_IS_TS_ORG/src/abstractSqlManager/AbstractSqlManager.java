package abstractSqlManager;

import java.sql.Connection;

public abstract class AbstractSqlManager {
    protected String jdbcUrl = DbAccessData.jdbcUrl;
    protected String username = DbAccessData.username;
    protected String password = DbAccessData.password;
    protected Connection connection;
}
