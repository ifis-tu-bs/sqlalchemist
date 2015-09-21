package models;

import com.avaje.ebean.Query;
import com.avaje.ebean.annotation.ConcurrencyMode;
import com.avaje.ebean.annotation.EntityConcurrencyMode;
import com.fasterxml.jackson.databind.node.*;

import helper.Random;
import play.Logger;
import play.Play;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import play.libs.Json;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "profile")
@EntityConcurrencyMode(ConcurrencyMode.NONE)
public class Profile extends Model {
    // unique ID
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @OneToOne
    private User user;

    //
    @Constraints.Required
    @Column(unique = true)
    private String username;

    @Embedded
    private PlayerStats playerStats;

    @Embedded
    public Settings settings;

    private boolean tutorialDone;
    private boolean storyDone;

    @ManyToMany
    private List<ShopItem> shopItems;

    @ManyToOne
    private Avatar avatar;

    @ManyToOne
    private StoryChallenge currentStory;

    @OneToOne
    private Scroll currentScroll;

    private int depth;

    private int coins;

    private float coinScale;
    private int scrollLimit;

    private int totalScore;
    private int playedTime;
    private int playedRuns;
    @Column(name = "totalCoins")
    private int totalCoins;
    private int doneSQL;
    private int solvedSQL;
    private int quote;

    private Date created_at;
    private Date edited_at;

    @Version
    public Date version;

    @Transient
    long ownRank;

    public static final Finder<Long, Profile> find = new Finder<>(Long.class, Profile.class);


//////////////////////////////////////////////////
//  Constructor
//////////////////////////////////////////////////

    /**
     * @param username the username
     */
    private Profile(String username) {
        super();
        this.setUsername(username);
        this.setPlayerStats(PlayerStats.defaultValues);
        this.setSettings(new Settings());

        this.setTutorialDone(false);
        this.setStoryDone(false);

        this.setShopItems(new ArrayList<>());

        List<ShopItem> defaultAvatar = ShopItem.getByPrice(0);

        for (ShopItem shopItem : defaultAvatar) {
            this.buy(shopItem);
        }

        ShopItem shopItem = this.shopItems.get(Random.randomInt(0, 2));
        this.setAvatar(shopItem.getAvatar().getId());

        this.setCurrentStory(null);
        this.setCurrentScroll(null);

        this.setDepth(0);

        this.setCoins(0);

        this.setCoinScale(1f);
        this.setScrollLimit(Play.application().configuration().getInt("Game.ScrollLimit"));
    }

    private void setPlayerStats(PlayerStats defaultValues) {
        this.playerStats = defaultValues;
    }

    private void setUsername(String username) {
        this.username = username;
    }

//////////////////////////////////////////////////
//  Object Methods
//////////////////////////////////////////////////

    @Override
    public void save() {
        this.created_at = new Date();
        super.save();
    }

    @Override
    public void update() {
        this.edited_at = new Date();
        super.update();
    }

//////////////////////////////////////////////////
//  Getter & Setter - Methods
//////////////////////////////////////////////////

    public boolean setAvatar(long id) {
        Avatar avatar_temp = Avatar.getById(id);

        if(avatar_temp == null)
            return false;

        this.avatar = avatar_temp;

        return true;
    }

    private int addCoinsFromScore(int score) {
        int coins = score;

        int collected = LofiCoinFlowLog.getCollectedCoinsSinceYesterday(this);
        collected = coins + collected;

        int coinLimit = Play.application().configuration().getInt("Game.CoinLimit");
        this.coinScale = (1 -  ( (float)collected / ((float)coinLimit + (float)collected)));

        coins = (int) (coins  * this.coinScale);

        this.totalCoins = this.totalCoins + coins;
        this.coins = this.coins + coins;
        if(this.coins >= 1000000) {
            this.coins = 999999;
        }
        LofiCoinFlowLog.add(this, coins);

        return coins;
    }

    public boolean isTutorialDone() {
        return tutorialDone;
    }

    public void setTutorialDone(boolean b) {
        this.tutorialDone = b;
    }

    public boolean isStoryDone() {
        return this.storyDone;
    }

    public void setCurrentStory(StoryChallenge currentStory) {
        this.currentStory = currentStory;
    }

    public StoryChallenge getCurrentStory() {
        return currentStory;
    }

    public PlayerStats getPlayerStats() {
        PlayerStats playerStats_sum = new PlayerStats();

        playerStats_sum.add(this.playerStats);
        playerStats_sum.add(this.avatar.getPlayerStats());

        List<Scroll> scrollList = ScrollCollection.getActiveScrolls(this);

        for(Scroll scroll : scrollList) {
            playerStats_sum.add(scroll.getPlayerStats());
        }

        for(ShopItem shopItem : this.shopItems) {
            if(shopItem.isBeltSlot()) {
                playerStats_sum.addBeltSlot();
            }
        }

        if(this.isStoryDone()) {
            playerStats_sum.add(new PlayerStats(0,0,0,0,1));
        }

        return playerStats_sum;
    }

    public boolean shopItemInList(ShopItem shopItem) {
        return this.shopItems.contains(shopItem);
    }

    public User getUser(){
        return this.user;
    }

    public String getUsername() {
        return this.username;
    }

    public int  addScore(int score) {
        this.totalScore = this.totalScore + score;
        return this.addCoinsFromScore(score);
    }

    public void addStatement() {
        this.doneSQL++;
        this.setQuote((int) (((float) this.solvedSQL / (float) this.doneSQL) * 100));
    }


    public void addRun() {
        this.playedRuns++;
    }

    public void addSuccessfully() {
        this.solvedSQL++;
        this.setQuote((int) (((float) this.solvedSQL / (float) this.doneSQL) * 100));
        if (this.solvedSQL > Play.application().configuration().getInt("User.AutoPromote")) {
            User user = this.getUser();
            if (user.getRole() < User.ROLE_CREATOR) {
                user.promote(User.ROLE_CREATOR);
            }
        }
    }

    public boolean buy(ShopItem shopItem) {
        if(this.coins >= shopItem.getPrice()){
            this.coins = this.coins - shopItem.getPrice();
            this.shopItems.add(shopItem);
            this.save();
            return true;
        } else {
            return false;
        }
    }

    public long getId() {
        return this.id;
    }

    public void setCurrentScroll(Scroll currentScroll) {
        this.currentScroll = currentScroll;
    }

    public void addCurrentScroll() {
        Scroll scroll = this.currentScroll;
        if(scroll.isRecipe()) {
            Inventory.create(this, scroll.getPotion());
        }  else {
            ScrollCollection.setActive(this, scroll);
        }

    }

    public void setDepth(int depth) {
        if(depth > this.depth) {
            this.depth = depth;
        }
    }

    public void addScroll(Scroll scroll) {
        this.scrollLimit = ScrollCollection.getLimit(this);
        if(this.scrollLimit > 0) {
            ScrollCollection.add(this, scroll);
            this.scrollLimit = ScrollCollection.getLimit(this);
        }
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void resetStory() {
        this.setCurrentStory(null);
        this.setCurrentScroll(null);
        this.setTutorialDone(false);
        ScrollCollection.reset(this);
        Inventory.reset(this);

        this.setDepthReset(0);
    }

    public void addTime(int time) {
        this.playedTime = this.playedTime + time;
    }

//////////////////////////////////////////////////
//  JSON
//////////////////////////////////////////////////

    public ObjectNode toJson() {
        ObjectNode node = Json.newObject();

        node.put("id",          this.id);


        node.put("created_at", String.valueOf(this.created_at));
        node.put("last_action", String.valueOf(this.edited_at));

        return node;
    }

    public ObjectNode toJsonProfile() {
        ObjectNode node = Json.newObject();

        node.put("id",          this.id);
        node.put("username",    this.username);
        node.put("coins",       this.coins);
        node.put("score",       this.totalScore);
        node.put("highScore",   this.toJsonHighScore());
        node.put("avatar",      this.avatar.toJson());

        return node;
    }

    /**
     *
     * @return  returns PlayerState as JSON
     */
    public ObjectNode toJsonPlayerState() {
        ObjectNode node = Json.newObject();

        node.put("id",          this.id);
        node.put("username",    this.username);
        node.put("settings",    this.settings.getSettings());
        node.put("student",     this.user.isStudent());
        node.put("storyDone",   this.storyDone);
        node.put("coins",       this.coins);
        node.put("coinScale",   this.coinScale);


        return node;
    }

    public ObjectNode toJsonCharacterState() {
        ObjectNode node = Json.newObject();
        PlayerStats playerStats_sum = this.getPlayerStats();

        node.put("attributes",      playerStats_sum.toJson());
        node.put("currentAvatar",   this.avatar.toJson());
        node.put("avatars_bought",  this.toJsonBoughtAvatars());
        node.put("scrollLimit",     this.scrollLimit);
        node.put("maxDepth",        this.depth);
        node.put("inventory",       Inventory.getJson_Inventory(this));
        node.put("belt",            Inventory.getJson_Belt(this));
        node.put("scrollCollection",ScrollCollection.toJsonAll(this));

        return node;
    }

    public ObjectNode toJsonHighScore() {
        ObjectNode highScoreJson = Json.newObject();

        highScoreJson.put("id",             this.id);
        highScoreJson.put("username",       this.username);
        highScoreJson.put("totalCoins",     this.totalCoins);
        highScoreJson.put("totalScore",     this.totalScore);
        highScoreJson.put("playedTime",     this.playedTime);
        highScoreJson.put("playedRuns",     this.playedRuns);
        highScoreJson.put("solvedSQL",      this.solvedSQL);
        highScoreJson.put("quote",          this.quote);

        return highScoreJson;
    }

    public ArrayNode toJsonBoughtAvatars(){
        ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();

        for(ShopItem shopItem : this.shopItems) {
            if(shopItem.isAvatar()) {
                arrayNode.add(shopItem.getAvatar().toJson());
            }
        }

        return arrayNode;
    }

    public static ObjectNode toJsonHighScoreAll(List<Profile> profiles, Profile profile) {
        ArrayNode newArray = JsonNodeFactory.instance.arrayNode();

        for (Profile profileI : profiles) {
            newArray.add(profileI.toJsonHighScore());
        }

        ObjectNode node = Json.newObject();

        node.put("highScore",   newArray);
        node.put("ownRank",     profile.ownRank + 1);
	node.put("own",		profile.toJsonHighScore());
        return node;
    }

//////////////////////////////////////////////////
//  Class Methods
//////////////////////////////////////////////////

    /**
     *
     * @param username asd
     * @return asd
     */
    public static Profile create(String username) {

        Profile profile = new Profile(username);

        try {
            profile.save();

            return profile;
        } catch (PersistenceException e) {
            Logger.warn("Profile.create UsernameTaken: " + e.getMessage());
            return null;
        }
    }

//////////////////////////////////////////////////
//  Ranking Methods
//////////////////////////////////////////////////

    public static List<Profile> sortByScore(Profile profile) {
        List<Profile> rankingScore;

        Query<Profile> orderBy = find.orderBy("totalScore desc");
        profile.ownRank = orderBy.findList().indexOf(profile);
        rankingScore = orderBy.setMaxRows(10).findList();

        return rankingScore;
    }

    public static List<Profile> sortByTime(Profile profile) {
        List<Profile> rankingTime;
        Query<Profile> orderBy = find.orderBy("playedTime desc");
        profile.ownRank = orderBy.findList().indexOf(profile);
        rankingTime = orderBy.setMaxRows(10).findList();


        return rankingTime;
    }

    public static List<Profile> sortByRuns(Profile profile) {
        List<Profile> rankingRuns;
        Query<Profile> orderBy = find.orderBy("playedRuns desc");
        profile.ownRank = orderBy.findList().indexOf(profile);
        rankingRuns = orderBy.setMaxRows(10).findList();

        return rankingRuns;
    }

    public static List<Profile> sortBySQL(Profile profile) {
        List<Profile> rankingSQL;
        Query<Profile> orderBy = find.orderBy("solvedSQL desc");
        profile.ownRank = orderBy.findList().indexOf(profile);
        rankingSQL = orderBy.setMaxRows(10).findList();
        return rankingSQL;
    }

    public static List<Profile> sortByRate(Profile profile) {
        List<Profile> rankingRate;
        Query<Profile> orderBy = find.orderBy("quote desc");
        profile.ownRank = orderBy.findList().indexOf(profile);
        rankingRate = orderBy.setMaxRows(10).findList();
        return rankingRate;
    }

    public static List<Profile> sortByCoins(Profile profile) {
        List<Profile> rankingRate;
        Query<Profile> orderBy = find.orderBy("totalCoins desc");
        profile.ownRank = orderBy.findList().indexOf(profile);
        rankingRate = orderBy.setMaxRows(10).findList();
        return rankingRate;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public void setStoryDone(boolean storyDone) {
        this.storyDone = storyDone;
    }

    public void setShopItems(ArrayList<ShopItem> shopItems) {
        this.shopItems = shopItems;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public void setCoinScale(float coinScale) {
        this.coinScale = coinScale;
    }

    public void setScrollLimit(Integer scrollLimit) {
        this.scrollLimit = scrollLimit;
    }

    public void setDepthReset(int depthReset) {
        this.depth = depthReset;
    }

    public void setQuote(int quote) {
        this.quote = quote;
    }
}
