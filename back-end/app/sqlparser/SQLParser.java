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
                Logger.warn("Statement not runnable: " + task.getRefStatement());
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

        if((status = dbConnection.runnable(task.getRefStatement())) != null) {
            Logger.warn("Statement not runnable: " + task.getRefStatement());
            //dbConnection.deleteDB();
            dbConnection.closeDBConn();
            return new SQLResult(task, status);
        }

        List<List<String>> refStatementResult = dbConnection.getResult();

        if((status = dbConnection.runnable(userStatement.getStatement())) != null) {
            Logger.warn("Statement not runnable: " + task.getRefStatement());
            //dbConnection.deleteDB();
            dbConnection.closeDBConn();
            return new SQLResult(task, status);
        }

        List<List<String>> userStatementResult = dbConnection.getResult();

        SQLResult result = new SQLResult(task, SQLResult.SUCCESSFULL);

        if(task.getEvaluationStrategy() == Task.EVALUATIONSTRATEGY_LIST) {
            if(userStatementResult.equals(refStatementResult)) {
                result = new SQLResult(task, SQLResult.SUCCESSFULL);
            }
        } else {
            Logger.info("not yet implemented ! ");
            if(refStatementResult.size() != userStatementResult.size()) {
                result = new SQLResult(task, SQLResult.SEMANTICS, "to much or to less columns");
            }
            if(refStatementResult.get(0).size() != userStatementResult.get(0).size()) {
                result = new SQLResult(task, SQLResult.SEMANTICS, "to much or to less rows");
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
