package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.avaje.ebean.Model;

import javax.persistence.*;
import java.util.List;

/**
 *
 *
 * @author fabiomazzone
 */
@Entity
@Table(name = "rating")
public class Rating extends Model {

    @Id
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private TaskSet taskSet;

    @ManyToOne
    private Task task;

    private long positiveRatings;
    private long negativeRatings;
    private long editRatings;

    public Rating(
        boolean positive,
        boolean negative,
        boolean needReview,
        User user) {

        if(user != null) {
            this.user = user;

            int votes = user.getRole().getVotes();

            this.positiveRatings = positive   ? votes : 0;
            this.negativeRatings = negative   ? votes : 0;
            this.editRatings     = needReview ? votes : 0;
        } else {
            this.positiveRatings = 0;
            this.negativeRatings = 0;
            this.editRatings = 0;
        }
  }

    public Long getId() {
        return id;
    }

    @JsonIgnore
    public User getUser() {
        return user;
    }

    public long getUserId() {
        return  (user != null)? user.getId(): 0;
    }

    public String getUserName() {
        return (user != null)? user.getUsername(): "";
    }

    @JsonIgnore
    public TaskSet getTaskSet() {
        return taskSet;
    }

    public void setTaskSet(TaskSet taskSet) {
        this.taskSet = taskSet;
    }

    @JsonIgnore
    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

  public long getPositiveRatings() {
    return positiveRatings;
  }

  public void setPositiveRatings(long positiveRatings) {
    this.positiveRatings = positiveRatings;
  }

  public long getNegativeRatings() {
    return negativeRatings;
  }

  public void setNegativeRatings(long negativeRatings) {
    this.negativeRatings = negativeRatings;
  }

  public long getEditRatings() {
    return editRatings;
  }

  public void setEditRatings(long editRatings) {
    this.editRatings = editRatings;
  }

  public static Rating sum(List<Rating> ratings) {
    if(ratings == null || ratings.size() == 0) {
      return new Rating(false, false, false, null);
    }
    Rating rating_sum = new Rating(false, false, false, null);

    for(Rating rating : ratings) {
      rating_sum.positiveRatings += rating.positiveRatings;
      rating_sum.negativeRatings += rating.negativeRatings;
      rating_sum.editRatings += rating.editRatings;
    }

    if(rating_sum.getPositiveRatings() - rating_sum.getNegativeRatings() < 0) {
      rating_sum.setNegativeRatings(rating_sum.getNegativeRatings() - rating_sum.getPositiveRatings());
      rating_sum.setPositiveRatings(0);
    } else {
      rating_sum.setPositiveRatings(rating_sum.getPositiveRatings() - rating_sum.getNegativeRatings());
      rating_sum.setNegativeRatings(0);
    }

    return rating_sum;
  }

  public void setRating(Rating rating) {
        this.positiveRatings = rating.positiveRatings;
        this.editRatings = rating.editRatings;
        this.negativeRatings = rating.negativeRatings;
    }
}
