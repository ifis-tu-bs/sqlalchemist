package sqlparser;

import models.ColumnDefinition;
import models.ForeignKeyRelation;
import models.TableDefinition;
import models.TaskSet;
import play.Logger;
import play.Play;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author fabiomazzone
 */
public class DBConnection{
    private final String dbUrl;

    private final TaskSet taskSet;

    Connection connection;

    public DBConnection(TaskSet taskSet) {
        this.taskSet = taskSet;

        this.dbUrl = "jdbc:h2:mem:" + taskSet.getId();

        try {
            Class.forName(Play.application().configuration().getString("input.driver"));
        } catch (ClassNotFoundException ex) {
            //Handle errors for Class.forName
            Logger.warn("Class not found");
        }
    }

    public int createDB() {
        int status;
        if((status = this.initDBConn()) != 0) {
            Logger.info("Cannot initialize DBConnection for TaskSet: " + this.taskSet.getId());
            return status;
        }

        if((status = this.create()) != 0){
            Logger.info("Cannot Create Tables for TaskSet: " + this.taskSet.getId());
            this.delete();
            this.closeDBConn();
            return status;
        }

        this.closeDBConn();
        return 0;
    }


    public int deleteDB() {
        int status;
        if((status = this.initDBConn()) != 0) {
            Logger.info("Cannot initialize DBConnection for TaskSet: " + this.taskSet.getId());
            return status;
        }
        this.delete();
        this.closeDBConn();
        return 0;
    }

    private int create() {
        int         status = 0;
        Statement   stmt;

        for(TableDefinition tableDefinition : this.taskSet.getTableDefinitions()) {
            List<String>    primKeys                    = new ArrayList<>();
            String          dataGenSetInsertStatement    = "INSERT INTO " + tableDefinition.getTableName() + "(";
            String          statement                   = "CREATE TABLE " + tableDefinition.getTableName() + "( ";

            for(int i = 0; i < tableDefinition.getColumnDefinitions().size(); i++) {
                ColumnDefinition columnDefinition = tableDefinition.getColumnDefinitions().get(i);
                dataGenSetInsertStatement   = dataGenSetInsertStatement + columnDefinition.getColumnName();
                statement                   = statement + columnDefinition.getColumnName() + " ";
                statement                   = statement + " " + columnDefinition.getDataType();

                if(columnDefinition.isNotNullable()) {
                    statement = statement + " NOT NULL";
                }
                if(columnDefinition.isPrimaryKey()) {
                    primKeys.add(columnDefinition.getColumnName());
                }

                if(i < tableDefinition.getColumnDefinitions().size() -1) {
                    statement = statement + ",";
                    dataGenSetInsertStatement = dataGenSetInsertStatement + ", ";
                }

            }

            dataGenSetInsertStatement = dataGenSetInsertStatement + ") VALUES (";
            // Set Primary Keys if necessary;
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
            // Close the statements
            statement = statement + " );";



            Logger.info(statement);
            Logger.debug("dataGenInsertStatement: " + dataGenSetInsertStatement);

            // Execute the Statements
            try {
                stmt = connection.createStatement();
                stmt.execute(statement);

                List<String> tableExtensions = new ArrayList<>(Arrays.asList(tableDefinition.getExtension().split("\n")));

                for(String tableExtension : tableExtensions) {
                    stmt.execute(tableExtension);
                }
            } catch (SQLException e) {
                Logger.error("DBConnection.create: " + e.getMessage());
                status = e.getErrorCode();
                break;
            }
        }
        //for(ForeignKeyRelation foreignKeyRelation : this.taskSet.getForeignKeyRelations()) {

        //}
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
                Logger.error("DBConnection.delete: " + e.getMessage());
                status = false;
            }
        }
        return status;
    }

    private int initDBConn() {
        try {
            this.connection = DriverManager.getConnection(this.dbUrl);
            return 0;
        } catch (SQLException e) {
            Logger.warn(e.getMessage());
            return e.getErrorCode();
        }
    }

    private int closeDBConn() {
        try {
            this.connection.close();
            return 0;
        } catch (SQLException e) {
            Logger.warn(e.getMessage());
            return e.getErrorCode();
        }
    }
}
