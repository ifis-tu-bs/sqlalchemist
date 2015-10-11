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
    public static TaskSet fromJson(Profile profile, JsonNode jsonNode) {

        return null;
    }

    public static ObjectNode toJson(TaskSet taskSet) {
        ObjectNode taskSetJson  = Json.newObject();
        ArrayNode tableDefNode  = JsonNodeFactory.instance.arrayNode();
        ArrayNode taskNode      = JsonNodeFactory.instance.arrayNode();
        ArrayNode commentNode   = JsonNodeFactory.instance.arrayNode();

        for(TableDefinition tableDefinition : taskSet.getTableDefinitions()) {
            tableDefNode.add(TableDefinitionView.toJson(tableDefinition));
        }

        List<Rating> ratings   = new ArrayList<>(taskSet.getRatings());

        for(Task task : taskSet.getTasks()) {
            taskNode.add(TaskView.toJson(task));
            ratings.addAll(task.getRatings());
        }

        for(Comment comment : taskSet.getComments()) {
            commentNode.add(CommentView.toJson(comment));
        }

        Rating      rating_sum  = Rating.sum(ratings);

        taskSetJson.put("id",                   taskSet.getId());
        taskSetJson.put("tableDefinitions",     tableDefNode);
        taskSetJson.put("relationsFormatted",   taskSet.getRelationsFormatted());
        taskSetJson.put("tasks",                taskNode);
        taskSetJson.put("creator",              taskSet.getCreator().toJson()); // ToDo
        taskSetJson.put("isHomeWork",           taskSet.isHomework());
        taskSetJson.put("rating",               rating_sum.toJson());
        taskSetJson.put("comments",             commentNode);
        taskSetJson.put("created_at",           String.valueOf(taskSet.getCreated_at()));
        taskSetJson.put("updated_at",           String.valueOf(taskSet.getUpdated_at()));

        return taskSetJson;
    }
}
