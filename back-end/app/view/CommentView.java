package view;

import models.Comment;
import models.User;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author fabiomazzone
 */
public class CommentView {
    public static Comment fromJsonForm(JsonNode commentBody, User user) {
        String comment = commentBody.path("comment").asText();
        return new Comment(comment, user);
    }
}
