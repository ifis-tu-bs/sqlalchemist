package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.ActionDAO;
import dao.RoleDAO;
import dao.SessionDAO;
import dao.UserDAO;

import forms.SignUp;

import models.Action;
import models.Role;
import models.Session;
import models.User;

import play.libs.Json;
import secured.SessionAuthenticator;
import secured.UserAuthenticator;

import play.data.Form;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import secured.user.CanReadUsers;
import view.ScoreView;

import java.io.IOException;
import java.util.List;

/**
 * This Class handle all User Actions
 *
 * @author Fabio Mazzone
 */
@Authenticated(SessionAuthenticator.class)
public class UserController extends Controller {

    @BodyParser.Of(BodyParser.Json.class)
    public Result create() {
        Session session = SessionDAO.getById(request().username());
        if(session == null) {
            return forbidden("Your Client is not registered");
        }

        Form<SignUp>signUpForm  = Form.form(SignUp.class).bindFromRequest();

        if(signUpForm.hasErrors()) {
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

        return redirect(routes.UserController.show(user.getUsername()));
    }


    @Authenticated(CanReadUsers.class)
    public Result index() {
        List<User> users = UserDAO.getAll();
        return ok(Json.toJson(users));
    }

    @Authenticated(UserAuthenticator.class)
    public Result show(String username) {
        User user = UserDAO.getBySession(request().username());
        if(!user.getUsername().equals(username) && !user.getRole().getUserPermissions().canRead()) {
            return forbidden(Json.parse("{'message':'you have not the permissions to read informations from other users'}"));
        }
        User userShow = UserDAO.getByUsername(username);
        if(userShow == null) {
            return notFound();
        }

        return ok(Json.toJson(userShow));
    }

    /**
     * This method delete the user
     * DELETE /users
     *
     * @return ok
     */
    @Authenticated(UserAuthenticator.class)
    public Result destroy(String username) {
        User user = UserDAO.getBySession(request().username());

        //user.disable();
        user.update();

        session().clear();

        return redirect(routes.SessionController.index());
    }

    @BodyParser.Of(BodyParser.Json.class)
    @Authenticated(UserAuthenticator.class)
    public Result edit(String username) {
        User userEdit = UserDAO.getByUsername(username);
        if(userEdit == null)
            return notFound();
        User user = UserDAO.getBySession(request().username());
        if(userEdit != user && !user.getRole().getUserPermissions().canUpdate()) {
            return forbidden(Json.parse("{'message': 'you have not the permissions to edit other user data'}"));
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.readerForUpdating(userEdit).readValue(request().body().asJson());
        } catch (IOException e) {
            e.printStackTrace();
            return internalServerError(Json.parse("{'message': 'unexpected exception!'}"));
        }

        userEdit.update();
        return redirect(routes.UserController.show(username));
    }
}
