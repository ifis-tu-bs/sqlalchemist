package controllers;

import bootstrap.BootstrapDB;

import dao.UserDAO;

import models.*;

import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.Play;


/**
 * The application class
 *
 * The methods of this class delivery the content-pages to the user, with an authentication key.
 *  This authentication key marks the rights of the current application.
 *
 */
public class Application extends Controller {

    /**
     * GET /init
     *
     * @return ok
     */
    public static Result init() {
        if(UserDAO.getAllUsers().size() == 0) {
            Logger.info("Application.init - Start initializing");
            // Init final Classes
            BootstrapDB.init();
            TaskFile.init();

            // Init residual Classes
            if(play.api.Play.isProd(play.api.Play.current())) {
                String username = Play.application().configuration().getString("admin.username");
                String email = Play.application().configuration().getString("admin.email");
                String password = Play.application().configuration().getString("admin.password");
                UserDAO.create(username, email, password, User.ROLE_ADMIN);
            } else {
                User user = UserDAO.create("admin", "admin@local.de", "password1234", User.ROLE_ADMIN);
                user.setStudent();
                user.update();

                UserDAO.create("test1", "test1@test.de", "test", User.ROLE_CREATOR);
                UserDAO.create("test2", "test2@test.de", "test", User.ROLE_USER);
                UserDAO.create("test3", "test3@test.de", "test", User.ROLE_USER);
            }
        }
        return ok("Ich habe fertig");
    }
}
