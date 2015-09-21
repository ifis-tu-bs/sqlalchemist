package controllers;

import helper.HMSAccessor;
import helper.SimpleText;
import play.Logger;
import play.mvc.*;
import play.Play;

import models.*;
import dao.UserDAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * The application class
 *
 * The methods of this class delivery the content-pages to the user, with an authentication key.
 *  This authentication key marks the rights of the current application.
 *
 */
public class Application extends Controller {

    /**
     * GET /init
     *
     * @return ok
     */
    public static Result init() {
        if(UserDAO.getAllUsers().size() == 0) {
            Logger.info("Application.init - Start initializing");
            // Init final Classes
            Map.init();
            Potion.init();
            Scroll.init();
            Avatar.init();
            ShopItem.init();
            TaskFile.init();

            // Init residual Classes
            Profile profile = null;
            if(play.api.Play.isProd(play.api.Play.current())) {
                String username = Play.application().configuration().getString("admin.username");
                String email = Play.application().configuration().getString("admin.email");
                String password = Play.application().configuration().getString("admin.password");
                UserDAO.create(username, email, password, User.ROLE_ADMIN);
            } else {
                User user = UserDAO.create("admin", "admin@local.de", "password1234", User.ROLE_ADMIN);
                user.setStudent();
                user.update();

                UserDAO.create("test1", "test1@test.de", "test", User.ROLE_CREATOR);
                UserDAO.create("test2", "test2@test.de", "test", User.ROLE_USER);
                UserDAO.create("test3", "test3@test.de", "test", User.ROLE_USER);
            }

            HomeWorkChallenge.init();
            SubmittedHomeWork.init();



            List<SimpleText> texts0 = new ArrayList<>();
            List<SimpleText> texts1 = new ArrayList<>();
            List<SimpleText> texts2 = new ArrayList<>();
            List<SimpleText> texts3 = new ArrayList<>();
            List<SimpleText> texts4 = new ArrayList<>();
            List<SimpleText> texts5 = new ArrayList<>();
            List<SimpleText> texts6 = new ArrayList<>();
            List<SimpleText> texts7 = new ArrayList<>();
            List<SimpleText> texts8 = new ArrayList<>();
            List<SimpleText> texts9 = new ArrayList<>();
            List<SimpleText> texts10 = new ArrayList<>();


            texts0.add(new SimpleText(
                    "Secreterry, the magical cabinet:" + "<br>" +
                            "   Oh look what the cat dragged in! ...The fat Lady let you in?" + "<br>" +
                            "   Unbelievable, Perenelle treats this Laboratory like an" + "<br>" +
                            "   amusement park. I really need to get rid of her. So what is your" + "<br>" +
                            "   deal? Are you looking for Nicolas Flamel? Well, as you see, " + "<br>" +
                            "   the great alchemist is not around, obviously!" + "<br>" +
                            "   So you might as well shove off." + "<br>" +
                            "" + "<br>" +
                            "(feel free to say the following line yourself) " + "<br>" +
                            "You:" + "<br>" +
                            "   I need to find him, where is he?",
                    SimpleText.SIMPLE_TEXT_PREREQUISITE_TUTORIAL_LAB_1, 3));

            texts0.add(new SimpleText(
                    "Secreterry, the magical cabinet:" + "<br>" +
                            "   Where he is? He is in the Dungeon. Where else should he be? " + "<br>" +
                            "   Do not even think about it! The Dungeon is really dangerous." + "<br>" +
                            "" + "<br>" +
                            "You: " + "<br>" +
                            "   Why did he go then?",
                    SimpleText.SIMPLE_TEXT_PREREQUISITE_TUTORIAL_LAB_2, 1));

            texts0.add(new SimpleText(
                    "Secreterry, the magical cabinet:" + "<br>" +
                            "   Why he went into the Dungeon? Well, like in every RPG, the" + "<br>" +
                            "   Dungeon is littered with treasures. Among them, is the legendary" + "<br>" +
                            "   'Golden Codd', an ancient artifact able to grant the gift" + "<br>" +
                            "   of life. Nicolas was looking for it and he has not returned yet." + "<br>" +
                            "" + "<br>" +
                            "You: " + "<br>" +
                            "   I will save him?",
                    SimpleText.SIMPLE_TEXT_PREREQUISITE_TUTORIAL_LAB_3, 3));

            texts0.add(new SimpleText(
                    "Secreterry, the magical cabinet:" + "<br>" +
                            "   You! I don't even think that you can get to the second floor, well " + "<br>" +
                            "   maybe with the guidance of a magical cabinet or database you" + "<br>" +
                            "   maybe can get to 'The Golden Codd'. Alright, I should give you a" + "<br>" +
                            "   little tutorial... I mean, a little tour around the Laboratory.",
                    SimpleText.SIMPLE_TEXT_PREREQUISITE_TUTORIAL_LAB_4, 3));

            texts0.add(new SimpleText(
                    "Secreterry, the magical cabinet:" + "<br>" +
                            "   In this mirror you can choose your avatar." + "<br>" +
                            "   So you can make yourself prettier.",
                    SimpleText.SIMPLE_TEXT_PREREQUISITE_TUTORIAL_LAB_SUCCESSFULL, 1));

            //(Spotlight on mirror: Click on mirror) 5

            texts0.add(new SimpleText(
                    "Secreterry, the magical cabinet:" + "<br>" +
                            "   If you have an enchantment you can upgrade" + "<br>" +
                            "   your attributes here. Enchantments can be" + "<br>" +
                            "   found in the Dungeon. They are white Scrolls." + "<br>" +
                            "   You can just simply pick them up! To go back" + "<br>" +
                            "   to the Laboratory, just click on the" + "<br>" +
                            "   Laboratory visible in the background.",
                    SimpleText.SIMPLE_TEXT_PREREQUISITE_TUTORIAL_SHEET_SUCCESSFULL, 3));

            //(highlight laboratory)  6 highlight door

            texts0.add(new SimpleText(
                    "Secreterry, the magical cabinet:" + "<br>" +
                            "   Let's see how well you do down in the Dungeon. To enter, click" + "<br>" +
                            "   on the door. Here are some lifesaving hints: With the 'Space Bar'," + "<br>" +
                            "   or by clicking on the screen, you can jump. Enemies will hurt" + "<br>" +
                            "   you if you don't jump over or directly on to them. The 'M' and" + "<br>" +
                            "   'N' keys and the buttons on the upper left corner control the" + "<br>" +
                            "   music and sounds. So, off you go!",
                    SimpleText.SIMPLE_TEXT_PREREQUISITE_TUTORIAL_LAB_BACK, 4));

            //(highlight door)
            //[First Run: Character dies.] 7

            texts0.add(new SimpleText(
                    "Secreterry, the magical cabinet:" + "<br>" +
                            "   This didn't go too well, quite as expected." + "<br>" +
                            "   This, by the way, was Vincent. The lovely dude with the axe." + "<br>" +
                            "   But at least you picked up a Potion Scroll. Go back to the" + "<br>" +
                            "   Laboratory by clicking next, hurry up!",
                    SimpleText.SIMPLE_TEXT_PREREQUISITE_TUTORIAL_DUNGEON_FIRST_TRY, 2));

            //(Next button clicked)
            //(Spotlight on the Belt) 8

            texts0.add(new SimpleText(
                    "Secreterry, the magical cabinet:" + "<br>" +
                            "   With this Scroll you can brew a Potion. Drinking them might" + "<br>" +
                            "   get you hooked and you run the risk of building up a serious" + "<br>" +
                            "   drinking habit, never the less chuck those suckers down as" + "<br>" +
                            "   much as you can, you do need every help you can get." + "<br>" +
                            "   To brew the Potion you have to visit the Potion Collection.",
                    SimpleText.SIMPLE_TEXT_PREREQUISITE_TUTORIAL_BELT, 3));

            //(Click on the potion bottle) 9

            texts0.add(new SimpleText(
                    "Secreterry, the magical cabinet:" + "<br>" +
                            "   There you see the Defense-Potion. You can get the ingredients" + "<br>" +
                            "   out of my drawers, if you ask nicely. The problem is, however," + "<br>" +
                            "   the only language I will accept is SQL. So if you click on the Scroll, " + "<br>" +
                            "   I will give you a riddle you have to solve in order to receive your Potion." + "<br>" +
                            "   You will be awarded with points and additional Potions" + "<br>" +
                            "   according to the time it took you to solve the task. Once" + "<br>" +
                            "   you finished the task, you will receive the Potion which you" + "<br>" +
                            "   afterwards can use in the Dungeons and then you maybe won't die that often.",
                    SimpleText.SIMPLE_TEXT_PREREQUISITE_TUTORIAL_BELT_SUCCESSFULL, 4));


            //(Correct Answer given)
            //(next clicked)

            texts0.add(new SimpleText(
                    "Secreterry, the magical cabinet:" + "<br>" +
                            "   Beginners luck! Of course you could have been faster, but I'll " + "<br>" +
                            "   give you the Potion anyway. To use it, you have to attach it to your belt." + "<br>" +
                            "   To put them into your belt, you simply have to click on the" + "<br>" +
                            "   Potion under the number and it will go directly to the first free" + "<br>" +
                            "   slot in your Belt. Click on the Potion in your belt and the" + "<br>" +
                            "   Potion goes back to your Potion Collection.",
                    SimpleText.SIMPLE_TEXT_PREREQUISITE_TUTORIAL_TASK, 4));

            //(Clicked Back)
            //(Clicked Dungeon)
            //(Starts second Run) 11

            texts0.add(new SimpleText(
                    "Secreterry, the magical cabinet:" + "<br>" +
                            "   Now that you have attached the Potion, don't forget to use it!" + "<br>" +
                            "   You can activate the Potion by clicking '1' or the Potion" + "<br>" +
                            "   itself on the lower left corner. So kiddo, that really was" + "<br>" +
                            "   an explanation for 3-year-olds. So do not screw this up." + "<br>" +
                            "   Don't get me wrong, it is funny when you screw up and die, " + "<br>" +
                            "   but we do not have all day.",
                    SimpleText.SIMPLE_TEXT_PREREQUISITE_TUTORIAL_DUNGEON_SECOND_TRY, 3));

            //(Not beaten the tutorial) 12

            texts0.add(new SimpleText(
                    "Secreterry, the magical cabinet:" + "<br>" +
                            "   For Franks sake, how can you have been beaten again, I told you: just use the Potion! ",
                    SimpleText.SIMPLE_TEXT_PREREQUISITE_TUTORIAL_POTION, 3));

            //(Clicked Dungeon or Potion-Collection) 13

            texts0.add(new SimpleText(
                    "Secreterry, the magical cabinet:" + "<br>" +
                            "   Well on one hand, you got past Vincent, but you died later. So use" + "<br>" +
                            "   your Scrolls to get new Potions or upgrade your attributes in the" + "<br>" +
                            "   Character-Sheet(the mirror)." + "<br>" +
                            "(End of the Tutorial)" + "<br>" + "<br>" +
                            "At this point you can either go back to the main menu or you can" + "<br>" +
                            "inspect  your progress by clicking on the Scroll Collection." + "<br>" +
                            "For more information click on the books on the shelf.",
                    SimpleText.SIMPLE_TEXT_PREREQUISITE_TUTORIAL_SUCCESSFULL, 2));

            //BOSS-Comments

            //After 1bossmap:
            texts1.add(new SimpleText(
                    "Secreterry, the magical cabinet:" + "<br>" +
                            "   Haha, the itsy bitsy student climbed up the castle. Well you need" + "<br>" +
                            "   to get the weak Jump-Potion. You have collected the Scroll with" + "<br>" +
                            "   the red ribbon? But do not forget that you can only get 3 Scrolls a day.",
                    SimpleText.SIMPLE_TEXT_PREREQUISITE_BOSSMAP_1, 2));

            //After 2bossmap:
            texts2.add(new SimpleText(
                    "Secreterry, the magical cabinet:" + "<br>" +
                            "   Great these little birds really have torn you to shreds, to put Vincent" + "<br>" +
                            "   inside there was maybe a little overkill, I suppose. " + "<br>" +
                            "   If you can not overcome a level, don't worry just start drinking.",
                    SimpleText.SIMPLE_TEXT_PREREQUISITE_BOSSMAP_2, 2));

            //After 3bossmap:
            texts3.add(new SimpleText(
                    "Secreterry, the magical cabinet:" + "<br>" +
                            "   Wow, that was a huge hole. You might know..." + "<br>" +
                            "   blabla..bla.use Jump-Potion blabla" + "<br>" +
                            "   bla..something about interior design bla" + "<br>" +
                            "   blabla.bla use Jump-Potion some more stuff" + "<br>" +
                            "   blabla huge hole.",
                    SimpleText.SIMPLE_TEXT_PREREQUISITE_BOSSMAP_3, 4));

            //After 4bossmap:
            texts4.add(new SimpleText(
                    "Secreterry, the magical cabinet:" + "<br>" +
                            "Do you know the saying: Give a man a Potion and he finishes a map," + "<br>" +
                            "give a man a Scroll and he will finish the whole level or leave him alone" + "<br>" +
                            "and he will figure it out, I like that one, so have fun.",
                    SimpleText.SIMPLE_TEXT_PREREQUISITE_BOSSMAP_4, 2));

            //After 5bossmap:
            texts5.add(new SimpleText(
                    "Secreterry, the magical cabinet:" + "<br>" +
                            "   The burnt child dreads the fire. Maybe but we still need" + "<br>" +
                            "   to get past this, so sweep yourself together. By the way," + "<br>" +
                            "   fire always hurts and it will ignore all your defenses.",
                    SimpleText.SIMPLE_TEXT_PREREQUISITE_BOSSMAP_5, 2));

            //After 6bossmap:
            texts6.add(new SimpleText(
                    "Secreterry, the magical cabinet:" + "<br>" +
                            "   DRAGON! Now we finally are getting somewhere!" + "<br>" +
                            "   Little hint: be aware of it's tail.",
                    SimpleText.SIMPLE_TEXT_PREREQUISITE_BOSSMAP_6, 1));

            //After 7bossmap:
            texts7.add(new SimpleText(
                    "Secreterry, the magical cabinet:" + "<br>" +
                            "   Wow, another huge hole. Remember the lecture I gave" + "<br>" +
                            "   you like 20 levels ago, so make a run for it.",
                    SimpleText.SIMPLE_TEXT_PREREQUISITE_BOSSMAP_7, 1));

            //After 8bossmap:
            texts8.add(new SimpleText(
                    "Secreterry, the magical cabinet:" + "<br>" +
                            "   Out of the frying pan and into the fire. All this heat is" + "<br>" +
                            "   probably the reason why I do not go down there, because I" + "<br>" +
                            "   am a cabinet and we tent to light up easily and no having" + "<br>" +
                            "   legs is although part of the hindrance.",
                    SimpleText.SIMPLE_TEXT_PREREQUISITE_BOSSMAP_8, 2));

            //After 9bossmap:
            texts9.add(new SimpleText(
                    "Secreterry, the magical cabinet:" + "<br>" +
                            "   Are you serious? Come on, it's not that hard!" + "<br>" +
                            "   Go faster!",
                    SimpleText.SIMPLE_TEXT_PREREQUISITE_BOSSMAP_9, 2));

            //After 10bossmap:
            texts10.add(new SimpleText(
                    "Secreterry, the magical cabinet:" + "<br>" +
                            "   HAHA Now that is the moment you have all waited for" + "<br>" +
                            "   Come here tiny student ",
                    SimpleText.SIMPLE_TEXT_PREREQUISITE_BOSSMAP_10, 1));


            List<Map> maps0 = Map.getByLevel(0);
            List<Map> maps1 = Map.getByLevel(1);
            List<Map> maps2 = Map.getByLevel(2);
            List<Map> maps3 = Map.getByLevel(3);
            List<Map> maps4 = Map.getByLevel(4);
            List<Map> maps5 = Map.getByLevel(5);
            List<Map> maps6 = Map.getByLevel(6);
            List<Map> maps7 = Map.getByLevel(7);
            List<Map> maps8 = Map.getByLevel(8);
            List<Map> maps9 = Map.getByLevel(9);
            List<Map> maps10 = Map.getByLevel(10);


            StoryChallenge storyChallenge10 = StoryChallenge.create(
                    "10. Level",
                    false,
                    texts10,
                    maps10,
                    null,
                    10);

            StoryChallenge storyChallenge9 = StoryChallenge.create(
                    "9. Level",
                    false,
                    texts9,
                    maps9,
                    storyChallenge10,
                    9);

            StoryChallenge storyChallenge8 = StoryChallenge.create(
                    "8. Level",
                    false,
                    texts8,
                    maps8,
                    storyChallenge9,
                    8);

            StoryChallenge storyChallenge7 = StoryChallenge.create(
                    "7. Level",
                    false,
                    texts7,
                    maps7,
                    storyChallenge8,
                    7);

            StoryChallenge storyChallenge6 = StoryChallenge.create(
                    "6. Level",
                    false,
                    texts6,
                    maps6,
                    storyChallenge7,
                    6);

            StoryChallenge storyChallenge5 = StoryChallenge.create(
                    "5. Level",
                    false,
                    texts5,
                    maps5,
                    storyChallenge6,
                    5);

            StoryChallenge storyChallenge4 = StoryChallenge.create(
                    "4. Level",
                    false,
                    texts4,
                    maps4,
                    storyChallenge5,
                    4);

            StoryChallenge storyChallenge3 = StoryChallenge.create(
                    "3. Level",
                    false,
                    texts3,
                    maps3,
                    storyChallenge4,
                    3);

            StoryChallenge storyChallenge2 = StoryChallenge.create(
                    "2. Level",
                    false,
                    texts2,
                    maps2,
                    storyChallenge3,
                    2);

            StoryChallenge storyChallenge1 = StoryChallenge.create(
                    "1. Level",
                    false,
                    texts1,
                    maps1,
                    storyChallenge2,
                    1);

            StoryChallenge storyChallenge0 = StoryChallenge.create(
                    "Tutorial",
                    true,
                    texts0,
                    maps0,
                    storyChallenge1,
                    0);

            Logger.info("");


            Logger.info("Application.init - Done initializing");
        }
        return ok("Ich habe fertig");
    }
}
