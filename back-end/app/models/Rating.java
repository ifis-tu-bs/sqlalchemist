package models;

import play.Play;
import com.avaje.ebean.Model;

import javax.persistence.*;
import java.util.List;

/**
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

    public User getUser() {
        return user;
    }

    public TaskSet getTaskSet() {
        return taskSet;
    }

    public void setTaskSet(TaskSet taskSet) {
        this.taskSet = taskSet;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
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
            rating_sum.positiveRatings += rating.positiveRatings;
            rating_sum.negativeRatings += rating.negativeRatings;
            rating_sum.editRatings += rating.editRatings;
        }

        return rating_sum;
    }

    public void setRating(Rating rating) {
        this.positiveRatings = rating.positiveRatings;
        this.editRatings = rating.editRatings;
        this.negativeRatings = rating.negativeRatings;
    }
}
