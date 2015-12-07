package controllers;

import dao.ActionDAO;
import dao.SessionDAO;
import dao.UserDAO;

import forms.SignUp;
import helper.MailSender;

import models.Action;
import models.Session;
import models.User;

import secured.SessionAuthenticator;
import secured.UserAuthenticator;

import play.Logger;
import play.data.Form;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import service.ServiceUser;
import view.ScoreView;

import java.util.List;

/**
 * This Class handle all User Actions
 *
 * @author Fabio Mazzone
 */
@Authenticated(SessionAuthenticator.class)
public class UserController extends Controller {

    /**
     * This method handle the user sign up
     * POST /signup
     *
     * Needs a JSON Body Request with values:
     *  username:   String
     *  id:         String (y-ID or E-Mail)
     *  password:   String
     *  [role:       int]
     *
     * @return this method returns a user
     */
    @BodyParser.Of(BodyParser.Json.class)
    public Result create() {
        Session session = SessionDAO.getById(request().username());
        if(session == null) {
            return forbidden("Your Client is not registered");
        }

        Form<SignUp>signUpForm  = Form.form(SignUp.class).bindFromRequest();

        if(signUpForm.hasErrors()) {
            Logger.error("signUpForm has errors");
            Logger.error(signUpForm.errorsAsJson().toString());
            return badRequest(signUpForm.errorsAsJson());
        }

        SignUp      signUp      = signUpForm.bindFromRequest().get();

        User user = UserDAO.create(signUp);
        if(user == null) {
            return unauthorized("Wrong  email or password");
        }

        session.setOwner(user);
        session.addAction(ActionDAO.create(Action.LOGIN));
        session.update();

        return redirect(routes.ProfileController.read());
    }

    /**
     * This method handle password changes
     * POST /users
     *
     * Needs a JSON Body Request with values:
     *  password_old:    String
     *  password_new:    String
     *
     * @return returns a ok if the old password is valid or a badRequest Status
     */
    @Authenticated(UserAuthenticator.class)
    public Result edit() {
        JsonNode  json = request().body().asJson();
        if (json == null) {
            return badRequest("Could not retrieve Json from POST body!");
        }

        User user = UserDAO.getByUsername(request().username());

        if (user.setPassword(
                json.findPath("password_old").textValue(),
                json.findPath("password_new").textValue())) {

            user.update();
            return ok("Password successfully changed");
        }

        return badRequest("Wrong password");
    }

    /**
     * This method delete the user
     * DELETE /users
     *
     * @return ok
     */
    @Authenticated(UserAuthenticator.class)
    public Result destroy() {
        User user = UserDAO.getByUsername(request().username());

        user.disable();
        user.update();

        return redirect(routes.ControllerSession.logout());
    }

    /**
     * This method handles Email verification
     * @param verifyCode The given Code by URL
     * @return Success state
     */
    @Authenticated(UserAuthenticator.class)
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

    /**
     * This method sends password changing mail
     *
     * @return Success state
     */
    @Authenticated(UserAuthenticator.class)
    public Result sendResetPasswordMail() {
        JsonNode  json = request().body().asJson();
        if (json == null) {
            return badRequest("Could not retrieve Json from POST body!");
        }
        User user = UserDAO.getByEmail(json.path("id").textValue());
        if(user == null) {
            return badRequest();
        }

        String newPassword = Integer.toHexString(user.getUsername().hashCode());

        user.setPassword(newPassword);
        user.update();

        MailSender.resetPassword(user.getEmail(), newPassword);

        return ok("Email has been sent");
    }

    @Authenticated(UserAuthenticator.class)
    public Result checkStudent() {
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

    @Authenticated(UserAuthenticator.class)
    public Result index() {
        ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();
        List<User> userList = UserDAO.getAllUsers();


        for(User user : userList) {
            ObjectNode node = user.toJsonShort();
            node.set("profile", ScoreView.toJson(user));
            arrayNode.add(node);
        }


        return ok(arrayNode);
    }

    @Authenticated(UserAuthenticator.class)
    public Result promote(long id) {
        User        user    = UserDAO.getByUsername(request().username());
        User        user1   = UserDAO.getById(id);
        JsonNode    body    = request().body().asJson();

        if(user == null) {
            Logger.info("UserController.promote  - User not found");
            return badRequest("User not found");
        }

        int role = body.path("role").asInt();

        if(role <= user.getRole() ) {
            user1.promote(role);
            return ok();
        }
        Logger.warn("UserController.promote - No Valid Role");
        return badRequest("No valid Role");
    }
}
