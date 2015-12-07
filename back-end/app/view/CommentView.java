package view;

import models.Comment;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import models.User;
import play.libs.Json;

/**
 * @author fabiomazzone
 */
public class CommentView {
    public static Comment fromJsonForm(JsonNode commentBody, User user) {
        String comment = commentBody.path("text").asText();
        return new Comment(comment, user);
    }

    public static ObjectNode toJson(Comment comment) {
        ObjectNode node = Json.newObject();

        node.set("profile",     comment.getCreator().toJsonProfile());
        node.put("text",        comment.getComment());
        node.put("createdAt",  String.valueOf(comment.getCreated_at()));

        return node;
    }
}
