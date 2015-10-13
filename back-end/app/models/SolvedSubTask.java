package models;

import com.avaje.ebean.annotation.ConcurrencyMode;
import com.avaje.ebean.annotation.EntityConcurrencyMode;
import play.Logger;
import play.db.ebean.Model;

import java.util.Date;
import java.util.List;

import javax.persistence.*;


/**
 * @author fabiomazzone
 */
@Entity
@Table(
        name = "solvedSubTask"
)
@EntityConcurrencyMode(ConcurrencyMode.NONE)
public class SolvedSubTask extends Model {
    @Id
    long id;

    @ManyToOne
    @Column(name = "profile_id")
    Profile profile;

    @ManyToOne
    @Column(name = "sub_task_id")
    SubTask subTask;

    int solved;
    int trys;

    Date lastSolved;

    public static final Finder<Long, SolvedSubTask> find = new Finder<>(Long.class, SolvedSubTask.class);

    public SolvedSubTask(
            Profile profile,
            SubTask subTask
    ) {
        super();

        this.profile = profile;
        this.subTask = subTask;

        this.solved = 0;
        this.trys = 0;
    }

    public SubTask getSubTask() {
      return this.subTask;
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
