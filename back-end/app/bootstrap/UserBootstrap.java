package bootstrap;

import dao.UserDAO;

import models.User;

import play.Play;

/**
 * @author fabiomazzone
 */
public class UserBootstrap {
    public static void init() {
        // Init residual Classes
        if(play.api.Play.isProd(play.api.Play.current())) {
            String username = Play.application().configuration().getString("admin.username");
            String email = Play.application().configuration().getString("admin.email");
            String password = Play.application().configuration().getString("admin.password");
            UserDAO.create(username, email, password, User.ROLE_ADMIN);
        } else {
            User user = UserDAO.create("admin", "admin@local.de", "password1234", User.ROLE_ADMIN);
            if(user != null) {
                user.setStudent();
                user.update();
            }

            UserDAO.create("test1", "test1@test.de", "test", User.ROLE_CREATOR);
            User test2 = UserDAO.create("test2", "test2@test.de", "test", User.ROLE_USER);
            if(test2 != null) {
                test2.setStudent();
                test2.save();
            }
            UserDAO.create("test3", "test3@test.de", "test", User.ROLE_USER);
        }
    }
}
