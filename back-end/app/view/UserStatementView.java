package view;

import com.fasterxml.jackson.databind.JsonNode;
import models.UserStatement;

/**
 * @author fabiomazzone
 */
public class UserStatementView {
    public static UserStatement fromJsonForm(JsonNode body) {
        String  statement   = body.findPath("statement").asText();
        int     time        =(body.findPath("time").asInt()/1000);


        return new UserStatement(statement, time);
    }
}
