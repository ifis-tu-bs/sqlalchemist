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
    public static TaskSet fromJsonForm(Profile profile, JsonNode jsonNode) {
        String                  taskSetName             = jsonNode.path("taskSetName").asText();
        JsonNode                tableDefinitionArray    = jsonNode.path("tableDefinitions");
        JsonNode                foreignKeyArray         = jsonNode.path("foreignKeys");
        JsonNode                taskArray               = jsonNode.path("tasks");
        List<TableDefinition>   tableDefinitions        = new ArrayList<>();
        List<ForeignKeyRelation>foreignKeyRelations     = new ArrayList<>();
        List<Task>              tasks                   = new ArrayList<>();
        boolean                 isHomework              = jsonNode.path("isHomework").asBoolean();
        TaskSet                 taskSet;

        for(JsonNode tableDefinitionNode : tableDefinitionArray) {
            tableDefinitions.add(TableDefinitionView.fromJsonForm(tableDefinitionNode));
        }

        // Setting foreignKey Attributes
        for(JsonNode foreignKeyNode : foreignKeyArray) {
            foreignKeyRelations.add(ForeignKeyRelationView.fromJsonForm(foreignKeyNode));
        }

        taskSet = new TaskSet(taskSetName, tableDefinitions, foreignKeyRelations, profile, isHomework);

        for(int i = 0; i < taskArray.size(); i++) {
            JsonNode taskNode = taskArray.get(i);
            Task task = TaskView.fromJsonForm(taskNode, taskSetName + " " + i, profile);
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
        ArrayNode   taskNode                = JsonNodeFactory.instance.arrayNode();
        List<Rating>ratings                 = taskSet.getRatings();
        ArrayNode   commentNode             = JsonNodeFactory.instance.arrayNode();


        for(TableDefinition tableDefinition : taskSet.getTableDefinitions()) {
            tableDefNode.add(TableDefinitionView.toJson(tableDefinition));
        }

        for(ForeignKeyRelation foreignKeyRelation : taskSet.getForeignKeyRelations()) {
            foreignKeyRelationNode.add(ForeignKeyRelationView.toJson(foreignKeyRelation));
        }

        for(Task task : taskSet.getTasks()) {
            taskNode.add(TaskView.toJsonList(task));
            ratings.addAll(task.getRatings());
        }

        for(Comment comment : taskSet.getComments()) {
            commentNode.add(CommentView.toJson(comment));
        }

        Rating      rating_sum  = Rating.sum(ratings);

        taskSetJson.put("id",                   taskSet.getId());
        taskSetJson.put("taskSetName",          taskSet.getTaskSetName());
        taskSetJson.put("tableDefinitions",     tableDefNode);
        taskSetJson.put("foreignKeyRelations",  foreignKeyRelationNode);
        taskSetJson.put("relationsFormatted",   taskSet.getRelationsFormatted());
        taskSetJson.put("tasks",                taskNode);
        taskSetJson.put("creator",              taskSet.getCreator().toJson()); // ToDo
        taskSetJson.put("isHomeWork",           taskSet.isHomework());
        taskSetJson.put("rating",               RatingView.toJson(rating_sum));
        taskSetJson.put("comments",             commentNode);
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
}
