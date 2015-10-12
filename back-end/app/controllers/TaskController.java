package controllers;

import dao.ProfileDAO;
import dao.ScrollDAO;
import dao.ScrollCollectionDAO;
import dao.SolvedTaskDAO;
import dao.SubmittedHomeWorkDAO;
import dao.TaskDAO;

import Exception.SQLAlchemistException;

import models.Comment;
import models.Profile;
import models.Rating;
import models.Scroll;
import models.Task;

import secured.UserSecured;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.*;

import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import view.CommentView;
import view.RatingView;
import view.TaskView;

import java.util.List;

/**
 * @author fabiomazzone
 */
@Authenticated(UserSecured.class)
public class TaskController extends Controller {

    /**
     * This method returns all created Task
     *
     * GET      /task
     *
     * @return returns a JSON Array filled with all Task
     */
    public static Result index() {
        ArrayNode       arrayNode= JsonNodeFactory.instance.arrayNode();
        List<Task>   tasks = TaskDAO.getAll();

        if (tasks == null) {
            Logger.warn("TaskController.index - no Tasks found");
            return badRequest("no Tasks found");
        }

        for(Task task : tasks) {
            arrayNode.add(TaskView.toJson(task));
        }

        return ok(arrayNode);
    }

    /**
     * This method returns the Task that matches to the given id
     *
     *  GET      /task/:id
     *
     * @param id    the id of a Task as long
     * @return      returns a Task
     */
    public static Result view(long id) {
        Task task = TaskDAO.getById(id);

        if (task == null) {
            Logger.warn("TaskController.view("+id+") - no task found");
            return null;
        }
        ObjectNode taskJsoned = TaskView.toJson(task);

        return ok(taskJsoned);
    }


    public static Result destroy(Long id) {
        return ok();
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
        return ok();
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
        return ok();
    }



    /**
     *
     * @param id potion id
     * @return
     */
    public static Result story(long id) {
        Profile profile = ProfileDAO.getByUsername(request().username());
        Scroll scroll   = ScrollDAO.getById(id);

        if(profile == null) {
            Logger.warn("TaskController.story - no valid profile found");
            return badRequest("no valid profile found");
        }

        if(ScrollCollectionDAO.contains(profile, scroll)) {
            profile.setCurrentScroll(scroll);
        } else {
            Logger.warn("TaskController.story - Scroll not in the collection: " + scroll.toJson());
            return badRequest("Scroll not in the collection");
        }

        Task task             = TaskDAO.getByScroll(profile, scroll);

        if(task == null) {
            Logger.warn("TaskController.story - no task found for ScrollID: " + id);
            return badRequest("no task found for given ScrollID");
        }

        profile.update();
        return ok(TaskView.toJson(task));
    }

    /**
     *
     * @param id task-id
     * @return
     */
    @SuppressWarnings("UnusedAssignment")
    public static Result storySolve(long id) {
        Profile profile = ProfileDAO.getByUsername(request().username());
        JsonNode body = request().body().asJson();

        if (body == null) {
            return badRequest("Could not retrieve Json from POST body!");
        }
        String  statement    = body.findPath("statement").textValue();
        int     time         = (body.findPath("time").intValue()/1000);

        if(statement == null) {
            return badRequest("Expecting Json data");
        }

        Task task = TaskDAO.getById(id);

        Logger.info(body.toString());

        ObjectNode node = Json.newObject();
        profile.addStatement();
        profile.addTime(time);
        Result result;
        try {
            if(task.solve(statement)) {
                SolvedTaskDAO.update(profile, task, true);
                profile.addSuccessfully();
                int coins = profile.addScore(task.getScore() / time);
                profile.addCurrentScroll();

                node.put("terry", "your answer was correct");
                node.put("time", time);
                node.put("score", task.getScore() / time);
                node.put("coins",  coins);

                result = ok(node);
            } else {
                SolvedTaskDAO.update(profile, task, false);

                node.put("terry", "SEMANTIC");
                node.put("time",        time);

                result = badRequest(node);
            }
        } catch (SQLAlchemistException e) {
            Logger.info("SQL");
            SolvedTaskDAO.update(profile, task, false);

            node.put("terry",       "SYNTAX");
            node.put("time",        time);
            node.put("DBMessage", e.getMessage());

            result = badRequest(node);
        }
        profile.update();
        return result;
    }

    /**
     *
     * @return
     */
    public static Result trivia(Long difficulty) {
        Profile profile = ProfileDAO.getByUsername(request().username());
        int points = difficulty.intValue();
        if(difficulty >= 5) {
            points = 5;
        } else if(difficulty <= 1) {
            points = 1;
        }

        Task task = TaskDAO.getByDifficulty(profile, points);

        if(task != null) {
            return ok(TaskView.toJson(task));
        }
        return badRequest("wrong difficulty");
    }

    /**
     *
     * @param id
     * @return
     */
    @SuppressWarnings("UnusedAssignment")
    public static Result triviaSolve(Long id) {
        Profile profile = ProfileDAO.getByUsername(request().username());
        JsonNode body   = request().body().asJson();
        Task task = TaskDAO.getById(id);

        if (profile == null || body == null) {
            Logger.warn("TaskController.triviaSolve - no profile or no jsonBody or wrong TaskId");
            return badRequest("no profile or no jsonBody or wrong TaskId");
        }
        Logger.info(body.toString());
        String  statement   = body.findPath("statement").asText();
        int     time         = (body.findPath("time").asInt()/1000);

        if(statement == null || time == 0) {
            Logger.warn("TaskController.triviaSolve - Expecting Json Data");
            return badRequest("Expecting Json data");
        }

        Result result;
        profile.addStatement();
        profile.addTime(time);
        ObjectNode node = Json.newObject();
        Logger.info(statement);
        try {
            if(task.solve(statement)) {
                SolvedTaskDAO.update(profile, task, true);
                int coins = profile.addScore(task.getScore() / time);
                profile.addSuccessfully();

                node.put("terry", "your answer was correct");
                node.put("time",    time);
                node.put("score", task.getScore() / time);
                node.put("coins",  coins);

                result = ok(node);
            } else {
                SolvedTaskDAO.update(profile, task, false);

                node.put("terry",   "symantic error");
                node.put("time",    time);

                result = badRequest(node);
            }
        } catch (SQLAlchemistException e) {
            SolvedTaskDAO.update(profile, task, false);

            node.put("terry",       "syntax error");
            node.put("time",        time);
            node.put("DBMessage",   e.getMessage());

            result = badRequest(node);
        }

        profile.update();
        return result;
    }

    /**
     *
     * @return
     */
    public static Result homework() {
        Profile profile = ProfileDAO.getByUsername(request().username());
        Task task = TaskDAO.getByChallengeID(1L, profile);

        return ok(TaskView.toJson(task));
    }

    /**
     *
     * @param id
     * @return
     */
    public static Result homeworkSolve(Long id) {
        Profile profile = ProfileDAO.getByUsername(request().username());
        JsonNode body   = request().body().asJson();
        Task task = TaskDAO.getById(id);

        if (profile == null || body == null) {
            Logger.warn("TaskController.homeworkSolve - no profile or no jsonBody or wrong TaskId");
            return badRequest("no profile or no jsonBody or wrong TaskId");
        }

        String  statement   = body.findPath("statement").asText();
        int     time        = body.findPath("time").asInt();

        if(statement == null || time == 0) {
            Logger.warn("TaskController.triviaSolve - Expecting Json Data");
            return badRequest("Expecting Json data");
        }

        profile.addTime(time);
        boolean correct;
        try {
            correct = task.solve(statement);
        } catch (SQLAlchemistException e) {
            correct = false;
        }

        SubmittedHomeWorkDAO.submit(profile, task, correct, statement);

        return ok("HomeWork Has Been Submitted");
    }


}
