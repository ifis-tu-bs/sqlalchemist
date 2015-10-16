package bootstrap;

import dao.AvatarDAO;
import dao.ShopItemDAO;

import models.Avatar;
import models.PlayerStats;
import play.Logger;

public class ShopItemBootstrap {
//////////////////////////////////////////////////
//  Init Method
//////////////////////////////////////////////////
    /**
     * Init method
     */
    public static void init() {
        Logger.info("Initialize 'Shop' data");

        Avatar avatar = AvatarDAO.create(
                "Skipping Al",
                "Al is a high ranked auror. \\nHe not only has an imposing personality, \\nbut is also a great fighter known for \\nhis variable offensive fighting style.",
                "alastor_moody",
                false,
                new PlayerStats(0,2,0,0,0));
        ShopItemDAO.create(avatar, 8000);


        avatar = AvatarDAO.create(
                "Fresh D!",
                "You might think he is not so nimble,\\nbecause of his advanced age.\\nBut he ist Airbus P. W. B. Fumbledoor!\\nHe is the best!",
                "albus_dumbledore",
                false,
                new PlayerStats(2, 1, 2, 2, 0));
        ShopItemDAO.create(avatar, 20000);


        avatar = AvatarDAO.create(
                        "Art Weasleby",
                        "Art has worked in the Ministry \\nOf Magic for several years, but as you \\nwill see, he has not lost his touch.",
                        "arthur_weasley",
                        false,
                        new PlayerStats(2,0,0,1,0));
        ShopItemDAO.create(avatar, 2000);


        avatar = AvatarDAO.create(
                "Ching Chang Cho",
                "The one that got away.\\nAs a Ravenclaus she just \\nlikes to be airborne.",
                "cho_chang",
                false,
                new PlayerStats(0,0,0,2,0));
        ShopItemDAO.create(avatar, 500);


        avatar = AvatarDAO.create(
                "Colin Creepy",
                "Hairy-P.-Fanboy of the first hour.\\nBeware, he is quick on his feet.",
                "colin_creevey",
                false,
                new PlayerStats(0,0,2,0,0));
        ShopItemDAO.create(avatar, 500);


        avatar = AvatarDAO.create(
                "Crabbe and Crabber",
                "Drag'os henchmen. Known for \\ntheir strength. They always stay \\nat each others side.",
                "crabbe_and_goyle",
                true,
                new PlayerStats(0,1,0,1,0));
        ShopItemDAO.create(avatar, 2000);


        avatar = AvatarDAO.create(
                "Dolly P.",
                "Little women in pink.\\nJust do not underestimate her,\\nshe is really robust.",
                "dolores_umbridge",
                false,
                new PlayerStats(0,1,1,0,0));
        ShopItemDAO.create(avatar, 2000);


        avatar = AvatarDAO.create(
                "Draco Femalfoy",
                "Not the nicest guy around.\\nQuite a big mouth and if push\\ncomes to shove, he is off in no time.",
                "draco_malfoy",
                false,
                new PlayerStats(0,0,3,0,0));
        ShopItemDAO.create(avatar, 2000);


        avatar = AvatarDAO.create(
                "Wilius Philwick",
                "Mr. „Swish and Flick“ himself!\\nThe Charms-Master is not\\n the toughest of the bunch,\\nbut he has unmatched agility.",
                "filius_flitwick",
                false,
                new PlayerStats(-2,0,4,2,0));
        ShopItemDAO.create(avatar, 8000);


        avatar = AvatarDAO.create(
                "Tour de la Flur",
                "The Beauty and a Beast, when it comes \\nto Agility. She really shows, why \\nshe is the Peauxpatons Champion",
                "fleur_delacour",
                false,
                new PlayerStats(0,0,1,2,0));
        ShopItemDAO.create(avatar, 2000);


        avatar = AvatarDAO.create(
                "James and Oliver",
                "Inseparable,\\nthe two Weasleys are really though.\\nTogether they are unstoppable.",
                "fred_and_george",
                true,
                new PlayerStats(4,0,0,0,0));
        ShopItemDAO.create(avatar, 8000);


        avatar = AvatarDAO.create(
                "James and Oliver Uniform",
                "Uniform seam to make one weak.\\nYeah maybe,but those two\\nare still some formidable runners.",
                "fred_and_george_2",
                true,
                new PlayerStats(1,1,0,0,0));
        ShopItemDAO.create(avatar, 2000);


        avatar = AvatarDAO.create(
                "Capt. Cockhard",
                "Simply the greatest Wizard of all time,\\nknown from all his adventures,\\nso this one is right up his alley.",
                "gilderoy_lockhart",
                false,
                new PlayerStats(0,0,0,0,0));
        ShopItemDAO.create(avatar, 20000);


        avatar = AvatarDAO.create(
                "Tonic Weaslbey",
                "Lovely little Tonic,\\nthe youngest of the family,\\nbut still a Weaslby,\\nso watch out.",
                "ginny_weasley",
                false,
                new PlayerStats(1,0,1,1,0));
        ShopItemDAO.create(avatar, 2000);


        avatar = AvatarDAO.create(
                "Hairy P.",
                "description",
                "harry_potter",
                false,
                new PlayerStats(0,0,0,1,0));
        ShopItemDAO.create(avatar, 0); //Bereits im Besitz des Benutzers


        avatar = AvatarDAO.create(
                "Hairy P. 2",
                "The boy who lived. This is a upgraded \\nversion of the previous one. Let \\nus see how the parsel-mouth \\nwill do in the dungeon.",
                "harry__potter",
                false,
                new PlayerStats(0,1,1,1,0));
        ShopItemDAO.create(avatar, 8000);


        avatar = AvatarDAO.create(
                "Hermene Mine Moe",
                "description",
                "hermione_granger",
                false,
                new PlayerStats(0,0,1,0,0));
        ShopItemDAO.create(avatar, 0); //Bereits im Besitz des Benutzers


        avatar = AvatarDAO.create(
                "Hermene Mine Moe 2",
                "The smartest girl you will ever see,\\nunfortunately intelligence \\nis not one of the attributes, \\nbut she is still awesome.",
                "hermione__granger",
                false,
                new PlayerStats(1,1,0,1,0));
        ShopItemDAO.create(avatar, 8000);


        avatar = AvatarDAO.create(
                "Borace Jelzhorn",
                "The Oldtimer. Still got some tricks\\nup his sleeves. Know to be slightly \\ncoward, he got great Speed.",
                "horace_slughorn",
                false,
                new PlayerStats(1,0,2,1,0));
        ShopItemDAO.create(avatar, 8000);


        avatar = AvatarDAO.create(
                "Usain Shacklebolt",
                "The famous Auror, is a great fighter.\\nLet us hope that is enough.",
                "kingsley_shacklebolt",
                false,
                new PlayerStats(-4,1,5,0,0));
        ShopItemDAO.create(avatar, 2000);


        avatar = AvatarDAO.create(
                "Student 72",
                "Mrs. Trelwaney's favourite student.\\nBrave and loyal. A typical Gryffindor.",
                "lavender_brown",
                false,
                new PlayerStats(1,0,1,0,0));
        ShopItemDAO.create(avatar, 500);


        avatar = AvatarDAO.create(
                "Lucy Femalfoy",
                "„Wait till his father hears about this.“\\nLet us see what he has to offer.",
                "lucius_malfoy",
                false,
                new PlayerStats(2,0,1,0,0));
        ShopItemDAO.create(avatar, 2000);


        avatar = AvatarDAO.create(
                "Lurra Ladybug",
                "The good soul of the house.\\n Yes..., she is weird, but give her a go.",
                "luna_lovegood",
                false,
                new PlayerStats(1,0,0,1,0));
        ShopItemDAO.create(avatar, 500);


        avatar = AvatarDAO.create(
                "Ms. McKittycat",
                "The Professor for Transfiguration,\\nis also an Animagus,\\nshe got the velocity of a cat and\\nmaybe some lives to spare as well.",
                "minerva_mcGonagel",
                false,
                new PlayerStats(2,0,0,2,0));
        ShopItemDAO.create(avatar, 8000);


        avatar = AvatarDAO.create(
                "Grieving Grace",
                "Myrtle is a ghost.\\nShe likes to get lost in great heights,\\nbut remember, for the purpose of\\nthis game, she is not immortal.",
                "moaning_myrtle2",
                false,
                new PlayerStats(0,0,0,4,0));
        ShopItemDAO.create(avatar, 8000);


        avatar = AvatarDAO.create(
                "Mother Weasleby",
                "Having 7 children, she knows how to \\ntake care of all kind of injuries.",
                "molly_weasley",
                false,
                new PlayerStats(4,0,0,0,0));
        ShopItemDAO.create(avatar, 2000);


        avatar = AvatarDAO.create(
                "Mr. Beans",
                "Mr. Beans is so boring, that all students \\nfall asleep. He died centenaries \\nyet he still teaches History.",
                "mr_binns2",
                false,
                new PlayerStats(0,0,0,3,0));
        ShopItemDAO.create(avatar, 2000);


        avatar = AvatarDAO.create(
                "Narziss Femalfoy",
                "The pure blood, she is not officially \\nevil, but nice is something different.\\nGreat mobility, low health.",
                "narcissa_malfoy",
                false,
                new PlayerStats(-1,0,2,2,0));
        ShopItemDAO.create(avatar, 2000);


        avatar = AvatarDAO.create(
                "Neville Strongbottom",
                "I've always hoped that he is\\nthe one from the prophecy.\\nChoose him, he is really good.",
                "neville_longbottom",
                false,
                new PlayerStats(0,1,0,0,0));
        ShopItemDAO.create(avatar, 500);


        avatar = AvatarDAO.create(
                "Nymphadora Tongues",
                "The girl of many colors, she is an Auror\\nwith bad temper, but great tempo.",
                "nymphadora_tonks",
                false,
                new PlayerStats(0,0,3,1,0));
        ShopItemDAO.create(avatar, 8000);


        avatar = AvatarDAO.create(
                "Party Partil",
                "The Parti-Twins are the cheapest \\nteam with the best agility.",
                "padma_and_parvati_patil",
                true,
                new PlayerStats(0,0,1,1,0));
        ShopItemDAO.create(avatar, 500);


        avatar = AvatarDAO.create(
                "Brussela Kraut",
                "Head of the Pufflehuff House.\\nShe really gets the job done,\\nplus she reminds me of my grandma.",
                "pomona_sprout",
                false,
                new PlayerStats(1,0,0,2,0));
        ShopItemDAO.create(avatar, 2000);


        avatar = AvatarDAO.create(
                "Wolfgang Wolf",
                "Full moon or not, this Lykanthrop is on \\nthe prowl. No one is as swift as him.",
                "remus_lupin",
                false,
                new PlayerStats(-2,0,3,3,0));
        ShopItemDAO.create(avatar, 8000);


        avatar = AvatarDAO.create(
                "Jon Weasleby",
                "discription",
                "ron_weasley",
                false,
                new PlayerStats(1,0,0,0,0));
        ShopItemDAO.create(avatar, 0); //Bereits im Besitz des Benutzers


        avatar = AvatarDAO.create(
                "Bon Weasleby 2",
                "The muscle of the gang. Quidditch \\nkeeper and the greatest Chess-player \\nof Hogwarts, you might think.",
                "ron__weasley",
                false,
                new PlayerStats(3,1,-1,0,0));
        ShopItemDAO.create(avatar, 8000);


        avatar = AvatarDAO.create(
                "Hoga",
                "Due to his giant half,\\nhe is a real juggernaut.\\nNot very fast, but unstoppable,\\nonce he got some momentum.",
                "rubeus_hagrid",
                false,
                new PlayerStats(5,2,-2,0,0));
        ShopItemDAO.create(avatar, 20000);


        avatar = AvatarDAO.create(
                "Seamus Gallagher",
                "Seamus is quite an explosive character and \\nhas often been in mortal danger.\\nNobody knows why he is still standing.",
                "seamus_finnigan",
                false,
                new PlayerStats(5,-1,0,0,0));
        ShopItemDAO.create(avatar, 500);


        avatar = AvatarDAO.create(
                "Serverus Snoopy",
                "The half-blood prince \\nand master of potion-making\\nalways has a elixir at hand.",
                "severus_snape",
                false,
                new PlayerStats(1,1,1,1,1));
        ShopItemDAO.create(avatar, 20000);



        avatar = AvatarDAO.create(
                "Sloop Dogg",
                "The only wizzard who escaped azkaban\\non his own with the speed of a hound.\\nHe should be able to escape the \\ndungeon, and all it throws at him.",
                "sirius_black",
                false,
                new PlayerStats(3,2,3,-2,0));
        ShopItemDAO.create(avatar, 20000);


        avatar = AvatarDAO.create(
                "Sybrille Truehorny",
                "Sybrille is gifted with the power \\nof foresight which gives her a \\nclear advantage in dodging obstacles.",
                "sybill_trelawney",
                false,
                new PlayerStats(0,0,2,1,0));
        ShopItemDAO.create(avatar, 2000);


        avatar = AvatarDAO.create(
                "Vollderlord",
                "He-Who-Must-Not-Be-Named-Or-Renamed-For-Legal-Reasons,\\nthe mightiest black magician of all time.\\nHe spreads fear and terror.",
                "voldemort",
                false,
                new PlayerStats(7,1,1,1,0));
        ShopItemDAO.create(avatar, 20000);



        ShopItemDAO.create("Belt Slot 1", 1000);
        ShopItemDAO.create("Belt Slot 2", 2000);
        ShopItemDAO.create("Belt Slot 3", 4000);
        ShopItemDAO.create("Belt Slot 4", 8000);
        ShopItemDAO.create("Belt Slot 5", 16000);

        Logger.info("Done initializing");
    }
}
