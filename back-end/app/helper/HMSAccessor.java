package helper;

import play.Logger;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Invisible
 */
public class HMSAccessor {
    private DatabaseMetaData metaData = null;
    private Connection connection = null;
    private PreparedStatement statement = null;
    private ResultSet resultSet = null;
    private final Map<String, String> results = new HashMap<>();

    public boolean identifyUser(String ynumber) {
        Logger.info("Starting HMS Request...");
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        Logger.info("ok");
        String user = "thesqlalchemist";
        String pw = "irrelevant";
        String connString = "jdbc:mysql://192.168.32.164:3306/HMS";

        try {
            connection = DriverManager
                    .getConnection(connString, user, pw);

            if (connection == null) {
                return false;
            }

            statement = connection.prepareStatement("SELECT mat_no, username From HMS.user WHERE username = ?");
            statement.setString(1, ynumber);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                results.put("username", resultSet.getString("username"));
                results.put("mat_no", resultSet.getString("mat_no"));
                connection.close();
                return true;
            } else {
                connection.close();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Map<String, String> getResults () {
        return results;
    }
}
