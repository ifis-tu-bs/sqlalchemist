package view;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import models.Comment;
import models.Profile;
import models.Rating;
import models.Task;

import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;

/**
 * @author fabiomazzone
 */
public class TaskView {
    public static Task fromJsonForm(JsonNode taskNode, Profile creator) {
        String  taskText            = taskNode.get("taskText").asText();
        String  refStatement        = taskNode.get("refStatement").asText();
        int     evaluationstrategy  = taskNode.get("evaluationstrategy").asInt();
        int     points              = taskNode.get("points").asInt();
        int     requiredTerm        = taskNode.get("requiredTerm").asInt();

        return new Task(taskText, refStatement, evaluationstrategy, points, requiredTerm, creator);
    }

    public static ObjectNode toJson(Task task) {
        ObjectNode json = Json.newObject();
        ArrayNode commentNode = JsonNodeFactory.instance.arrayNode();

        Rating rating_sum = Rating.sum(task.getRatings());

        for(Comment comment : task.getComments()) {
          commentNode.add(CommentView.toJson(comment));
        }

        json.put("id",                  task.getId());
        json.put("taskSet",             task.getTaskSet().getId());
        json.put("relationsFormatted",  task.getTaskSet().getRelationsFormatted());
        json.put("taskText",            task.getTaskText());
        json.put("refStatement",        task.getRefStatement());
        json.put("evalstrategy",        task.getEvaluationstrategy());
        json.put("points",              task.getPoints());
        json.put("requiredTerm",        task.getRequiredTerm());

        json.put("creator",             task.getCreator().toJson());

        json.put("rating",              rating_sum.toJson());
        json.put("comments",            commentNode);

        json.put("created_at",          String.valueOf(task.getCreated_at()));
        json.put("updated_at",          String.valueOf(task.getUpdated_at()));
        return json;
    }

}
