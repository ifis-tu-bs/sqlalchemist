package models;

import com.fasterxml.jackson.databind.node.ObjectNode;

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

    @ManyToOne
    Profile profile;

    long positiveRatings;
    long negativeRatings;
    long editRatings;

  public Rating(
      boolean positive,
      boolean negative,
      boolean needReview,
      Profile profile) {

    if(profile != null) {
      this.profile = profile;

      int votes = profile.getUser().getRole() == User.ROLE_ADMIN ? Play.application().configuration().getInt("Rating.Admin.votes") : 1;

      this.positiveRatings = positive   ? votes : 0;
      this.negativeRatings = negative   ? votes : 0;
      this.editRatings     = needReview ? votes : 0;
    } else {
      this.positiveRatings = 0;
      this.negativeRatings = 0;
      this.editRatings = 0;
    }
  }

    /**
     * getter for profile
     * @return returns the profile
     */
    public Profile getProfile() {
        return this.profile;
    }

    public long getPositiveRatings() {
        return positiveRatings;
    }

    public long getNegativeRatings() {
        return negativeRatings;
    }

    public long getEditRatings() {
        return editRatings;
    }

    public static Rating sum(List<Rating> ratings) {
    if(ratings == null || ratings.size() == 0) {
      return new Rating(false, false, false, null);
    }
    Rating rating_sum = new Rating(false, false, false, null);

    for(Rating rating : ratings) {
      rating_sum.positiveRatings    += rating.positiveRatings;
      rating_sum.negativeRatings    += rating.negativeRatings;
      rating_sum.editRatings        += rating.editRatings;
    }
    return rating_sum;
  }
}
