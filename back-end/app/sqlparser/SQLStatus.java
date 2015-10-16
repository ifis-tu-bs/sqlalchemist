package sqlparser;

import java.sql.SQLException;

/**
 * @author fabiomazzone
 */
public class SQLStatus {
    private SQLException sqlException;
    public SQLStatus(SQLException e) {
        this.sqlException = e;
    }

    public SQLException getSqlException() {
        return sqlException;
    }
}
