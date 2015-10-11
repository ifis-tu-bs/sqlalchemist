package models;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.db.ebean.Model;
import play.libs.Json;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * this class objects matches to the task files on disk drive
 *
 * @author fabiomazzone
 */
@Entity
@Table(name = "TaskSet")
public class TaskSet extends Model {
    @Id
    private
    long id;

    @OneToMany(cascade = CascadeType.ALL)
    private
    List<TableDefinition>  tableDefinitions;
    private String           relationsFormatted;
    @OneToMany(cascade = CascadeType.ALL)
    private
    List<Task>       tasks;
    private boolean          isHomework;

    // Social Information's
    //@ManyToMany
    private Profile creator;
    @OneToMany(cascade = CascadeType.ALL)
    private
    List<Rating> ratings;
    @OneToMany(cascade = CascadeType.ALL)
    private
    List<Comment> comments;

    private Date created_at;
    private Date updated_at;

    public static final Finder<Long, TaskSet> find = new Finder<>(Long.class, TaskSet.class);

//////////////////////////////////////////////////
//  constructor
//////////////////////////////////////////////////

    /**
     * this is the constructor for an TaskSet object
     *
     * @param tableDefinitions
     * @param tasks             a list of all tasks that should be in this set
     * @param creator           the creator of this object as Profile object
     * @param isHomeWork        a flag
     */
    private TaskSet(
            List<TableDefinition> tableDefinitions,
            List<Task> tasks,
            Profile creator,
            boolean isHomeWork) {
        super();

        this.tableDefinitions   = tableDefinitions;
        this.tasks              = tasks;

        this.creator            = creator;
        this.isHomework         = isHomeWork;

        // Initialize Social Components
        this.ratings            = new ArrayList<>();
        this.comments = new ArrayList<>();

        this.updated_at         = new Date();
        this.created_at         = new Date();
    }

    /**
     * this method updated the database entry with this entity
     */
    @Override
    public void update() {
        this.updated_at = new Date();
        super.update();
    }

    /**
     * this method deletes the database entry of this entity
     */
    @Override
    public void delete() {
        List<Task> tasks = this.tasks;
        if(tasks  != null && tasks.size() > 0) {
            for(Task task: tasks) {
                task.delete();
            }
        }
        super.delete();
    }

//////////////////////////////////////////////////
//  getter & setter methods
//////////////////////////////////////////////////

    public long getId() {
        return this.id;
    }

    public List<TableDefinition> getTableDefinitions() {
        return tableDefinitions;
    }

    public String getRelationsFormatted() {
        return relationsFormatted;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public boolean isHomework() {
        return isHomework;
    }

    public Profile getCreator() {
        return creator;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }


    public boolean contains(Task task) {
        return this.tasks.contains(task);
    }
//////////////////////////////////////////////////
//  json method
//////////////////////////////////////////////////

    public ObjectNode toHomeWorkJsonForProfile(Profile profile) {
        ObjectNode  objectNode = Json.newObject();

        ArrayNode subTasks   = JsonNodeFactory.instance.arrayNode();

        for(Task task : this.tasks) {
            subTasks.add(task.toHomeWorkJsonForProfile(profile));
        }

        objectNode.put("schema",        this.relationsFormatted);
        objectNode.put("subTasks",      subTasks);

        return objectNode;
    }

//////////////////////////////////////////////////
//  action
//////////////////////////////////////////////////

    /**
     * add subtasks of a List to own subtask list
     * @param tasks   a list filled with subtasks
     */
    public void addTasks(List<Task> tasks)
    {
        for(Task task : tasks) {
            this.tasks.add(task);
        }
    }

    /**
     * This is the methode to add a rating to this entity
     */
    public void addRating(Rating rating) {
      if(this.ratings != null && this.ratings.size() > 0) {
        for(Rating ratingI : this.ratings) {
          if(ratingI.getProfile().getId() == rating.getProfile().getId()) {
            ratingI.delete();
            break;
          }
        }
      } else {
        this.ratings = new ArrayList<>();
      }

      this.ratings.add(rating);
    }

    /**
     * this method adds comments to the TaskFile
     *
     * @param comment   the comment to be added
     */
    public void addComment(Comment comment)
    {
        this.comments.add(comment);
    }
//////////////////////////////////////////////////
//  create methods
//////////////////////////////////////////////////

    public boolean submittedAll(List<Object> submits) {
        boolean answer = true;
        for (Task task : this.tasks) {
            answer &= submits.contains(task.getId());
        }
        return answer;
    }


}
