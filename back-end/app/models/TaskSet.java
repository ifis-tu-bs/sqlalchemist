package models;

import com.avaje.ebean.annotation.ConcurrencyMode;
import com.avaje.ebean.annotation.EntityConcurrencyMode;
import view.TableDefinitionView;

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
 * this entity holds the schema and a set of tasks thats operate on it
 *
 * @author fabiomazzone
 */
@Entity
@Table(name = "TaskSet")
@EntityConcurrencyMode(ConcurrencyMode.NONE)
public class TaskSet extends Model {
    @Id
    private long id;

    private String                  taskSetName;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "taskSet")
    private List<TableDefinition>   tableDefinitions;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "taskSet")
    private List<ForeignKeyRelation>foreignKeyRelations;
    private String                  relationsFormatted;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "taskSet")
    private List<Task>              tasks;
    private final boolean                 isHomework;

    // Social Information's
    @ManyToOne
    private final Profile creator;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "taskSet")
    private List<Rating> ratings;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "taskSet")
    private List<Comment> comments;

    private final Date createdAt;
    private Date updatedAt;

    public static final Finder<Long, TaskSet> find = new Finder<>(Long.class, TaskSet.class);

//////////////////////////////////////////////////
//  constructor
//////////////////////////////////////////////////

    /**
     * this is the constructor for an TaskSet object
     *
     * @param tableDefinitions      a list of table definitions
     * @param foreignKeyRelations   a list of all foreign key relations
     * @param tasks                 a list of all tasks that should be in this set
     * @param creator               the creator of this object as Profile object
     * @param isHomeWork            a flag
     */
    public TaskSet(
            String                      taskSetName,
            List<TableDefinition>       tableDefinitions,
            List<ForeignKeyRelation>    foreignKeyRelations,
            List<Task>                  tasks,
            Profile                     creator,
            boolean                     isHomeWork) {

        this.taskSetName            = taskSetName;
        this.tableDefinitions       = tableDefinitions;
        this.foreignKeyRelations    = foreignKeyRelations;
        this.tasks                  = tasks;

        this.creator                = creator;
        this.isHomework             = isHomeWork;

        // Initialize Social Components
        this.ratings                = new ArrayList<>();
        this.comments               = new ArrayList<>();

        this.updatedAt = new Date();
        this.createdAt = new Date();
    }

    @Override
    public void save() {
        this.prepareStoring();
        super.save();
    }

    public void prepareStoring() {
        if(this.foreignKeyRelations != null) {
            for(ForeignKeyRelation foreignKeyRelation : this.foreignKeyRelations) {
                TableDefinition sourceTable = null, destinationTable = null;
                for(TableDefinition tableDefinition : this.tableDefinitions) {
                    if(tableDefinition.getTableName().equals(foreignKeyRelation.getSourceTable())) {
                        sourceTable = tableDefinition;
                        continue;
                    }
                    if(tableDefinition.getTableName().equals(foreignKeyRelation.getDestinationTable())) {
                        destinationTable = tableDefinition;
                    }
                }
                if(sourceTable != null && destinationTable != null) {
                    ColumnDefinition sourceColumn = null, destinationColumn = null;
                    for(ColumnDefinition columnDefinition : sourceTable.getColumnDefinitions()) {
                        if(columnDefinition.getColumnName().equals(foreignKeyRelation.getSourceColumn())) {
                            sourceColumn = columnDefinition;
                            break;
                        }
                    }
                    for(ColumnDefinition columnDefinition : destinationTable.getColumnDefinitions()) {
                        if(columnDefinition.getColumnName().equals(foreignKeyRelation.getDestinationColumn())) {
                            destinationColumn = columnDefinition;
                            break;
                        }
                    }
                    if(sourceColumn != null && destinationColumn != null) {
                        sourceColumn.setForeignKey(destinationColumn);
                    }
                }
            }
        }

        String relationsFormatted = "";
        for(int i = 0; i < this.tableDefinitions.size(); i++)  {
            relationsFormatted = relationsFormatted + TableDefinitionView.toString(this.tableDefinitions.get(i));
            if(i < this.tableDefinitions.size() - 1)
                relationsFormatted = relationsFormatted + ",";
        }
        this.relationsFormatted = relationsFormatted;
        this.updatedAt = new Date();
    }

//////////////////////////////////////////////////
//  getter & setter methods
//////////////////////////////////////////////////

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTaskSetName() {
        return taskSetName;
    }

    public void setTaskSetName(String taskSetName) {
        this.taskSetName = taskSetName;
    }

    public List<TableDefinition> getTableDefinitions() {
        return tableDefinitions;
    }

    public void setTableDefinitions(List<TableDefinition> tableDefinitions) {
        this.tableDefinitions = tableDefinitions;
    }

    public List<ForeignKeyRelation> getForeignKeyRelations() {
        return foreignKeyRelations;
    }

    public void setForeignKeyRelations(List<ForeignKeyRelation> foreignKeyRelations) {
        this.foreignKeyRelations = foreignKeyRelations;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
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
     * This is the method to add a rating to this entity
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
        rating.setTaskSet(this);
        rating.save();
    }

    /**
     * this method adds comments to the TaskFile
     *
     * @param comment   the comment to be added
     */
    public void addComment(Comment comment)  {
        if(this.comments == null) {
            this.comments = new ArrayList<>();
        }
        this.comments.add(comment);
        comment.setTaskSet(this);
        comment.save();
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
