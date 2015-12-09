package view;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.SubmittedHomeWork;
import play.libs.Json;

/**
 * Created by Invisible on 08.12.2015.
 */
public class SubmittedHomeWorkView {
    public static ObjectNode toJson(SubmittedHomeWork submittedHomeWork) {
        ObjectNode objectNode = Json.newObject();

        objectNode.put("done", submittedHomeWork.getSolve());
        objectNode.put("statement", submittedHomeWork.getStatement());
        objectNode.put("semanticChecksDone" ,submittedHomeWork.getSemanticChecksDone());
        objectNode.put("syntaxChecksDone", submittedHomeWork.getSyntaxChecksDone());

        return objectNode;
    }

}
