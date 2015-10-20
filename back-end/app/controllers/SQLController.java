package controllers;

import dao.*;

import models.*;

import secured.UserSecured;
import sqlparser.SQLParser;

import view.SQLResultView;
import view.TaskView;
import view.UserStatementView;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.Logger;
import play.Play;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

import java.util.List;

/**
 * @author fabiomazzone
 */
@Security.Authenticated(UserSecured.class)
public class SQLController extends Controller {
    public Result story(long id) {
        Profile profile = ProfileDAO.getByUsername(request().username());
        Scroll scroll   = ScrollDAO.getById(id);

        if(scroll == null) {
            Logger.warn("SQLController.story - invalid Scroll ID");
            return badRequest("invalid Scroll ID");
        }

        if(ScrollCollectionDAO.contains(profile, scroll)) {
            profile.setCurrentScroll(scroll);
        } else {
            Logger.warn("TaskController.story - Scroll not in the collection: " + scroll.toJson());
            return badRequest("Scroll not in the collection");
        }
        float difficulty = 0;

        if(scroll.isRecipe()) {
            difficulty = scroll.getPotion().getPowerLevel();
        } else {
            difficulty++;
            List<ScrollCollection> scrollList = ScrollCollectionDAO.getScrollCollection(profile);
            for(ScrollCollection scrollCollection : scrollList) {
                Scroll scrollI = scrollCollection.getScroll();
                if( !scrollI.isRecipe() && scrollI.getType() == scroll.getType()) {
                    difficulty++;
                }
            }
        }
        Logger.info("Difficulty: " + Math.round(difficulty / 2));
        //Task task = TaskDAO.getByDifficulty(profile, Math.round(difficulty / 2));
        Task task = TaskDAO.getById(1L);

        if(task == null) {
            Logger.warn("TaskController.story - no task found for ScrollID: " + id);
            return badRequest("no task found for given ScrollID");
        }

        profile.update();
        return ok(TaskView.toJsonExercise(task));
    }

    public Result storySolve(long id) {
        Profile         profile         = ProfileDAO.getByUsername(request().username());
        JsonNode        body            = request().body().asJson();
        Task            task            = TaskDAO.getById(id);
        UserStatement   userStatement   = UserStatementView.fromJsonForm(body);

        if (userStatement == null) {
            Logger.warn("TaskController.triviaSolve - invalid json body");
            return badRequest("invalid json body");
        }
        if(task == null) {
            Logger.warn("TaskController.triviaSolve - no task found");
            return badRequest("no task found");
        }

        SQLResult   sqlResult   = SQLParser.checkUserStatement(task, userStatement);
        ObjectNode  resultNode;
        Result      result;
        boolean     status;

        if(sqlResult.getType() != SQLResult.SUCCESSFULL) {
            status = false;
            resultNode = SQLResultView.toJson(sqlResult, userStatement);
            result = badRequest(resultNode);
        } else {
            profile.addSuccessfully();
            int coins = profile.addScore(task.getScore() / userStatement.getTime());
            profile.addCurrentScroll();
            status = true;
            resultNode = SQLResultView.toJson(sqlResult, userStatement, coins);
            result = ok(resultNode);
        }

        profile.addStatement();
        profile.addTime(userStatement.getTime());

        SolvedTaskDAO.update(profile, task, status);

        profile.update();
        return result;

    }

    public Result trivia(int difficulty, boolean stay) {
        Profile profile = ProfileDAO.getByUsername(request().username());

        if(difficulty < 0 && difficulty > Play.application().configuration().getInt("HighestTaskDifficulty")) {
            Logger.warn("SQLController.trivia - difficulty is out of range");
            return badRequest("difficulty is out of range");
        }

        //Task task = TaskDAO.getByDifficulty(profile, difficulty);
        Task task = TaskDAO.getById(1L);

        if(task == null) {
            Logger.warn("SQLController.trivia - no task available");
            return badRequest("no task available");
        }

        return ok(TaskView.toJsonExercise(task));
    }

    public Result triviaSolve(Long id) {
        Profile         profile         = ProfileDAO.getByUsername(request().username());
        JsonNode        body            = request().body().asJson();
        Task            task            = TaskDAO.getById(id);
        UserStatement userStatement     = UserStatementView.fromJsonForm(body);

        if (userStatement == null) {
            Logger.warn("TaskController.triviaSolve - invalid json body");
            return badRequest("invalid json body");
        }
        if(task == null) {
            Logger.warn("TaskController.triviaSolve - no task found");
            return badRequest("no task found");
        }

        SQLResult   sqlResult   = SQLParser.checkUserStatement(task, userStatement);
        ObjectNode  resultNode;
        Result      result;
        boolean     status;

        if(sqlResult.getType() != SQLResult.SUCCESSFULL) {
            status = false;
            resultNode = SQLResultView.toJson(sqlResult, userStatement);
            result = badRequest(resultNode);
        } else {
            profile.addSuccessfully();
            int coins = profile.addScore(task.getScore() / userStatement.getTime());
            status = true;
            resultNode = SQLResultView.toJson(sqlResult, userStatement, coins);
            result = ok(resultNode);
        }

        profile.addStatement();
        profile.addTime(userStatement.getTime());

        SolvedTaskDAO.update(profile, task, status);

        profile.update();
        return result;
    }


    public Result homework() {
        //Profile profile = ProfileDAO.getByUsername(request().username());
        //Task task = TaskDAO.getByChallengeID(1L, profile);

        return badRequest("not implemented yet");
    }

    /*
    public Result homeworkSolve(Long id) {
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
*/

}
