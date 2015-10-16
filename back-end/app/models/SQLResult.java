package models;

import sqlparser.SQLStatus;

/**
 * @author fabiomazzone
 */
public class SQLResult {
    public static final int ERROR = 2;
    public static final int SEMANTICS = 1;
    public static final int SUCCESSFULL = 0;

    private Task task;

    private int type;
    private SQLStatus sqlStatus;


    public SQLResult(Task task, int type) {
        this.task = task;
        this.type = type;
        this.sqlStatus = null;
    }

    public SQLResult(Task task, int type, SQLStatus sqlStatus) {
        this.task = task;
        this.type       = SQLResult.ERROR;
        this.sqlStatus   = sqlStatus;
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
}
