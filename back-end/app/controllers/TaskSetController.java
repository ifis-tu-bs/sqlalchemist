package controllers;

import dao.ProfileDAO;
import dao.TaskSetDAO;

import models.*;

import play.mvc.Security;
import secured.UserSecured;
import view.*;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.JsonNode;

import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;

/**
 * This controller is for the TaskFiles
 *
 * @author fabiomazzone
 */

@Security.Authenticated(UserSecured.class)
public class TaskSetController extends Controller {
    /**
     * This method creates TaskFile objects from Json
     *
     * POST     /TaskSet/
     * Needs a TaskSet Json Object
     *
     * @return returns the created TaskSet
     */

    public static Result create() {
        Profile profile = ProfileDAO.getByUsername(request().username());
        JsonNode jsonNode = request().body().asJson();

        TaskSet taskSet = TaskSetView.fromJsonForm(profile, jsonNode);

        if (taskSet == null) {
            Logger.warn("TaskSetController.create - The request body doesn't contain a valid TaskSet Json Object");
            return badRequest("JsonBody is corrupted");
        }

        try {
            taskSet.save();
        } catch (PersistenceException pe) {
            Logger.warn(pe.getMessage());
            return badRequest("taskSet can't be saved");
        }

        return redirect(routes.TaskSetController.view(taskSet.getId()));
    }

    /**
     * This method returns all created TaskFiles
     *
     * GET      /TaskSet/
     *
     * @return returns a JSON Array filled with all taskSets
     */
    public static Result read() {
        ArrayNode     taskSetNode = JsonNodeFactory.instance.arrayNode();
        List<TaskSet> taskSetList = TaskSetDAO.getAll();

        if (taskSetList == null) {
            Logger.warn("TaskSet.index - no TaskSet found");
            return badRequest("no TaskSet found");
        }

        for(TaskSet taskSet : taskSetList) {
            taskSetNode.add(TaskSetView.toJson(taskSet));
        }

        return ok(taskSetNode);
    }

    /**
     * This method returns the TaskSet that matches to the given id
     *
     *  GET      /taskSet/:id
     *
     * @param id    the id of a TaskSet as long
     * @return      returns a TaskSet
     */
    public static Result view(Long id) {
        TaskSet taskSet = TaskSetDAO.getById(id);

        if (taskSet == null) {
            Logger.warn("TaskFileController.view("+id+") - no TaskSet found");
            return badRequest("no TaskSet found");
        }
        return ok(TaskSetView.toJson(taskSet));
    }

    /**
     * This method updates the TaskSet
     *
     * @param id
     * @return
     */
    public static Result update(Long id) {
        //Profile     profile     = ProfileDAO.getByUsername("admin");
        JsonNode    jsonNode    = request().body().asJson();
        TaskSet     taskSet     = TaskSetDAO.getById(id);
        Logger.info(jsonNode.toString());

        JsonNode    tableDefinitionsNode            = jsonNode.path("tableDefinitions");
        JsonNode    foreignKeyRelationsNode         = jsonNode.path("foreignKeyRelations");

        String                  taskSetName         = jsonNode.path("taskSetName").asText();
        List<TableDefinition>   tableDefinitions    = new ArrayList<>();
        List<ForeignKeyRelation>foreignKeyRelations = new ArrayList<>();

        if(taskSet == null ) {
            Logger.warn("TaskSetController - no TaskSet found for id: " + id);
            return badRequest("no TaskSet found");
        }
        // Delete old Data
        for(TableDefinition tableDefinition : taskSet.getTableDefinitions()) {
            tableDefinition.delete();
        }

        for(ForeignKeyRelation foreignKeyRelation : taskSet.getForeignKeyRelations()) {
            foreignKeyRelation.delete();
        }

        // Create new Data
        for(JsonNode tableDefinitionNode : tableDefinitionsNode) {
            tableDefinitions.add(TableDefinitionView.fromJsonForm(tableDefinitionNode));
        }

        for(JsonNode foreignKeyRelationNode : foreignKeyRelationsNode) {
            foreignKeyRelations.add(ForeignKeyRelationView.fromJsonForm(foreignKeyRelationNode));
        }

        taskSet.setTaskSetName(taskSetName);
        taskSet.setTableDefinitions(tableDefinitions);
        taskSet.setForeignKeyRelations(foreignKeyRelations);
        //taskSet.setTasks(taskSetEdit.getTasks());

        taskSet.save();
        return redirect(routes.TaskSetController.view(taskSet.getId()));
    }

    /**
     * This method deletes an TaskSet object
     */
    public static Result delete(Long id) {
        TaskSet taskSet = TaskSetDAO.getById(id);

        if(taskSet == null) {
            Logger.warn("TaskSetController.delete - no TaskSet found for id: " + id);
            return badRequest("no TaskSet found");
        }

        taskSet.delete();

        return redirect(routes.TaskSetController.read());
    }

    /**
     * this method controls the rating of the TaskSet
     *
     * POST      /TaskSet/:id/rate
     * Needs a JSON Body Request with values:
     *  positive    :int
     *  negative    :int
     *  needReview  :int
     *
     * @param id    the id of the TaskFile
     * @return      returns a http code with a result of the operation
     */
    public static Result rate(Long id) {
        JsonNode   ratingBody  = request().body().asJson();
        TaskSet    taskSet     = TaskSetDAO.getById(id);
        Profile    profile     = ProfileDAO.getByUsername(request().username());
        Rating     rating      = RatingView.fromJsonForm(ratingBody, profile);

        if(taskSet == null) {
            Logger.warn("TaskSetController.rate("+id+") - no task found");
            return badRequest("no SubTask found");
        }
        if(rating == null) {
            Logger.warn("TaskSetController.rate() - invalid json body");
            return badRequest("invalid json body");
        }

        taskSet.addRating(rating);
        taskSet.update();

        return redirect(routes.TaskSetController.view(taskSet.getId()));
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
        Profile     profile     = ProfileDAO.getByUsername(request().username());
        JsonNode    commentBody = request().body().asJson();
        TaskSet     taskSet     = TaskSetDAO.getById(id);
        Comment     comment     = CommentView.fromJsonForm(commentBody, profile);

        if (taskSet == null) {
            Logger.warn("TaskSetController.comment("+id+") - no TaskSet found");
            return badRequest("no TaskSet found");
        }

        if(comment == null) {
            Logger.warn("TaskSetController.comment() - invalid json body");
            return badRequest("invalid json body");
        }

        taskSet.addComment(comment);
        taskSet.update();
        return redirect(routes.TaskSetController.view(taskSet.getId()));
    }
}
