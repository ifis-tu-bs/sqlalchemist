package models;

/**
 * @author fabiomazzone
 */
public class UserStatement {
    private final String    statement;
    private final int       time;

    public UserStatement(String statement, int time) {
        this.statement  = statement;
        this.time       = time;
    }


    public String getStatement() {
        return statement;
    }

    public int getTime() {
        return time;
    }
}
