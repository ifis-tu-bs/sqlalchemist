package dao;

import models.UserSession;

public class UserSessionDAO {
  public static UserSession getBySessionID(String sessionID) {
    if(sessionID == null || sessionID == "") {
      return null;
    }
    return UserSession.find.where().eq("sessionID", sessionID).findUnique();
  }
}
