package models;

/**
 * @author fabiomazzone
 */
public class SQLResult {
    public static final int ERROR = 2;
    public static final int SEMANTICS = 1;
    public static final int SUCCESSFULL = 0;

    private Task task;

    private int type;
    private int sqlError;


    public SQLResult(Task task, int type) {
        this.task = task;
        this.type = type;
        this.sqlError = 0;
    }

    public SQLResult(Task task, int type, int sqlerror) {
        this.task = task;
        this.type       = type;
        this.sqlError   = sqlerror;
    }

    public Task getTask() {
        return task;
    }

    public int getType() {
        return type;
    }

    public int getSQLError() {
        return sqlError;
    }
}
