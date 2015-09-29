package bootstrap;

import models.Avatar;
import dao.AvatarDAO;
import models.PlayerStats;

import play.Logger;

public class AvatarBootstrap {
  //////////////////////////////////////////////////
  //  Init Method
  //////////////////////////////////////////////////

  /**
   * Method to initialize the avatar data
   */
  public static void init() {
    Logger.info("Initialize 'Avatar' data");

    /*
    AvatarDAO.create(
        "",
        "description",
        "assets/data/img/avatare/",
        "",
        "assets/",
        4,
        0,
        5,
        15,
        1);
    */
    AvatarDAO.create(
                  "Alastor Moody",
                  "„Mad-Eye“ is a high ranked auror. \\nHe not only has an imposing personality, \\nbut is also a great fighter known for \\nhis variable offensive fighting style.",
                  "alastor_moody",
                  "assets/",
                  false,
                  new PlayerStats(0,2,0,0,0));

          AvatarDAO.create(
                  "Albus Dumbledore",
                  "You might think he is not so nimble,\\nbecause of his advanced age.\\nBut he ist Albus P. W. B. Dumbledore!\\nHe is the best!",
                  "albus_dumbledore",
                  "assets/",
                  false,
                  new PlayerStats(2,1,2,2,0));

          AvatarDAO.create(
                  "Arthur Weasley",
                  "Arthur has worked in the Ministry \\nOf Magic for several years, but as you \\nwill see, he has not lost his touch.",
                  "arthur_weasley",
                  "assets/",
                  false,
                  new PlayerStats(2,0,0,1,0));

          AvatarDAO.create(
                  "Cho Chang",
                  "The one that got away.\\nAs a Ravenclaw she just \\nlikes to be airborne.",
                  "cho_chang",
                  "assets/",
                  false,
                  new PlayerStats(0,0,0,2,0));

          AvatarDAO.create(
                  "Colin Creevey",
                  "Harry-Potter-Fanboy of the first hour.\\nBeware, he is quick on his feet.",
                  "colin_creevey",
                  "assets/",
                  false,
                  new PlayerStats(0,0,2,0,0));

          AvatarDAO.create(
                  "Crabbe and Goyle",
                  "Dracos henchmen. Known for \\ntheir strength. They always stay \\nat each others side.",
                  "crabbe_and_goyle",
                  "assets/",
                  true,
                  new PlayerStats(0,1,0,1,0));

          AvatarDAO.create(
                  "Dolores Umbridge",
                  "Little women in pink.\\nJust do not underestimate her,\\nshe is really robust.",
                  "dolores_umbridge",
                  "assets/",
                  false,
                  new PlayerStats(0,1,1,0,0));

          AvatarDAO.create(
                  "Draco Malfoy",
                  "Not the nicest guy around.\\nQuite a big mouth and if push\\ncomes to shove, he is off in no time.",
                  "draco_malfoy",
                  "assets/",
                  false,
                  new PlayerStats(0,0,3,0,0));

          AvatarDAO.create(
                  "Filius Flitwick",
                  "Mr. „Swish and Flick“ himself!\\nThe Charms-Master is not\\n the toughest of the bunch,\\nbut he has unmatched agility.",
                  "filius_flitwick",
                  "assets/",
                  false,
                  new PlayerStats(-2,0,4,2,0));

          AvatarDAO.create(
                  "Fleur Delacour",
                  "The Beauty and a Beast, when it comes \\nto Agility. She really shows, why \\nshe is the Beauxbatons Champion",
                  "fleur_delacour",
                  "assets/",
                  false,
                  new PlayerStats(0,0,1,2,0));

          AvatarDAO.create(
                  "Fred and George",
                  "Inseparable,\\nthe two Weasleys are really though.\\nTogether they are unstoppable.",
                  "fred_and_george",
                  "assets/",
                  true,
                  new PlayerStats(4,0,0,0,0));

          AvatarDAO.create(
                  "Fred and George Uniform",
                  "Uniform seam to make one weak.\\nYeah maybe,but those two\\nare still some formidable runners.",
                  "fred_and_george_2",
                  "assets/",
                  true,
                  new PlayerStats(1,1,0,0,0));

          AvatarDAO.create(
                  "Gilderoy Lockhart",
                  "Simply the greatest Wizard of all time,\\nknown from all his adventures,\\nso this one is right up his alley.",
                  "gilderoy_lockhart",
                  "assets/",
                  false,
                  new PlayerStats(0,0,0,0,0));

          AvatarDAO.create(
                  "Ginny Weasley",
                  "Lovely little Ginny,\\nthe youngest of the family,\\nbut still a Weasley,\\nso watch out.",
                  "ginny_weasley",
                  "assets/",
                  false,
                  new PlayerStats(1,0,1,1,0));

          AvatarDAO.create(
                  "Harry Potter",
                  "description",
                  "harry_potter",
                  "assets/",
                  false,
                  new PlayerStats(0,0,0,1,0));

          AvatarDAO.create(
                  "Harry Potter 2",
                  "The boy who lived. This is a upgraded \\nversion of the previous one. Let \\nus see how the parsel-mouth \\nwill do in the dungeon.",
                  "harry__potter",
                  "assets/",
                  false,
                  new PlayerStats(0,1,1,1,0));

          AvatarDAO.create(
                  "Hermione Granger",
                  "description",
                  "hermione_granger",
                  "assets/",
                  false,
                  new PlayerStats(0,0,1,0,0));

          AvatarDAO.create(
                  "Hermione Granger 2",
                  "The smartest girl you will ever see,\\nunfortunately intelligence \\nis not one of the attributes, \\nbut she is still awesome.",
                  "hermione__granger",
                  "assets/",
                  false,
                  new PlayerStats(1,1,0,1,0));

          AvatarDAO.create(
                  "Horace Slughorn",
                  "The Oldtimer. Still got some tricks\\nup his sleeves. Know to be slightly \\ncoward, he got great Speed.",
                  "horace_slughorn",
                  "assets/",
                  false,
                  new PlayerStats(1,0,2,1,0));

          AvatarDAO.create(
                  "Kingsley Shacklebolt",
                  "The famous Auror, is a great fighter.\\nLet us hope that is enough.",
                  "kingsley_shacklebolt",
                  "assets/",
                  false,
                  new PlayerStats(1,1,0,0,0));

          AvatarDAO.create(
                  "Lavender Brown",
                  "Mrs. Trelwaney's favourite student.\\nBrave and loyal. A typical Gryffindor.",
                  "lavender_brown",
                  "assets/",
                  false,
                  new PlayerStats(1,0,1,0,0));

          AvatarDAO.create(
                  "Lucius Malfoy",
                  "„Wait till his father hears about this.“\\nLet us see what he has to offer.",
                  "lucius_malfoy",
                  "assets/",
                  false,
                  new PlayerStats(2,0,1,0,0));

          AvatarDAO.create(
                  "Luna Lovegood",
                  "The good soul of the house. Yes..., she is \\nweird, but give her a go.",
                  "luna_lovegood",
                  "assets/",
                  false,
                  new PlayerStats(1,0,0,1,0));

          AvatarDAO.create(
                  "Minerva McGonagall",
                  "The Professor for Transfiguration,\\nis also an Animagus,\\nshe got the velocity of a cat and\\nmaybe some lives to spare as well.",
                  "minerva_mcGonagel",
                  "assets/",
                  false,
                  new PlayerStats(2,0,0,2,0));

          AvatarDAO.create(
                  "Moaning Myrtle",
                  "Myrtle is a ghost.\\nShe likes to get lost in great heights,\\nbut remember, for the purpose of\\nthis game, she is not immortal.",
                  "moaning_myrtle2",
                  "assets/",
                  false,
                  new PlayerStats(0,0,0,4,0));

          AvatarDAO.create(
                  "Molly Weasley",
                  "Having 7 children, she knows how to \\ntake care of all kind of injuries.",
                  "molly_weasley",
                  "assets/",
                  false,
                  new PlayerStats(3,0,0,0,0));

          AvatarDAO.create(
                  "Mr. Binns",
                  "Mr. Binns is so boring, that all students \\nfall asleep. He often dreams of \\nflying through the clouds.",
                  "mr_binns2",
                  "assets/",
                  false,
                  new PlayerStats(0,0,0,3,0));

          AvatarDAO.create(
                  "Narcissa Malfoy",
                  "The pure blood, she is not officially \\nevil, but nice is something different.",
                  "narcissa_malfoy",
                  "assets/",
                  false,
                  new PlayerStats(1,0,2,0,0));

          AvatarDAO.create(
                  "Neville Longbottom",
                  "I've always hoped that he is\\nthe one from the prophecy.\\nChoose him, he is really good.",
                  "neville_longbottom",
                  "assets/",
                  false,
                  new PlayerStats(0,1,0,0,0));

          AvatarDAO.create(
                  "Nymphadora Tonks",
                  "The girl of many colors, she is an Auror\\nwith bad temper, but great tempo.",
                  "nymphadora_tonks",
                  "assets/",
                  false,
                  new PlayerStats(0,0,3,1,0));

          AvatarDAO.create(
                  "Padma and Parvati",
                  "The Patil-Twins are the cheapest \\nteam with the best agility.",
                  "padma_and_parvati_patil",
                  "assets/",
                  true,
                  new PlayerStats(0,0,1,1,0));

          AvatarDAO.create(
                  "Pomona Sprout",
                  "Head of the Hufflepuff House.\\nShe really gets the job done,\\nplus she reminds me of my grandma.",
                  "pomona_sprout",
                  "assets/",
                  false,
                  new PlayerStats(1,0,0,2,0));

          AvatarDAO.create(
                  "Remus Lupin",
                  "Full moon or not, this Lykanthrop is on \\nthe prowl. No one is as swift as him.",
                  "remus_lupin",
                  "assets/",
                  false,
                  new PlayerStats(0,0,2,2,0));

          AvatarDAO.create(
                  "Ron Weasley",
                  "discription",
                  "ron_weasley",
                  "assets/",
                  false,
                  new PlayerStats(1,0,0,0,0));

          AvatarDAO.create(
                  "Ron Weasley 2",
                  "The muscle of the gang. Quidditch \\nkeeper and the greatest Chess-player \\nof Hogwarts, you might think.",
                  "ron__weasley",
                  "assets/",
                  false,
                  new PlayerStats(2,1,0,0,0));

          AvatarDAO.create(
                  "Rubeus Hagrid",
                  "Due to his giant half,\\nhe is a real juggernaut.\\nNot very fast, but unstoppable,\\nonce he got some momentum.",
                  "rubeus_hagrid",
                  "assets/",
                  false,
                  new PlayerStats(5,2,-2,0,0));

          AvatarDAO.create(
                  "Seamus Finnigan",
                  "Seamus is quite an explosive character and \\nhas often been in mortal danger.\\nNobody knows why he is still standing.",
                  "seamus_finnigan",
                  "assets/",
                  false,
                  new PlayerStats(5,-1,0,0,0));

          AvatarDAO.create(
                  "Severus Snape",
                  "The half-blood prince \\nand master of potion-making\\nalways has a elixir at hand.",
                  "severus_snape",
                  "assets/",
                  false,
                  new PlayerStats(1,1,1,1,1));


          AvatarDAO.create(
                  "Sirius Black",
                  "The only wizzard who escaped azkaban\\non his own with the speed of a hound.\\nHe should be able to escape the \\ndungeon, dangers as well.",
                  "sirius_black",
                  "assets/",
                  false,
                  new PlayerStats(3,2,3,-2,0));

          AvatarDAO.create(
                  "Sybill Trelawney",
                  "Sybill is gifted with the power \\nof foresight which gives her a \\nclear advantage in dodging obstacles.",
                  "sybill_trelawney",
                  "assets/",
                  false,
                  new PlayerStats(0,0,2,1,0));

          AvatarDAO.create(
                  "Voldemort",
                  "He-Who-Must-Not-Be-Named, the \\nmightiest black magician of all time.\\nHe spread fear and terror.",
                  "voldemort",
                  "assets/",
                  false,
                  new PlayerStats(7,1,1,1,0));

          Logger.info("Done initializing");
      }
}
