package helper;

import play.Logger;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Invisible on 30.06.2015.
 */
public class HMSAccessor {
    private DatabaseMetaData metaData = null;
    private Connection connection = null;
    private PreparedStatement statement = null;
    private ResultSet resultSet = null;
    private Map<String, String> results = new HashMap<>();

    public boolean identifyUser(String email) {

        try {

            if (!Pattern.matches(".+@tu-b.+\\.de", email)) {
                Logger.info("No need of checking email, it is not a TUBS Mail address");
                return false;
            }


            Logger.info("Starting HMS Request...");
// This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");

            String user = "thesqlalchemist";
            String pw = "irrelevant";
            String connString = "jdbc:mysql://192.168.32.164:3306/HMS";

            // Setup the connection with the DB
            connection = DriverManager
//                    .getConnection("jdbc:mysql://192.168.32.164:3306/HMS?"
//                            + "user=thesqlalchemist&password=irrelevant");
                    .getConnection(connString, user, pw);
            if (connection == null) {
                return false;
            }


            if (Pattern.matches("y[0-9]{7}@tu-b.+\\.de", email)) {
                String ynumber = email.split("@")[0];

                statement = connection.prepareStatement("SELECT mat_no, username From HMS.user WHERE username = ?");
                statement.setString(1, ynumber);

                resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    results.put("ynumber", resultSet.getString("username"));
                    results.put("matnumber", resultSet.getString("mat_no"));
                    return true;
                } else {
                    return false;
                }
            } else if (Pattern.matches(".+\\..+@tu-b.+\\.de", email)) {
                statement = connection.prepareStatement("SELECT mat_no, username FROM HMS.user WHERE email = ?");

                statement.setString(1, email);

                resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    results.put("ynumber", resultSet.getString("username"));
                    results.put("matnumber", resultSet.getString("mat_no"));
                    return true;
                } else {
                    return false;
                }

            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public Map<String, String> getResults () {
        return results;
    }
}
