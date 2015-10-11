package models;

import com.avaje.ebean.annotation.ConcurrencyMode;
import com.avaje.ebean.annotation.EntityConcurrencyMode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.db.ebean.Model;
import play.libs.Json;

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
    private int type;

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
    public ShopItem(String name, String desc, String thumbnailUrl, int type, Avatar avatar, int price) {

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

  public long getId() {
    return this.id;
  }
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
}
