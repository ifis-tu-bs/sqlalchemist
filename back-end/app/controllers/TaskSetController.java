package controllers;

import dao.ProfileDAO;
import dao.TaskSetDAO;

import models.*;

import play.mvc.Http;
import secured.CreatorSecured;
import secured.UserSecured;

import sqlparser.SQLParser;
import sqlparser.SQLStatus;
import view.*;

import com.fasterxml.jackson.databind.JsonNode;

import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

import javax.persistence.PersistenceException;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
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

    @Security.Authenticated(CreatorSecured.class)
    public Result create() {
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
        SQLStatus err;
        if((err = SQLParser.createDB(taskSet)) != null) {
            Logger.warn("TaskSetController.create - " + err.getSqlException().getMessage());
                    taskSet.delete();
            return badRequest(err.getSqlException().getMessage());
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
    public Result read() {
        List<TaskSet> taskSetList = TaskSetDAO.getAll();

        if (taskSetList == null) {
            Logger.warn("TaskSet.index - no TaskSet found");
            return badRequest("no TaskSet found");
        }

        return ok(TaskSetView.toJson(taskSetList));
    }

    /**
     * This method returns the TaskSet that matches to the given id
     *
     *  GET      /taskSet/:id
     *
     * @param id    the id of a TaskSet as long
     * @return      returns a TaskSet
     */
    public Result view(Long id) {
        TaskSet taskSet = TaskSetDAO.getById(id);

        if (taskSet == null) {
            Logger.warn("TaskFileController.view("+id+") - no TaskSet found");
            return badRequest("no TaskSet found");
        }
        return ok(TaskSetView.toJson(taskSet));
    }
    /**
     * This method returns all created TaskFiles
     *
     * GET      /TaskSet/
     *
     * @return returns a JSON Array filled with all taskSets
     */
    public Result readHomeWorks() {
        List<TaskSet> taskSetList = TaskSetDAO.getAllHomeWorkTaskSets();

        if (taskSetList == null) {
            Logger.warn("TaskSet.index - no TaskSet found");
            return badRequest("no TaskSet found");
        }

        return ok(TaskSetView.toJson(taskSetList));
    }

    /**
     * This method updates the TaskSet
     *
     * @param id        the if of the taskSet
     * @return          returns an redirection to the updated taskSet
     */
    @Security.Authenticated(CreatorSecured.class)
    public Result update(Long id) {
        //Profile     profile     = ProfileDAO.getByUsername("admin");
        JsonNode    jsonNode    = request().body().asJson();
        TaskSet     taskSet     = TaskSetDAO.getById(id);

        Logger.info(jsonNode.toString());

        if(taskSet == null ) {
            Logger.warn("TaskSetController - no TaskSet found for id: " + id);
            return badRequest("no TaskSet found");
        }
        List<TableDefinition>   tableDefinitionsOld    = new ArrayList<>(taskSet.getTableDefinitions());
        List<ForeignKeyRelation>foreignKeyRelationsOld = new ArrayList<>(taskSet.getForeignKeyRelations());


        SQLParser.deleteDB(taskSet);
        TaskSetView.updateFromJson(taskSet, jsonNode);


        SQLStatus err;
        if((err = SQLParser.createDB(taskSet)) != null) {
            Logger.warn("TaskSetController.update - " + err.getSqlException().getMessage() );
            taskSet = TaskSetDAO.getById(id);
            SQLStatus err2;
            if((err2 = SQLParser.createDB(taskSet)) != null) {
                Logger.warn("TaskSetController.update - " + err2.getSqlException().getMessage());
                return badRequest(err2.getSqlException().getMessage());
            }
            return badRequest(err.getSqlException().getMessage());
        }


        // Delete old Data
        tableDefinitionsOld.forEach(models.TableDefinition::delete);


        foreignKeyRelationsOld.forEach(models.ForeignKeyRelation::delete);


        taskSet.save();
        return redirect(routes.TaskSetController.view(taskSet.getId()));
    }

    /**
     * This method deletes an TaskSet object
     */
    @Security.Authenticated(CreatorSecured.class)
    public Result delete(Long id) {
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
    public Result rate(Long id) {
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
    public Result comment(Long id) {
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
        return ok(CommentView.toJson(comment));
    }
/*
    public Result upload() {
        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart uploadJson = body.getFile("uploadJson");



    }
*/


    /**
     * Returns the FileName of the desired Download.
     * @return
     */
    public Result download() {
        JsonNode jsonNode = request().body().asJson();

        ArrayList<TaskSet> taskSets = new ArrayList<>();
        for (JsonNode node : jsonNode.findPath("taskSetIds")) {
            taskSets.add(TaskSetDAO.getById(node.longValue()));
        }

        String fileName = "exportTaskSets_" + new Date().getTime() + ".json";

        File outputFile = new File("public/download/" + fileName);
        try {
            outputFile.createNewFile();

            PrintWriter printWriter = new PrintWriter(outputFile);

            printWriter.print(TaskSetView.toJson(taskSets));

            printWriter.close();

            return ok(fileName);
        } catch (IOException e) {
            Logger.warn(e.getMessage());
            return ok(fileName);
        }
    }
}
