package controllers;


import dao.HomeWorkDAO;
import dao.SubmittedHomeWorkDAO;
import dao.TaskSetDAO;
import dao.UserDAO;

import models.HomeWork;
import models.SubmittedHomeWork;
import models.Task;
import models.TaskSet;
import models.User;

import secured.AdminSecured;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import secured.StudentSecured;
import view.HomeWorkView;
import view.TaskView;

import javax.persistence.PersistenceException;
import java.util.List;

/**
 * @author Invisible
 */
public class HomeWorkController extends Controller {


    @Authenticated(AdminSecured.class)
    public Result getAll() {
        User user = UserDAO.getByUsername(request().username());

        List<HomeWork> homeWorkList = HomeWorkDAO.getAll();

        ArrayNode arrayNode = HomeWorkView.toJson(homeWorkList);

        return ok(arrayNode);
    }

    @Authenticated(AdminSecured.class)
    public Result create() {
        User user = UserDAO.getByUsername(request().username());

        JsonNode json = request().body().asJson();

        HomeWork homeWork = HomeWorkView.fromJsonForm(user.getProfile(), json);

        if (homeWork == null) {
            Logger.warn("HomeWorkController.create - The request body doesn't contain a valid HomeWork Json Object");
            return badRequest("Json body is corrupted");
        }

        homeWork.save();

        return ok();
    }

    /**
     * Gives back the current HomeWork and whether the task is submitted for the given profile
     */
    @Authenticated(StudentSecured.class)
    public Result getHomeWorkForCurrentSession() {
        User user = UserDAO.getByUsername(request().username());

        List<HomeWork> homeWorkList = HomeWorkDAO.getAllStudent();

        ArrayNode arrayNode = HomeWorkView.toJsonExerciseForProfile(homeWorkList, user.getProfile());

        return ok(arrayNode);
    }

    @Authenticated(AdminSecured.class)
    public Result getAllStudents () {
        return ok("dummy");
    }

    @Authenticated(AdminSecured.class)
    public Result delete(Long id) {
        User user = UserDAO.getByUsername(request().username());

        try {
            HomeWorkDAO.getById(id).delete();
        } catch (PersistenceException pe) {
            Logger.warn("HomeWorkController.delete - Could not delete" + pe.getMessage());
            return badRequest("Cannot delete. Maybe there are already Submits?");
        }
        return ok("Deleted");
    }

    @Authenticated(AdminSecured.class)
    public Result getSubmitsForHomeworkTaskSet() {

        JsonNode json = request().body().asJson();


        if (json == null) {
            return badRequest("Invalid json data");
        }

        HomeWork homeWork = HomeWorkDAO.getById(json.findPath("homeWork").longValue());
        TaskSet taskSet = TaskSetDAO.getById(json.findPath("taskSet").longValue());

        if (homeWork == null || taskSet == null) {
            return badRequest("Homework or taskfile have not been specified correctly");
        }

        ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();

        for (Task task : taskSet.getTasks()) {
            ArrayNode submits = SubmittedHomeWork.toJsonAll(SubmittedHomeWorkDAO.getSubmitsForSubtask(task));
            ObjectNode objectNode = TaskView.toJsonList(task);
            objectNode.set("submits", submits);

            arrayNode.add(objectNode);
        }

        return ok(arrayNode);
    }


    @Authenticated(AdminSecured.class)
    public Result getForTaskInHomeWork(Long taskId, Long homeWorkId) {
        ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();

        List<SubmittedHomeWork> submits = SubmittedHomeWorkDAO.getSubmitsForTaskInHomeWork(taskId, homeWorkId);

        for (SubmittedHomeWork submittedHomeWork : submits) {
            arrayNode.add(submittedHomeWork.toJson());
        }

        return ok(arrayNode);
    }


}
