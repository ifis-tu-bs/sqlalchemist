package bootstrap;

import dao.ScrollCollectionDAO;
import dao.ScrollDAO;
import dao.UserDAO;

import models.Scroll;
import models.ScrollCollection;
import models.User;

import play.Play;

import java.util.List;

/**
 * @author fabiomazzone
 */
public class UserBootstrap {
    public static void init() {
        // Init residual Classes
        if(play.api.Play.isProd(play.api.Play.current())) {
            String username = "sqlalchemist";
            String email = Play.application().configuration().getString("admin.email");
            String password = Play.application().configuration().getString("admin.password");
            UserDAO.create(username, email, password, User.ROLE_ADMIN);
        } else {
            User user = UserDAO.create("sqlalchemist", "admin@local.de", "password", User.ROLE_ADMIN);
            if(user != null) {
                user.setStudent();
                user.update();
            }

            User nicole = UserDAO.create("nicole", "nicole@nicole.de", "1234", User.ROLE_USER);
            nicole.setStudent();
            nicole.update();

            List<Scroll> scrolls = ScrollDAO.getAll();
            for(Scroll scroll : scrolls) {
                ScrollCollectionDAO.add(nicole.getProfile(), scroll);
            }

            User student = UserDAO.create("student", "student@test.de", "1234", User.ROLE_USER);
            if(student != null) {
                student.setStudent();
                student.update();
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
