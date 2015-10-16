package sqlparser;

import models.ColumnDefinition;
import models.ForeignKeyRelation;
import models.TableDefinition;
import models.TaskSet;
import play.Logger;
import play.Play;

import java.sql.*;
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

    private SQLStatus initDBConn() {
        try {
            this.connection = DriverManager.getConnection(this.dbUrl);
            return null;
        } catch (SQLException e) {
            Logger.warn(e.getMessage());
            return new SQLStatus(e);
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

    public SQLStatus createDB() {
        SQLStatus status;
        if((status = this.initDBConn()) != null) {
            Logger.info("Cannot initialize DBConnection for TaskSet: " + this.taskSet.getId());
            return status;
        }

        if((status = this.create()) != null){
            Logger.info("Cannot Create Tables for TaskSet: " + this.taskSet.getId());
            this.delete();
            this.closeDBConn();
            return status;
        }

        this.closeDBConn();
        return null;
    }


    public SQLStatus deleteDB() {
        SQLStatus status;
        if((status = this.initDBConn()) != null) {
            Logger.info("Cannot initialize DBConnection for TaskSet: " + this.taskSet.getId());
            return status;
        }
        this.delete();
        this.closeDBConn();
        return null;
    }

    private SQLStatus create() {
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

                Logger.debug(tableDefinition.getExtension());
                List<String> tableExtensions = new ArrayList<>(Arrays.asList(tableDefinition.getExtension().split("\n")));

                for(String tableExtension : tableExtensions) {
                    Logger.debug(tableExtension);
                    stmt.execute(tableExtension);
                }

            } catch (SQLException e) {
                Logger.error("DBConnection.create: " + e.getMessage());
                return new SQLStatus(e);
            }
        }
        for(ForeignKeyRelation foreignKeyRelation : this.taskSet.getForeignKeyRelations()) {
            String addForeignKey = "ALTER TABLE " + foreignKeyRelation.getSourceTable() + " ADD FOREIGN KEY (" + foreignKeyRelation.getSourceColumn() + ") REFERENCES " + foreignKeyRelation.getDestinationTable() + "(" + foreignKeyRelation.getDestinationColumn() + ");";
            Logger.info(addForeignKey);

            // Execute the Statements
            try {
                stmt = connection.createStatement();
                stmt.execute(addForeignKey);
            } catch (SQLException e) {
                Logger.error("DBConnection.create: " + e.getMessage());
                return new SQLStatus(e);
            }
        }
        try {
            ResultSet rs = connection.createStatement().executeQuery("Select * FROM User");
            printResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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


    /**
     * Method tranformResultSet.
     *
     * Transforms a ResultSet into a multidimensional Stringarray.
     *
     * @param rs ResultSet, ResultSet of a SELECT-statement
     * @return List, List containing the name of
     *               the DB-table-column and the associated value
     * @throws java.sql.SQLException, SQLException se
     */
    private ArrayList<ArrayList<String>> transformResultSet(ResultSet rs) throws SQLException {
        ArrayList<ArrayList<String>> result = new ArrayList<>();
        ArrayList<String> resultColumnName = new ArrayList<>();
        ArrayList<String> resultRow;
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnsNumber = rsmd.getColumnCount();

        for (int i = 0; i < columnsNumber; i++) {
            resultColumnName.add(rsmd.getColumnName(i + 1));
        }
        result.add(resultColumnName);

        while (rs.next()) {
            resultRow = new ArrayList<>();
            for (int i = 0; i < columnsNumber; i++) {
                resultRow.add(rs.getString(i + 1));
            }
            result.add(resultRow);
        }

        return result;
    }

    /**
     * Method printResultSet.
     *
     * Prints out the ResultSet of a SELECT-statement.
     *
     * @param rs ResultSet, ResultSet of a SELECT-statement
     * @throws java.sql.SQLException, SQLException se
     */
    private void printResultSet(ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        while (rs.next()) {
            for (int i = 1; i <= columnsNumber; i++) {
                if (i > 1) {
                    Logger.info(",  ");
                }
                Logger.info(rsmd.getColumnName(i) + ": " + rs.getString(i));
            }
            Logger.info("");
        }
    }
}
