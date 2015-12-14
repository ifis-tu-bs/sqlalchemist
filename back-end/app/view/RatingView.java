package view;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Rating;
import models.User;
import play.libs.Json;

/**
 * @author fabiomazzone
 */
public class RatingView {
    public static Rating fromJsonForm(JsonNode ratingBody, User user) {
        boolean positive    = ratingBody.path("positive").asBoolean();
        boolean negative    = ratingBody.path("negative").asBoolean();
        boolean needReview  = ratingBody.path("needReview").asBoolean();

        return new Rating(positive, negative, needReview, user);
    }

    public static ObjectNode toJson(Rating rating) {
        ObjectNode ratingNode = Json.newObject();

        ratingNode.put("positive",    rating.getPositiveRatings());
        ratingNode.put("negative",    rating.getNegativeRatings());
        ratingNode.put("needReview",  rating.getEditRatings());

        return ratingNode;
    }
}