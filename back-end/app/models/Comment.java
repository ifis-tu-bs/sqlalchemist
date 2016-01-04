package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;


/**
 * This model describes the comment.
 * Every comment is related to a subTask, the profile of the user, who created the comment and the content (comment) as a string.
 */
@Entity
@Table(name = "comment")
public class Comment extends Model{

    /** auto-generated ID for this comment. */
    @Id
    private long id;

    @ManyToOne
    private TaskSet taskSet;

    @ManyToOne
    private Task task;

    /** Relation to the profile of the user, who created the comment. */
    @ManyToOne
    private final User creator;

    /** Content of the comment. */
    private final String comment;

    private final Date created_at;

    /**
     * Finder for this class
     */
    public static final Model.Finder<Long, Comment> find = new Finder<>(Comment.class);

//////////////////////////////////////////////////
//  Constructor
//////////////////////////////////////////////////

    public Comment(String comment, User creator) {
        this.creator    = creator;
        this.comment    = comment;

        this.created_at = new Date();
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

    @JsonIgnore
    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    @JsonIgnore
    public User getCreator() {
        return creator;
    }

    public long getCreatorId() {
        return creator.getId();
    }

    public String getCreatorName() {
        return creator.getUsername();
    }

    public String getComment() {
        return comment;
    }

    public Date getCreated_at() {
        return created_at;
    }
}
