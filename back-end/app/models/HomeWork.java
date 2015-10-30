package models;

import com.avaje.ebean.Model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @author fabiomazzone
 */
@Entity
@Table(name = "HomeWork")
public class HomeWork extends Model {

    @Id
    private long id;

    @ManyToOne
    private final Profile creator;

    @ManyToMany(cascade = CascadeType.ALL)
    private final List<TaskSet> taskSets;

    private List<SubmittedHomeWork> submittedHomeWorks;

    private final Date start_at;

    private final Date expire_at;

    private final String homeWorkName;

    public static final Model.Finder<Long, HomeWork> find = new Model.Finder<>(Long.class, HomeWork.class);

//////////////////////////////////////////////////
//  Constructor
//////////////////////////////////////////////////

    public HomeWork(
            String homeWorkName,
            Profile creator,
            List<TaskSet> taskFiles,
            Date start_at,
            Date expires_at) {

        this.homeWorkName = homeWorkName;
        this.creator = creator;
        this.taskSets = taskFiles;
        this.start_at = start_at;
        this.expire_at = expires_at;

        this.save();
    }

//////////////////////////////////////////////////
//  Getter / Setter
//////////////////////////////////////////////////


    public List<TaskSet> getTaskSets() {
        return this.taskSets;
    }

    public Profile getCreator() {
        return this.creator;
    }

    public long getId() {
        return this.id;
    }

    public String getHomeWorkName() {
        return this.homeWorkName;
    }

    public Date getExpire_at() {
        return this.expire_at;
    }

    public Date getStart_at() {
        return this.start_at;
    }

//////////////////////////////////////////////////
//  Object Methods
//////////////////////////////////////////////////




    public boolean contains(Task task) {
        for (TaskSet taskSet : this.taskSets) {
            if (taskSet.contains(task))
                return true;
        }
        return false;
    }

    public boolean submittedAll(List<Object> submits) {
        boolean answer = true;
        for (TaskSet taskSet : this.taskSets) {
            answer &= taskSet.submittedAll(submits);
        }
        return answer;
    }

}
