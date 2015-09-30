package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.*;

import models.Profile;
import models.TaskFile;
import models.Comment;
import dao.ProfileDAO;
import dao.CommentDAO;

import Exception.SQLAlchemistException;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;


import secured.CreatorSecured;

import java.util.List;

/**
 * This controller is for the TaskFiles
 *
 * @author fabiomazzone
 */

@Authenticated(CreatorSecured.class)
public class TaskFileController extends Controller {

    /**
     * This method returns all created TaskFiles
     *
     * GET      /taskFile
     *
     * @return returns a JSON Array filled with all taskFiles
     */
    public static Result index() {
        ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();
        List<TaskFile> taskFileList = TaskFile.getAll();

        if (taskFileList == null) {
            Logger.warn("TaskFile.index - no TaskFiles found");
            return badRequest("no TaskFiles found");
        }

        for(TaskFile taskFile : taskFileList) {
            arrayNode.add(taskFile.toJson());
        }

        return ok(arrayNode);
    }

    /**
     * This method creates TaskFile objects from Json
     *
     * POST     /taskFile
     * Needs a JSON Body Request with values:
     *  text: String
     *
     * @return returns the created TaskFile
     */

    public static Result create() {
        Profile profile = ProfileDAO.getByUsername(request().username());
        JsonNode jsonNode = request().body().asJson();

        String xml = jsonNode.get("text").textValue();
        boolean isHomeWork = jsonNode.get("isHomeWork").asBoolean();

        if (xml == null) {
            Logger.warn("TaskFileController.create - no text in Json.text");
            return badRequest("no text in Json.text");
        }

        TaskFile taskFile;
        try {
            taskFile = TaskFile.create(profile, xml, isHomeWork);

        } catch (SQLAlchemistException e) {
            return badRequest(e.getMessage());
        }
        return ok(taskFile.toJson());
    }

    /**
     * This method returns the TaskFile that matches to the given id
     *
     *  GET      /taskFile/:id
     *
     * @param id    the id of a TaskFile as long
     * @return      returns a TaskFile
     */
    public static Result view(Long id) {
        TaskFile taskFile = TaskFile.getById(id);

        if (taskFile == null) {
            Logger.warn("TaskFileController.view("+id+") - no TaskFile found");
            return badRequest("no TaskFile found");
        }
        ObjectNode taskFileJsoned = taskFile.toJson();
        taskFileJsoned.put("xmlContent", taskFile.getXmlContent());
        taskFileJsoned.put("commentList", Comment.toJsonAll(taskFile.getCommentList()));
        return ok(taskFileJsoned);
    }

    public static Result destroy(Long id) {
        return ok();
    }

    // ToDO
    public static Result edit(Long id) {
        Result result = create();

        if(result.toScala().header().status() != 200) {
            return result;
        }

        TaskFile taskFile = TaskFile.getById(id);

        if(taskFile == null) {
            Logger.warn("TaskFileController.edit - no TaskFile found");
            return badRequest("no TaskFile found");
        }
        taskFile.delete();

        return ok();
    }

    /**
     * this method controls the rating of the TaskFiles
     *
     * POST      /taskFile/:id/rate
     * Needs a JSON Body Request with values:
     *  positive    :int
     *  negative    :int
     *  needReview  :int
     *
     * @param id    the id of the TaskFile
     * @return      returns a http code with a result of the operation
     */
    public static Result rate(Long id) {
        JsonNode body       = request().body().asJson();
        TaskFile taskFile   = TaskFile.getById(id);
        Profile     profile = ProfileDAO.getByUsername(request().username());

        if (taskFile == null) {
            Logger.warn("TaskFileController.rate("+id+") - no TaskFile found");
            return badRequest("no TaskFile found");
        }

        int p = body.findPath("positive").asInt();
        int n = body.findPath("negative").asInt();
        int e = body.findPath("needReview").asInt();

        boolean status;

        if(p > 0 || n > 0 || e > 0) {
            status = taskFile.rate(profile, p > 0, e > 0, n > 0);
        } else {
            Logger.warn("TaskFileController.rate - json body was empty");
            return badRequest("json body was empty");
        }
        taskFile.update();
        if(!status) {
            Logger.warn("TaskFileController.rate - already rated");
            return badRequest("try again later");
        }
        taskFile.update();
        return ok();
    }


    /**
     * this method handels the comments for TaskFiles
     *
     * GET      /taskFile/:id/comment
     * Needs a JSON Body Request with values:
     *  text        :String
     *
     * @param id    the id of the TaskFile
     * @return      returns a http code with a result message
     */
    public static Result comment(Long id) {
        Profile     profile = ProfileDAO.getByUsername(request().username());
        JsonNode    body    = request().body().asJson();
        TaskFile    taskFile= TaskFile.getById(id);

        if (taskFile == null) {
            Logger.warn("TaskFileController.comment("+id+") - no TaskFile found");
            return badRequest("no TaskFile found");
        }

        String  text = body.findPath("text").asText();

        if (text == null || text.equals("")) {
            Logger.warn("TaskFileController.comment - invalid json");
            return badRequest("Invalid json or empty text");
        }

        Comment comment = CommentDAO.create(profile, text);
        if (comment == null) {
            Logger.warn("TaskFileController.comment - can't create comment");
            return badRequest("can't create comment");
        }
        taskFile.addComment(comment);
        taskFile.update();
        return ok();
    }

}
