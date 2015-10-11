package view;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import models.Rating;
import models.Task;

import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;

/**
 * @author fabiomazzone
 */
public class TaskView {
    public static ObjectNode toJson(Task task) {
        ObjectNode json = Json.newObject();
        ArrayNode commentNode = JsonNodeFactory.instance.arrayNode();

        Rating rating_sum = Rating.sum(task.getRatings());

        for(int i = 0; i < task.getComments().size() && i < 100; i++) {
            commentNode.add(CommentView.toJson(task.getComments().get(i)));
        }

        json.put("id",                  task.getId());
        json.put("taskSet",             task.getTaskSet().getId());
        json.put("relationsFormatted",  task.getTaskSet().getRelationsFormatted());
        json.put("taskText",            task.getTaskText());
        json.put("refStatement",        task.getRefStatement());
        json.put("points",              task.getPoints());
        json.put("requiredTerm",        task.getRequiredTerm());

        json.put("creator",             task.getCreator().toJson());

        json.put("comments",            commentNode);
        json.put("rating",              rating_sum.toJson());

        json.put("created_at",          String.valueOf(task.getCreated_at()));
        json.put("updated_at",          String.valueOf(task.getUpdated_at()));
        return json;
    }
}

