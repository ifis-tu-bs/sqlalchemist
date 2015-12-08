package view;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.HomeWork;
import models.TaskSet;
import models.User;

import java.util.List;

/**
 * Created by Invisible on 08.12.2015.
 */
public class UserView {

    public static ObjectNode studentToJson(User user) {
        ObjectNode objectNode = JsonNodeFactory.instance.objectNode();

        objectNode.put("matNr", user.getMatNR());
        objectNode.put("email", user.getEmail());

        return objectNode;
    }

    public static ArrayNode studentToJson(List<User> userList) {
        ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();

        for(User user : userList) {
            arrayNode.add(studentToJson(user));
        }

        return arrayNode;
    }

    public static ObjectNode studentToJsonWithHomeWork(HomeWork homeWork, User user) {
        ObjectNode objectNode = studentToJson(user);
        int done = 0, all = 0;


        ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();

        for (TaskSet taskSet : homeWork.getTaskSets()) {
            ObjectNode objectNode1 = TaskSetView.toJson(taskSet);
            objectNode1.set("tasks", TaskView.toJsonHomeWorkForProfileWithRefStatement(taskSet.getTasks(),homeWork,user));
            done += objectNode1.get("tasks").get("done").asInt();
            all += objectNode1.get("tasks").get("all").asInt();
            arrayNode.add(objectNode1);
        }

        objectNode.put("taskSets", arrayNode);
        objectNode.put("done", done);
        objectNode.put("all", all);

        return objectNode;
    }

    public static ArrayNode studentToJsonWithHomeWork(HomeWork homeWork, List<User> userList) {
        ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();

        for (User user : userList) {
            arrayNode.add(studentToJsonWithHomeWork(homeWork,user));
        }

        return arrayNode;
    }


}
