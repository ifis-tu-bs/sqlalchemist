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


        sqlResultNode.put("type", sqlResult.getType());
        sqlResultNode.put("time", userStatement.getTime());
        if(sqlResult.getType() == SQLResult.ERROR) {
            sqlResultNode.put("terry", "syntax error");
            sqlResultNode.put("SQLError", sqlResult.getSqlStatus().getSqlException().getMessage());
        } else {
            sqlResultNode.put("terry", "semantic error");
            sqlResultNode.put("SQLError", sqlResult.getMessage());
        }

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

    public static ObjectNode toJson(SQLResult sqlResult, UserStatement userStatement, boolean submit) {
        ObjectNode sqlResultNode = Json.newObject();

        sqlResultNode.put("type",       sqlResult.getType());

        if ( sqlResult.getType() == SQLResult.SEMANTICS) {
            if( submit ) {
                sqlResultNode.put("terry", "semantic error");
                sqlResultNode.put("SQLError", sqlResult.getMessage());
            } else {
                sqlResultNode.put("terry", "Your Syntax is correct");
            }
       } else if(sqlResult.getType() == SQLResult.ERROR) {
            sqlResultNode.put("terry", "syntax error");
            sqlResultNode.put("SQLError", sqlResult.getSqlStatus().getSqlException().getMessage());
        } else {
            sqlResultNode.put("terry",      "your answer was correct");
        }

        sqlResultNode.put("time",       userStatement.getTime());
        sqlResultNode.put("score",      sqlResult.getTask().getScore() / userStatement.getTime());


        return sqlResultNode;
    }
}
