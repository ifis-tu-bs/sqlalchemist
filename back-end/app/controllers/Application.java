package controllers;

import bootstrap.BootstrapDB;

import dao.UserDAO;

import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;


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
    public Result init() {
        if(UserDAO.getAllUsers().size() == 0) {
            Logger.info("Application.init - Start initializing");
            // Init final Classes
            BootstrapDB.init();
        }
        return ok("Ich habe fertig");
    }
}
