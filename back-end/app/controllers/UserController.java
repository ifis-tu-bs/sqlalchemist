package controllers;

import dao.ActionDAO;
import dao.SessionDAO;
import dao.UserDAO;

import forms.SignUp;

import models.Action;
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

        return redirect(routes.ProfileController.read());
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
            return forbidden(Json.parse("{'message':'you have not to read informations from other users'}"));
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

    @Authenticated(UserAuthenticator.class)
    public Result indexOld() {
        ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();
        List<User> userList = UserDAO.getAll();


        for(User user : userList) {
            ObjectNode node = user.toJsonShort();
            node.set("profile", ScoreView.toJson(user));
            arrayNode.add(node);
        }


        return ok(arrayNode);
    }

    @BodyParser.Of(BodyParser.Json.class)
    @Authenticated(UserAuthenticator.class)
    public Result edit(String username) {
        return temporaryRedirect("out of order");
        /*
        User        user    = UserDAO.getByUsername(request().username());
        User        user1   = UserDAO.getById(id);
        JsonNode    body    = request().body().asJson();

        if(user == null) {
            Logger.info("UserController.promote  - User not found");
            return badRequest("User not found");
        }

        Role role = body.path("role").asInt();

        if(role <= user.getRole() ) {
            user1.promote(role);
            return ok();
        }
        Logger.warn("UserController.promote - No Valid Role");
        return badRequest("No valid Role");
        */
    }
}
