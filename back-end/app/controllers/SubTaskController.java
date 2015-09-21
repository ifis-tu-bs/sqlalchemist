package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.*;


import sqlgame.exception.MySQLAlchemistException;
import models.*;

import dao.UserDAO;
import dao.ProfileDAO;

import Exception.SQLAlchemistException;
import play.Logger;
import play.libs.Json;
import play.mvc.*;
import secured.UserSecured;

import java.util.List;


/**
 * @author fabiomazzone
 */
@Security.Authenticated(UserSecured.class)
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
        List<SubTask>   subTasks = SubTask.getAll();

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
        SubTask subTask = SubTask.getById(id);

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
        SubTask subTask     = SubTask.getById(id);
        Profile profile     = ProfileDAO.getByUsername(request().username());

        if (subTask == null) {
            Logger.warn("SubTaskController.rate("+id+") - no SubTask found");
            return badRequest("no SubTask found");
        }

        int p = body.findPath("positive").asInt();
        int n = body.findPath("negative").asInt();
        int e = body.findPath("needReview").asInt();

        boolean status = false;


        if(p > 0 || n > 0 || e > 0) {
            status = subTask.rate(profile, p > 0, e > 0, n > 0);
        } else {
            Logger.warn("SubTaskController.rate - json body was empty");
            return badRequest("json body was empty");
        }
        subTask.update();
        if(!status) {
            Logger.warn("SubTaskController.rate - already rated");
            return badRequest("try again later");
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
        SubTask     subTask= SubTask.getById(id);

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

        Comment comment = Comment.create(profile, text);
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
        Scroll scroll   = Scroll.getById(id);

        if(profile == null) {
            Logger.warn("SubTaskController.story - no valid profile found");
            return badRequest("no valid profile found");
        }

        if(ScrollCollection.contains(profile, scroll)) {
            profile.setCurrentScroll(scroll);
        } else {
            Logger.warn("SubTaskController.story - Scroll not in the collection: " + scroll.toJson());
            return badRequest("Scroll not in the collection");
        }

        SubTask subTask             = SubTask.getByScroll(profile, scroll);

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

        SubTask subTask = SubTask.getById(id);

        Logger.info(body.toString());

        ObjectNode node = Json.newObject();
        profile.addStatement();
        profile.addTime(time);
        Result result = null;
        try {
            if(subTask.solve(statement)) {
                SolvedSubTask.update(profile, subTask, true);
                profile.addSuccessfully();
                int coins = profile.addScore(subTask.getScore() / time);
                profile.addCurrentScroll();

                node.put("terry", "your answer was correct");
                node.put("time", time);
                node.put("score", subTask.getScore() / time);
                node.put("coins",  coins);

                result = ok(node);
            } else {
                SolvedSubTask.update(profile, subTask, false);

                node.put("terry", "SEMANTIC");
                node.put("time",        time);

                result = badRequest(node);
            }
        } catch (SQLAlchemistException e) {
            Logger.info("SOL");
            SolvedSubTask.update(profile, subTask, false);

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

        SubTask subTask = SubTask.getByDifficulty(profile, points);

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
    public static Result triviaSolve(Long id) {
        Profile profile = ProfileDAO.getByUsername(request().username());
        JsonNode body   = request().body().asJson();
        SubTask subTask = SubTask.getById(id);

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

        Result result = null;
        profile.addStatement();
        profile.addTime(time);
        ObjectNode node = Json.newObject();
        Logger.info(statement);
        try {
            if(subTask.solve(statement)) {
                SolvedSubTask.update(profile, subTask, true);
                int coins = profile.addScore(subTask.getScore() / time);
                profile.addSuccessfully();

                node.put("terry", "your answer was correct");
                node.put("time",    time);
                node.put("score", subTask.getScore() / time);
                node.put("coins",  coins);

                result = ok(node);
            } else {
                SolvedSubTask.update(profile, subTask, false);

                node.put("terry",   "symantic error");
                node.put("time",    time);

                result = badRequest(node);
            }
        } catch (SQLAlchemistException e) {
            SolvedSubTask.update(profile, subTask, false);

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
        SubTask subTask = SubTask.getByChallengeID(1L, profile);

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
        SubTask subTask = SubTask.getById(id);

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

        SubmittedHomeWork.submit(profile, subTask, correct, statement);

        return ok("HomeWork Has Been Submitted");
    }


}
