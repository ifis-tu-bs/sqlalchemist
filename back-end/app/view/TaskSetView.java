package view;

import models.*;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.JsonNode;

import play.libs.Json;

import java.util.ArrayList;
import java.util.List;

/**
 * This static class transforms TaskSet and JSON objects.
 *
 * @author fabiomazzone
 */
public class TaskSetView {
    public static TaskSet fromJsonForm(User user, JsonNode jsonNode) {
        String                  taskSetName             = jsonNode.path("taskSetName").asText();
        JsonNode                tableDefinitionArray    = jsonNode.path("tableDefinitions");
        JsonNode                foreignKeyArray         = jsonNode.path("foreignKeyRelations");
        JsonNode                taskArray               = jsonNode.path("tasks");
        List<TableDefinition>   tableDefinitions        = new ArrayList<>();
        List<ForeignKeyRelation>foreignKeyRelations     = new ArrayList<>();
        List<Task>              tasks                   = new ArrayList<>();
        boolean                 isHomework              = jsonNode.path("isHomeWork").asBoolean();
        TaskSet                 taskSet;

        for(JsonNode tableDefinitionNode : tableDefinitionArray) {
            tableDefinitions.add(TableDefinitionView.fromJsonForm(tableDefinitionNode));
        }

        // Setting foreignKey Attributes
        for(JsonNode foreignKeyNode : foreignKeyArray) {
            foreignKeyRelations.add(ForeignKeyRelationView.fromJsonForm(foreignKeyNode));
        }

        taskSet = new TaskSet(taskSetName, tableDefinitions, foreignKeyRelations, user, isHomework);

        for(int i = 0; i < taskArray.size(); i++) {
            JsonNode taskNode = taskArray.get(i);
            Task task = TaskView.fromJsonForm(taskNode, taskSetName + " " + i, user);
            task.setTaskSet(taskSet);
            tasks.add(task);
        }

        taskSet.setTasks(tasks);

        return taskSet;
    }

    public static ObjectNode toJson(TaskSet taskSet) {
        ObjectNode  taskSetJson             = Json.newObject();
        ArrayNode   tableDefNode            = JsonNodeFactory.instance.arrayNode();
        ArrayNode   foreignKeyRelationNode  = JsonNodeFactory.instance.arrayNode();
        ArrayNode   taskNode                = TaskView.toJsonList(taskSet.getTasks());
        Rating      rating                  = Rating.sum(taskSet.getRatings());
        ArrayNode   commentNode             = JsonNodeFactory.instance.arrayNode();


        for(TableDefinition tableDefinition : taskSet.getTableDefinitions()) {
            tableDefNode.add(TableDefinitionView.toJson(tableDefinition));
        }

        for(ForeignKeyRelation foreignKeyRelation : taskSet.getForeignKeyRelations()) {
            foreignKeyRelationNode.add(ForeignKeyRelationView.toJson(foreignKeyRelation));
        }

        for(Comment comment : taskSet.getComments()) {
            commentNode.add(CommentView.toJson(comment));
        }

        taskSetJson.put("id",                   taskSet.getId());
        taskSetJson.put("taskSetName",          taskSet.getTaskSetName());
        taskSetJson.set("tableDefinitions",     tableDefNode);
        taskSetJson.set("foreignKeyRelations",  foreignKeyRelationNode);
        taskSetJson.put("relationsFormatted",   taskSet.getRelationsFormatted());
        taskSetJson.set("tasks",                taskNode);
        taskSetJson.set("creator",              taskSet.getCreator().toJsonProfile()); // ToDo
        taskSetJson.put("isHomeWork",           taskSet.isHomework());
        taskSetJson.set("rating",               RatingView.toJson(rating));
        taskSetJson.set("comments",             commentNode);
        taskSetJson.put("createdAt",            String.valueOf(taskSet.getCreatedAt()));
        taskSetJson.put("updatedAt",            String.valueOf(taskSet.getUpdatedAt()));

        return taskSetJson;
    }

    public static void updateFromJson(TaskSet taskSet, JsonNode jsonNode) {
        JsonNode    tableDefinitionsNode            = jsonNode.path("tableDefinitions");
        JsonNode    foreignKeyRelationsNode         = jsonNode.path("foreignKeyRelations");
        String                  taskSetName         = jsonNode.path("taskSetName").asText();
        List<TableDefinition>   tableDefinitions    = new ArrayList<>();
        List<ForeignKeyRelation>foreignKeyRelations = new ArrayList<>();

        // Create new Data
        for(JsonNode tableDefinitionNode : tableDefinitionsNode) {
            tableDefinitions.add(TableDefinitionView.fromJsonForm(tableDefinitionNode));
        }

        for(JsonNode foreignKeyRelationNode : foreignKeyRelationsNode) {
            foreignKeyRelations.add(ForeignKeyRelationView.fromJsonForm(foreignKeyRelationNode));
        }

        taskSet.setTaskSetName(taskSetName);
        taskSet.setTableDefinitions(tableDefinitions);
        taskSet.setForeignKeyRelations(foreignKeyRelations);
    }

    public static ArrayNode toJson(List<TaskSet> taskSetList) {
        ArrayNode taskSetNode = JsonNodeFactory.instance.arrayNode();

        for(TaskSet taskSet : taskSetList) {
            taskSetNode.add(TaskSetView.toJson(taskSet));
        }

        return taskSetNode;
    }

    public static ObjectNode toJsonHomeWork(TaskSet taskSet) {
        ObjectNode  taskSetJson             = Json.newObject();

        taskSetJson.put("id",                   taskSet.getId());
        taskSetJson.put("taskSetName",          taskSet.getTaskSetName());

        return taskSetJson;
    }
}
