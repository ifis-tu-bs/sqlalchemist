package bootstrap;


import dao.*;
import models.TaskSet;
import play.Logger;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Invisible
 */
class HomeWorkBootstrap {

    public static void init () {
        if(!play.api.Play.isProd(play.api.Play.current())) {
            if(HomeWorkDAO.getAll() == null || HomeWorkDAO.getAll().size() == 0) {
                Logger.info("Initialize 'HomeWork' data");

                Calendar calendar = Calendar.getInstance();

                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                calendar.set(Calendar.SECOND, 59);

                Date tempDate = calendar.getTime();

                List<TaskSet> taskSetList = TaskSetDAO.getAllHomeWorkTaskSets();

                HomeWorkDAO.create("DefaultHomeWork 1", UserDAO.getByUsername("sqlalchemist"), taskSetList, new Date(), tempDate);

                //SubmittedHomeWorkDAO.submit(ProfileDAO.getByUsername("sqlalchemist"), TaskDAO.getAll().get(0), HomeWorkDAO.getAll().get(0), true, "Muhahaha");
                //SubmittedHomeWorkDAO.submit(ProfileDAO.getByUsername("test2"), TaskDAO.getAll().get(0), HomeWorkDAO.getAll().get(0), true, "Muhahaha2");

                Logger.info("Done initializing");
            }
        }
    }
}
