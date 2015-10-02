package dao;

import models.Profile;
import models.Rating;

import play.Logger;

import javax.persistence.PersistenceException;

public class RatingDAO {

  /**
   *  This function creates a new Rating object in the database.
   */
  public static Rating create(Profile profile, boolean positive, boolean needReview, boolean negative) {
      Rating rating = new Rating(profile, positive, negative, needReview);

      try {
          rating.save();

          return rating;
      } catch (PersistenceException e) {
          Logger.warn("Rating.create - catches an PersistenceException: " + e.getMessage());
          return null;
      }
  }
}
