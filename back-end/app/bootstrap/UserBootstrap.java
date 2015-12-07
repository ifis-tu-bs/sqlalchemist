package bootstrap;

import dao.ScrollCollectionDAO;
import dao.ScrollDAO;
import dao.UserDAO;

import forms.SignUp;
import models.Scroll;

import models.User;

import play.Play;

import java.util.List;

/**
 * @author fabiomazzone
 */
public class UserBootstrap {
    public static void init() {
        if(UserDAO.getAll().size() == 0) {
            // Init residual Classes
            if(play.api.Play.isProd(play.api.Play.current())) {
                SignUp signUp = new SignUp();
                signUp.setUsername("sqlalchemist");
                signUp.setEmail(Play.application().configuration().getString("admin.email"));
                signUp.setPassword(Play.application().configuration().getString("admin.password"));
                UserDAO.create(signUp);
            } else {
                SignUp signUp1 = new SignUp();
                signUp1.setUsername("sqlalchemist");
                signUp1.setEmail("admin@local.de");
                signUp1.setPassword("password");
                User user = UserDAO.create(signUp1);
                if(user != null) {
                    user.setStudent(true);
                    user.update();
                }

                SignUp nicnac = new SignUp();
                nicnac.setUsername("nicnac");
                nicnac.setEmail("nicole@nicole.de");
                nicnac.setPassword("1234");
                User nicole = UserDAO.create(nicnac);
                nicole.setStudent(true);
                nicole.update();
                List<Scroll> scrolls = ScrollDAO.getAll();
                for(Scroll scroll : scrolls) {
                    ScrollCollectionDAO.add(nicole, scroll);
                }

                SignUp student = new SignUp();
                student.setUsername("student");
                student.setEmail("student@test.de");
                student.setPassword("1234");
                User studentU = UserDAO.create(student);
                if(studentU != null) {
                    studentU.setStudent(true);
                    studentU.update();
                }

                SignUp signUp2 = new SignUp();
                signUp2.setUsername("test2");
                signUp2.setEmail("test2@test.de");
                signUp2.setPassword("test");
                User test2 = UserDAO.create(signUp2);
                if(test2 != null) {
                    test2.setStudent(true);
                    test2.save();
                }
            }
        }
    }
}
