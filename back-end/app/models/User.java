package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import dao.*;
import helper.MailSender;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.databind.node.ObjectNode;

import helper.Random;
import org.mindrot.jbcrypt.BCrypt;

import play.libs.Json;
import view.ScoreView;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

/**
 *
 *
 * @version 0.5
 * @author Fabio Mazzone
 */
@Entity
@Table(name = "user")
public class User extends Model {
    @Id
    private long                        id;
    @Column(unique = true)
    private String                      email;
    @Column(unique = true)
    private String                      username;

    private String                      password;

    @NotNull
    @ManyToOne
    private Role                        role;

    @Column(unique = true, name = "email_verify_code")
    private String                      emailVerifyCode;
    @Column(unique = true)
    private String                      matNR;
    @Column(unique = true)
    private String                      yID;

    @Embedded
    private Settings                    settings;

    @Embedded
    private PlayerStats                 playerStats;

    private boolean                     tutorialDone;
    private boolean                     storyDone;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<ShopItem>              shopItems;
    @ManyToOne
    private Avatar                      avatar;


    @ManyToOne
    private StoryChallenge              currentStory;
    @ManyToOne
    private Scroll                      currentScroll;
    @ManyToOne
    private TaskSet                     currentTaskSet;
    @ManyToOne
    private HomeWork                    currentHomeWork;

    private int                         depth;
    private int                         coins;

    private float                       coinScale;

    @Embedded
    private Score                       score;

    @OneToMany(cascade = CascadeType.ALL)
    private List<SubmittedHomeWork>     submittedHomeWorks;
    @OneToMany(mappedBy = "creator")
    private List<TaskSet>               tasks;
    @OneToMany(mappedBy = "user")
    private List<Rating>                ratings;
    @OneToMany(mappedBy = "creator")
    private List<Comment>               comments;

    private boolean                     isActive;

    @ManyToOne
    private UserGroup group;

    private Calendar createdAt;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//  Constructor
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     *
     * @param email     email
     * @param password  password
     */
    public User(
            String  email,
            String  username,
            String  password,
            Role    role) {

        this.email = email;
        this.username = username;
        setPassword(password);

        setRole(role);

        emailVerifyCode = UUID.randomUUID().toString();

        setSettings(new Settings(true, true));
        setPlayerStats(PlayerStats.getDefault());

        this.setTutorialDone(false);
        this.setStoryDone(false);

        this.shopItems = new ArrayList<>();
        List<ShopItem> defaultAvatar = ShopItemDAO.getFreeShopItems();
        defaultAvatar.forEach(this::addShopItem);

        ShopItem shopItem = this.shopItems.get(Random.randomInt(2));
        this.setAvatar(shopItem.getAvatar());

        this.currentStory   = StoryChallengeDAO.getFirstLevel();
        this.currentScroll  = null;
        this.currentTaskSet = null;
        this.currentHomeWork= null;

        this.setDepth(0);
        this.setCoins(0);

        this.setCoinScale(1f);

        this.score          = new Score();

        isActive            = true;
        createdAt = Calendar.getInstance();
    }


    public ObjectNode toJsonProfile() {
        ObjectNode node = Json.newObject();

        node.put("id",          this.id);
        node.put("username",    this.username);
        node.put("coins",       this.coins);
        node.put("score",       this.getScore().getTotalScore());
        node.set("highScore",   ScoreView.toJson(this));
        node.set("avatar",      Json.toJson(this.avatar));

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
        node.set("settings",    Json.toJson(this.settings));
        node.put("student",     this.isStudent());
        node.put("storyDone",   this.storyDone);
        node.put("coins",       this.coins);
        node.put("coinScale",   this.coinScale);


        return node;
    }

    public ObjectNode toJsonUser() {
        ObjectNode node = Json.newObject();

        node.put("id",          this.id);
        node.put("username",    this.username);
        if(this.matNR != null) {
            node.put("matno",       this.matNR);
        } else {
            node.put("matno",       "");
        }
        node.put("role",    "User");


        node.put("email",       this.email);


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
        node.put("createdAt",   simpleDateFormat.format(this.createdAt.getTime()));

        return node;
    }

    public ObjectNode toJsonShort () {
        ObjectNode node = Json.newObject();

        node.put("id",          this.id);

        node.put("role",        this.getUserRoleString());

        node.put("email",       this.email);

        return node;
    }

    private String getUserRoleString() {
        String userRole = (role != null) ? role.getRoleName() : "???";


        if(this.isStudent())
            userRole += " & Student";

        return userRole;
    }

    public ObjectNode toJsonCharacterState() {
        ObjectNode node = Json.newObject();
        PlayerStats playerStats_sum = this.getPlayerStats();

        node.set("attributes",      Json.toJson(playerStats_sum));
        node.set("currentAvatar",   Json.toJson(this.avatar));
        node.set("avatars_bought",  this.toJsonBoughtAvatars());
        node.put("scrollLimit",     getScrollLimit());
        node.put("maxDepth",        getDepth());
        node.set("inventory",       InventoryDAO.getJson_Inventory(this));
        node.set("belt",            InventoryDAO.getJson_Belt(this));
        node.set("scrollCollection",ScrollCollection.toJsonAll(this));

        return node;
    }

    public ArrayNode toJsonBoughtAvatars(){
        ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();

        this.shopItems.stream().filter(ShopItem::isTypeAvatar).forEach(shopItem -> arrayNode.add(Json.toJson(shopItem.getAvatar())));

        return arrayNode;
    }

//////////////////////////////////////////////////
//  Actions Methods
//////////////////////////////////////////////////

    public void promote(int role) {
    //    this.role = role;
        this.save();
        MailSender.getInstance().sendPromoteMail(this);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//  Getter & Setter
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public long getId() {
        return id;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *  Change Password
     *
     * @param oldPassword current User Password
     * @param newPassword new User Password
     * @return  returns true if successful
     */
    public boolean setPassword(String oldPassword, String newPassword) {
        if(BCrypt.checkpw(oldPassword, this.password)) {
            this.setPassword(newPassword);
            return true;
        }
        return false;
    }

    /**
     * Set Password)
     * @param newPassword    asd
     */
    public void setPassword(String newPassword) {
        this.password = BCrypt.hashpw(newPassword, BCrypt.gensalt());
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonIgnore
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @JsonProperty("roleID")
    public long getRoleID() {
        return this.role.getId();
    }

    public void setRoleID(long id) {
        Role role = RoleDAO.getById(id);
        this.setRole(role);
    }

    public String getRoleName() {
        return this.role.getRoleName();
    }

    @JsonIgnore
    public String getEmailVerifyCode() {
        return this.emailVerifyCode;
    }

    public boolean isEmailVerified() {
        return this.emailVerifyCode == null;
    }

    public void setEmailVerified() {
        this.emailVerifyCode = null;
    }

    @JsonIgnore
    public String getMatNR() {
        return matNR;
    }

    public void setMatNR(String matNR) {
        this.matNR = matNR;
    }

    @JsonIgnore
    public String getYID() {
        return yID;
    }

    public void setYID(String yID) {
        this.yID = yID;
    }

    @JsonProperty("student")
    public boolean isStudent() {
        return (this.yID != null || this.matNR != null);
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public PlayerStats getPlayerStats() {
        PlayerStats playerStats_sum = new PlayerStats();

        playerStats_sum.add(this.playerStats);
        playerStats_sum.add(this.avatar.getAttributes());

        List<Scroll> scrollList = ScrollCollectionDAO.getActiveScrolls(this);

        for(Scroll scroll : scrollList) {
            playerStats_sum.add(scroll.getPlayerStats());
        }

        this.shopItems.stream().filter(ShopItem::isTypeBeltSlot).forEach(shopItem -> playerStats_sum.addBeltSlot());

        if(this.isStoryDone()) {
            playerStats_sum.add(new PlayerStats(0,0,0,0,1));
        }
        return playerStats_sum;
    }

    public void setPlayerStats(PlayerStats playerStats) {
        this.playerStats = playerStats;
    }

    public boolean isStoryDone() {
        return storyDone;
    }

    public void setStoryDone(boolean storyDone) {
        this.storyDone = storyDone;
    }

    public boolean isTutorialDone() {
        return tutorialDone;
    }

    public void setTutorialDone(boolean tutorialDone) {
        this.tutorialDone = tutorialDone;
    }

    public void addShopItem(ShopItem item) {
        shopItems.add(item);
    }

    @JsonIgnore
    public List<ShopItem> getShopItems() {
        return shopItems;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public boolean setAvatar(Avatar avatar) {
        boolean contains = false;
        for(ShopItem shopItem : this.getShopItems()) {
            if(shopItem.getAvatar().getId() == avatar.getId()) {
                this.avatar = avatar;
                contains = true;
                break;
            }
        }

        return contains;
    }

    public Scroll getCurrentScroll() {
        return currentScroll;
    }

    public void setCurrentScroll(Scroll currentScroll) {
        this.currentScroll = currentScroll;
    }
    @JsonIgnore
    public StoryChallenge getCurrentStory() {
        return currentStory;
    }

    public void setCurrentStory(StoryChallenge currentStory) {
        this.currentStory = currentStory;
    }

    @JsonProperty("currentStory")
    public String getCurrentStoryName() {
        return (currentStory != null) ? currentStory.getName() : "";
    }

    @JsonIgnore
    public TaskSet getCurrentTaskSet() {
        return currentTaskSet;
    }

    public void setCurrentTaskSet(TaskSet currentTaskSet) {
        this.currentTaskSet = currentTaskSet;
    }

    @JsonIgnore
    public HomeWork getCurrentHomeWork() {
        return currentHomeWork;
    }

    public void setCurrentHomeWork(HomeWork currentHomeWork) {
        this.currentHomeWork = currentHomeWork;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public float getCoinScale() {
        return coinScale;
    }

    public void setCoinScale(float coinScale) {
        this.coinScale = coinScale;
    }

    public int getScrollLimit() {
        return ScrollCollectionDAO.getLimit(this);
    }

    public Score getScore() {
        return score;
    }

    public void setScore(Score score) {
        this.score = score;
    }

    public boolean isActive() {
        return isActive;
    }

    public void isActive(boolean active) {
        isActive = active;
    }

    public UserGroup getGroup() {
        return group;
    }

    public void setGroup(UserGroup group) {
        this.group = group;
    }

    public Calendar getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Calendar createdAt) {
        this.createdAt = createdAt;
    }
}
