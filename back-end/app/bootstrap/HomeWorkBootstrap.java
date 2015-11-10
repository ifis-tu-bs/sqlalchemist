package bootstrap;


import dao.*;
import models.HomeWork;
import play.Logger;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Invisible
 */
class HomeWorkBootstrap {

    public static void init () {
        if(!play.api.Play.isProd(play.api.Play.current())) {
        Logger.info("Initialize 'HomeWork' data");

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);

        Date tempDate = calendar.getTime();

        HomeWorkDAO.create("DefaultHomeWork 1", ProfileDAO.getByUsername("sqlalchemist"), TaskSetDAO.getAll(), new Date(), tempDate);

        SubmittedHomeWorkDAO.submit(ProfileDAO.getByUsername("sqlalchemist"), TaskDAO.getAll().get(0), HomeWorkDAO.getAll().get(0), true, "Muhahaha");

          Logger.info("Done initializing");
        }
    }
}
