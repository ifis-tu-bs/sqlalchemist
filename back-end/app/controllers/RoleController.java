package controllers;

import dao.RoleDAO;
import dao.UserDAO;
import models.Role;

import play.libs.Json;
import secured.UserAuthenticator;
import secured.role.CanCreateRole;

import play.Logger;
import play.data.Form;
import play.mvc.BodyParser;
import play.mvc.Result;
import play.mvc.Controller;
import play.mvc.Security.Authenticated;
import secured.role.CanDeleteRole;
import secured.role.CanReadRole;
import secured.role.CanUpdateRole;

import javax.persistence.PersistenceException;
import java.util.List;

/**
 * @author fabiomazzone
 */
@Authenticated(UserAuthenticator.class)
public class RoleController extends Controller{

    @BodyParser.Of(BodyParser.Json.class)
    @Authenticated(CanCreateRole.class)
    public Result create() {
        Logger.info(request().body().asJson().toString());
        Form<Role> roleForm = Form.form(Role.class).bindFromRequest();

        if(roleForm.hasErrors()) {
            Logger.info(roleForm.toString());
            Logger.info(roleForm.errorsAsJson().toString());
            return badRequest(roleForm.errorsAsJson());
        }

        Role role = roleForm.get();
        role.setCreator(UserDAO.getBySession(request().username()));
        role.save();

        Logger.info(Json.toJson(role).toString());

        response().setHeader(LOCATION, routes.RoleController.show(role.getId()).url());
        return created();
    }

    @Authenticated(CanReadRole.class)
    public Result index() {

        List<Role> roles = RoleDAO.getAll();
        return ok(Json.toJson(roles));
    }

    @Authenticated(CanReadRole.class)
    public Result show(long id) {
        Role role = RoleDAO.getById(id);
        if(role == null)
            return notFound();

        return ok(Json.toJson(role));
    }

    @BodyParser.Of(BodyParser.Json.class)
    @Authenticated(CanUpdateRole.class)
    public Result update(long id) {
        Role role = RoleDAO.getById(id);
        if(role == null)
            return notFound();

        return badRequest("not yet implemented");
    }

    @Authenticated(CanDeleteRole.class)
    public Result delete(long id) {
        Role role = RoleDAO.getById(id);
        if(role == null)
            return notFound();

        if(RoleDAO.getAll().size() <= 1) {
            return badRequest("{'message': 'You cannot delete the Role if it the last one!'}");
        }
        try {
            role.delete();
            return ok();
        } catch (PersistenceException e) {
            e.printStackTrace();
            return internalServerError("{'message': 'unexpected exception!'}");
        }
    }
}
