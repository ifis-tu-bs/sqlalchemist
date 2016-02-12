package controllers;

import dao.UserDAO;

import forms.ChangePassword;
import forms.ForgotPassword;

import helper.MailSender;

import models.User;

import play.libs.Json;
import secured.UserAuthenticator;

import play.Logger;
import play.data.Form;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;

/**
 * Password Controller
 *
 * Created by fabiomazzone on 09/12/15.
 */
public class PasswordController extends Controller {
    @BodyParser.Of(BodyParser.Json.class)
    @Authenticated(UserAuthenticator.class)
    public Result changePassword(String username) {
        User userEdit = UserDAO.getByUsername(username);
        if(userEdit == null) {
            return notFound();
        }
        User user = UserDAO.getBySession(request().username());
        if(user.getId() != userEdit.getId() && !user.getRole().getUserPermissions().canUpdate()) {
            return forbidden("you have not the permissions to change passwords from another user");
        }

        Form<ChangePassword> changePasswordForm = Form.form(ChangePassword.class).bindFromRequest();
        if(changePasswordForm.hasErrors()) {
            return badRequest(changePasswordForm.errorsAsJson());
        }

        ChangePassword changePassword = changePasswordForm.get();

        if(changePassword.oldPassword.isEmpty() && userEdit.getId() != user.getId()) {
            Logger.info("Test");
            userEdit.setPassword(changePassword.newPassword);
        } else if (!userEdit.setPassword(changePassword.oldPassword, changePassword.newPassword)) {
            return badRequest(Json.parse("{\"oldPassword\":\"the password was incorrect\"}"));
        }
        userEdit.update();

        return ok();
    }

    /**
     * This method sends password changing mail
     *
     * @return Success state
     */
    @BodyParser.Of(BodyParser.Json.class)
    public Result sendResetPasswordMail() {
        Form<ForgotPassword> forgotPasswordForm = Form.form(ForgotPassword.class).bindFromRequest();

        if(forgotPasswordForm.hasErrors()) {
            Logger.error("forgotPasswordForm has errors");
            Logger.error(forgotPasswordForm.errorsAsJson().toString());
            return badRequest(forgotPasswordForm.errorsAsJson());
        }

        ForgotPassword forgotPassword = forgotPasswordForm.bindFromRequest().get();

        User user = UserDAO.getByEmail(forgotPassword.getEmail());

        String newPassword = Integer.toHexString(user.getUsername().hashCode());

        user.setPassword(newPassword);
        user.update();

        MailSender.resetPassword(user.getEmail(), newPassword);

        return ok("Email has been sent");
    }
}
