package controllers;

import dao.UserDAO;
import models.User;
import play.Logger;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import secured.UserAuthenticator;
import service.ServiceUser;

/**
 *
 * Created by fabiomazzone on 09/12/15.
 */
public class ValidationController extends Controller {
    /**
     * This method handles Email verification
     * @param verifyCode The given Code by URL
     * @return Success state
     */
    public Result verifyEmail(String verifyCode) {
        User user = UserDAO.getByVerifyCode(verifyCode);
        if (user == null) {
            return badRequest("User not found. Please register again");
        } else if(user.isEmailVerified()) {
            return badRequest("User already verified");
        }
        user.setEmailVerified();
        user.update();

        return ok("Successfully verified email");
    }

    @BodyParser.Of(BodyParser.Json.class)
    @Security.Authenticated(UserAuthenticator.class)
    public Result checkStudent(String username) {
        User user = UserDAO.getBySession(request().username());

        if (user == null || !user.getUsername().equals(username)) {
            return badRequest("No User found");
        }

        if (user.isStudent()) {
            return ok();
        }

        Logger.info(request().body().asJson().toString());

        String yID = request().body().asJson().path("yID").asText();
        if (yID == null || yID.isEmpty()) {
            return badRequest("no valid request !");
        }

        if (!ServiceUser.updateStudentState(user, yID)) {
            return badRequest("No user or not found in HMS Database!");
        }

        user.update();

        return ok();
    }
}
