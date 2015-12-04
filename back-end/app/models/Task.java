package models;

import com.avaje.ebean.annotation.ConcurrencyMode;
import com.avaje.ebean.annotation.EntityConcurrencyMode;

import com.avaje.ebean.Model;

import javax.persistence.*;
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
    private String  taskText;
    private String  refStatement;
    private int     evaluationStrategy;
    private int     points;
    private int     requiredTerm;

    @ManyToOne
    private final Profile   creator;
    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "task")
    private List<Comment>   comments;
    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "task")
    private List<Rating>    ratings;

    private boolean available;

    private final Date created_at;
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
            Profile creator,
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

    public Profile getCreator() {
        return creator;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void addRating(Rating rating) {
        if(this.ratings != null && this.ratings.size() > 0) {
            for(Rating ratingI : this.ratings) {
                if(ratingI.getProfile().getId() == rating.getProfile().getId()) {
                    ratingI.setRating(rating);
                    ratingI.update();
                    break;
                }
            }
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

    public Date getCreated_at() {
        return created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    //////////

    public int getScore() {
        return (this.points * 100) * 50;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
