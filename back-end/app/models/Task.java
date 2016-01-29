package models;

import com.avaje.ebean.annotation.ConcurrencyMode;
import com.avaje.ebean.annotation.EntityConcurrencyMode;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fabiomazzone
 */
@Entity
@Table(name = "task")
@EntityConcurrencyMode(ConcurrencyMode.NONE)
public class Task extends Model {

    public static final int EVALUATIONSTRATEGY_SET = 1;
    public static final int EVALUATIONSTRATEGY_LIST = 2;

    @Id
    private Long id;

    @ManyToOne
    private TaskSet taskSet;

    private String  taskName;

    @Column(columnDefinition = "Text")
    private String  taskText;

    @Column(columnDefinition = "Text")
    private String  refStatement;
    private int     evaluationStrategy;
    private int     points;
    private int     requiredTerm;

    @ManyToOne
    private User   creator;

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "task")
    private List<Comment>   comments;

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "task")
    private List<Rating>    ratings;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "task")
    private List<SolvedTask> solvedTaskList;

    private boolean available;

    private Date created_at;
    private Date updated_at;

    private int availableSyntaxChecks;
    private int availableSemanticChecks;

    public static final Finder<Long, Task> find = new Finder<>(Task.class);

//////////////////////////////////////////////////
//  constructor
//////////////////////////////////////////////////

    /**
     *
     * @param taskName              the name of this task
     * @param taskText              the description of the task
     * @param refStatement          the reference statement
     * @param evaluationStrategy    the evaluationStrategy
     * @param points                the value of points
     * @param requiredTerm          the required Terms
     * @param creator               the creator
     */
    public Task(
            String taskName,
            String taskText,
            String refStatement,
            int evaluationStrategy,
            int points,
            int requiredTerm,
            User creator,
            int availableSyntaxChecks,
            int availableSemanticChecks) {

        this.taskName           = taskName;
        this.taskText           = taskText;
        this.refStatement       = refStatement;
        this.evaluationStrategy = evaluationStrategy;
        this.points             = points;
        this.requiredTerm       = requiredTerm;

        this.creator            = creator;

        // Initialize Social Components
        this.comments           = new ArrayList<>();
        this.ratings            = new ArrayList<>();

        this.available          = false;

        this.created_at         = new Date();
        this.updated_at         = new Date();

        this.availableSyntaxChecks = availableSyntaxChecks;
        this.availableSemanticChecks = availableSemanticChecks;
    }

    /**
     * this method updated the database entry with this entity
     */
    @Override
    public void update() {
        this.updated_at = new Date();
        super.update();
    }

//////////////////////////////////////////////////
//  Getter & Setter
//////////////////////////////////////////////////

    public long getId() {
        return id;
    }

    @JsonIgnore
    public TaskSet getTaskSet() {
        return taskSet;
    }

    public void setTaskSet(TaskSet taskSet) {
        this.taskSet = taskSet;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskText() {
        return taskText;
    }

    public void setTaskText(String taskText) {
        this.taskText = taskText;
    }

    public String getRefStatement() {
        return refStatement;
    }

    public void setRefStatement(String refStatement) {
        this.refStatement = refStatement;
    }

    public int getEvaluationStrategy() {
        return evaluationStrategy;
    }

    public void setEvaluationStrategy(int evaluationStrategy) {
        this.evaluationStrategy = evaluationStrategy;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getRequiredTerm() {
        return requiredTerm;
    }

    public int getAvailableSyntaxChecks() {
        return availableSyntaxChecks;
    }

    public int getAvailableSemanticChecks() {
        return availableSemanticChecks;
    }

    public void setAvailableSyntaxChecks(int availableSyntaxChecks) {
        this.availableSyntaxChecks = availableSyntaxChecks;
    }

    public void setAvailableSemanticChecks(int availableSemanticChecks) {
        this.availableSemanticChecks = availableSemanticChecks;
    }

    public void setRequiredTerm(int requiredTerm) {
        this.requiredTerm = requiredTerm;
    }

    public User getCreator() {
        return creator;
    }

    @JsonIgnore
    public List<Rating> getRatings() {
        return ratings;
    }

  @JsonProperty("rating")
  public Rating getRating() {
        return Rating.sum(this.getRatings());
    }

  public void addRating(Rating rating) {
    if(this.ratings != null && this.ratings.size() > 0) {
      for(Rating ratingI : this.ratings) {
        if(ratingI.getUser().getId() == rating.getUser().getId()) {
          ratingI.setRating(rating);
          ratingI.update();
          return;
        }
      }
      this.ratings.add(rating);
      rating.setTask(this);
      rating.save();
    } else {
      this.ratings = new ArrayList<>();
      this.ratings.add(rating);
      rating.setTask(this);
      rating.save();
    }
  }

    public List<Comment> getComments() {
        return this.comments;
    }
    public void addComment(Comment comment) {
        if(this.comments == null) {
            this.comments = new ArrayList<>();
        }
        this.comments.add(comment);
        comment.setTask(this);
        comment.save();
    }

    public String getCreated_at() {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        return df.format(this.created_at);
    }

    public String getUpdated_at() {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        return df.format(this.updated_at);
    }

    //////////

    public int getScore() {
        return (this.points * 100) * 50;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
