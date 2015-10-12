package view;

import models.Comment;
import models.Profile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.libs.Json;

/**
 * @author fabiomazzone
 */
public class CommentView {
    public static Comment fromJsonForm(JsonNode commentBody, Profile profile) {
        String comment = commentBody.path("text").asText();
        return new Comment(comment, profile);
    }

    public static ObjectNode toJson(Comment comment) {
        ObjectNode node = Json.newObject();

        node.put("profile",     comment.getProfile().toJson());
        node.put("text",        comment.getComment());
        node.put("created_at",  String.valueOf(comment.getCreated_at()));

        return node;
    }
}
