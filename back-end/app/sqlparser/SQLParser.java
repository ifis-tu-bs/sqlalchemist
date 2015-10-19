package sqlparser;

import models.SQLResult;
import models.Task;
import models.TaskSet;
import models.UserStatement;
import play.Logger;

import java.util.List;


/**
 * @author fabiomazzone
 */
public class SQLParser {

    public static SQLStatus createDB(TaskSet taskSet) {
        DBConnection    dbConnection = new DBConnection(taskSet);
        SQLStatus       status;

        if((status = dbConnection.initDBConn()) != null) {
            return status;
        }

        if((status = dbConnection.createDB()) != null) {
            Logger.warn("Cannot Create Tables for TaskSet: " + taskSet.getId());
            dbConnection.deleteDB();
            dbConnection.closeDBConn();
            return status;
        }

        List<Task> tasks = taskSet.getTasks();
        for(Task task : tasks) {
            Logger.debug("run: " + task.getRefStatement());
            if((status = dbConnection.runnable(task.getRefStatement())) != null) {
                Logger.warn("SQLParser.createDB - Statement not runnable: " + task.getRefStatement());
                dbConnection.deleteDB();
                dbConnection.closeDBConn();
                return status;
            }
        }
        dbConnection.deleteDB();
        dbConnection.closeDBConn();
        return null;
    }

    public static SQLResult checkUserStatement(Task task, UserStatement userStatement) {
        DBConnection    dbConnection    = new DBConnection(task.getTaskSet());
        SQLStatus       status;

        if((status = dbConnection.initDBConn()) != null) {
            return new SQLResult(task, status);
        }

        if((status = dbConnection.createDB()) != null) {
            return new SQLResult(task, status);
        }

        if((status = dbConnection.runnable(userStatement.getStatement())) != null) {
            Logger.warn("Statement not runnable: " + task.getRefStatement());
            //dbConnection.deleteDB();
            dbConnection.closeDBConn();
            return new SQLResult(task, status);
        }
        List<List<String>> userStatementResult = dbConnection.getResult();

        if((status = dbConnection.runnable(task.getRefStatement())) != null) {
            Logger.warn("Statement not runnable: " + task.getRefStatement());
            //dbConnection.deleteDB();
            dbConnection.closeDBConn();
            return new SQLResult(task, status);
        }
        List<List<String>> refStatementResult = dbConnection.getResult();




        SQLResult result = new SQLResult(task, SQLResult.SUCCESSFULL);

        DBConnection.printResult(userStatementResult);
        DBConnection.printResult(refStatementResult);
        if(task.getEvaluationStrategy() == Task.EVALUATIONSTRATEGY_LIST) {
            if(userStatementResult.equals(refStatementResult)) {
                result = new SQLResult(task, SQLResult.SUCCESSFULL);
            }
            result = new SQLResult(task, SQLResult.SEMANTICS, "Errrrrorr");
        } else {
            if(refStatementResult.size() != userStatementResult.size()) {
                result = new SQLResult(task, SQLResult.SEMANTICS, "to much or to less columns");
            } else if(refStatementResult.get(0).size() != userStatementResult.get(0).size()) {
                result = new SQLResult(task, SQLResult.SEMANTICS, "to much or to less rows");
            } else {
                for(int i = 0; i < refStatementResult.size(); i++) {
                    List<String> refRow = refStatementResult.get(i);
                    for(int j = 0; j < userStatementResult.size(); j++) {
                        List<String> userRow = userStatementResult.get(j);
                    }
                }
                for(List<String> refColumn : refStatementResult) {
                    List<String> userColumn = null;
                    for(int i = 0; i < userStatementResult.size(); i++) {
                        List<String> userColumnIter = userStatementResult.get(i);
                        if(refColumn.get(0).equals(userColumnIter.get(0))) {
                            Logger.info("Column Match");
                            userColumn = userColumnIter;
                            break;
                        }
                    }
                    if(userColumn == null) {
                        Logger.info("Column not found ! :(");
                        result = new SQLResult(task, SQLResult.SEMANTICS, "Column's do not match");
                        break;
                    }
                    for(String refRow : refColumn) {
                        String userRow = null;
                        for(String userRowIter : userColumn) {
                            if(refRow.equals(userRowIter)) {
                                userRow = userRowIter;
                                Logger.info("Row Match");
                                break;
                            }
                        }
                        if(userRow == null) {
                            Logger.info("Row not found ! :(");
                            result = new SQLResult(task, SQLResult.SEMANTICS, "Row's do not match 1");
                            break;
                        }
                        userColumn.remove(userRow);
                    }
                    if(userColumn.isEmpty()) {
                      userStatementResult.remove(userColumn);
                    } else {
                      result = new SQLResult(task, SQLResult.SEMANTICS, "Row's do not match");
                      break;
                    }
                }
            }
        }

        dbConnection.deleteDB();
        dbConnection.closeDBConn();
        //return new SQLResult(task, SQLResult.SEMANTICS);
        return result;
    }

    public static SQLStatus deleteDB(TaskSet taskSet) {
        DBConnection    dbConnection = new DBConnection(taskSet);
        SQLStatus       status;

        if((status = dbConnection.initDBConn()) != null) {
            return status;
        }

        dbConnection.deleteDB();
        dbConnection.closeDBConn();
        return null;
    }
}
