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



    Date last_solved;

    static final Finder<Long, SolvedSubTask> find = new Finder<>(Long.class, SolvedSubTask.class);

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

    public static void update(
            Profile profile,
            SubTask subTask,
            boolean solved
    ) {
        SolvedSubTask solvedSubTask = SolvedSubTask.getByProfileAndSubTask(profile, subTask);
        if (solvedSubTask == null) {
            solvedSubTask = SolvedSubTask.create(profile, subTask);
        }

        solvedSubTask.pushTrys();

        if(solved) {
            solvedSubTask.pushSolved();
            solvedSubTask.last_solved = new Date();
        }
        solvedSubTask.update();
    }

    private void pushTrys() {
        this.trys = this.trys + 1;
    }

    private void pushSolved() {
        this.solved = this.solved +  1;
    }

    private static SolvedSubTask create(Profile profile, SubTask subTask) {
        SolvedSubTask solvedSubTask = new SolvedSubTask(profile, subTask);

        solvedSubTask.save();

        return solvedSubTask;
    }

    private static SolvedSubTask getByProfileAndSubTask(Profile profile, SubTask subTask) {
        if(profile == null || subTask == null) {
            Logger.warn("SolvedSubTask.getByProfileAndSubTask - profile or subTask is null");
            return null;
        }

        try {
            SolvedSubTask solvedSubTask = find.where().eq("profile", profile).eq("subTask", subTask).findUnique();


            if(solvedSubTask == null) {
                Logger.warn("SolvedSubTask.getByProfileAndSubTask - Can't find existing solvedSubTask object!");
                return null;
            }

            return solvedSubTask;
        } catch (NullPointerException e) {
            Logger.warn("SolvedSubTask.getByProfileAndSubTask - Can't find existing solvedSubTask object!");
            return null;
        }
    }

    public static List<SolvedSubTask> getAllDoneSubTask(Profile profile) {
        try {
            float mittelwert_try = 0;
            int i = 0;

            List<SolvedSubTask> solvedSubTaskList = find.where().eq("profile", profile).findList();

            for(SolvedSubTask solvedSubTask : solvedSubTaskList) {
                if(i == 0) {
                    mittelwert_try = solvedSubTask.trys;
                } else {
                    mittelwert_try = (mittelwert_try + solvedSubTask.trys) / 2;
                }

                i++;
            }

            List<SolvedSubTask> solvedSubTasks = find.where().eq("profile", profile).ge("trys", mittelwert_try).findList();
            if(solvedSubTasks == null || solvedSubTaskList.size() == 0) {
                Logger.warn("SolvedSubTask.getAllDoneSubTask - no SubTaskCandidates found");
                return null;
            }

            return solvedSubTasks;
        } catch (NullPointerException e) {
            Logger.warn("SolvedSubTask.getAllDoneSubTask - no SubTaskCandidates found");
            return null;
        }
    }


    public SubTask getSubTask() {
        return this.subTask;
    }
}
