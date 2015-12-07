package controllers;

import dao.*;

import models.*;

import play.mvc.Security;
import secured.UserAuthenticator;
import service.ServiceScore;
import service.ServiceUser;
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

import java.util.List;

/**
 * @author fabiomazzone
 */
@Security.Authenticated(UserAuthenticator.class)
public class SQLController extends Controller {
    public Result story(long id) {
        User    user    = UserDAO.getBySession(request().username());
        Scroll  scroll  = ScrollDAO.getById(id);

        if(scroll == null) {
            Logger.warn("SQLController.story - invalid Scroll ID");
            return badRequest("invalid Scroll ID");
        }

        if(ScrollCollectionDAO.contains(user, scroll)) {
            user.setCurrentScroll(scroll);
        } else {
            Logger.warn("TaskController.story - Scroll not in the collection: " + scroll.toJson());
            return badRequest("Scroll not in the collection");
        }
        float difficulty = 0;

        if(scroll.isRecipe()) {
            difficulty = scroll.getPotion().getPowerLevel();
        } else {
            difficulty++;
            List<ScrollCollection> scrollList = ScrollCollectionDAO.getScrollCollection(user);
            for(ScrollCollection scrollCollection : scrollList) {
                Scroll scrollI = scrollCollection.getScroll();
                if( !scrollI.isRecipe() && scrollI.getType() == scroll.getType()) {
                    difficulty++;
                }
            }
        }
        Task task = TaskDAO.getNewTask(user, Math.round(difficulty / 2), false);

        if(task == null) {
            Logger.warn("TaskController.story - no task found for ScrollID: " + id);
            return badRequest("no task found for given ScrollID");
        }

        user.update();
        return ok(TaskView.toJsonExercise(task));
    }

    public Result storySolve(long id) {
        User            user            = UserDAO.getBySession(request().username());
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
            ServiceScore.addSuccessfully(user);
            int coins = ServiceScore.addScore(user, task.getScore() / userStatement.getTime());
            ServiceUser.addCurrentScroll(user);
            status = true;
            resultNode = SQLResultView.toJson(sqlResult, userStatement, coins);
            result = ok(resultNode);
        }
        ServiceScore.addStatement(user);
        ServiceScore.addTime(user, userStatement.getTime());

        SolvedTaskDAO.update(user, task, status);

        user.update();
        return result;

    }

    public Result trivia(int difficulty, boolean stay) {
        User    user    = UserDAO.getBySession(request().username());

        if(difficulty < 0 && difficulty > Play.application().configuration().getInt("HighestTaskDifficulty")) {
            Logger.warn("SQLController.trivia - difficulty is out of range");
            return badRequest("difficulty is out of range");
        }
        Logger.info("SQLCOntroller.trivia");

        Task task = TaskDAO.getNewTask(user, difficulty, stay);


        if(task == null) {
            Logger.warn("SQLController.trivia - no task available");
            return badRequest("no task available");
        }
        user.setCurrentTaskSet(task.getTaskSet());
        user.update();
        return ok(TaskView.toJsonExercise(task));
    }

    public Result triviaSolve(Long id) {
        User    user    = UserDAO.getBySession(request().username());
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
            ServiceScore.addSuccessfully(user);
            int coins = ServiceScore.addScore(user, task.getScore() / userStatement.getTime());
            status = true;
            resultNode = SQLResultView.toJson(sqlResult, userStatement, coins);
            result = ok(resultNode);
        }

        ServiceScore.addStatement(user);
        ServiceScore.addTime(user, userStatement.getTime());

        SolvedTaskDAO.update(user, task, status);

        user.update();
        return result;
    }


    public Result homework(long homeWorkID, long taskID) {
        User    user    = UserDAO.getBySession(request().username());
        HomeWork homeWork = HomeWorkDAO.getById(homeWorkID);
        Task task = TaskDAO.getById(taskID);
        SubmittedHomeWork submittedHomeWork = SubmittedHomeWorkDAO.getSubmitsForProfileHomeWorkTask(user, homeWork, task);
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

        user.setCurrentHomeWork(homeWork);
        user.update();
        return ok(taskNode);
    }

    public Result homeworkSolve(Long TaskID, boolean submit) {
        User            user            = UserDAO.getBySession(request().username());
        HomeWork        homeWork        = user.getCurrentHomeWork();
        Task            task            = TaskDAO.getById(TaskID);
        JsonNode        body            = request().body().asJson();
        UserStatement   userStatement   = UserStatementView.fromJsonForm(body);
        SubmittedHomeWork submittedHomeWork = SubmittedHomeWorkDAO.getSubmitsForProfileHomeWorkTask(user, homeWork, task);

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
            submittedHomeWork = SubmittedHomeWorkDAO.create(user, task, homeWork);
            if(submittedHomeWork == null) {
                Logger.warn("Cant Create SubmittedHomeWork object");
                return badRequest("Cant Create SubmittedHomeWork object");
            }
        }

        if((task.getAvailableSyntaxChecks() - submittedHomeWork.getSyntaxChecksDone()) <= 0) {
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

        ServiceScore.addStatement(user);
        ServiceScore.addTime(user, userStatement.getTime());

        if (submit) {
            Logger.info("Submit");
            submittedHomeWork.submit(status, userStatement.getStatement());
        } else {
            Logger.info("check");
            submittedHomeWork.addSyntaxCheck();
        }

        submittedHomeWork.update();
        return result;
    }
}
