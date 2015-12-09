package controllers;

import dao.UserDAO;
import models.User;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import secured.UserAuthenticator;
import service.ServiceUser;

/**
 * Created by fabiomazzone on 09/12/15.
 */
public class ValidationController extends Controller {
    /**
     * This method handles Email verification
     * @param verifyCode The given Code by URL
     * @return Success state
     */
    @Security.Authenticated(UserAuthenticator.class)
    public Result verifyEmail(String verifyCode) {
        if (Long.parseLong(verifyCode, 16) % 97 != 1) {
            return badRequest("Sorry, this Verify-Code has not been sent");
        }

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

    @Security.Authenticated(UserAuthenticator.class)
    public Result checkStudent(String username) {
        User user = UserDAO.getByUsername(request().username());

        if (user == null) {
            return badRequest("No User found");
        }

        if (user.isStudent()) {
            return ok();
        }

        if (!ServiceUser.updateStudentState(user)) {
            return badRequest("No user or not found in HMS Database!");
        }

        return ok();
    }
}
