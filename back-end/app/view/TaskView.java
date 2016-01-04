package view;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import dao.SolvedTaskDAO;
import dao.SubmittedHomeWorkDAO;
import models.*;

import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;

import java.util.List;

/**
 * @author fabiomazzone
 */
public class TaskView {
    public static Task fromJsonForm(JsonNode taskNode, String taskNameDefault, User user) {
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

        return new Task(taskName, taskText, refStatement, evaluationStrategy, points, requiredTerm, user, availableSyntaxChecks, availableSemanticChecks);
    }

    public static ObjectNode toJson(Task task) {
        ObjectNode json = Json.newObject();

        Rating rating_sum = Rating.sum(task.getRatings());

        json.put("id",                  task.getId());
        json.put("taskName",            task.getTaskName());
        json.put("taskSet",             task.getTaskSet().getId());
        json.put("relationsFormatted",  task.getTaskSet().getRelationsFormatted());
        json.put("taskText",            task.getTaskText());
        json.put("refStatement",        task.getRefStatement());
        json.put("evaluationStrategy",  task.getEvaluationStrategy());
        json.put("points",              task.getPoints());
        json.put("requiredTerm",        task.getRequiredTerm());
        json.put("availableSyntaxChecks",   task.getAvailableSyntaxChecks());
        json.put("availableSemanticChecks", task.getAvailableSemanticChecks());

        json.set("creator",             task.getCreator().toJsonUser());

        json.set("rating",              RatingView.toJson(rating_sum));
        json.set("comments",            Json.toJson(task.getComments()));

        json.put("createdAt",           String.valueOf(task.getCreated_at()));
        json.put("updatedAt",           String.valueOf(task.getUpdated_at()));
        return json;
    }

    public static ObjectNode toJsonExercise(Task task) {
        ObjectNode  json        = Json.newObject();
        Rating      rating_sum  = Rating.sum(task.getRatings());
        TaskSet     taskSet     = task.getTaskSet();

        json.put("id",                  task.getId());
        json.put("taskSet",             taskSet.getId());
        json.put("name",            task.getTaskName());
        json.put("relationsFormatted",  taskSet.getRelationsFormatted());
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
            taskNode.add(TaskView.toJson(task));
        }

        return taskNode;
    }

    /**
     * Examines, whether the Profile has already Submitted (NOT SOLVED) a given Task
     */
    public static ObjectNode toJsonHomeWorkForProfile(Task task, HomeWork homework, User user) {
        ObjectNode json = toJsonExercise(task);
        SubmittedHomeWork submittedHomeWork = SubmittedHomeWorkDAO.getSubmitsForProfileHomeWorkTask(user, homework, task);
        if(submittedHomeWork != null) {
            json.put("done",        submittedHomeWork.getSolve());
        } else {
            json.put("done",        false);
        }



        return json;
    }

    public static ArrayNode toJsonHomeWorkForProfileList(List<Task> taskList, HomeWork homework, User user) {
        ArrayNode taskNode = JsonNodeFactory.instance.arrayNode();

        for(Task task : taskList) {
            taskNode.add(TaskView.toJsonHomeWorkForProfile(task, homework, user));
        }

        return taskNode;
    }

    /**
     * Examines, whether the Profile has already Submitted (NOT SOLVED) a given Task. Also uses the whole Task (inclusive refStatement
     */
    public static ObjectNode toJsonHomeWorkForProfileWithRefStatement(Task task, HomeWork homework, User user) {
        ObjectNode json = toJson(task);
        SubmittedHomeWork submittedHomeWork = SubmittedHomeWorkDAO.getSubmitsForProfileHomeWorkTask(user, homework, task);
        if(submittedHomeWork != null) {
            json.set("submit",      SubmittedHomeWorkView.toJson(submittedHomeWork));
            json.put("done",        submittedHomeWork.getSolve());
        } else {
            json.put("done",        false);
        }



        return json;
    }

    public static ObjectNode toJsonHomeWorkForProfileWithRefStatement(List<Task> taskList, HomeWork homework, User user) {
        ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();
        int done = 0, all = taskList.size();

        for(Task task : taskList) {
            ObjectNode objectNode = TaskView.toJsonHomeWorkForProfileWithRefStatement(task, homework, user);
            if (objectNode.get("done").asBoolean())
                done++;
            arrayNode.add(objectNode);
        }

        ObjectNode objectNode = Json.newObject();

        objectNode.set("tasks", arrayNode);
        objectNode.put("done", done);
        objectNode.put("all", all);

        return objectNode;
    }
}
