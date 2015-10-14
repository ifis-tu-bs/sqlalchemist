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
import java.util.ArrayList;
import java.util.List;

/**
 * @author fabiomazzone
 */
public class DBConnection{
    private long id;
    private String dbUrl;

    private TaskSet taskSet;

    Connection connection;

    private static final int CONNECTION_ERROR = -1;
    private static final int CREATE_TABLE_ERROR = -2;

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

    public int recreateDB() {
        if(!this.initDBConn()) {
            Logger.info("Cannot initialize DBConnection for TaskSet: " + this.taskSet.getId());
            return DBConnection.CONNECTION_ERROR;
        }

        if(!this.create()){
            Logger.info("Cannot Create Tables for TaskSet: " + this.taskSet.getId());
            this.delete();
            this.closeDBConn();
            return DBConnection.CREATE_TABLE_ERROR;
        }

        this.closeDBConn();
        return 0;
    }


    public int deleteDB() {
        if(!this.initDBConn()) {
            Logger.info("Cannot initialize DBConnection for TaskSet: " + this.taskSet.getId());
            return DBConnection.CONNECTION_ERROR;
        }
        this.delete();
        this.closeDBConn();
        return 0;
    }

    private boolean create() {
        boolean status = true;
        Statement stmt;
        for(TableDefinition tableDefinition : this.taskSet.getTableDefinitions()) {
            List<String> primKeys = new ArrayList<>();
            String statement = "CREATE TABLE " + tableDefinition.getTableName() + "( ";
            for(int i = 0; i < tableDefinition.getColumnDefinitions().size(); i++) {
                ColumnDefinition columnDefinition = tableDefinition.getColumnDefinitions().get(i);
                statement = statement + columnDefinition.getColumnName() + " ";
                statement = statement + " " + columnDefinition.getDataType();
                if(columnDefinition.isNotNullable()) {
                    statement = statement + " NOT NULL";
                }
                if(columnDefinition.isPrimaryKey()) {
                    primKeys.add(columnDefinition.getColumnName());
                }
                if(i < tableDefinition.getColumnDefinitions().size() -1 )
                    statement = statement + ",";
            }
            if(primKeys.size() > 0) {
                String primaryKey = ", PRIMARY KEY (";
                for (int i = 0; i < primKeys.size(); i++) {
                    String primKey = primKeys.get(i);
                    primaryKey = primaryKey + primKey;
                    if(i < primKeys.size() - 1)
                        primaryKey = primaryKey + ",";
                }
                primaryKey = primaryKey + ")";
                statement = statement + primaryKey;
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

    private boolean delete() {
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