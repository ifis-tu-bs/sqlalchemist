package bootstrap;

import dao.PotionDAO;
import dao.ScrollDAO;

import models.Potion;
import models.Scroll;

import play.Logger;

class ScrollBootstrap {
//////////////////////////////////////////////////
//  Init Method
//////////////////////////////////////////////////
    /**
     * Method to initialize the scroll data
     */
    public static void init() {
        Logger.info("Initializing 'Recipes' data");
        Potion potion;

        //Health + 1
        potion = PotionDAO.create("Weak Health Potion",      Potion.HEALTH_POTION, Potion.POWER_LEVEL_1, 1);
        ScrollDAO.create(0, "Recipe for the Weak Health Potion", Scroll.TYPE_RECIPE, potion);

        potion = PotionDAO.create("Moderate Health Potion",  Potion.HEALTH_POTION, Potion.POWER_LEVEL_2, 2);
        ScrollDAO.create(1, "Recipe for the Moderate Health Potion", Scroll.TYPE_RECIPE, potion);

        potion = PotionDAO.create("Great Health Potion",     Potion.HEALTH_POTION, Potion.POWER_LEVEL_3, 3);
        ScrollDAO.create(2, "Recipe for the Great Health Potion", Scroll.TYPE_RECIPE, potion);

        potion = PotionDAO.create("Powerful Health Potion",  Potion.HEALTH_POTION, Potion.POWER_LEVEL_4, 4);
        ScrollDAO.create(3, "Recipe for the Powerful Health Potion", Scroll.TYPE_RECIPE, potion);

        potion = PotionDAO.create("Insane Health Potion",    Potion.HEALTH_POTION, Potion.POWER_LEVEL_5, 5);
        ScrollDAO.create(4, "Recipe for the Insane Health Potion", Scroll.TYPE_RECIPE, potion);

        //Speed * 2
        potion = PotionDAO.create("Weak Speed Potion",       Potion.SPEED_POTION, Potion.POWER_LEVEL_1, 2);
        ScrollDAO.create(5, "Recipe for the Weak Speed Potion", Scroll.TYPE_RECIPE, potion);

        potion = PotionDAO.create("Moderate Speed Potion",   Potion.SPEED_POTION, Potion.POWER_LEVEL_2, 4);
        ScrollDAO.create(6, "Recipe for the Moderate Speed Potion", Scroll.TYPE_RECIPE, potion);

        potion = PotionDAO.create("Great Speed Potion",      Potion.SPEED_POTION, Potion.POWER_LEVEL_3, 6);
        ScrollDAO.create(7, "Recipe for the Great Speed Potion", Scroll.TYPE_RECIPE, potion);

        potion = PotionDAO.create("Powerful Speed Potion",   Potion.SPEED_POTION, Potion.POWER_LEVEL_4, 8);
        ScrollDAO.create(8 ,"Recipe for the Powerful Speed Potion", Scroll.TYPE_RECIPE, potion);

        potion = PotionDAO.create("Insane Speed Potion",     Potion.SPEED_POTION, Potion.POWER_LEVEL_5, 10);
        ScrollDAO.create(9, "Recipe for the Insane Speed Potion", Scroll.TYPE_RECIPE, potion);


        //Jump * 3
        potion = PotionDAO.create("Weak Jump Potion",        Potion.JUMP_POTION, Potion.POWER_LEVEL_1, 3);
        ScrollDAO.create(10, "Recipe for the Weak Jump Potion", Scroll.TYPE_RECIPE, potion);

        potion = PotionDAO.create("Moderate Jump Potion",    Potion.JUMP_POTION, Potion.POWER_LEVEL_2, 6);
        ScrollDAO.create(11, "Recipe for the Moderate Jump Potion", Scroll.TYPE_RECIPE, potion);

        potion = PotionDAO.create("Great Jump Potion",       Potion.JUMP_POTION, Potion.POWER_LEVEL_3, 9);
        ScrollDAO.create(12, "Recipe for the Great Jump Potion", Scroll.TYPE_RECIPE, potion);

        potion = PotionDAO.create("Powerful Jump Potion",    Potion.JUMP_POTION, Potion.POWER_LEVEL_4, 12);
        ScrollDAO.create(13, "Recipe for the Powerful Jump Potion", Scroll.TYPE_RECIPE, potion);

        potion = PotionDAO.create("Insane Jump Potion",      Potion.JUMP_POTION, Potion.POWER_LEVEL_5, 15);
        ScrollDAO.create(14, "Recipe for the Insane Jump Potion", Scroll.TYPE_RECIPE, potion);


        //Def * 2
        potion = PotionDAO.create("Weak Defense Potion",     Potion.DEFENSE_POTION, Potion.POWER_LEVEL_1, 2);
        ScrollDAO.create(15, "Recipe for the Weak Defense Potion", Scroll.TYPE_RECIPE, potion);

        potion = PotionDAO.create("Moderate Defense Potion", Potion.DEFENSE_POTION, Potion.POWER_LEVEL_2, 4);
        ScrollDAO.create(16, "Recipe for the Moderate Defense Potion", Scroll.TYPE_RECIPE, potion);

        potion = PotionDAO.create("Great Defense Potion",    Potion.DEFENSE_POTION, Potion.POWER_LEVEL_3, 6);
        ScrollDAO.create(17, "Recipe for the Great Defense Potion", Scroll.TYPE_RECIPE, potion);

        potion = PotionDAO.create("Powerful Defense Potion", Potion.DEFENSE_POTION, Potion.POWER_LEVEL_4, 8);
        ScrollDAO.create(18, "Recipe for the Powerful Defense Potion", Scroll.TYPE_RECIPE, potion);

        potion = PotionDAO.create("Insane Defense Potion",   Potion.DEFENSE_POTION, Potion.POWER_LEVEL_5, 10);
        ScrollDAO.create(19, "Recipe for the Insane Defense Potion", Scroll.TYPE_RECIPE, potion);

        
        Logger.info("Initializing 'Enchantments' data");

        ScrollDAO.create(20, "Endurance of a hog",                      Scroll.TYPE_SCROLL_HEALTH, null);
        ScrollDAO.create(21, "Boyce Codd training",                     Scroll.TYPE_SCROLL_HEALTH, null);
        ScrollDAO.create(22, "Iron will",                               Scroll.TYPE_SCROLL_HEALTH, null);
        ScrollDAO.create(23, "Increased strength",                      Scroll.TYPE_SCROLL_HEALTH, null);
        ScrollDAO.create(24, "Heart of a lion",                         Scroll.TYPE_SCROLL_HEALTH, null);
        ScrollDAO.create(25, "Healing factor",                          Scroll.TYPE_SCROLL_HEALTH, null);
        ScrollDAO.create(26, "Enpowered spirit",                        Scroll.TYPE_SCROLL_HEALTH, null);
        ScrollDAO.create(27, "Stamina of a bull",                       Scroll.TYPE_SCROLL_HEALTH, null);
        ScrollDAO.create(28, "A sip of unicornblood",                   Scroll.TYPE_SCROLL_HEALTH, null);
        ScrollDAO.create(29, "Blood of a giant",                        Scroll.TYPE_SCROLL_HEALTH, null);

        ScrollDAO.create(30, "Tracking boots",                          Scroll.TYPE_SCROLL_SPEED, null);
        ScrollDAO.create(31, "Light armor",                             Scroll.TYPE_SCROLL_SPEED, null);
        ScrollDAO.create(32, "A hot pot of coffee",                     Scroll.TYPE_SCROLL_SPEED, null);
        ScrollDAO.create(33, "Squirrel entchantment",                   Scroll.TYPE_SCROLL_SPEED, null);
        ScrollDAO.create(34, "Painting yellow arrows on the ground",    Scroll.TYPE_SCROLL_SPEED, null);
        ScrollDAO.create(35, "Another pointy hat",                      Scroll.TYPE_SCROLL_SPEED, null);
        ScrollDAO.create(36, "Rocket boots",                            Scroll.TYPE_SCROLL_SPEED, null);
        ScrollDAO.create(37, "Paint a lightning on your chest",         Scroll.TYPE_SCROLL_SPEED, null);
        ScrollDAO.create(38, "Chaos Diamond",                           Scroll.TYPE_SCROLL_SPEED, null);
        ScrollDAO.create(39, "Speed, yes the drug.",                    Scroll.TYPE_SCROLL_SPEED, null);

        ScrollDAO.create(40, "Jumping springs",                         Scroll.TYPE_SCROLL_JUMP, null);
        ScrollDAO.create(41, "The Golden Snitch",                       Scroll.TYPE_SCROLL_JUMP, null);
        ScrollDAO.create(42, "Levitation enchantment",                  Scroll.TYPE_SCROLL_JUMP, null);
        ScrollDAO.create(43, "Flying umbrella",                         Scroll.TYPE_SCROLL_JUMP, null);
        ScrollDAO.create(44, "The good old swish and flick",            Scroll.TYPE_SCROLL_JUMP, null);
        ScrollDAO.create(45, "Weed to get high",                        Scroll.TYPE_SCROLL_JUMP, null);
        ScrollDAO.create(46, "A pointy hat for great aerodynamics",     Scroll.TYPE_SCROLL_JUMP, null);
        ScrollDAO.create(47, "The broom entchantment for humans",       Scroll.TYPE_SCROLL_JUMP, null);
        ScrollDAO.create(48, "Winged boots",                            Scroll.TYPE_SCROLL_JUMP, null);
        ScrollDAO.create(49, "Ravenclaws diadem",                       Scroll.TYPE_SCROLL_JUMP, null);

        ScrollDAO.create(50, "Evil repelling coat",                     Scroll.TYPE_SCROLL_DEFENSE, null);
        ScrollDAO.create(51, "Aura of defense",                         Scroll.TYPE_SCROLL_DEFENSE, null);
        ScrollDAO.create(52, "Hardened leather boots",                  Scroll.TYPE_SCROLL_DEFENSE, null);
        ScrollDAO.create(53, "Fancy pants",                             Scroll.TYPE_SCROLL_DEFENSE, null);
        ScrollDAO.create(54, "The shell of a svnTortoise",              Scroll.TYPE_SCROLL_DEFENSE, null);
        ScrollDAO.create(55, "Titanium rope",                           Scroll.TYPE_SCROLL_DEFENSE, null);
        ScrollDAO.create(56, "Impenetrable skin",                       Scroll.TYPE_SCROLL_DEFENSE, null);
        ScrollDAO.create(57, "Becoming a fullmetal alchemist",          Scroll.TYPE_SCROLL_DEFENSE, null);
        ScrollDAO.create(58, "Scales of a dragon",                      Scroll.TYPE_SCROLL_DEFENSE, null);
        ScrollDAO.create(59, "Impenetrable armor of doom,epicness, invicibility and awesomeness", Scroll.TYPE_SCROLL_DEFENSE, null);

        Logger.info("Done initializing");
    }
}
