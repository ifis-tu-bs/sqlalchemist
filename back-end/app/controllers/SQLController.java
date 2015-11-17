package controllers;

import akka.actor.Nobody;
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
        Task task = TaskDAO.getNewTask(profile, Math.round(difficulty / 2), false);

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
        Logger.info("SQLCOntroller.trivia");

        Task task = TaskDAO.getNewTask(profile, difficulty, stay);


        if(task == null) {
            Logger.warn("SQLController.trivia - no task available");
            return badRequest("no task available");
        }
        profile.setCurrentTaskSet(task.getTaskSet());
        profile.update();
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


    public Result homework(long homeWorkID, long taskID) {
        Profile profile = ProfileDAO.getByUsername(request().username());
        HomeWork homeWork = HomeWorkDAO.getById(homeWorkID);
        Task task = TaskDAO.getById(taskID);
        SubmittedHomeWork submittedHomeWork = SubmittedHomeWorkDAO.getSubmitsForProfileHomeWorkTask(profile, homeWork, task);
        boolean contains = false;
        for(TaskSet taskSet : homeWork.getTaskSets()) {
            if(taskSet.contains(task)) {
                contains = true;
                break;
            }
        }

        if(!contains) {
            Logger.info("Keine Task gefunden :(");
            return badRequest("Keine Task gefunden :(");
        }
        ObjectNode taskNode = TaskView.toJsonExercise(task);

        if(submittedHomeWork == null) {
            taskNode.put("syntaxChecksDone", 0);
            taskNode.put("semanticChecksDone", 0);
        } else {
            taskNode.put("syntaxChecksDone", submittedHomeWork.getSyntaxChecksDone());
            taskNode.put("semanticChecksDone", submittedHomeWork.getSemanticChecksDone());
        }

        profile.setCurrentHomeWork(homeWork);
        profile.update();
        return ok(taskNode);
    }

    public Result homeworkSolve(Long TaskID, Boolean submit) {
        Profile         profile         = ProfileDAO.getByUsername(request().username());
        HomeWork        homeWork        = profile.getCurrentHomeWork();
        Task            task            = TaskDAO.getById(TaskID);
        JsonNode        body            = request().body().asJson();
        UserStatement   userStatement   = UserStatementView.fromJsonForm(body);
        SubmittedHomeWork submittedHomeWork = SubmittedHomeWorkDAO.getSubmitsForProfileHomeWorkTask(profile, homeWork, task);

        if (homeWork == null) {
            Logger.warn("No active HomeWork");
            return badRequest("No active HomeWork");
        }
        if (userStatement == null) {
            Logger.warn("TaskController.triviaSolve - invalid json body");
            return badRequest("invalid json body");
        }
        if(task == null) {
            Logger.warn("TaskController.triviaSolve - no task found");
            return badRequest("no task found");
        }
        if (submittedHomeWork == null) {
            submittedHomeWork = SubmittedHomeWorkDAO.create(profile, task, homeWork);
            if(submittedHomeWork == null) {
                Logger.warn("Cant Create SubmittedHomeWork object");
                return badRequest("Cant Create SubmittedHomeWork object");
            }
        }

        if((task.getAvailableSyntaxChecks() - submittedHomeWork.getSyntaxChecksDone() ) <= 0) {
            Logger.warn("You have no SyntaxChecks left");
            return badRequest("You have no SyntaxChecks left");
        }

        if((task.getAvailableSemanticChecks()- submittedHomeWork.getSemanticChecksDone()) <= 0) {
            Logger.warn("You have no SemanticChecks left");
            return badRequest("You have no SemanticChecks left");
        }

        SQLResult   sqlResult   = SQLParser.checkUserStatement(task, userStatement);
        ObjectNode  resultNode;
        Result      result;
        boolean     status;

        resultNode = SQLResultView.toJson(sqlResult, userStatement, submit);
        if(sqlResult.getType() == SQLResult.SEMANTICS) {
            status = false;
            result = badRequest(resultNode);
        } else {
            status = true;
            result = ok(resultNode);
        }

        profile.addStatement();
        profile.addTime(userStatement.getTime());

        if (submit) {
            submittedHomeWork.submit(status, userStatement.getStatement());
        } else {
            submittedHomeWork.addSyntaxCheck();
        }

        submittedHomeWork.update();
        return result;
    }
}
