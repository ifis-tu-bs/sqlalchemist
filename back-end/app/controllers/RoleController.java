package controllers;

import dao.RoleDAO;
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

import java.util.List;

/**
 * @author fabiomazzone
 */
@Authenticated(UserAuthenticator.class)
public class RoleController extends Controller{

    @BodyParser.Of(BodyParser.Json.class)
    @Authenticated(CanCreateRole.class)
    public Result create() {
        Form<Role> roleForm = Form.form(Role.class).bindFromRequest();

        if(roleForm.hasErrors()) {
            Logger.info(roleForm.toString());
            Logger.info(roleForm.errorsAsJson().toString());
            return badRequest(roleForm.errorsAsJson());
        }

        response().setHeader(LOCATION, "");
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

        return badRequest("not yet implemented");
    }
}
