package models;

import com.fasterxml.jackson.databind.node.ObjectNode;

import play.Logger;
import play.Play;
import play.db.ebean.Model;
import play.libs.Json;

import javax.persistence.*;
import java.util.List;

/**
 * @author fabiomazzone
 */
@Entity
@Table
public class Rating extends Model {

    @Id
    Long id;

    @OneToOne
    Profile profile;

    long positiveRatings;
    long negativeRatings;
    long editRatings;

  private Rating() {
    this(null, false, false, false);
  }

  public Rating(
      Profile profile,
      boolean positive,
      boolean negative,
      boolean needReview) {

    if(profile != null) {
      this.profile = profile;

      int votes = profile.getUser().getRole() == User.ROLE_ADMIN ? Play.application().configuration().getInt("Rating.Admin.votes") : 1;

      this.positiveRatings = positive   ? votes : 0;
      this.negativeRatings = negative   ? votes : 0;
      this.editRatings     = needReview ? votes : 0;
    }
  }

    /**
     * getter for profile
     * @return returns the profile
     */
    public Profile getProfile() {
        return this.profile;
    }

    public ObjectNode toJson() {
        ObjectNode node = Json.newObject();

        node.put("positive",    this.positiveRatings);
        node.put("negative",    this.negativeRatings);
        node.put("needReview",  this.editRatings);

        return node;
    }

  public static Rating sum(List<Rating> ratings) {
    if(ratings == null || ratings.size() == 0) {
      return null;
    }
    Rating rating_sum = new Rating();

    for(Rating rating : ratings) {
      rating_sum.positiveRatings    += rating.positiveRatings;
      rating_sum.negativeRatings    += rating.negativeRatings;
      rating_sum.editRatings        += rating.editRatings;
    }
    return rating_sum;
  }
}
