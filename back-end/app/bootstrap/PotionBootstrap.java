package bootstrap;

import models.Potion;

import dao.PotionDAO;

import play.Logger;

public class PotionBootstrap {
  //////////////////////////////////////////////////
  //  Init Method
  //////////////////////////////////////////////////

  public static void init(){
    Logger.info("Initialize 'Potion' data");

    PotionDAO.create("Weak Health Potion",      Potion.HEALTH_POTION, Potion.POWER_LEVEL_1, 1);
    PotionDAO.create("Moderate Health Potion",  Potion.HEALTH_POTION, Potion.POWER_LEVEL_2, 2);
    PotionDAO.create("Great Health Potion",     Potion.HEALTH_POTION, Potion.POWER_LEVEL_3, 3);
    PotionDAO.create("Powerful Health Potion",  Potion.HEALTH_POTION, Potion.POWER_LEVEL_4, 4);
    PotionDAO.create("Insane Health Potion",    Potion.HEALTH_POTION, Potion.POWER_LEVEL_5, 5);

    //Speed * 2
    PotionDAO.create("Weak Speed Potion",       Potion.SPEED_POTION, Potion.POWER_LEVEL_1, 2);
    PotionDAO.create("Moderate Speed Potion",   Potion.SPEED_POTION, Potion.POWER_LEVEL_2, 4);
    PotionDAO.create("Great Speed Potion",      Potion.SPEED_POTION, Potion.POWER_LEVEL_3, 6);
    PotionDAO.create("Powerful Speed Potion",   Potion.SPEED_POTION, Potion.POWER_LEVEL_4, 8);
    PotionDAO.create("Insane Speed Potion",     Potion.SPEED_POTION, Potion.POWER_LEVEL_5, 10);

    //Jump * 3
    PotionDAO.create("Weak Jump Potion",        Potion.JUMP_POTION, Potion.POWER_LEVEL_1, 3);
    PotionDAO.create("Moderate Jump Potion",    Potion.JUMP_POTION, Potion.POWER_LEVEL_2, 6);
    PotionDAO.create("Great Jump Potion",       Potion.JUMP_POTION, Potion.POWER_LEVEL_3, 9);
    PotionDAO.create("Powerful Jump Potion",    Potion.JUMP_POTION, Potion.POWER_LEVEL_4, 12);
    PotionDAO.create("Insane Jump Potion",      Potion.JUMP_POTION, Potion.POWER_LEVEL_5, 15);

    //Def * 2
    PotionDAO.create("Weak Defense Potion",     Potion.DEFENSE_POTION, Potion.POWER_LEVEL_1, 2);
    PotionDAO.create("Moderate Defense Potion", Potion.DEFENSE_POTION, Potion.POWER_LEVEL_2, 4);
    PotionDAO.create("Great Defense Potion",    Potion.DEFENSE_POTION, Potion.POWER_LEVEL_3, 6);
    PotionDAO.create("Powerful Defense Potion", Potion.DEFENSE_POTION, Potion.POWER_LEVEL_4, 8);
    PotionDAO.create("Insane Defense Potion",   Potion.DEFENSE_POTION, Potion.POWER_LEVEL_5, 10);

    Logger.info("Done initializing");
  }
}
