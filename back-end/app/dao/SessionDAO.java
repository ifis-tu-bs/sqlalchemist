package dao;

import com.avaje.ebean.Model;
import models.Session;

/**
 * @author fabiomazzone
 */
public class SessionDAO {
    private static Model.Finder<String, Session> find = new Model.Finder<String, Session>(Session.class);

    public static Session create() {
        Session session = new Session();

        session.save();

        return session;
    }

    public static Session getById(String sessionID) {
        return (sessionID != null && !sessionID.isEmpty()) ? find.byId(sessionID) : null;
    }
}
