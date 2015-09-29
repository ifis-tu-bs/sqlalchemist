package models;


import com.avaje.ebean.annotation.ConcurrencyMode;
import com.avaje.ebean.annotation.EntityConcurrencyMode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import dao.AvatarDAO;

import play.db.ebean.Model;
import play.libs.Json;

import play.Logger;

import javax.persistence.*;
import java.util.List;

/**
 * @author Stefan Hanisch
 */
@Entity
@Table(name = "ShopItem")
@EntityConcurrencyMode(ConcurrencyMode.NONE)
public class ShopItem extends Model{
    // unique ID
    @Id
    private long id;

    public static final int TYPE_AVATAR = 0;
    public static final int TYPE_BELT = 1;
    @Column(name = "type")
    int type;

    @Column(name = "ShopItemName")
    private String name;

    @Column(name ="ShopItem_desc")
    private String desc;

    private String thumbnailUrl;

    @Column(name = "price")
    private int price;

    @OneToOne
    private Avatar avatar;

    public static final Model.Finder<Long, ShopItem> find = new Model.Finder<>(
            Long.class, ShopItem.class);

//////////////////////////////////////////////////
//  Constructor
//////////////////////////////////////////////////

    /**
     *
     * @param name          name of the item
     * @param thumbnailUrl  url of the thumbnail
     * @param price         price of the item
     * @param avatar        optional avatar
     */
    protected ShopItem(String name, String desc, String thumbnailUrl, int type, Avatar avatar, int price) {

        this.name           = name;
        this.desc           = desc;
        this.thumbnailUrl   = thumbnailUrl;
        this.type           = type;
        this.price          = price;
        if(type == TYPE_AVATAR) {
            this.avatar         = avatar;
        }

    }

//////////////////////////////////////////////////
//  Json Method
//////////////////////////////////////////////////

    /**
     *
     * @return returns the ShopItem-Object as a Json model
     */
    public ObjectNode toJson(Profile profile) {
        ObjectNode node = Json.newObject();

        node.put("id",              this.id);
        node.put("name",            this.name);
        node.put("desc",            this.desc);
        node.put("type",            this.type);
        node.put("thumbnailUrl",    this.thumbnailUrl);
        node.put("price",           this.price);
        if(this.type == TYPE_AVATAR) {
            node.put("avatar",          this.avatar.toJson());
        }
        node.put("bought",          profile.shopItemInList(this));

        return node;
    }

    public static ArrayNode toJsonAll(Profile profile, List<ShopItem> shopItems) {
        ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();

        for(ShopItem shopItem : shopItems) {

            arrayNode.add(shopItem.toJson(profile));
        }

        return arrayNode;
    }

//////////////////////////////////////////////////
//  Getter & Setter - methods
//////////////////////////////////////////////////

    /**
     *
     * @return returns the price of the shopItem
     */
    public Integer getPrice() {
        return price;
    }

    /**
     *
     * @return returns true if the shopItem is a Avatar
     */
    public boolean isAvatar() {
        return this.type == TYPE_AVATAR;
    }

    /**
     *
     * @return returns the Avatar
     */
    public Avatar getAvatar() {
        return this.avatar;
    }

    public boolean isBeltSlot() {
        return this.type == TYPE_BELT;
    }

    public long getId() {return this.id; }

//////////////////////////////////////////////////
//  Create Method
//////////////////////////////////////////////////

    /**
     *
     * @param avatar        avatar
     * @param price         price
     * @return  returns the ShopItem
     */
    public static ShopItem create(Avatar avatar, int price) {

        return create(avatar.getName(), avatar.getDesc(), avatar.getAvatar(), TYPE_AVATAR, avatar, price);
    }

    /**
     *
     * @param name          name for the item
     * @param thumbnailUrl  thumbnailUrl
     * @param price         price
     * @return  returns the ShopItem
     */
    public static ShopItem create(String name, String thumbnailUrl, int price) {
        return create(name, null, thumbnailUrl, TYPE_BELT, null, price);
    }

    /**
     *
     * @param name          name
     * @param thumbnailUrl  thumbnailUrl
     * @param type          type
     * @param avatar        avatar
     * @param price         price
     * @return  returns the ShopItem
     */
    public static ShopItem create(String name, String desc, String thumbnailUrl, int type, Avatar avatar, int price) {
        if( name == null || thumbnailUrl == null) {
            return null;
        }
        ShopItem shopItem = new ShopItem(name, desc, thumbnailUrl, type, avatar, price);

        try {
            shopItem.save();
        } catch(PersistenceException pe) {
            Logger.warn("ShopItem.create - caught PersistenceException: " + pe.getMessage());
            ShopItem shopItem_comp = getByName(name);
            if(shopItem_comp != null) {
                Logger.warn("ShopItem.create - found alternative \"ShopItem\"");
                return shopItem_comp;
            }
            Logger.error("ShopItem.create - Can't create ShopItem and didn't found aquivalent Object");
            return null;
        }
        return shopItem;
    }

//////////////////////////////////////////////////
//  Object Getter Methods
//////////////////////////////////////////////////

    /**
     *
     * @param id id of the shopItem
     * @return  returns the shopItem
     */
    public static ShopItem getById(long id) {
        return find.byId(id);
    }

    public static ShopItem getByName(String name) {
        ShopItem shopItem;
        if ((shopItem = find.where().eq("name", name).findUnique()) == null) {
            Logger.warn("ShopItem.getByName(" + name + ") - No ShopItem found !");
            return null;
        }
        return shopItem;
    }

    public static List<ShopItem> getByPrice(int price) {
        List<ShopItem> shopItems = find.where().eq("price", price).findList();

        return shopItems;
    }

    public static List<ShopItem> getAvatarList() {
        return find.where().eq("type", TYPE_AVATAR).ne("price", 0).orderBy("price asc").findList();
    }

    public static List<ShopItem> getBeltList() {
        return find.where().eq("type", TYPE_BELT).orderBy("price asc").findList();
    }

//////////////////////////////////////////////////
//  Init Method
//////////////////////////////////////////////////

    /**
     *n Init method
     */
    public static void init() {
        Logger.info("Initialize 'ShopItem' data");


        ShopItem.create(AvatarDAO.getByName("Alastor Moody"), 8000);
        ShopItem.create(AvatarDAO.getByName("Albus Dumbledore"), 20000);
        ShopItem.create(AvatarDAO.getByName("Arthur Weasley"), 2000);
        ShopItem.create(AvatarDAO.getByName("Cho Chang"), 500);
        ShopItem.create(AvatarDAO.getByName("Colin Creevey"), 500);
        ShopItem.create(AvatarDAO.getByName("Crabbe and Goyle"), 2000);
        ShopItem.create(AvatarDAO.getByName("Dolores Umbridge"), 2000);
        ShopItem.create(AvatarDAO.getByName("Draco Malfoy"), 2000);
        ShopItem.create(AvatarDAO.getByName("Filius Flitwick"), 8000);
        ShopItem.create(AvatarDAO.getByName("Fleur Delacour"), 2000);
        ShopItem.create(AvatarDAO.getByName("Fred and George"), 8000);
        ShopItem.create(AvatarDAO.getByName("Fred and George Uniform"), 2000);
        ShopItem.create(AvatarDAO.getByName("Gilderoy Lockhart"), 20000);
        ShopItem.create(AvatarDAO.getByName("Ginny Weasley"), 2000);
        ShopItem.create(AvatarDAO.getByName("Harry Potter"), 0); //Bereits im Besitz des Benutzers
        ShopItem.create(AvatarDAO.getByName("Harry Potter 2"), 8000);
        ShopItem.create(AvatarDAO.getByName("Hermione Granger"), 0); //Bereits im Besitz des Benutzers
        ShopItem.create(AvatarDAO.getByName("Hermione Granger 2"), 8000);
        ShopItem.create(AvatarDAO.getByName("Horace Slughorn"), 8000);
        ShopItem.create(AvatarDAO.getByName("Kingsley Shacklebolt"), 2000);
        ShopItem.create(AvatarDAO.getByName("Lavender Brown"), 500);
        ShopItem.create(AvatarDAO.getByName("Lucius Malfoy"), 2000);
        ShopItem.create(AvatarDAO.getByName("Luna Lovegood"), 500);
        ShopItem.create(AvatarDAO.getByName("Minerva McGonagall"), 8000);
        ShopItem.create(AvatarDAO.getByName("Moaning Myrtle"), 8000);
        ShopItem.create(AvatarDAO.getByName("Molly Weasley"), 2000);
        ShopItem.create(AvatarDAO.getByName("Mr. Binns"), 2000);
        ShopItem.create(AvatarDAO.getByName("Narcissa Malfoy"), 2000);
        ShopItem.create(AvatarDAO.getByName("Neville Longbottom"), 500);
        ShopItem.create(AvatarDAO.getByName("Nymphadora Tonks"), 8000);
        ShopItem.create(AvatarDAO.getByName("Padma and Parvati"), 500);
        ShopItem.create(AvatarDAO.getByName("Pomona Sprout"), 2000);
        ShopItem.create(AvatarDAO.getByName("Remus Lupin"), 8000);
        ShopItem.create(AvatarDAO.getByName("Ron Weasley"), 0); //Bereits im Besitz des Benutzers
        ShopItem.create(AvatarDAO.getByName("Ron Weasley 2"), 8000);
        ShopItem.create(AvatarDAO.getByName("Rubeus Hagrid"), 20000);
        ShopItem.create(AvatarDAO.getByName("Seamus Finnigan"), 500);
        ShopItem.create(AvatarDAO.getByName("Severus Snape"), 20000);
        ShopItem.create(AvatarDAO.getByName("Sirius Black"), 20000);
        ShopItem.create(AvatarDAO.getByName("Sybill Trelawney"), 2000);
        ShopItem.create(AvatarDAO.getByName("Voldemort"), 20000);

        ShopItem.create("Belt Slot 1", "url",  1000);
        ShopItem.create("Belt Slot 2", "url",  2000);
        ShopItem.create("Belt Slot 3", "url",  4000);
        ShopItem.create("Belt Slot 4", "url",  8000);
        ShopItem.create("Belt Slot 5", "url", 16000);

        Logger.info("Done initializing");
    }
}
