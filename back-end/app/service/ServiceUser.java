package service;

import models.User;
import org.mindrot.jbcrypt.BCrypt;

/**
 * @author fabiomazzone
 */
public class ServiceUser {

    public static User authenticate(String email, String password) {
        if(email == null || password == null) {
            return null;
        }

        User user = dao.UserDAO.getByEmail(email);
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            return user;
        }
        return null;
    }
}
