package view;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.SQLResult;
import models.UserStatement;
import play.libs.Json;

/**
 * @author fabiomazzone
 */
public class SQLResultView {
    public static ObjectNode toJson(SQLResult sqlResult, UserStatement userStatement) {
        ObjectNode sqlResultNode = Json.newObject();


        sqlResultNode.put("type",       sqlResult.getType());
        sqlResultNode.put("terry",      "semantic error");
        sqlResultNode.put("time",       userStatement.getTime());
        sqlResultNode.put("SQLError",   sqlResult.getSqlStatus().getSqlException().getMessage());

        return sqlResultNode;
    }

    public static ObjectNode toJson(SQLResult sqlResult, UserStatement userStatement, int coins) {
        ObjectNode sqlResultNode = Json.newObject();

        sqlResultNode.put("type",       sqlResult.getType());
        sqlResultNode.put("terry",      "your answer was correct");
        sqlResultNode.put("time",       userStatement.getTime());
        sqlResultNode.put("score",      sqlResult.getTask().getScore() / userStatement.getTime());
        sqlResultNode.put("coins",      coins);


        return sqlResultNode;
    }
}
