package models;

import com.avaje.ebean.annotation.ConcurrencyMode;
import com.avaje.ebean.annotation.EntityConcurrencyMode;

import play.db.ebean.Model;

import java.util.Date;

import javax.persistence.*;


/**
 * @author fabiomazzone
 */
@Entity
@Table(
        name = "solvedSubTask"
)
@EntityConcurrencyMode(ConcurrencyMode.NONE)
public class SolvedTask extends Model {
    @Id
    long id;

    @ManyToOne
    @Column(name = "profile_id")
    Profile profile;

    @ManyToOne
    @Column(name = "sub_task_id")
    Task task;

    int solved;
    int trys;

    Date lastSolved;

    public static final Finder<Long, SolvedTask> find = new Finder<>(Long.class, SolvedTask.class);

    public SolvedTask(
            Profile profile,
            Task task
    ) {
        super();

        this.profile = profile;
        this.task = task;

        this.solved = 0;
        this.trys = 0;
    }

    public Task getTask() {
      return this.task;
    }

    public void pushSolved() {
        this.solved = this.solved +  1;
    }


    public int getTrys() {
      return this.trys;
    }

    public void pushTrys() {
        this.trys = this.trys + 1;
    }

    public void setLastSolved(Date date) {
      this.lastSolved = date;
    }

    public Date getLastSolved() {
      return this.lastSolved;
    }

}
