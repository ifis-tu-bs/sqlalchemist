package view;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import dao.HomeWorkDAO;
import dao.ProfileDAO;
import dao.TaskSetDAO;
import models.HomeWork;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Profile;
import models.TaskSet;
import play.Logger;
import play.libs.Json;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Invisible on 16.10.2015.
 */
public class HomeWorkView {

    public static HomeWork fromJsonForm (Profile profile, JsonNode node) {
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
        for (JsonNode taskFileJson : node.findPath("tasks")) {
            taskSets.add(TaskSetDAO.getById(taskFileJson.longValue()));
        }

        if (utcTimeFrom > utcTimeTo || utcTimeTo < new Date().getTime()) {
            return null;
        }


        Date start = new Date(utcTimeFrom);
        Date end = new Date(utcTimeTo);
        Logger.info(String.valueOf(start) + "//" + String.valueOf(end) + "::" + taskSets.toString());


        if (HomeWorkDAO.create(name, profile, taskSets, start, end) == null) {
            Logger.info("HomeWorkController.Create got null for create. Some data have not been matching constraints!");
            return null;
        }
        return null;
    }

    public static ObjectNode toJson(HomeWork homeWork) {
        ObjectNode objectNode = Json.newObject();
        ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();

        for (TaskSet taskSet : homeWork.getTaskSets()) {
            arrayNode.add(TaskSetView.toJson(taskSet));
        }

        objectNode.put("id",    homeWork.getId());
        objectNode.put("homeWorkName",  homeWork.getHomeWorkName());
        objectNode.put("taskSets",  arrayNode);
        objectNode.put("creator",   homeWork.getCreator().toJson());
        objectNode.put("start_at",  String.valueOf(homeWork.getStart_at()));
        objectNode.put("expire_at", String.valueOf(homeWork.getExpire_at()));

        return objectNode;
    }

    public static ArrayNode toJson(List<HomeWork> homeWorkList) {
        ArrayNode homeWorkNode = JsonNodeFactory.instance.arrayNode();

        for(HomeWork homeWork : homeWorkList) {
            homeWorkNode.add(HomeWorkView.toJson(homeWork));
        }

        return homeWorkNode;
    }

    public static ObjectNode toJsonExcerciseForProfile(HomeWork homeWork, Profile profile) {

        ObjectNode objectNode = Json.newObject();
        ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();


        for (TaskSet taskSet : homeWork.getTaskSets()) {
            ObjectNode taskSetJson = TaskSetView.toJson(taskSet);
            taskSetJson.set("tasks",    TaskView.toJsonHomeWorkForProfileList(taskSet.getTasks(), profile));

            arrayNode.add(taskSetJson);
        }


        objectNode.put("name",     homeWork.getHomeWorkName());
        objectNode.put("taskSets", arrayNode);

        return objectNode;
    }

}
