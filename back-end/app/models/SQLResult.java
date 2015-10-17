package models;

import sqlparser.SQLStatus;

/**
 * @author fabiomazzone
 */
public class SQLResult {
    public static final int ERROR = 2;
    public static final int SEMANTICS = 1;
    public static final int SUCCESSFULL = 0;

    private final Task task;

    private final int         type;
    private SQLStatus   sqlStatus;
    private String      message;


    public SQLResult(Task task, int type) {
        this.task = task;
        this.type = type;
        this.sqlStatus = null;
    }

    public SQLResult(Task task, SQLStatus sqlStatus) {
        this.task = task;
        this.type       = SQLResult.ERROR;
        this.sqlStatus   = sqlStatus;
    }

    public SQLResult(Task task, int type, String message) {
        this.task = task;
        this.type = type;
        this.message = message;
    }

    public Task getTask() {
        return task;
    }

    public int getType() {
        return type;
    }

    public SQLStatus getSqlStatus() {
        return sqlStatus;
    }

    public String getMessage() {
        return message;
    }
}
