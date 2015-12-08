package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.RoleDAO;
import dao.UserDAO;
import models.Role;

import play.libs.Json;
import secured.UserAuthenticator;
import secured.role.CanCreateRole;

import play.data.Form;
import play.mvc.BodyParser;
import play.mvc.Result;
import play.mvc.Controller;
import play.mvc.Security.Authenticated;
import secured.role.CanDeleteRole;
import secured.role.CanReadRole;
import secured.role.CanUpdateRole;

import javax.persistence.PersistenceException;
import java.io.IOException;
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
            return badRequest(roleForm.errorsAsJson());
        }

        Role role = roleForm.get();
        role.setCreator(UserDAO.getBySession(request().username()));
        role.save();

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

        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.readerForUpdating(role).readValue(request().body().asJson());
        } catch (IOException e) {
            e.printStackTrace();
            return internalServerError("{'message': 'unexpected exception!'}");
        }

        role.update();
        return ok();
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
