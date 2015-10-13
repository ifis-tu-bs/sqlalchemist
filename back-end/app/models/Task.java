package models;

import Exception.SQLAlchemistException;

import com.fasterxml.jackson.databind.node.*;


import play.db.ebean.Model;
import play.libs.Json;

import javax.persistence.*;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fabiomazzone
 */
@Entity
@Table(name = "Task")
public class Task extends Model {
    @Id
    private long id;

    @ManyToOne
    private TaskSet taskSet;

    private String  taskName;
    private String  taskText;
    private String  refStatement;
    private int     evaluationstrategy;
    private int     points;
    private int     requiredTerm;

    @ManyToOne
    private Profile     creator;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "task")
    private List<Comment> comments;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "task")
    private List<Rating> ratings;

    private Date created_at;
    private Date updated_at;

    public static final Finder<Long, Task> find = new Finder<>(Long.class, Task.class);

//////////////////////////////////////////////////
//  constructor
//////////////////////////////////////////////////

    /**
     *
     * @param taskText
     * @param refStatement
     * @param evaluationstrategy
     * @param points
     * @param requiredTerm
     * @param creator
     */
    public Task(
            String taskName,
            String taskText,
            String refStatement,
            int evaluationstrategy,
            int points,
            int requiredTerm,
            Profile creator) {

        this.taskName           = taskName;
        this.taskText           = taskText;
        this.refStatement       = refStatement;
        this.evaluationstrategy = evaluationstrategy;
        this.points             = points;
        this.requiredTerm       = requiredTerm;

        this.creator            = creator;

        // Initialize Social Components
        this.comments           = new ArrayList<>();
        this.ratings            = new ArrayList<>();

        this.created_at         = new Date();
        this.updated_at         = new Date();
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
//  json method
//////////////////////////////////////////////////


    public ObjectNode toHomeWorkJsonForProfile(Profile profile) {
        ObjectNode json = Json.newObject();

        json.put("id",          this.id);
        //json.put("exercise",    this.exercise);
        //json.put("done",        SubmittedHomeWorkDAO.getCurrentSubmittedHomeWorkForProfileAndSubTask(profile, this) != null);

        return json;
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

    public String getTaskName() {
        return taskName;
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

    public int getEvaluationstrategy() {
        return evaluationstrategy;
    }

    public void setEvaluationstrategy(int evaluationstrategy) {
        this.evaluationstrategy = evaluationstrategy;
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

    public void setRequiredTerm(int requiredTerm) {
        this.requiredTerm = requiredTerm;
    }

    public Profile getCreator() {
        return creator;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }


//////////////////////////////////////////////////
//  Action
//////////////////////////////////////////////////

  /**
   * This is the methode to add a rating to this entity
   */
  public void addRating(Rating rating) {
      if(this.ratings != null && this.ratings.size() > 0) {
          for(Rating ratingI : this.ratings) {
              if(ratingI.getProfile().getId() == rating.getProfile().getId()) {
                  this.ratings.remove(ratingI);
                  this.update();
                  ratingI.delete();
                  break;
              }
          }
      } else {
          this.ratings = new ArrayList<>();
      }

      this.ratings.add(rating);
      rating.setTask(this);
      rating.save();
      this.update();
  }

    public static final int TASK_SOLVE_STATEMENT_CORRECT = 0;
    public static final int TASK_SOLVE_STATEMENT_ERROR = 1;

    /**
     *
     * @param statement
     * @return
     */
    public boolean solve(String statement) throws SQLAlchemistException {
        return false;
        /*
        Task task = this.taskFile.getTask();
        try {
            task.startTask("local");
        } catch (MySQLAlchemistException e) {
            throw new SQLAlchemistException("Database Exception, try later");
        }
        try {
            boolean status = task.isUserStatementCorrect(statement, this.index);

            task.closeTask();
            Logger.info("status: " + status);
            return status;
        } catch (MySQLAlchemistException e) {
            Logger.warn("SubTask.solve - catches MySQLAlchemistException: " + e.getMyMessage());
            try {
                task.closeTask();
            } catch (MySQLAlchemistException e1) {}
            throw new SQLAlchemistException(e.getMyMessage());
        }*/
    }

    /**
     * this method adds comments to the SubTask
     *
     * @param comment   the comment to be added
     */
    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    public List<Comment> getComments() {
        return this.comments;
    }

    public int getScore() {
        return (this.points * 100) * 50;
    }
}
