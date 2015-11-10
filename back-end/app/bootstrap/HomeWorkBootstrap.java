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

        HomeWorkDAO.create("DefaultHomeWork 1", ProfileDAO.getByUsername("admin"), TaskSetDAO.getAll(), new Date(), tempDate);

        SubmittedHomeWorkDAO.submit(ProfileDAO.getByUsername("admin"), TaskDAO.getAll().get(0), HomeWorkDAO.getAll().get(0), true, "Muhahaha");
        SubmittedHomeWorkDAO.submit(ProfileDAO.getByUsername("test2"), TaskDAO.getAll().get(0), HomeWorkDAO.getAll().get(0), true, "Muhahaha2");


          Logger.info("Done initializing");
        }
    }
}
