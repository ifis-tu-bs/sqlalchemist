package dao;

import models.UserSession;

import java.util.Objects;

public class UserSessionDAO {
  public static UserSession getBySessionID(String sessionID) {
    if(sessionID == null || Objects.equals(sessionID, "")) {
      return null;
    }
    return UserSession.find.where().eq("sessionID", sessionID).findUnique();
  }
}
