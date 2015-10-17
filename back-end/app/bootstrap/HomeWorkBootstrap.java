package bootstrap;


import dao.*;
import models.TaskSet;
import play.Logger;

import java.util.Date;

/**
 * Created by Invisible on 16.10.2015.
 */
public class HomeWorkBootstrap {

    public static void init () {
        Logger.info("Initialize 'HomeWork' data");

        Date tempDate = new Date();
        tempDate.setHours(23);
        tempDate.setMinutes(59);
        tempDate.setSeconds(59);
        HomeWorkDAO.create("DefaultHomeWork 1", ProfileDAO.getByUsername("admin"), TaskSetDAO.getAll(), new Date(), tempDate);

        SubmittedHomeWorkDAO.submit(ProfileDAO.getByUsername("admin"), TaskDAO.getAll().get(0),true, "Muhahaha");
        SubmittedHomeWorkDAO.submit(ProfileDAO.getByUsername("test2"), TaskDAO.getAll().get(0),true, "Muhahaha2");


        Logger.info("Done initializing");
    }
}
