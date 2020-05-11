package source;

import java.sql.*;

public class Mysql {

    Connection connection;
    Statement statement;

    public Mysql(String url, String user, String password) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection(url, user, password);
        statement = connection.createStatement();
    }

    public ResultSet setQuery(String query) throws SQLException {
        return statement.executeQuery(query);
    }

    public int updateQuery (String query) throws SQLException {
        return statement.executeUpdate(query);
    }

}
