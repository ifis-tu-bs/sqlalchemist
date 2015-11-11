package view;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import dao.SolvedTaskDAO;
import models.Comment;
import models.Profile;
import models.Rating;
import models.Task;

import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;

import java.util.List;

/**
 * @author fabiomazzone
 */
public class TaskView {
    public static Task fromJsonForm(JsonNode taskNode, String taskNameDefault, Profile creator) {
        String  taskName            = taskNameDefault;
        if(taskNode.has("taskName")) {
            taskName = taskNode.get("taskName").asText();
        }

        String  taskText            = taskNode.get("taskText").asText();
        String  refStatement        = taskNode.get("refStatement").asText();
        int     evaluationStrategy  = taskNode.get("evaluationStrategy").asInt();
        int     points              = taskNode.get("points").asInt();
        int     requiredTerm        = taskNode.get("requiredTerm").asInt();
        int     availableSyntaxChecks       = taskNode.get("availableSyntaxChecks").asInt();
        int     availableSemanticChecks     = taskNode.get("availableSemanticChecks").asInt();

        return new Task(taskName, taskText, refStatement, evaluationStrategy, points, requiredTerm, creator, availableSyntaxChecks, availableSemanticChecks);
    }

    public static ObjectNode toJsonList(Task task) {
        ObjectNode json = Json.newObject();
        ArrayNode commentNode = JsonNodeFactory.instance.arrayNode();

        Rating rating_sum = Rating.sum(task.getRatings());

        for(Comment comment : task.getComments()) {
          commentNode.add(CommentView.toJson(comment));
        }

        json.put("id",                  task.getId());
        json.put("taskName",            task.getTaskName());
        json.put("taskSet",             task.getTaskSet().getId());
        json.put("relationsFormatted",  task.getTaskSet().toString());
        json.put("taskText",            task.getTaskText());
        json.put("refStatement",        task.getRefStatement());
        json.put("evaluationStrategy",  task.getEvaluationStrategy());
        json.put("points",              task.getPoints());
        json.put("requiredTerm",        task.getRequiredTerm());
        json.put("availableSyntaxChecks",   task.getAvailableSyntaxChecks());
        json.put("availableSemanticChecks", task.getAvailableSemanticChecks());

        json.set("creator",             task.getCreator().toJson());

        json.set("rating",              RatingView.toJson(rating_sum));
        json.set("comments",            commentNode);

        json.put("createdAt",           String.valueOf(task.getCreated_at()));
        json.put("updatedAt",           String.valueOf(task.getUpdated_at()));
        return json;
    }

    public static ObjectNode toJsonExercise(Task task) {
        ObjectNode  json        = Json.newObject();
        Rating      rating_sum  = Rating.sum(task.getRatings());

        json.put("id",                  task.getId());
        json.put("taskName",            task.getTaskName());
        json.put("relationsFormatted",  task.getTaskSet().toString());
        json.put("taskText",            task.getTaskText());
        json.put("points",              task.getPoints());
        json.put("requiredTerm",        task.getRequiredTerm());
        json.put("availableSyntaxChecks",   task.getAvailableSyntaxChecks());
        json.put("availableSemanticChecks", task.getAvailableSemanticChecks());

        json.set("rating",              RatingView.toJson(rating_sum));

        json.put("createdAt",           String.valueOf(task.getCreated_at()));
        json.put("updatedAt",           String.valueOf(task.getUpdated_at()));

        return json;
    }

    public static ArrayNode toJsonList(List<Task> taskList) {
        ArrayNode taskNode = JsonNodeFactory.instance.arrayNode();

        for(Task task : taskList) {
            taskNode.add(TaskView.toJsonList(task));
        }

        return taskNode;
    }

    /**
     * Examines, whether the Profile has already Submitted (NOT SOLVED) a given Task
     */
    public static ObjectNode toJsonHomeWorkForProfile(Task task, Profile profile) {
        ObjectNode json = Json.newObject();

        json.put("id",          task.getId());
        json.put("name",        task.getTaskName());
        json.put("done",        SolvedTaskDAO.getByProfileAndTask(profile, task) != null);

        return json;
    }

    public static ArrayNode toJsonHomeWorkForProfileList(List<Task> taskList, Profile profile) {
        ArrayNode taskNode = JsonNodeFactory.instance.arrayNode();

        for(Task task : taskList) {
            taskNode.add(TaskView.toJsonHomeWorkForProfile(task, profile));
        }

        return taskNode;
    }
}
