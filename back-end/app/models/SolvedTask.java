package models;

import com.avaje.ebean.annotation.ConcurrencyMode;
import com.avaje.ebean.annotation.EntityConcurrencyMode;

import com.avaje.ebean.Model;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.*;


/**
 * @author fabiomazzone
 */
@Entity
@Table(name = "solvedtask")
@EntityConcurrencyMode(ConcurrencyMode.NONE)
public class SolvedTask extends Model {
    @Id
    private long        id;

    @ManyToOne
    private User        user;

    @ManyToOne
    private final Task  task;

    private boolean     solved;
    private int         timeNeeded;
    private Calendar    timestamp;

    public SolvedTask(
            User user,
            Task task,
            boolean solved,
            int     timeNeeded) {
        super();

        this.user = user;
        this.task = task;

        this.solved = solved;
        this.timeNeeded = timeNeeded;

        this.timestamp = Calendar.getInstance();
    }

  public long getId() {
    return id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Task getTask() {
    return task;
  }

  public boolean isSolved() {
    return solved;
  }

  public void setSolved(boolean solved) {
    this.solved = solved;
  }

  public int getTimeNeeded() {
    return timeNeeded;
  }

  public void setTimeNeeded(int timeNeeded) {
    this.timeNeeded = timeNeeded;
  }

  public Calendar getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Calendar timestamp) {
    this.timestamp = timestamp;
  }
}
