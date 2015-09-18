package controllers;

import com.fasterxml.jackson.databind.JsonNode;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.*;

import Exception.EmailTakenException;
import Exception.UsernameTakenException;

import play.Logger;
import play.mvc.*;
import play.libs.*;

import secured.*;

import java.util.List;

/**
 * This Class handle all User Actions
 *
 * @author Fabio Mazzone
 */
public class UserController extends Controller {

    /**
     * This method handle the user sign up
     * POST /signup
     *
     * Needs a JSON Body Request with values:
     *  username:   String
     *  id:         String (y-ID or E-Mail)
     *  password:   String
     *  role:       int
     *
     * @return this method returns a user
     */
    public static Result create() {

        JsonNode  json = request().body().asJson();
        if (json == null) {
            return badRequest("Could not retrieve Json from POST body!");
        }
        String    username  = json.findPath("username").textValue();
        String    id        = json.findPath("id").textValue();
        String    password  = json.findPath("password").textValue();
        int       role      = json.findPath("role").asInt();

        ObjectNode node = Json.newObject();
        node.put("username", 0);
        node.put("id", 0);

        if(username == null || id == null || password == null) {
            return badRequest("Expecting Json data");
        } else {
            username = username.trim();
            id = id.trim();
            password = password.trim();
            try {
                if (role == 0) {
                    User.create(username, id, password);
                } else {
                    User.create(username, id, password, role);
                }
                return controllers.SessionController.create();
            } catch (UsernameTakenException e) {
                Logger.warn("User.create - Username Taken");
                node.remove("username");
                node.put("username", 1);
                return badRequest(node);
            } catch (EmailTakenException e) {
                Logger.warn("User.create - Id Taken");
                node.remove("id");
                node.put("id", 1);
                return badRequest(node);
            }

        }
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
    @Security.Authenticated(UserSecured.class)
    public static Result edit() {
        JsonNode  json = request().body().asJson();
        if (json == null) {
            return badRequest("Could not retrieve Json from POST body!");
        }

        User user = UserSession.getSession(session()).getUser();

        if (user.changePassword(
                json.findPath("password_old").textValue(),
                json.findPath("password_new").textValue())) {

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
    @Security.Authenticated(UserSecured.class)
    public static Result destroy() {
    	return ok("Test");
    }

    /**
     * This method handles Email verification
     * @param verifyCode The given Code by URL
     * @return Success state
     */
    public static Result verifyEmail(String verifyCode) {
        if (Long.parseLong(verifyCode, 16) % 97 != 1) {
            return badRequest("Sorry, this Verify-Code has not been sent");
        }

        int success = User.verifyEmail(verifyCode);
        if (success == User.USER_EMAIL_VERIFY_ALREADY_VERIFIED) {
            return badRequest("User already verified");
        } else if (success == User.USER_EMAIL_VERIFY_NOT_FOUND) {
            return badRequest("User not found. Please register again");
        }
        return ok("Successfully verified email");
    }

    /**
     * This method sends password changing mail
     *
     * @return Success state
     */
    public static Result sendResetPasswordMail() {
        JsonNode  json = request().body().asJson();

        if (json == null) {
            return badRequest("Could not retrieve Json from POST body!");
        }

        User user = User.getByEmail(json.findPath("id").textValue());

        user.sendResetPasswordMail();

        return ok("Email has been sent");
    }

    /**
     *
     * @param resetCode
     * @return
     */
    public static Result doResetPassword(String resetCode) {
        try {
            if (Long.parseLong(resetCode, 16) % 97 != 1) {
                return badRequest("Sorry, this Reset-Code has not been sent");
            }
        } catch (NumberFormatException e) {
            return badRequest("Sorry, this Reset-Code has not been sent");
        }

        JsonNode json = request().body().asJson();

        if (json == null) {
            return badRequest("Could not retrieve Json from POST body!");
        }

        if (!User.doResetPassword(resetCode, json.findPath("newpassword").textValue())) {
            return badRequest("User not found - Wrong Link reset link?");
        }

        return ok("Password has been changed.");
    }

    public static Result checkStudent() {
        User user = User.getProfile(session()).getUser();

        if (user == null) {
            return badRequest("No User found");
        }

        if (user.isStudent()) {
            return ok();
        }

        if (!User.updateStudentState(user)) {
            return badRequest("No user or not found in HMS Database!");
        }

        return ok();
    }

    @Security.Authenticated(AdminSecured.class)
    public static Result getAllUsers() {
        ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();
        List<User> userList = User.getAllUsers();


        for(User user : userList) {
            ObjectNode node = user.toJsonShort();
            node.put("profile", user.getProfile().toJsonHighScore());
            arrayNode.add(node);
        }


        return ok(arrayNode);
    }

    @Security.Authenticated(AdminSecured.class)
    public static Result promote(long id) {
        User        user    = User.getProfile(session()).getUser();
        User        user1   = User.getById(id);
        JsonNode    body    = request().body().asJson();

        if(user == null) {
            Logger.info("UserController.promote  - User not found");
            badRequest("User not found");
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
