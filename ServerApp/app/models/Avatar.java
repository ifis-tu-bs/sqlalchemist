package models;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.helper.PlayerStats;
import play.Logger;
import play.db.ebean.Model;
import play.libs.Json;

import javax.persistence.*;

/**
 *  Mapping-Class for Avatar on the File System
 *
 *  @version 1.0
 *  @author fabiomazzone
 */
@Entity
@Table(name = "avatar")
public class Avatar extends Model {

    @Id
    private long id;

    @Column(name = "avatar_name", unique = true)
    private final String name;

    @Column(name = "Avatardesc")
    private final String desc;

    @Column(name = "avatar_filename", unique = true)
    private final String avatarFilename;

    private final boolean isTeam;

    @Column(name = "soundURL")
    private final String soundURL;

    @Embedded
    private final PlayerStats playerStats;

    private static final Finder<Long, Avatar> find = new Finder<>(Long.class, Avatar.class);

//////////////////////////////////////////////////
//  Constructor
//////////////////////////////////////////////////

    /**
     * Avatar constructor
     * @param name                  name of the avatar
     * @param desc                  avatar description
     * @param avatarFilename        avatarFilename
     * @param soundURL              sound url
     * @param player_stat_health    stat_health
     * @param player_stat_defense   stat_defense
     * @param player_stat_speed     stat_speed
     * @param player_stat_jump      stat_jump
     * @param player_belt_slot      stat_slot
     */
    private Avatar(
            String name,
            String desc,
            String avatarFilename,
            String soundURL,
            boolean isTeam,
            int player_stat_health,
            int player_stat_defense,
            int player_stat_speed,
            int player_stat_jump,
            int player_belt_slot) {
        super();

        this.name           = name;
        this.desc           = desc;
        this.avatarFilename= avatarFilename;
        this.soundURL       = soundURL;
        this.playerStats    = new PlayerStats(
                player_stat_health,
                player_stat_defense,
                player_stat_speed,
                player_stat_jump,
                player_belt_slot
        );
        this.isTeam = isTeam;
    }

//////////////////////////////////////////////////
//  Json Method
//////////////////////////////////////////////////

    /**
     * Json method
     * @return returns the Avatar-object as a Json model
     */
    public ObjectNode toJson() {
        ObjectNode node = Json.newObject();

        node.put("id",              this.id);
        node.put("name",            this.getName());
        node.put("desc",            this.desc);
        node.put("avatarFilename",  this.avatarFilename);
        node.put("soundURL",        this.soundURL);
        node.put("isTeam",          this.isTeam);
        node.put("attributes",      this.playerStats.toJson());

        return node;
    }


    /**
     *
     * @return
     */
    public PlayerStats getPlayerStats() {
        return this.playerStats;
    }

    public String getName() {
        return this.name.contains("2") ? this.name.replace('2', ' ').trim() : this.name;
    }

    public String getAvatar() {
        return this.avatarFilename;
    }

    public String getDesc() {
        return this.desc;
    }

    public long getId() {
        return this.id;
    }
//////////////////////////////////////////////////
//  Create Method
//////////////////////////////////////////////////

    public static Avatar create(
            String  name,
            String  desc,
            String  avatarFilename,
            String  soundURL,
            boolean isTeam,
            int     player_stat_health,
            int     player_stat_defense,
            int     player_stat_speed,
            int     player_stat_jump,
            int     player_belt_slot) {

            Avatar avatar = new Avatar(
                    name,
                    desc,
                    avatarFilename,
                    soundURL,
                    isTeam,
                    player_stat_health,
                    player_stat_defense,
                    player_stat_speed,
                    player_stat_jump,
                    player_belt_slot );

            try {
                avatar.save();
            } catch (PersistenceException pe) {
                Avatar avatar_res = find.where().eq("name", name).findUnique();
                if(avatar_res != null && avatar_res.name.equalsIgnoreCase(name)) {
                    Logger.warn("Can't create Avatar(duplicate) " + avatar.toJson().toString());
                    return avatar_res;
                }
                Logger.error("Can't create Avatar: " + avatar.toJson());
                return null;
            }
            return avatar;

    }

    /**
     * This is a create method for an avatar-object
     *
     * @param name                  name of the avatar
     * @param avatarFilename        file name of the avatar files
     * @param soundURL              sound url
     * @param player_stat_health    stat_health
     * @param player_stat_defense   stat_defense
     * @param player_stat_speed     stat_speed
     * @param player_stat_jump      stat_jump
     * @param player_belt_slot      stat_slot
     */
    public static Avatar create(
            String name,
            String desc,
            String avatarFilename,
            String soundURL,
            int player_stat_health,
            int player_stat_defense,
            int player_stat_speed,
            int player_stat_jump,
            int player_belt_slot) {
        return create(name, desc, avatarFilename, soundURL, false, player_stat_health, player_stat_defense, player_stat_speed, player_stat_jump, player_belt_slot);
    }

//////////////////////////////////////////////////
//  Object Getter Methods
//////////////////////////////////////////////////

    /**
     * Get an avatar by its id
     * @param id id of the avatar
     * @return Avatar with the id
     */
    public static Avatar getById(long id) {
        return find.byId(id);
    }

    /**
     * Get an avatar object by its name
     *
     * @param name  name of the Avatar
     * @return      returns the Avatar that matches to the given name
     */
    public static Avatar getByName(String name) {
        return find.where().eq("name", name).findUnique();
    }


//////////////////////////////////////////////////
//  Init Method
//////////////////////////////////////////////////

    /**
     * Method to initialize the avatar data
     */
    public static void init() {
        Logger.info("Initialize 'Avatar' data");

        /*
        Avatar.create(
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
        Avatar.create(
                "Alastor Moody",
                "„Mad-Eye“ is a high ranked auror. \\nHe not only has an imposing personality, \\nbut is also a great fighter known for \\nhis variable offensive fighting style.",
                "alastor_moody",
                "assets/",
                0,
                2,
                0,
                0,
                0);

        Avatar.create(
                "Albus Dumbledore",
                "You might think he is not so nimble,\\nbecause of his advanced age.\\nBut he ist Albus P. W. B. Dumbledore!\\nHe is the best!",
                "albus_dumbledore",
                "assets/",
                2,
                1,
                2,
                2,
                0);

        Avatar.create(
                "Arthur Weasley",
                "Arthur has worked in the Ministry \\nOf Magic for several years, but as you \\nwill see, he has not lost his touch.",
                "arthur_weasley",
                "assets/",
                2,
                0,
                0,
                1,
                0);

        Avatar.create(
                "Cho Chang",
                "The one that got away.\\nAs a Ravenclaw she just \\nlikes to be airborne.",
                "cho_chang",
                "assets/",
                0,
                0,
                0,
                2,
                0);

        Avatar.create(
                "Colin Creevey",
                "Harry-Potter-Fanboy of the first hour.\\nBeware, he is quick on his feet.",
                "colin_creevey",
                "assets/",
                0,
                0,
                2,
                0,
                0);

        Avatar.create(
                "Crabbe and Goyle",
                "Dracos henchmen. Known for \\ntheir strength. They always stay \\nat each others side.",
                "crabbe_and_goyle",
                "assets/",
                true,
                0,
                1,
                0,
                1,
                0);

        Avatar.create(
                "Dolores Umbridge",
                "Little women in pink.\\nJust do not underestimate her,\\nshe is really robust.",
                "dolores_umbridge",
                "assets/",
                0,
                1,
                1,
                0,
                0);

        Avatar.create(
                "Draco Malfoy",
                "Not the nicest guy around.\\nQuite a big mouth and if push\\ncomes to shove, he is off in no time.",
                "draco_malfoy",
                "assets/",
                0,
                0,
                3,
                0,
                0);

        Avatar.create(
                "Filius Flitwick",
                "Mr. „Swish and Flick“ himself!\\nThe Charms-Master is not\\n the toughest of the bunch,\\nbut he has unmatched agility.",
                "filius_flitwick",
                "assets/",
                -2,
                0,
                4,
                2,
                0);

        Avatar.create(
                "Fleur Delacour",
                "The Beauty and a Beast, when it comes \\nto Agility. She really shows, why \\nshe is the Beauxbatons Champion",
                "fleur_delacour",
                "assets/",
                0,
                0,
                1,
                2,
                0);

        Avatar.create(
                "Fred and George",
                "Inseparable,\\nthe two Weasleys are really though.\\nTogether they are unstoppable.",
                "fred_and_george",
                "assets/",
                true,
                4,
                0,
                0,
                0,
                0);

        Avatar.create(
                "Fred and George Uniform",
                "Uniform seam to make one weak.\\nYeah maybe,but those two\\nare still some formidable runners.",
                "fred_and_george_2",
                "assets/",
                true,
                1,
                1,
                0,
                0,
                0);

        Avatar.create(
                "Gilderoy Lockhart",
                "Simply the greatest Wizard of all time,\\nknown from all his adventures,\\nso this one is right up his alley.",
                "gilderoy_lockhart",
                "assets/",
                0,
                0,
                0,
                0,
                0);

        Avatar.create(
                "Ginny Weasley",
                "Lovely little Ginny,\\nthe youngest of the family,\\nbut still a Weasley,\\nso watch out.",
                "ginny_weasley",
                "assets/",
                1,
                0,
                1,
                1,
                0);

        Avatar.create(
                "Harry Potter",
                "description",
                "harry_potter",
                "assets/",
                0,
                0,
                0,
                1,
                0);

        Avatar.create(
                "Harry Potter 2",
                "The boy who lived. This is a upgraded \\nversion of the previous one. Let \\nus see how the parsel-mouth \\nwill do in the dungeon.",
                "harry__potter",
                "assets/",
                0,
                1,
                1,
                1,
                0);

        Avatar.create(
                "Hermione Granger",
                "description",
                "hermione_granger",
                "assets/",
                0,
                0,
                1,
                0,
                0);

        Avatar.create(
                "Hermione Granger 2",
                "The smartest girl you will ever see,\\nunfortunately intelligence \\nis not one of the attributes, \\nbut she is still awesome.",
                "hermione__granger",
                "assets/",
                1,
                1,
                0,
                1,
                0);

        Avatar.create(
                "Horace Slughorn",
                "The Oldtimer. Still got some tricks\\nup his sleeves. Know to be slightly \\ncoward, he got great Speed.",
                "horace_slughorn",
                "assets/",
                1,
                0,
                2,
                1,
                0);

        Avatar.create(
                "Kingsley Shacklebolt",
                "The famous Auror, is a great fighter.\\nLet us hope that is enough.",
                "kingsley_shacklebolt",
                "assets/",
                1,
                1,
                0,
                0,
                0);

        Avatar.create(
                "Lavender Brown",
                "Mrs. Trelwaney's favourite student.\\nBrave and loyal. A typical Gryffindor.",
                "lavender_brown",
                "assets/",
                1,
                0,
                1,
                0,
                0);

        Avatar.create(
                "Lucius Malfoy",
                "„Wait till his father hears about this.“\\nLet us see what he has to offer.",
                "lucius_malfoy",
                "assets/",
                2,
                0,
                1,
                0,
                0);

        Avatar.create(
                "Luna Lovegood",
                "The good soul of the house. Yes..., she is \\nweird, but give her a go.",
                "luna_lovegood",
                "assets/",
                1,
                0,
                0,
                1,
                0);

        Avatar.create(
                "Minerva McGonagall",
                "The Professor for Transfiguration,\\nis also an Animagus,\\nshe got the velocity of a cat and\\nmaybe some lives to spare as well.",
                "minerva_mcGonagel",
                "assets/",
                2,
                0,
                0,
                2,
                0);

        Avatar.create(
                "Moaning Myrtle",
                "Myrtle is a ghost.\\nShe likes to get lost in great heights,\\nbut remember, for the purpose of\\nthis game, she is not immortal.",
                "moaning_myrtle2",
                "assets/",
                0,
                0,
                0,
                4,
                0);

        Avatar.create(
                "Molly Weasley",
                "Having 7 children, she knows how to \\ntake care of all kind of injuries.",
                "molly_weasley",
                "assets/",
                3,
                0,
                0,
                0,
                0);

        Avatar.create(
                "Mr. Binns",
                "Mr. Binns is so boring, that all students \\nfall asleep. He often dreams of \\nflying through the clouds.",
                "mr_binns2",
                "assets/",
                0,
                0,
                0,
                3,
                0);

        Avatar.create(
                "Narcissa Malfoy",
                "The pure blood, she is not officially \\nevil, but nice is something different.",
                "narcissa_malfoy",
                "assets/",
                1,
                0,
                2,
                0,
                0);
        
        Avatar.create(
                "Neville Longbottom",
                "I've always hoped that he is\\nthe one from the prophecy.\\nChoose him, he is really good.",
                "neville_longbottom",
                "assets/",
                0,
                1,
                0,
                0,
                0);

        Avatar.create(
                "Nymphadora Tonks",
                "The girl of many colors, she is an Auror\\nwith bad temper, but great tempo.",
                "nymphadora_tonks",
                "assets/",
                0,
                0,
                3,
                1,
                0);

        Avatar.create(
                "Padma and Parvati",
                "The Patil-Twins are the cheapest \\nteam with the best agility.",
                "padma_and_parvati_patil",
                "assets/",
                true,
                0,
                0,
                1,
                1,
                0);

        Avatar.create(
                "Pomona Sprout",
                "Head of the Hufflepuff House.\\nShe really gets the job done,\\nplus she reminds me of my grandma.",
                "pomona_sprout",
                "assets/",
                1,
                0,
                0,
                2,
                0);

        Avatar.create(
                "Remus Lupin",
                "Full moon or not, this Lykanthrop is on \\nthe prowl. No one is as swift as him.",
                "remus_lupin",
                "assets/",
                0,
                0,
                2,
                2,
                0);

        Avatar.create(
                "Ron Weasley",
                "discription",
                "ron_weasley",
                "assets/",
                1,
                0,
                0,
                0,
                0);

        Avatar.create(
                "Ron Weasley 2",
                "The muscle of the gang. Quidditch \\nkeeper and the greatest Chess-player \\nof Hogwarts, you might think.",
                "ron__weasley",
                "assets/",
                2,
                1,
                0,
                0,
                0);

        Avatar.create(
                "Rubeus Hagrid",
                "Due to his giant half,\\nhe is a real juggernaut.\\nNot very fast, but unstoppable,\\nonce he got some momentum.",
                "rubeus_hagrid",
                "assets/",
                5,
                2,
                -2,
                0,
                0);

        Avatar.create(
                "Seamus Finnigan",
                "Seamus is quite an explosive character and \\nhas often been in mortal danger.\\nNobody knows why he is still standing.",
                "seamus_finnigan",
                "assets/",
                5,
                -1,
                0,
                0,
                0);

        Avatar.create(
                "Severus Snape",
                "The half-blood prince \\nand master of potion-making\\nalways has a elixir at hand.",
                "severus_snape",
                "assets/",
                1,
                1,
                1,
                1,
                1);


        Avatar.create(
                "Sirius Black",
                "The only wizzard who escaped azkaban\\non his own with the speed of a hound.\\nHe should be able to escape the \\ndungeon, dangers as well.",
                "sirius_black",
                "assets/",
                3,
                2,
                3,
                -2,
                0);

        Avatar.create(
                "Sybill Trelawney",
                "Sybill is gifted with the power \\nof foresight which gives her a \\nclear advantage in dodging obstacles.",
                "sybill_trelawney",
                "assets/",
                0,
                0,
                2,
                1,
                0);

        Avatar.create(
                "Voldemort",
                "He-Who-Must-Not-Be-Named, the \\nmightiest black magician of all time.\\nHe spread fear and terror.",
                "voldemort",
                "assets/",
                7,
                1,
                1,
                1,
                0);

        Logger.info("Done initializing");
    }
}
