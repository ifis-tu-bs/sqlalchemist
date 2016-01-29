package sqlparser;

import models.SQLResult;
import models.Task;
import models.TaskSet;
import models.UserStatement;
import play.Logger;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


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

    public static SQLResult checkRefStatement(TaskSet taskSet, Task task) {
        DBConnection    dbConnection = new DBConnection(taskSet);
        SQLStatus       status;


        if((status = dbConnection.initDBConn()) != null) {
            return new SQLResult(task, status);
        }

        if((status = dbConnection.createDB()) != null) {
            return new SQLResult(task, status);
        }

        if((status = dbConnection.runnable(task.getRefStatement())) != null) {
            Logger.warn("Statement not runnable: " + task.getRefStatement());
            dbConnection.closeDBConn();
            return new SQLResult(task, status);
        }

        dbConnection.deleteDB();
        dbConnection.closeDBConn();
        return new SQLResult(task, SQLResult.SUCCESSFUL);
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
            Logger.warn("Statement not runnable: " + userStatement.getStatement());
            //dbConnection.deleteDB();
            dbConnection.closeDBConn();
            return new SQLResult(task, status);
        }
        List<Set<String>> userStatementResult = dbConnection.getResult();

        if((status = dbConnection.runnable(task.getRefStatement())) != null) {
            Logger.warn("Statement not runnable: " + task.getRefStatement());
            //dbConnection.deleteDB();
            dbConnection.closeDBConn();
            return new SQLResult(task, status);
        }
        List<Set<String>> refStatementResult = dbConnection.getResult();




        SQLResult result = new SQLResult(task, SQLResult.SUCCESSFUL);


        if(refStatementResult.size() != userStatementResult.size()) {
            result = new SQLResult(task, SQLResult.SEMANTICS, "your result set has too many or to few rows");
        } else if(refStatementResult.get(0).size() != userStatementResult.get(0).size()) {
            result = new SQLResult(task, SQLResult.SEMANTICS, "your result set has too many or to few columns");
        } else if(task.getEvaluationStrategy() == Task.EVALUATIONSTRATEGY_LIST) {
            if(userStatementResult.equals(refStatementResult)) {
                result = new SQLResult(task, SQLResult.SUCCESSFUL);
            } else {
                result = new SQLResult(task, SQLResult.SEMANTICS, "your result set is not equal to the asked one, maybe the order is incorrect ?");
            }
        } else {
            Set<Set<String>> refStatementSetSet = toSetSet(refStatementResult);
            Set<Set<String>> userStatementSetSet = toSetSet(userStatementResult);

            if(refStatementSetSet.equals(userStatementSetSet)) {
                result = new SQLResult(task, SQLResult.SUCCESSFUL);
            } else {
                result = new SQLResult(task, SQLResult.SEMANTICS, "your result set is not equal to the asked one");
            }
        }

        dbConnection.deleteDB();
        dbConnection.closeDBConn();
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

    private static Set<Set<String>> toSetSet(List<Set<String>> listSet) {
        Set<Set<String>> setSet = listSet.stream().collect(Collectors.toSet());
        return setSet;
    }
}
