package controllers;

import dao.ProfileDAO;
import dao.UserDAO;

import helper.MailSender;
import models.Profile;
import models.User;

import secured.UserSecured;
import secured.AdminSecured;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.libs.Json;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;

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
     *  [role:       int]
     *
     * @return this method returns a user
     */
    public static Result create() {
        Profile profile = ProfileDAO.getByUsername(request().username());
        JsonNode  json = request().body().asJson();
        if (json == null) {
            return badRequest("Could not retrieve Json from POST body!");
        }
        String    username  = json.path("username").textValue().trim();
        String    email     = json.path("id").textValue().trim();
        String    password  = json.path("password").textValue().trim();
        int       role      = json.path("role").asInt();
        ObjectNode node     = Json.newObject();

        if(profile == null || profile.getUser().getRole() != User.ROLE_ADMIN) {
            role = 0;
        }

        if(username.length() > 0 || email.length() >0 || password.length() > 0) {
            return badRequest("Expecting Json data");
        } else {
            boolean isValid = true;
            // Check the availability of the username
            if(UserDAO.getByUsername(username) != null) {
                isValid = false;
                node.put("username", 1);
            } else {
                node.put("username", 0);
            }
            // Check the availability of the email
            if(UserDAO.getByUsername(email) != null) {
                isValid = false;
                node.put("id", 1);
            } else {
                node.put("id", 0);
            }
            if(!isValid) {
                return badRequest(node);
            }

            try {
                UserDAO.create(username, email, password, role);
            } catch (IllegalArgumentException ex) {
                return badRequest(ex.getMessage());
            }

            return controllers.SessionController.create();
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
    @Authenticated(UserSecured.class)
    public static Result edit() {
        JsonNode  json = request().body().asJson();
        if (json == null) {
            return badRequest("Could not retrieve Json from POST body!");
        }

        User user = UserDAO.getByUsername(request().username());

        if (user.changePassword(
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
    @Authenticated(UserSecured.class)
    public static Result destroy() {
        User user = UserDAO.getByUsername(request().username());

        user.delete();

        return ok("test");
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
        User user = UserDAO.getByEmail(json.path("id").textValue());
        if(user == null) {
            return badRequest();
        }

        String newPassword = Integer.toHexString(user.getProfile().getUsername().hashCode());

        user.setPassword(newPassword);
        user.update();

        MailSender.resetPassword(user.getEmail(), newPassword);

        return ok("Email has been sent");
    }

    public static Result checkStudent() {
        User user = UserDAO.getByUsername(request().username());

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

    @Authenticated(AdminSecured.class)
    public static Result getAllUsers() {
        ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();
        List<User> userList = UserDAO.getAllUsers();


        for(User user : userList) {
            ObjectNode node = user.toJsonShort();
            node.put("profile", user.getProfile().toJsonHighScore());
            arrayNode.add(node);
        }


        return ok(arrayNode);
    }

    @Authenticated(AdminSecured.class)
    public static Result promote(long id) {
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
