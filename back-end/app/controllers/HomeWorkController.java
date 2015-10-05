package controllers;


import dao.HomeWorkChallengeDAO;
import dao.ProfileDAO;
import dao.SubmittedHomeWorkDAO;
import dao.TaskFileDAO;
import dao.UserDAO;

import models.HomeWorkChallenge;
import models.SubmittedHomeWork;
import models.SubTask;
import models.TaskFile;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Invisible on 30.06.2015.
 */
@Authenticated(AdminSecured.class)
public class HomeWorkController extends Controller {

    public static Result getSubmitsForHomeworkTaskFile() {
        /*
            TODO: Make JSON with all Students and their done HW, that are expired!
            TODO: Make senseful rating of success of their tasks
         */

        JsonNode json = request().body().asJson();


        if (json == null) {
            return badRequest("Invalid json data");
        }

        HomeWorkChallenge homeWorkChallenge = HomeWorkChallengeDAO.getById(json.findPath("homework").longValue());
        TaskFile taskFile = TaskFileDAO.getByFileName(json.findPath("taskFile").textValue());

        if (homeWorkChallenge == null || taskFile == null) {
            return badRequest("Homework or taskfile have not been specified correctly");
        }

        ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();

        for (SubTask subTask : taskFile.getSubTasks()) {
            ArrayNode submits = SubmittedHomeWork.toJsonAll(SubmittedHomeWorkDAO.getSubmitsForSubtask(subTask));
            ObjectNode objectNode = subTask.toJson();
            objectNode.put("submits", submits);

            arrayNode.add(objectNode);
        }

        return ok(arrayNode);
    }

    public static Result getAllStudents () {
        return ok("dummy");
    }

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

        ArrayList<TaskFile> taskFiles = new ArrayList<>();
        for (JsonNode taskFileJson : json.findPath("tasks")) {
            taskFiles.add(TaskFileDAO.getByFileName(taskFileJson.textValue()));
        }

        if (utcTimeFrom > utcTimeTo || utcTimeTo < new Date().getTime()) {
            return badRequest("Times do not fit");
        }


        Date start = new Date(utcTimeFrom);
        Date end = new Date(utcTimeTo);
        Logger.info(String.valueOf(start) + "//" + String.valueOf(end) + "::" + taskFiles.toString());

        /*
            TODO: Make HomeWorkChallenge.create(); You May add your INTEGER CONSTANTS. i just set them to 0
         */

        // Look at dat and fill in, what you want there :D -#
        //                                                  |
        //                                                  v
        //
        if (HomeWorkChallengeDAO.create(name, ProfileDAO.getByUsername(request().username()), 0, 0, taskFiles, 0, start, end) == null) {
            Logger.info("HomeWorkController.Create got null for create. Some data have not been matching constraints!");
            return badRequest("Data did not match constraints");
        }

        return ok();
    }

    public static Result delete(Long id) {
        User user = UserDAO.getByUsername(request().username());

        if (user.getRole() <= User.ROLE_CREATOR) {
            return badRequest("Need to be higher Level User");
        }

        HomeWorkChallengeDAO.getById(id).delete();

        return ok("Deleted");
    }

    public static Result getForSubTask(Long subTaskId, Long homeWorkChallengeId) {
        ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();

        List<SubmittedHomeWork> submits = SubmittedHomeWorkDAO.getSubmitsForSubtaskAndHomeWorkChallenge(subTaskId, homeWorkChallengeId);

        for (SubmittedHomeWork submittedHomeWork : submits) {
            arrayNode.add(submittedHomeWork.toJson());
        }

        return ok(arrayNode);
    }
}
