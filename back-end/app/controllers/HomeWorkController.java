package controllers;


import dao.HomeWorkChallengeDAO;
import dao.ProfileDAO;
import dao.SubmittedHomeWorkDAO;
import dao.TaskSetDAO;
import dao.UserDAO;

import models.HomeWorkChallenge;
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
import view.TaskView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Invisible on 30.06.2015.
 */
public class HomeWorkController extends Controller {

    @Authenticated(AdminSecured.class)
    public static Result getSubmitsForHomeworkTaskSet() {
        /*
            TODO: Make JSON with all Students and their done HW, that are expired!
            TODO: Make senseful rating of success of their tasks
         */

        JsonNode json = request().body().asJson();


        if (json == null) {
            return badRequest("Invalid json data");
        }

        HomeWorkChallenge homeWorkChallenge = HomeWorkChallengeDAO.getById(json.findPath("homework").longValue());
        TaskSet taskFile = TaskSetDAO.getById(json.findPath("taskFile").longValue());

        if (homeWorkChallenge == null || taskFile == null) {
            return badRequest("Homework or taskfile have not been specified correctly");
        }

        ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();

        for (Task task : taskFile.getTasks()) {
            ArrayNode submits = SubmittedHomeWork.toJsonAll(SubmittedHomeWorkDAO.getSubmitsForSubtask(task));
            ObjectNode objectNode = TaskView.toJson(task);
            objectNode.put("submits", submits);

            arrayNode.add(objectNode);
        }

        return ok(arrayNode);
    }

    @Authenticated(AdminSecured.class)
    public static Result getAllStudents () {
        return ok("dummy");
    }

    @Authenticated(AdminSecured.class)
    public static Result getAll() {
        User user = UserDAO.getByUsername(request().username());

        if (user.getRole() <= User.ROLE_CREATOR) {
            return badRequest("Need to be higher Level User");
        }

        ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();
        List<HomeWorkChallenge> homeWorkList = HomeWorkChallengeDAO.getAll();

        if (homeWorkList == null) {
            return badRequest("No HomeWorks found");
        }

        for (HomeWorkChallenge homeWork : homeWorkList) {
            arrayNode.add(homeWork.toJson());
        }

        return ok(arrayNode);
    }

    /**
     *  Create
     * @return
     */
    @Authenticated(AdminSecured.class)
    public static Result create() {
        User user = UserDAO.getByUsername(request().username());

        if (user.getRole() <= User.ROLE_CREATOR) {
            return badRequest("Need to be higher Level User");
        }

        JsonNode json = request().body().asJson();

        if (json == null) {
            return badRequest("Expecting JSON data");
        }

        long utcTimeFrom = json.findPath("from").longValue();
        long utcTimeTo = json.findPath("to").longValue();
        String name = json.findPath("name").textValue();

        if (name == null || name.equals("")) {
            return badRequest("Name must be specified");
        }

        ArrayList<TaskSet> taskSets = new ArrayList<>();
        for (JsonNode taskFileJson : json.findPath("tasks")) {
            taskSets.add(TaskSetDAO.getById(taskFileJson.longValue()));
        }

        if (utcTimeFrom > utcTimeTo || utcTimeTo < new Date().getTime()) {
            return badRequest("Times do not fit");
        }


        Date start = new Date(utcTimeFrom);
        Date end = new Date(utcTimeTo);
        Logger.info(String.valueOf(start) + "//" + String.valueOf(end) + "::" + taskSets.toString());

        /*
            TODO: Make HomeWorkChallenge.create(); You May add your INTEGER CONSTANTS. i just set them to 0
         */

        // Look at dat and fill in, what you want there :D -#
        //                                                  |
        //                                                  v
        //
        if (HomeWorkChallengeDAO.create(name, ProfileDAO.getByUsername(request().username()), 0, 0, taskSets, 0, start, end) == null) {
            Logger.info("HomeWorkController.Create got null for create. Some data have not been matching constraints!");
            return badRequest("Data did not match constraints");
        }

        return ok();
    }

    @Authenticated(AdminSecured.class)
    public static Result delete(Long id) {
        User user = UserDAO.getByUsername(request().username());

        if (user.getRole() <= User.ROLE_CREATOR) {
            return badRequest("Need to be higher Level User");
        }

        HomeWorkChallengeDAO.getById(id).delete();

        return ok("Deleted");
    }

    @Authenticated(AdminSecured.class)
    public static Result getForTask(Long subTaskId, Long homeWorkChallengeId) {
        ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();

        List<SubmittedHomeWork> submits = SubmittedHomeWorkDAO.getSubmitsForSubtaskAndHomeWorkChallenge(subTaskId, homeWorkChallengeId);

        for (SubmittedHomeWork submittedHomeWork : submits) {
            arrayNode.add(submittedHomeWork.toJson());
        }

        return ok(arrayNode);
    }

    @Authenticated(StudentSecured.class)
    public static Result getCurrentHomeWorkForCurrentSession() {
        User user = UserDAO.getByUsername(request().username());


        HomeWorkChallenge homeWorkChallenge;
        if((homeWorkChallenge = HomeWorkChallengeDAO.getCurrent()) == null) {
            return badRequest("No Current HomeWork");
        }

        JsonNode jsonNode = homeWorkChallenge.toHomeWorkJsonForProfile(user.getProfile());

        return ok(jsonNode);
    }
}
