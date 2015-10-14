package sqlparser;

import models.Task;
import models.TaskSet;

/**
 * @author fabiomazzone
 */
public class SQLParser {

    private TaskSet taskSet;
    private Task    task;

    public SQLParser(Task task) {
        this.taskSet    = task.getTaskSet();
    }

    public static int checkStatement(Task task, String statement) {
        return 0;
    }

    public static int initialize(TaskSet taskSet) {
        DBConnection dbConnection = new DBConnection(taskSet);
        return dbConnection.recreateDB();
    }

    public static void delete(TaskSet taskSet) {
        DBConnection dbConnection = new DBConnection(taskSet);
        dbConnection.deleteDB();
    }
}
