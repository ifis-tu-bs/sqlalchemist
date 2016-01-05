package controllers;

import dao.*;

import models.*;

import play.libs.Json;
import secured.UserAuthenticator;
import view.CommentView;
import view.RatingView;
import view.TaskView;

import com.fasterxml.jackson.databind.JsonNode;

import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;

import java.util.List;

/**
 * @author fabiomazzone
 */
@Authenticated(UserAuthenticator.class)
public class TaskController extends Controller {

    /**
     * This method creates an Object
     *
     * @param taskSetId     the id of the taskSet
     * @return              returns an redirection to the new task
     */
    @Authenticated
    public Result create(Long taskSetId) {
        User        user        = UserDAO.getBySession(request().username());
        JsonNode    taskNode    = request().body().asJson();
        TaskSet     taskSet     = TaskSetDAO.getById(taskSetId);
        if(taskSet == null) {
            Logger.warn("TaskController.create - cannot find TaskSet");
            return badRequest("cannot find TaskSet");
        }
        String      taskName    = taskSet.getTaskSetName() + "" + taskSet.getTasks().size();
        Task        task        = TaskView.fromJsonForm(taskNode, taskName, user);

        if(task == null) {
            Logger.warn("TaskController.create - invalid json");
            return badRequest("invalid json");
        }

        task.setTaskSet(taskSet);
        task.save();

        return redirect(routes.TaskController.view(task.getId()));
    }

    /**
     * This method returns all Task from a TaskSet
     *
     * GET      /task
     *
     * @return returns a JSON Array filled with all Task
     */
    public Result read(long id) {
        TaskSet taskSet = TaskSetDAO.getById(id);
        if(taskSet == null) {
            Logger.warn("TaskController.read - TaskSet not found");
            return notFound();
        }

        List<Task>  taskList = taskSet.getTasks();

        if (taskList == null) {
            Logger.warn("TaskSet.index - no TaskSet found");
            return ok();
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
    public Result view(Long taskId) {
        Task task = TaskDAO.getById(taskId);

        if (task == null) {
            Logger.warn("TaskController.view("+taskId+") - no task found");
            return badRequest("no task found");
        }

        return ok(TaskView.toJson(task));
    }

    public Result update(Long taskId) {
        User        user        = UserDAO.getBySession(request().username());
        Task        task        = TaskDAO.getById(taskId);

        if(task == null) {
            Logger.warn("TaskController.update - cannot find Task");
            return badRequest("cannot find Task");
        }
        JsonNode    taskNode    = request().body().asJson();
        Task        taskNew     = TaskView.fromJsonForm(taskNode, "", user);

        task.setTaskName(taskNew.getTaskName());
        task.setTaskText(taskNew.getTaskText());
        task.setRefStatement(taskNew.getRefStatement());
        task.setEvaluationStrategy(taskNew.getEvaluationStrategy());
        task.setPoints(taskNew.getPoints());
        task.setRequiredTerm(taskNew.getRequiredTerm());
        task.setAvailableSyntaxChecks(taskNew.getAvailableSyntaxChecks());
        task.setAvailableSemanticChecks(taskNew.getAvailableSemanticChecks());

        task.update();

        return redirect(routes.TaskController.view(task.getId()));
    }

    public Result delete(Long taskId) {
        Task task = TaskDAO.getById(taskId);
        long taskSetId = task.getTaskSet().getId();

        task.delete();

        return redirect(routes.TaskController.read(taskId));
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
    public Result rate(Long id) {
        JsonNode    body        = request().body().asJson();
        Task        task        = TaskDAO.getById(id);
        User        user        = UserDAO.getBySession(request().username());
        Rating      rating      = RatingView.fromJsonForm(body, user);

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

        Rating      rating_sum  = Rating.sum(task.getRatings());
        if(rating_sum.getEditRatings() >= 500 || rating_sum.getNegativeRatings() > rating_sum.getPositiveRatings()) {
            task.setAvailable(false);
        } else if(rating_sum.getPositiveRatings() >= 200) {
            task.setAvailable(true);
        }

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
    public Result comment(Long id) {
        User        user        = UserDAO.getBySession(request().username());
        JsonNode    body    = request().body().asJson();
        Task        task    = TaskDAO.getById(id);
        Comment     comment  = CommentView.fromJsonForm(body, user);

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
        return ok(Json.toJson(comment));
    }
}
