package controllers;

import dao.*;

import models.*;

import play.libs.Json;
import play.mvc.BodyParser;
import secured.UserAuthenticator;
import view.CommentView;
import view.RatingView;
import view.TaskView;

import com.fasterxml.jackson.databind.JsonNode;

import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    @BodyParser.Of(BodyParser.Json.class)
    public Result create(Long taskSetId) {
        User        user        = UserDAO.getBySession(request().username());
        Role        role        = user.getRole();
        JsonNode    taskNode    = request().body().asJson();
        TaskSet     taskSet     = TaskSetDAO.getById(taskSetId);

        if(taskSet == null) {
            Logger.warn("TaskController.create - cannot find TaskSet");
            return notFound("cannot find TaskSet");
        }

        if(taskSet.getCreator().getId() == user.getId()) {
            if(!role.getOwnTaskPermissions().canCreate()) {
                return forbidden("You have not the permissions to create a task to this taskSet");
            }
        } else {
            if(!role.getForeignTaskPermissions().canCreate()) {
                return forbidden("You have not the permissions to create a task to this taskSet");
            }
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
        User        user        = UserDAO.getBySession(request().username());
        Role        role        = user.getRole();
        TaskSet     taskSet     = TaskSetDAO.getById(id);

        if(taskSet == null) {
            Logger.warn("TaskController.read - TaskSet not found");
            return notFound();
        }

        List<Task>  taskList = new ArrayList<>();

        if(taskSet.getCreator().getId() == user.getId()) {
            if(!role.getOwnTaskSetPermissions().canRead()) {
                return forbidden("You have not the permissions to read the tasks of this taskset");
            }
        } else {
            if(!role.getForeignTaskSetPermissions().canRead()) {
                return forbidden("You have not the permissions to read the tasks of this taskset");
            }
        }

        if(role.getOwnTaskPermissions().canRead()) {
            taskList.addAll(taskSet.getTasks().stream().filter(task -> task.getCreator().getId() == user.getId()).collect(Collectors.toList()));
        }
        if(role.getForeignTaskPermissions().canRead()) {
            taskList.addAll(taskSet.getTasks().stream().filter(task -> task.getCreator().getId() == user.getId()).collect(Collectors.toList()));
        }


        if (taskList.size() == 0) {
            Logger.warn("TaskSet.index - no tasks found");
            return ok();
        }

        return ok(Json.toJson(taskList));
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
        User        user        = UserDAO.getBySession(request().username());
        Role        role        = user.getRole();
        Task        task        = TaskDAO.getById(taskId);

        if (task == null) {
            Logger.warn("TaskController.view("+taskId+") - no task found");
            return notFound("no task found");
        }

        if(task.getCreator().getId() == user.getId()) {
            if(role.getOwnTaskPermissions().canRead()) {
                return forbidden("you have not the permission to view this task");
            }
        } else {
            if(role.getForeignTaskPermissions().canRead()) {
                return forbidden("you have not the permission to view this task");
            }
        }

        return ok(Json.toJson(task));
    }

    public Result update(Long taskId) {
        User        user        = UserDAO.getBySession(request().username());
        Role        role        = user.getRole();
        Task        task        = TaskDAO.getById(taskId);

        if(task == null) {
            Logger.warn("TaskController.update - cannot find Task");
            return notFound();
        }

        if(task.getCreator().getId() == user.getId()) {
            if(!role.getOwnTaskPermissions().canUpdate()) {
                return forbidden("You have not the permissions to update this task");
            }
        } else {
            if(!role.getForeignTaskPermissions().canUpdate()) {
                return forbidden("You have not the permissions to update this task");
            }
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
        User user = UserDAO.getBySession(request().username());
        Role role = user.getRole();
        Task task = TaskDAO.getById(taskId);
        long taskSetId = task.getTaskSet().getId();

        if(task.getCreator().getId() == user.getId()) {
            if(!role.getOwnTaskPermissions().canDelete()) {
                return forbidden("you have not the permissions to delete this tasks");
            }
        } else {
            if(!role.getForeignTaskPermissions().canDelete()) {
                return forbidden("you have not the permissions to delete this task");
            }
        }

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
