package dao;

import models.Profile;

/**
 * This class containes all database getter & setters for profile objects
 */
public class ProfileDAO {
  public static Profile getById(long id) {
    return Profile.find.where().eq("id", id).findUnique();
  }

  public static Profile getByUsername(String username) {
    if(username == null || username.equals("")) {
      return null;
    }
    return Profile.find.where().eq("username", username).findUnique();
  }
}
