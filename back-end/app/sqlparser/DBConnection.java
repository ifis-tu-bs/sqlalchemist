package sqlparser;

import models.TableDefinition;
import models.TaskSet;
import play.Logger;
import play.Play;

import java.sql.*;
import java.util.*;

/**
 * @author fabiomazzone
 */
class DBConnection{
    private final String dbUrl;

    private final TaskSet taskSet;

    private ResultSet           resultSet;
    private List<Set<String>>  result;

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
    List<String> sqlStatements = this.taskSet.getSQLStatements();

    try {
      for(String sqlStatement : sqlStatements) {
        this.statement.execute(sqlStatement);
      }
    } catch (SQLException e) {
      Logger.error("DBConnection.create: " + e.getMessage());
      return new SQLStatus(e);
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

    public List<Set<String>> getResult() {
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
    private List<Set<String>> transformResultSet(ResultSet rs) throws SQLException {
        List<Set<String>> table    = new ArrayList<>();

        ResultSetMetaData rsmd = rs.getMetaData();
        int columnsNumber = rsmd.getColumnCount();

        Set<String> row = new HashSet<>();
        for(int i = 1; i <= columnsNumber; i++) {
            row.add(rsmd.getColumnLabel(i));
        }
        table.add(row);
        while (rs.next()) {
            row = new HashSet<>();
            for(int i = 1; i <= columnsNumber; i++) {
                row.add(rs.getString(i));
            }
            table.add(row);
        }
        return table;
    }
}
