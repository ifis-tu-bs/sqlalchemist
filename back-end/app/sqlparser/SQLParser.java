package sqlparser;

import models.SQLResult;
import models.Task;
import models.TaskSet;
import models.UserStatement;

/**
 * @author fabiomazzone
 */
public class SQLParser {

    public static SQLResult checkStatement(Task task, UserStatement userStatement) {
        DBConnection    dbConnection    = new DBConnection(task.getTaskSet());
        int             status;
        if((status = dbConnection.createDB()) != 0) {
            return new SQLResult(task, SQLResult.ERROR, status);
        }

        return new SQLResult(task, SQLResult.SEMANTICS);
    }

    public static int initialize(TaskSet taskSet) {
        DBConnection dbConnection = new DBConnection(taskSet);
        return dbConnection.createDB();
    }

    public static void delete(TaskSet taskSet) {
        DBConnection dbConnection = new DBConnection(taskSet);
        dbConnection.deleteDB();
    }
}
