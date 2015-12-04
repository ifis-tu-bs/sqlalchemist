package models;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.ConcurrencyMode;
import com.avaje.ebean.annotation.EntityConcurrencyMode;

import view.TableDefinitionView;

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
@Table(name = "taskset")
@EntityConcurrencyMode(ConcurrencyMode.NONE)
public class TaskSet extends Model {
    @Id
    private Long id;

    private String                    taskSetName;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "taskSet")
    private List<TableDefinition>     tableDefinitions;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "taskSet")
    private List<ForeignKeyRelation>  foreignKeyRelations;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "taskSet")
    private List<Task>                tasks;

    private final boolean             isHomework;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<HomeWork> homeWorks;

    // Social Information's
    @ManyToOne
    private final Profile creator;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "taskSet")
    private List<Rating> ratings;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "taskSet")
    private List<Comment> comments;

    private boolean available;

    private Date createdAt;
    private Date updatedAt;

    public static final Finder<Long, TaskSet> find = new Finder<>(TaskSet.class);

//////////////////////////////////////////////////
//  constructor
//////////////////////////////////////////////////

    /**
     * this is the constructor for an TaskSet object
     *
     * @param taskSetName           the name of the taskSet
     * @param tableDefinitions      a list of table definitions
     * @param foreignKeyRelations   a list of all foreign key relations
     * @param creator               the creator of this object as Profile object
     * @param isHomeWork            a flag
     */
    public TaskSet(
            String                      taskSetName,
            List<TableDefinition>       tableDefinitions,
            List<ForeignKeyRelation>    foreignKeyRelations,
            Profile                     creator,
            boolean                     isHomeWork) {

        this.taskSetName            = taskSetName;
        this.tableDefinitions       = tableDefinitions;
        this.foreignKeyRelations    = foreignKeyRelations;

        this.creator                = creator;
        this.isHomework             = isHomeWork;

        this.available              = false;

        // Initialize Social Components
        this.ratings                = new ArrayList<>();
        this.comments               = new ArrayList<>();

        this.updatedAt              = new Date();
        this.createdAt              = new Date();
    }

    @Override
    public void save() {
        this.prepareStoring();
        super.save();
    }

    private void prepareStoring() {
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

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public boolean contains(Task task) {
        return this.tasks.contains(task);
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

    /**
     * This is the method to add a rating to this entity
     */
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
            rating.setTaskSet(this);
            rating.save();
        }
    }

    public List<Comment> getComments() {
        return comments;
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


    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
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

    public String getRelationsFormatted() {
        String relationsFormatted = "";
        if(this.getTableDefinitions() == null) {
            return "";
        }
        for(int i = 0; i < this.getTableDefinitions().size(); i++)  {
            relationsFormatted = relationsFormatted + TableDefinitionView.toString(this.getTableDefinitions().get(i));
            if(i < this.getTableDefinitions().size() - 1)
                relationsFormatted = relationsFormatted + ",";
        }
        return relationsFormatted;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
