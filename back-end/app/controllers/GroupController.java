package controllers;

import dao.GroupDAO;
import models.Group;

import secured.UserAuthenticator;
import secured.group.CanCreateGroup;
import secured.group.CanDeleteRole;
import secured.group.CanReadGroup;
import secured.group.CanUpdateGroup;

import com.fasterxml.jackson.databind.ObjectMapper;
import play.data.Form;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;

import java.io.IOException;
import java.util.List;

/**
 * @author fabiomazzone
 */
@Authenticated(UserAuthenticator.class)
public class GroupController extends Controller {

    @Authenticated(CanCreateGroup.class)
    @BodyParser.Of(BodyParser.Json.class)
    public Result create() {
        Form<Group> groupForm = Form.form(Group.class).bindFromRequest();

        if(groupForm.hasErrors()) {
            return badRequest(groupForm.errorsAsJson());
        }

        Group group = groupForm.get();
        group.save();

        response().setHeader(LOCATION, routes.GroupController.show(group.getId()).url());
        return created();
    }

    @Authenticated(CanReadGroup.class)
    public Result index() {
        List<Group> groupList = GroupDAO.getAll();
        return ok(Json.toJson(groupList));
    }

    @Authenticated(CanReadGroup.class)
    public Result show(long id) {
        Group group = GroupDAO.getById(id);
        return ok(Json.toJson(group));
    }

    @BodyParser.Of(BodyParser.Json.class)
    @Authenticated(CanUpdateGroup.class)
    public Result update(long id) {
        Group group = GroupDAO.getById(id);
        if(group == null)
            return notFound();

        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.readerForUpdating(group).readValue(request().body().asJson());
        } catch (IOException e) {
            e.printStackTrace();
            return internalServerError(Json.parse("{\"message\": \"unexpected exception!\"}"));
        }

        group.update();
        return ok();
    }

    @Authenticated(CanDeleteRole.class)
    public Result delete(long id) {
        Group group = GroupDAO.getById(id);
        if(group == null)
            return notFound();

        group.delete();
        return ok();
    }
}
