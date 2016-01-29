package controllers;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import dao.SessionDAO;
import dao.TaskSetDAO;

import dao.UserDAO;
import models.*;

import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Http;

import play.mvc.Security.Authenticated;
import secured.homework.CanReadHomeworks;
import secured.taskset.CanCreateTaskSet;
import secured.UserAuthenticator;
import sqlparser.SQLParser;
import sqlparser.SQLStatus;
import view.*;

import com.fasterxml.jackson.databind.JsonNode;

import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;

import javax.persistence.PersistenceException;
import java.io.*;
import java.util.ArrayList;

import java.util.List;

/**
 * This controller is for the TaskFiles
 *
 * @author fabiomazzone
 */
@Authenticated(UserAuthenticator.class)
public class TaskSetController extends Controller {
    /**
     * This method creates TaskFile objects from Json
     *
     * POST     /TaskSet/
     * Needs a TaskSet Json Object
     *
     * @return returns the created TaskSet
     */
    @BodyParser.Of(BodyParser.Json.class)
    @Authenticated(CanCreateTaskSet.class)
    public Result create() {
        User user = UserDAO.getBySession(request().username());
        JsonNode jsonNode = request().body().asJson();

        TaskSet taskSet = TaskSetView.fromJsonForm(user, jsonNode);

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
        User user = UserDAO.getBySession(request().username());
        Role role = user.getRole();
        List<TaskSet> taskSetList;

        if(role.getForeignTaskSetPermissions().canRead() && role.getOwnTaskSetPermissions().canRead()) {
            taskSetList = TaskSetDAO.getAllExceptHomeworks();
        } else if (role.getForeignTaskSetPermissions().canRead()) {
            taskSetList = TaskSetDAO.getAllExceptOwn(user);
        } else if (role.getOwnTaskSetPermissions().canRead()) {
            taskSetList = TaskSetDAO.getAllOwn(user);
        } else {
            Logger.warn("TaskSetController - you have not the permission to read TaskSets");
            return forbidden("you have not the permission to read TaskSets");
        }

        if(taskSetList == null) {
            Logger.warn("TaskSet.index - no TaskSet found");
            return badRequest("no TaskSet found");
        }

        if(role.getHomeworkPermissions().canRead()) {
            List<TaskSet> homeworks = TaskSetDAO.getAllHomeWorkTaskSets();
            if(homeworks != null)
                taskSetList.addAll(homeworks);
        }

        return ok(Json.toJson(taskSetList));
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
        User user = UserDAO.getBySession(request().username());
        Role role = user.getRole();
        TaskSet taskSet = TaskSetDAO.getById(id);

        if (taskSet == null) {
            Logger.warn("TaskFileController.view("+id+") - no TaskSet found");
            return notFound("no TaskSet found");
        }

        if(taskSet.getCreator().getId() == user.getId()) {
            if(!role.getOwnTaskSetPermissions().canRead()) {
                return forbidden("You have not the permissions to read this TaskSet");
            }
        } else {
            if(!role.getForeignTaskSetPermissions().canRead()) {
                return forbidden("You have not the permissions to read this TaskSet");
            }
        }


        return ok(Json.toJson(taskSet));
    }
    /**
     * This method returns all created TaskFiles
     *
     * GET      /TaskSet/
     *
     * @return returns a JSON Array filled with all taskSets
     */
    @Authenticated(CanReadHomeworks.class)
    public Result readHomeWorks() {
        List<TaskSet> taskSetList = TaskSetDAO.getAllHomeWorkTaskSets();

        if (taskSetList == null) {
            Logger.warn("TaskSet.index - no TaskSet found");
            return badRequest("no TaskSet found");
        }

        return ok(Json.toJson(taskSetList));
    }

    /**
     * This method updates the TaskSet
     *
     * @param id        the if of the taskSet
     * @return          returns an redirection to the updated taskSet
     */
    @BodyParser.Of(BodyParser.Json.class)
    public Result update(Long id) {
        User user = UserDAO.getBySession(request().username());
        Role role = user.getRole();
        JsonNode    jsonNode    = request().body().asJson();
        TaskSet     taskSet     = TaskSetDAO.getById(id);

        if(taskSet == null ) {
            Logger.warn("TaskSetController - no TaskSet found for id: " + id);
            return notFound("no TaskSet found");
        }

        if(taskSet.getCreator().getId() == user.getId()) {
            if(!role.getOwnTaskSetPermissions().canUpdate()) {
                Logger.warn("TaskSetController.update - you have not the permissions to update this TaskSet");
                return forbidden("you have not the permissions to update this TaskSet");
            }
        } else {
            if(!role.getForeignTaskSetPermissions().canUpdate()) {
                Logger.warn("TaskSetController.update - you have not the permissions to update this TaskSet");
                return forbidden("you have not the permissions to update this TaskSet");
            }
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
    public Result delete(Long id) {
        User user = UserDAO.getBySession(request().username());
        Role role = user.getRole();
        TaskSet taskSet = TaskSetDAO.getById(id);

        if(taskSet == null) {
            Logger.warn("TaskSetController.delete - no TaskSet found for id: " + id);
            return notFound("no TaskSet found");
        }

        if(taskSet.getCreator().getId() == user.getId()) {
            if(!role.getOwnTaskSetPermissions().canDelete()) {
                Logger.warn("TaskSetController.update - you have not the permissions to delete this TaskSet");
                return forbidden("you have not the permissions to delete this TaskSet");
            }
        } else {
            if(!role.getForeignTaskSetPermissions().canDelete()) {
                Logger.warn("TaskSetController.update - you have not the permissions to delete this TaskSet");
                return forbidden("you have not the permissions to delete this TaskSet");
            }
        }

        try {
            taskSet.delete();
        } catch (PersistenceException pe) {
            Logger.warn("Not deleting TaskSet: " + taskSet.getId() + ", - " + pe.getMessage());
            return badRequest("Cannot delete TaskSet. There are already solved Tasks");
        }

        return ok();
    }

  /**
   * this method controls the rating of the TaskSet
   *
   * POST      /TaskSet/:id/rate
   * Needs a JSON Body Request with values:
   *  positive    :boolean
   *  negative    :boolean
   *  needReview  :boolean
   *
   * @param id    the id of the TaskFile
   * @return      if the operation fails the function returns a bad request with an error message
   */
  public Result rate(Long id) {
    JsonNode    ratingBody  = request().body().asJson();
    TaskSet     taskSet     = TaskSetDAO.getById(id);
    User        user        = UserDAO.getBySession(request().username());
    Rating      rating      = RatingView.fromJsonForm(ratingBody, user);

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

    Rating      rating_sum  = taskSet.getRating();
    if(rating_sum.getEditRatings() >= 200 || rating_sum.getNegativeRatings() > 200) {
      taskSet.setAvailable(false);
    } else if(rating_sum.getPositiveRatings() >= 200) {
      taskSet.setAvailable(true);
    }

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
        User user = UserDAO.getBySession(request().username());
        JsonNode    commentBody = request().body().asJson();
        TaskSet     taskSet     = TaskSetDAO.getById(id);
        Comment     comment     = CommentView.fromJsonForm(commentBody, user);

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
        return ok(Json.toJson(comment));
    }

    public Result upload() {
        User user = UserDAO.getBySession(request().username());

        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart uploadJson = body.getFile("uploadJson");

        try {
            JsonParser jsonParser = new MappingJsonFactory().createParser(uploadJson.getFile());

            JsonNode node = jsonParser.readValueAsTree();

            for (JsonNode taskSetNode : node) {
                TaskSet taskSet = TaskSetView.fromJsonForm(user, taskSetNode);

                taskSet.save();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



        Logger.info("Have following file: " + uploadJson.getFilename());



        return redirect("/admin#/task");
    }



    /**
     * Returns the FileName of the desired Download.
     * @return
     */
    public Result download() {
        JsonNode jsonNode = request().body().asJson();

        Session userSession = SessionDAO.getById(request().username());

        Logger.info("Export:" + userSession.getId());

        ArrayList<TaskSet> taskSets = new ArrayList<>();
        for (JsonNode node : jsonNode.findPath("taskSetIds")) {
            taskSets.add(TaskSetDAO.getById(node.longValue()));
        }

        String fileName = "exportTaskSets_" + userSession.getId() + ".json";

        File downloadDir = new File("download");

        if (!downloadDir.exists())
            downloadDir.mkdir();

        File outputFile = new File("download/" + fileName);
        try {
            //outputFile.createNewFile();
            PrintWriter printWriter = new PrintWriter(outputFile);

            printWriter.print(Json.toJson(taskSets));

            printWriter.close();

            return ok(fileName);
        } catch (IOException e) {
            Logger.warn(e.getMessage());
            return ok(fileName);
        }
    }
}
