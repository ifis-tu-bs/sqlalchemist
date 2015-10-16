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

    public static SQLStatus checkTaskSetConfiguration(TaskSet taskSet) {
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
/*        DBConnection    dbConnection    = new DBConnection(task.getTaskSet());
        SQLStatus       status;

        if((status = dbConnection.initDBConn()) != null) {
            return new SQLResult(task, SQLResult.ERROR, status);
        }

        if((status = dbConnection.createDB()) != null) {
            return new SQLResult(task, SQLResult.ERROR, status);
        }

        if((status = dbConnection.runnable(task.getRefStatement())) != null) {
            Logger.warn("Statement not runnable: " + task.getRefStatement());
            dbConnection.deleteDB();
            dbConnection.closeDBConn();
            return new SQLResult(task, SQLResult.ERROR, status);
        }

        List<List<String>> refStatementResult = dbConnection.getResult();

        if((status = dbConnection.runnable(task.getRefStatement())) != null) {
            Logger.warn("Statement not runnable: " + task.getRefStatement());
            dbConnection.deleteDB();
            dbConnection.closeDBConn();
            return new SQLResult(task, SQLResult.ERROR, status);
        }

        List<List<String>> userStatementResult = dbConnection.getResult();

        if(task.getEvaluationStrategy() == Task.EVALUATIONSTRATEGY_LIST) {
            if(userStatementResult.equals(refStatementResult)) {
                dbConnection.deleteDB();
                dbConnection.closeDBConn();
                return new SQLResult(task, SQLResult.SUCCESSFULL);
            }
        } else {
            Logger.info("not yet implemented ! ");
        }

        //return new SQLResult(task, SQLResult.SEMANTICS);*/
        return new SQLResult(task, SQLResult.SUCCESSFULL);
    }
}
