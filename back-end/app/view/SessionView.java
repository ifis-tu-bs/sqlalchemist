package view;

import models.Session;

import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;

/**
 * @author fabiomazzone
 */
public class SessionView {

    public static ObjectNode toJson(Session session) {
        ObjectNode sessionObject = Json.newObject();

        sessionObject.put("sessionID",  session.getId());
        sessionObject.put("owner",      (session.getOwner() != null) ? session.getOwner().getUsername() : "");

        return sessionObject;
    }
}
