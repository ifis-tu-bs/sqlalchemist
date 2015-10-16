package sqlparser;

import models.SQLResult;
import models.Task;
import models.TaskSet;
import models.UserStatement;

import java.util.List;


/**
 * @author fabiomazzone
 */
public class SQLParser {

    public static SQLResult checkStatement(Task task, UserStatement userStatement) {
        DBConnection    dbConnection    = new DBConnection(task.getTaskSet());
        SQLStatus       status;
        if((status = dbConnection.createDB()) != null) {
            return new SQLResult(task, SQLResult.ERROR, status);
        }

        List<List<String>> refStatement;

        //return new SQLResult(task, SQLResult.SEMANTICS);
        return new SQLResult(task, SQLResult.SUCCESSFULL);
    }

    public static SQLStatus initialize(TaskSet taskSet) {
        DBConnection dbConnection = new DBConnection(taskSet);
        return dbConnection.createDB();
    }

    public static void delete(TaskSet taskSet) {
        DBConnection dbConnection = new DBConnection(taskSet);
        dbConnection.deleteDB();
    }
}
