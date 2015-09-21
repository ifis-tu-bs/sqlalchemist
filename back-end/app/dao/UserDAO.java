package dao;

import models.User;
import models.Profile;
import models.UserSession;

import play.Logger;
import play.mvc.Http;

import java.util.List;

public class UserDAO {

  /**
   *
   */
  public static User getById(long id) {
      return User.find.byId(id);
  }

  /**
   *
   * @param email
   * @return
   */
  public static User getByEmail(String email) {
    if(email == null) {
      return null;
    }
    return User.find.where().eq("email", email).findUnique();
  }

  public static User getByUsername(String username) {
    if(username == null) {
      return null;
    }
    return ProfileDAO.getByUsername(username).getUser();
  }

  /**
   *
   * @param y_ID
   * @return
   */
  private static User getByY_ID(String y_ID) {
    if(y_ID == null) {
      return null;
    }
    return User.find.where().eq("y_ID",y_ID).findUnique();
  }

  /**
   * @return
   */
  public static List<User> getAllStudendts() {
    List<User> studentList = User.find.where().eq("isStudent", true).findList();
    if (studentList.size() == 0) {
      return null;
    }
    return studentList;
  }

  public static List<User> getAllUsers() {
    return User.find.all();
  }

}
