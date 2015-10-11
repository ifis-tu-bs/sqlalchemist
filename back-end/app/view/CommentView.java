package view;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Comment;
import play.libs.Json;

/**
 * @author fabiomazzone
 */
public class CommentView {
    public static ObjectNode toJson(Comment comment) {
        ObjectNode node = Json.newObject();

        node.put("profile", comment.getProfile().toJson());
        node.put("text",    comment.getText());
        node.put("written", String.valueOf(comment.getCreated_at()));

        return node;
    }
}
