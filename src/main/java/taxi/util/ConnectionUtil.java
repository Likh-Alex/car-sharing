package taxi.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionUtil {
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Can't find SQL Driver", e);
        }
    }

    public static Connection getConnection() {
        Properties dbCredentials = new Properties();
        dbCredentials.put("user", "root");
        dbCredentials.put("password", "1234");
        String url = "jdbc:mysql://localhost:3306/taxi_service?serverTimezone=UTC";

        try {
            return DriverManager.getConnection(url, dbCredentials);
        } catch (SQLException e) {
            throw new RuntimeException("Can not establish the connection to the DB", e);
        }
    }
}
