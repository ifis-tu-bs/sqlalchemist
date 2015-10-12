package view;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Profile;
import models.Rating;
import play.libs.Json;

/**
 * Created by fabiomazzone on 12/10/15.
 */
public class RatingView {
    public static Rating fromJsonForm(JsonNode ratingBody, Profile profile) {
        boolean positive    = ratingBody.asBoolean();
        boolean negative    = ratingBody.asBoolean();
        boolean needReview  = ratingBody.asBoolean();

        return new Rating(positive, negative, needReview, profile);
    }

    public static ObjectNode toJson(Rating rating) {
        ObjectNode ratingNode = Json.newObject();

        ratingNode.put("positive",    rating.getPositiveRatings());
        ratingNode.put("negative",    rating.getNegativeRatings());
        ratingNode.put("needReview",  rating.getEditRatings());

        return ratingNode;
    }
}