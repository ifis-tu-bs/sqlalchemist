package models;

import com.avaje.ebeaninternal.server.deploy.generatedproperty.GeneratedInsertTimestamp;
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

    public ObjectNode toJson() {
        ObjectNode node = Json.newObject();

        node.put("positive",    this.positiveRatings);
        node.put("negative",    this.negativeRatings);
        node.put("needReview",  this.editRatings);

        return node;
    }

    public void clear() {
        this.positiveRatings = 0;
        this.negativeRatings = 0;
        this.editRatings = 0;
    }


    public void add(List<Rating> ratings) {
        if(ratings == null || ratings.size() == 0) {
            return;
        }
        for(Rating rating : ratings) {
            this.positiveRatings    += rating.positiveRatings;
            this.negativeRatings    += rating.negativeRatings;
            this.editRatings        += rating.editRatings;
        }
    }

    /**
     * getter for profile
     * @return returns the profile
     */
    public Profile getProfile() {
        return this.profile;
    }

    public boolean rate(boolean positive, boolean needReview, boolean negative) {
        int votes = profile.getUser().getRole() == User.ROLE_ADMIN ? Play.application().configuration().getInt("Rating.Admin.votes") : 1;

        clear();

        if(positive) {
            this.positiveRatings += votes;
            return true;
        } else if (needReview) {
            this.editRatings += votes;
            return true;
        } else if (negative){
            this.negativeRatings += votes;
            return true;
        }

        return false;
    }

    public static Rating create(Profile profile, boolean positive, boolean needReview, boolean negative) {
        Rating rating = new Rating();

        rating.profile = profile;
        rating.rate(positive, needReview, negative);

        try {
            rating.save();

            return rating;
        } catch (PersistenceException e) {
            Logger.warn("Rating.create - catches an PersistenceException: " + e.getMessage());
            return null;
        }
    }
}
