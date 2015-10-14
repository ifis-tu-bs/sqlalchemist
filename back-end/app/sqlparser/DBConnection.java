package sqlparser;

import models.ColumnDefinition;
import models.TableDefinition;
import models.TaskSet;
import play.Logger;
import play.Play;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author fabiomazzone
 */
public class DBConnection{
    private long id;
    private String dbUrl;

    private TaskSet taskSet;

    Connection connection;


    public DBConnection(TaskSet taskSet) {
        this.taskSet = taskSet;

        this.dbUrl = "jdbc:h2:" + Play.application().configuration().getString("input.dbsPath") + taskSet.getId();

        Logger.info("sqlparser.DBConnection.dbUrl: " + this.dbUrl);

        try {
            Class.forName(Play.application().configuration().getString("input.driver"));
        } catch (ClassNotFoundException ex) {
            //Handle errors for Class.forName
            Logger.warn("Class not found");
        }
    }

    public boolean recreateDB() {
        boolean status;
        status = this.initDBConn();

        status = this.create();

        if(!status) {
            this.delete();
        }


        this.closeDBConn();

        return status;
    }


    public void deleteDB() {
        this.initDBConn();

        this.delete();


        this.closeDBConn();

    }

    private boolean create() {
        boolean status = true;
        Statement stmt;
        for(TableDefinition tableDefinition : this.taskSet.getTableDefinitions()) {
            String statement = "CREATE TABLE " + tableDefinition.getTableName() + "( ";
            for(int i = 0; i < tableDefinition.getColumnDefinitions().size(); i++) {
                ColumnDefinition columnDefinition = tableDefinition.getColumnDefinitions().get(i);
                statement = statement + columnDefinition.getColumnName() + " ";
                statement = statement + " " + columnDefinition.getDataType();

                if(i < tableDefinition.getColumnDefinitions().size() -1 )
                    statement = statement + ", ";
            }
            statement = statement + " );";

            Logger.info(statement);
            try {
                stmt = connection.createStatement();
                stmt.execute(statement);
            } catch (SQLException e) {
                Logger.error("DBConnection.deleteDB: " + e.getMessage());
                status = false;
            }
        }
        return status;
    }

    public boolean delete() {
        boolean status = true;
        Statement stmt;
        for(TableDefinition tableDefinition : this.taskSet.getTableDefinitions()) {
            try {
                stmt = connection.createStatement();
                String statement = "DROP TABLE IF EXISTS " + tableDefinition.getTableName() + ";";

                stmt.execute(statement);

                Logger.info(statement);
            } catch (SQLException e) {
                Logger.error("DBConnection.deleteDB: " + e.getMessage());
                status = false;
            }
        }
        return status;
    }

    private boolean initDBConn() {
        try {
            this.connection = DriverManager.getConnection(this.dbUrl);
            return true;
        } catch (SQLException e) {
            Logger.warn(e.getSQLState());
            return false;
        }
    }

    private void closeDBConn() {
        try {
            this.connection.close();
        } catch (SQLException e) {
            Logger.warn(e.getSQLState());
        }
    }
}
