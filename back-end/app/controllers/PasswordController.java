package controllers;

import dao.UserDAO;

import forms.ForgotPassword;

import helper.MailSender;

import models.User;

import secured.UserAuthenticator;

import com.fasterxml.jackson.databind.JsonNode;

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
        User user = UserDAO.getByUsername(username);
        if(user == null) {
            return notFound();
        }
        JsonNode json = request().body().asJson();
        if (json == null) {
            return badRequest("Could not retrieve Json from POST body!");
        }



        if (user.setPassword(
                json.findPath("password_old").textValue(),
                json.findPath("password_new").textValue())) {

            user.update();
            return ok("Password successfully changed");
        }

        return badRequest("Wrong password");
    }

    /**
     * This method sends password changing mail
     *
     * @return Success state
     */
    public Result sendResetPasswordMail() {
        Form<ForgotPassword> forgotPasswordForm = Form.form(ForgotPassword.class).bindFromRequest();

        if(forgotPasswordForm.hasErrors()) {
            Logger.error("forgetPasswordForm has errors");
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
