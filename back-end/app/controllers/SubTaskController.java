package controllers;

import dao.CommentDAO;
import dao.ProfileDAO;
import dao.RatingDAO;
import dao.ScrollDAO;
import dao.ScrollCollectionDAO;
import dao.SolvedSubTaskDAO;
import dao.SubmittedHomeWorkDAO;
import dao.SubTaskDAO;

import Exception.SQLAlchemistException;

import models.Comment;
import models.Profile;
import models.Rating;
import models.Scroll;
import models.ScrollCollection;
import models.SolvedSubTask;
import models.SubmittedHomeWork;
import models.SubTask;

import secured.UserSecured;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.*;

import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;

import java.util.List;

/**
 * @author fabiomazzone
 */
@Authenticated(UserSecured.class)
public class SubTaskController extends Controller {

    /**
     * This method returns all created SubTask
     *
     * GET      /task
     *
     * @return returns a JSON Array filled with all SubTask
     */
    public static Result index() {
        ArrayNode       arrayNode= JsonNodeFactory.instance.arrayNode();
        List<SubTask>   subTasks = SubTaskDAO.getAll();

        if (subTasks == null) {
            Logger.warn("SubTaskController.index - no SubTasks found");
            return badRequest("no SubTasks found");
        }

        for(SubTask subTask : subTasks) {
            arrayNode.add(subTask.toJson());
        }

        return ok(arrayNode);
    }

    /**
     * This method returns the SubTask that matches to the given id
     *
     *  GET      /task/:id
     *
     * @param id    the id of a SubTask as long
     * @return      returns a SubTask
     */
    public static Result view(long id) {
        SubTask subTask = SubTaskDAO.getById(id);

        if (subTask == null) {
            Logger.warn("SubTaskController.view("+id+") - no subTask found");
            return null;
        }
        ObjectNode subTaskJsoned = subTask.toJson();
        subTaskJsoned.put("commentList", Comment.toJsonAll(subTask.getCommentList()));

        return ok(subTaskJsoned);
    }


    public static Result destroy(Long id) {
        return ok();
    }

    /**
     * this method controls the rating of the SubTask
     *
     * POST      /task/:id/rate
     * Needs a JSON Body Request with values:
     *  positive    :int
     *  negative    :int
     *  needReview  :int
     *
     * @param id    the id of the SubTask
     * @return      returns a http code with a result of the operation
     */
    public static Result rate(Long id) {
        JsonNode body       = request().body().asJson();
        SubTask subTask     = SubTaskDAO.getById(id);
        Profile profile     = ProfileDAO.getByUsername(request().username());

        if (subTask == null) {
            Logger.warn("SubTaskController.rate("+id+") - no SubTask found");
            return badRequest("no SubTask found");
        }

        boolean p = body.findPath("positive").asInt() > 0;
        boolean n = body.findPath("negative").asInt() > 0;
        boolean r = body.findPath("needReview").asInt() > 0;

        if( p && !n && !r ||
           !p &&  n && !r ||
           !p && !n && r) {
          Rating rating = RatingDAO.create(profile, p, n, r);
          if(rating != null) {
            subTask.addRating(rating);
          } else {
            Logger.warn("SubTaskController.rate - Rating cannot be saved");
            return badRequest("Please try again later");
          }
        } else {
          Logger.warn("SubTaskController.rate - Json body was invalid");
          return badRequest("Please try again later");
        }
        subTask.update();
        return ok();
    }

    /**
     * this method handles the comments for SubTask
     *
     * POST     /task/:id/comment
     * Needs a JSON Body Request with values:
     *  text        :String
     *
     * @param id    the id of the SubTask
     * @return      returns a http code with a result message
     */
    public static Result comment(Long id) {
        Profile     profile = ProfileDAO.getByUsername(request().username());
        JsonNode    body    = request().body().asJson();
        SubTask     subTask= SubTaskDAO.getById(id);

        if (subTask == null) {
            Logger.warn("TaskFileController.comment("+id+") - no TaskFile found");
            return badRequest("no TaskFile found");
        }


        String  text = body.findPath("text").asText();

        //Logger.info("Text: " + text);

        if (text == null || text.equals("")) {
            Logger.warn("TaskFileController.comment - invalid json or empty text");
            return badRequest("Text is either null or empty");
        }

        Comment comment = CommentDAO.create(profile, text);
        if (comment == null) {
            Logger.warn("TaskFileController.comment - can't create comment");
            return badRequest("can't create comment");
        }
        subTask.addComment(comment);
        Logger.info(subTask.getCommentList().toString());
        subTask.update();
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
            Logger.warn("SubTaskController.story - no valid profile found");
            return badRequest("no valid profile found");
        }

        if(ScrollCollectionDAO.contains(profile, scroll)) {
            profile.setCurrentScroll(scroll);
        } else {
            Logger.warn("SubTaskController.story - Scroll not in the collection: " + scroll.toJson());
            return badRequest("Scroll not in the collection");
        }

        SubTask subTask             = SubTaskDAO.getByScroll(profile, scroll);

        if(subTask == null) {
            Logger.warn("SubTaskController.story - no subTask found for ScrollID: " + id);
            return badRequest("no subTask found for given ScrollID");
        }

        profile.update();
        return ok(subTask.toJsonExercise());
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

        SubTask subTask = SubTaskDAO.getById(id);

        Logger.info(body.toString());

        ObjectNode node = Json.newObject();
        profile.addStatement();
        profile.addTime(time);
        Result result;
        try {
            if(subTask.solve(statement)) {
                SolvedSubTaskDAO.update(profile, subTask, true);
                profile.addSuccessfully();
                int coins = profile.addScore(subTask.getScore() / time);
                profile.addCurrentScroll();

                node.put("terry", "your answer was correct");
                node.put("time", time);
                node.put("score", subTask.getScore() / time);
                node.put("coins",  coins);

                result = ok(node);
            } else {
                SolvedSubTaskDAO.update(profile, subTask, false);

                node.put("terry", "SEMANTIC");
                node.put("time",        time);

                result = badRequest(node);
            }
        } catch (SQLAlchemistException e) {
            Logger.info("SQL");
            SolvedSubTaskDAO.update(profile, subTask, false);

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
        };

        SubTask subTask = SubTaskDAO.getByDifficulty(profile, points);

        if(subTask != null) {
            return ok(subTask.toJsonExercise());
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
        SubTask subTask = SubTaskDAO.getById(id);

        if (profile == null || body == null) {
            Logger.warn("SubTaskController.triviaSolve - no profile or no jsonBody or wrong SubTaskId");
            return badRequest("no profile or no jsonBody or wrong SubTaskId");
        }
        Logger.info(body.toString());
        String  statement   = body.findPath("statement").asText();
        int     time         = (body.findPath("time").asInt()/1000);

        if(statement == null || time == 0) {
            Logger.warn("SubTaskController.triviaSolve - Expecting Json Data");
            return badRequest("Expecting Json data");
        }

        Result result;
        profile.addStatement();
        profile.addTime(time);
        ObjectNode node = Json.newObject();
        Logger.info(statement);
        try {
            if(subTask.solve(statement)) {
                SolvedSubTaskDAO.update(profile, subTask, true);
                int coins = profile.addScore(subTask.getScore() / time);
                profile.addSuccessfully();

                node.put("terry", "your answer was correct");
                node.put("time",    time);
                node.put("score", subTask.getScore() / time);
                node.put("coins",  coins);

                result = ok(node);
            } else {
                SolvedSubTaskDAO.update(profile, subTask, false);

                node.put("terry",   "symantic error");
                node.put("time",    time);

                result = badRequest(node);
            }
        } catch (SQLAlchemistException e) {
            SolvedSubTaskDAO.update(profile, subTask, false);

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
        SubTask subTask = SubTaskDAO.getByChallengeID(1L, profile);

        return ok(subTask.toJsonExercise());
    }

    /**
     *
     * @param id
     * @return
     */
    public static Result homeworkSolve(Long id) {
        Profile profile = ProfileDAO.getByUsername(request().username());
        JsonNode body   = request().body().asJson();
        SubTask subTask = SubTaskDAO.getById(id);

        if (profile == null || body == null) {
            Logger.warn("SubTaskController.homeworkSolve - no profile or no jsonBody or wrong SubTaskId");
            return badRequest("no profile or no jsonBody or wrong SubTaskId");
        }

        String  statement   = body.findPath("statement").asText();
        int     time        = body.findPath("time").asInt();

        if(statement == null || time == 0) {
            Logger.warn("SubTaskController.triviaSolve - Expecting Json Data");
            return badRequest("Expecting Json data");
        }

        profile.addTime(time);
        boolean correct;
        try {
            correct = subTask.solve(statement);
        } catch (SQLAlchemistException e) {
            correct = false;
        }

        SubmittedHomeWorkDAO.submit(profile, subTask, correct, statement);

        return ok("HomeWork Has Been Submitted");
    }


}
