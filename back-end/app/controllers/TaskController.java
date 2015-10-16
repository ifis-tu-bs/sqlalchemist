package controllers;

import dao.*;

import models.*;

import secured.CreatorSecured;
import secured.UserSecured;

import view.CommentView;
import view.RatingView;
import view.TaskView;

import com.fasterxml.jackson.databind.JsonNode;

import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

import java.util.List;

/**
 * @author fabiomazzone
 */
@Security.Authenticated(UserSecured.class)
public class TaskController extends Controller {

    /**
     * This method creates an Object
     *
     * @param taskSetId     the id of the taskSet
     * @return              returns an redirection to the new task
     */
    @Security.Authenticated(CreatorSecured.class)
    public static Result create(Long taskSetId) {
        Profile     profile     = ProfileDAO.getByUsername(request().username());
        JsonNode    taskNode    = request().body().asJson();
        TaskSet     taskSet     = TaskSetDAO.getById(taskSetId);
        if(taskSet == null) {
            Logger.warn("TaskController.create - cannot find TaskSet");
            return badRequest("cannot find TaskSet");
        }
        String      taskName    = taskSet.getTaskSetName() + "" + taskSet.getTasks().size();
        Task        task        = TaskView.fromJsonForm(taskNode, taskName, profile);

        if(task == null) {
            Logger.warn("TaskController.create - invalid json");
            return badRequest("invalid json");
        }

        task.setTaskSet(taskSet);
        task.save();

        return redirect(routes.TaskController.view(task.getId()));
    }

    /**
     * This method returns all created Task
     *
     * GET      /task
     *
     * @return returns a JSON Array filled with all Task
     */
    public static Result read() {
        List<Task>  taskList = TaskDAO.getAll();

        if (taskList == null) {
            Logger.warn("TaskSet.index - no TaskSet found");
            return badRequest("no TaskSet found");
        }

        return ok(TaskView.toJsonList(taskList));
    }

    /**
     * This method returns the Task that matches to the given id
     *
     *  GET      /task/:id
     *
     * @param taskId    the id of a Task as long
     * @return      returns a Task
     */
    public static Result view(Long taskId) {
        Task task = TaskDAO.getById(taskId);

        if (task == null) {
            Logger.warn("TaskController.view("+taskId+") - no task found");
            return badRequest("no task found");
        }

        return ok(TaskView.toJsonList(task));
    }

    @Security.Authenticated(CreatorSecured.class)
    public static Result update(Long taskId) {
        Profile     profile     = ProfileDAO.getByUsername(request().username());
        Task        task        = TaskDAO.getById(taskId);

        if(task == null) {
            Logger.warn("TaskController.update - cannot find Task");
            return badRequest("cannot find Task");
        }
        JsonNode    taskNode    = request().body().asJson();
        Task        taskNew     = TaskView.fromJsonForm(taskNode, "", profile);

        task.setTaskName(taskNew.getTaskName());
        task.setTaskText(taskNew.getTaskText());
        task.setRefStatement(taskNew.getRefStatement());
        task.setEvaluationStrategy(taskNew.getEvaluationStrategy());
        task.setPoints(taskNew.getPoints());
        task.setRequiredTerm(taskNew.getRequiredTerm());

        task.update();

        return redirect(routes.TaskController.view(task.getId()));
    }

    @Security.Authenticated(CreatorSecured.class)
    public static Result delete(Long taskId) {
        Task task = TaskDAO.getById(taskId);

        task.delete();

        return redirect(routes.TaskController.read());
    }

    /**
     * this method controls the rating of the Task
     *
     * POST      /task/:id/rate
     * Needs a JSON Body Request with values:
     *  positive    :int
     *  negative    :int
     *  needReview  :int
     *
     * @param id    the id of the Task
     * @return      returns a http code with a result of the operation
     */
    public static Result rate(Long id) {
        JsonNode body       = request().body().asJson();
        Task task           = TaskDAO.getById(id);
        Profile profile     = ProfileDAO.getByUsername(request().username());
        Rating rating       = RatingView.fromJsonForm(body, profile);

        if (task == null) {
            Logger.warn("TaskController.rate("+id+") - no Task found");
            return badRequest("no Task found");
        }
        if(rating == null) {
            Logger.warn("TaskController.rate - invalid json body");
            return badRequest("invalid json body");
        }

        task.addRating(rating);
        task.update();
        return redirect(routes.TaskController.view(task.getId()));
    }

    /**
     * this method handles the comments for Task
     *
     * POST     /task/:id/comment
     * Needs a JSON Body Request with values:
     *  text        :String
     *
     * @param id    the id of the Task
     * @return      returns a http code with a result message
     */
    public static Result comment(Long id) {
        Profile     profile = ProfileDAO.getByUsername(request().username());
        JsonNode    body    = request().body().asJson();
        Task        task    = TaskDAO.getById(id);
        Comment     comment  = CommentView.fromJsonForm(body, profile);

        if (task == null) {
            Logger.warn("TaskController.comment("+id+") - no Task found");
            return badRequest("no Task found");
        }

        if (comment == null) {
            Logger.warn("TaskController.comment - invalid json body");
            return badRequest("invalid json body");
        }

        task.addComment(comment);
        task.update();
        return ok(CommentView.toJson(comment));
    }
}
