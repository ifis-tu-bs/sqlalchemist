package view;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import dao.HomeWorkDAO;
import dao.TaskSetDAO;
import dao.UserDAO;
import models.HomeWork;
import com.fasterxml.jackson.databind.node.ObjectNode;

import models.TaskSet;
import models.User;
import play.Logger;
import play.libs.Json;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.text.DateFormatSymbols;
/**
 * @author Invisible
 */
public class HomeWorkView {

    public static HomeWork fromJsonForm (User user, JsonNode node) {
        if (node == null) {
            return null;
        }

        long utcTimeFrom = node.findPath("start_at").longValue();
        long utcTimeTo = node.findPath("expire_at").longValue();
        String name = node.findPath("homeWorkName").textValue();

        if (name == null || name.equals("")) {
            return null;
        }

        ArrayList<TaskSet> taskSets = new ArrayList<>();
        for (JsonNode taskFileJson : node.findPath("taskSetIds")) {
            taskSets.add(TaskSetDAO.getById(taskFileJson.longValue()));
        }

        if (utcTimeFrom > utcTimeTo || utcTimeTo < new Date().getTime()) {
            return null;
        }


        Date start = new Date(utcTimeFrom);
        Date end = new Date(utcTimeTo);
        Logger.info(String.valueOf(start) + "//" + String.valueOf(end) + "::" + taskSets.toString());



        HomeWork homeWork = HomeWorkDAO.create(name, user, taskSets, start, end);

        if (homeWork == null) {
            Logger.info("HomeWorkController.Create got null for create. Some data have not been matching constraints!");
            return null;
        }
        return homeWork;
    }

    private static ObjectNode toJson(HomeWork homeWork) {
        ObjectNode objectNode = Json.newObject();
        ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();

        for (TaskSet taskSet : homeWork.getTaskSets()) {
            arrayNode.add(TaskSetView.toJson(taskSet));
        }

        objectNode.put("id",    homeWork.getId());
        objectNode.put("homeWorkName",  homeWork.getHomeWorkName());
        objectNode.set("taskSets",  arrayNode);
        if(homeWork.getCreator() != null) {
            objectNode.set("creator", homeWork.getCreator().toJsonUser());
        } else {
            objectNode.put("creator", "None");
        }
        objectNode.put("start_at",  String.valueOf(homeWork.getStart_at()));
        objectNode.put("expire_at", String.valueOf(homeWork.getExpire_at()));

        return objectNode;
    }

    private static ObjectNode toJsonStudent(HomeWork homeWork) {
        ObjectNode objectNode = Json.newObject();
        ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();

        for (TaskSet taskSet : homeWork.getTaskSets()) {
            arrayNode.add(TaskSetView.toJson(taskSet));
        }

        objectNode.put("id",    homeWork.getId());
        objectNode.put("homeWorkName",  homeWork.getHomeWorkName());
        objectNode.set("creator",   homeWork.getCreator().toJsonUser());
        objectNode.put("start_at",  String.valueOf(homeWork.getStart_at()));
        objectNode.put("expire_at", String.valueOf(homeWork.getExpire_at()));

        return objectNode;
    }

    public static ArrayNode toJsonStudent(List<HomeWork> homeWorkList) {
        ArrayNode homeWorkNode = JsonNodeFactory.instance.arrayNode();

        for(HomeWork homeWork : homeWorkList) {
            homeWorkNode.add(HomeWorkView.toJsonStudent(homeWork));
        }

        return homeWorkNode;
    }

    public static ArrayNode toJson(List<HomeWork> homeWorkList) {
        ArrayNode homeWorkNode = JsonNodeFactory.instance.arrayNode();

        for(HomeWork homeWork : homeWorkList) {
            homeWorkNode.add(HomeWorkView.toJson(homeWork));
        }

        return homeWorkNode;
    }

    public static ObjectNode toJsonExerciseForUser(HomeWork homeWork, User user) {
        ObjectNode objectNode = Json.newObject();
        ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", new DateFormatSymbols(Locale.US));


        for (TaskSet taskSet : homeWork.getTaskSets()) {
            ObjectNode taskSetJson = TaskSetView.toJsonHomeWork(taskSet);
            taskSetJson.set("tasks",    TaskView.toJsonHomeWorkForProfileList(taskSet.getTasks(), homeWork, user));

            arrayNode.add(taskSetJson);
        }

        objectNode.put("id",        homeWork.getId());
        objectNode.put("name",      homeWork.getHomeWorkName());
        objectNode.set("taskSets",  arrayNode);
        objectNode.put("start_at",  sdf.format(homeWork.getStart_at()));
        objectNode.put("expire_at", sdf.format(homeWork.getExpire_at()));
        objectNode.put("expired",   homeWork.getExpire_at().compareTo(new Date()) < 0);

        return objectNode;
    }

    public static ArrayNode toJsonExerciseForUser(List<HomeWork> homeWorks, User user) {
        ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();

        for (HomeWork homeWork : homeWorks) {
            arrayNode.add(toJsonExerciseForUser(homeWork, user));
        }

        return arrayNode;
    }

    public static ObjectNode toJsonAdminTool(HomeWork homeWork) {
        ObjectNode objectNode = toJson(homeWork);
        objectNode.set("students", UserView.studentToJsonWithHomeWork(homeWork, UserDAO.getAllStudents()));
        objectNode.remove("taskSets");


        return objectNode;
    }

    public static ArrayNode toJsonAdminTool(List<HomeWork> homeWorkList) {
        ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();

        for (HomeWork homeWork : homeWorkList) {
            arrayNode.add(toJsonAdminTool(homeWork));
        }

        return arrayNode;
    }
}
