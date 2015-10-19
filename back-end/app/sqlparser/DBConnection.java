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
class DBConnection{
    private final String dbUrl;

    private final TaskSet taskSet;

    private ResultSet           resultSet;
    private List<List<String>>  result;

    private Connection  connection;
    private Statement   statement;

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

    public SQLStatus initDBConn() {
        try {
            this.connection = DriverManager.getConnection(this.dbUrl);
            this.statement  = this.connection.createStatement();
            return null;
        } catch (SQLException e) {
            Logger.warn(e.getMessage());
            this.connection = null;
            return new SQLStatus(e);
        }
    }

    public SQLStatus createDB() {
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



            Logger.debug(statement) ;
            Logger.debug("DBConnection.create - dataGenInsertStatement: " + dataGenSetInsertStatement);

            // Execute the Statements
            try {
                this.statement.execute(statement);

                List<String> tableExtensions = new ArrayList<>(Arrays.asList(tableDefinition.getExtension().split("\n")));

                for(String tableExtension : tableExtensions) {
                    Logger.debug(tableExtension);
                    this.statement.execute(tableExtension);
                }

            } catch (SQLException e) {
                Logger.error("DBConnection.create: " + e.getMessage());
                return new SQLStatus(e);
            }
        }
        for(ForeignKeyRelation foreignKeyRelation : this.taskSet.getForeignKeyRelations()) {
            String addForeignKey = "ALTER TABLE " + foreignKeyRelation.getSourceTable() + " ADD FOREIGN KEY (" + foreignKeyRelation.getSourceColumn() + ") REFERENCES " + foreignKeyRelation.getDestinationTable() + "(" + foreignKeyRelation.getDestinationColumn() + ");";
            Logger.debug(addForeignKey) ;

            // Execute the Statements
            try {
                this.statement.execute(addForeignKey);
            } catch (SQLException e) {
                Logger.error("DBConnection.create: " + e.getMessage());
                return new SQLStatus(e);
            }
        }
        return null;
    }

    public SQLStatus runnable(String statement) {
        try {
            this.resultSet = this.statement.executeQuery(statement);
            this.result = this.transformResultSet(this.resultSet);
        } catch (SQLException e) {
            Logger.warn("DBConnection.runnable - Statement not runnable: " + statement);
            return new SQLStatus(e);
        }
        return null;
    }

    public boolean deleteDB() {
        boolean status = true;
        for(TableDefinition tableDefinition : this.taskSet.getTableDefinitions()) {
            try {
                String statement = "DROP TABLE IF EXISTS " + tableDefinition.getTableName() + ";";

                this.statement.execute(statement);

                Logger.debug(statement);
            } catch (SQLException e) {
                Logger.error("DBConnection.delete: " + e.getMessage());
                status = false;
            }
        }
        return status;
    }

    public int closeDBConn() {
        try {
            if(this.resultSet != null) {
                this.resultSet.close();
            }
            this.statement.close();
            this.connection.close();
            return 0;
        } catch (SQLException e) {
            Logger.warn(e.getMessage());
            return e.getErrorCode();
        }
    }

    public List<List<String>> getResult() {
        return this.result;
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
    private List<List<String>> transformResultSet(ResultSet rs) throws SQLException {
        List<List<String>> table    = new ArrayList<>();

        ResultSetMetaData rsmd = rs.getMetaData();
        int columnsNumber = rsmd.getColumnCount();

        for(int i = 1; i <= columnsNumber; i++) {
            List<String> column = new ArrayList<>();
            column.add(rsmd.getColumnName(i));
            table.add(column);
        }
        while (rs.next()) {
            for(int i = 0; i < table.size(); i++) {
                List<String> column = table.get(i);
                column.add(rs.getString(i+1));
            }
        }
        return table;
    }

    /**
     * Method printResultSet.
     *
     * Prints out the ResultSet of a SELECT-statement.
     *
     * @throws java.sql.SQLException, SQLException se
     */
    private void printResult() throws SQLException {
        for(List<String> column : this.result) {
            column.forEach(Logger::info);
        }
    }
}
