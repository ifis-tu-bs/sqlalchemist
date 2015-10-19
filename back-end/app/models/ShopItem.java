package models;

import com.avaje.ebean.annotation.ConcurrencyMode;
import com.avaje.ebean.annotation.EntityConcurrencyMode;

import com.avaje.ebean.Model;

import javax.persistence.*;

/**
 * @author fabiomazzone
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
    private final int type;

    @Column(name = "ShopItemName")
    private final String name;

    @Column(name ="ShopItem_desc")
    private final String desc;

    private final String thumbnailUrl;

    @Column(name = "price")
    private final int price;

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
//  Getter & Setter - methods
//////////////////////////////////////////////////

    public long getId() {
    return this.id;
  }

    public int getType() {
        return type;
    }

    /**
     *
     * @return returns true if the shopItem is a Avatar
     */
    public boolean isTypeAvatar() {
        return this.type == TYPE_AVATAR;
    }

    public boolean isTypeBeltSlot() {
        return this.type == TYPE_BELT;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
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
     * @return returns the Avatar
     */
    public Avatar getAvatar() {
        return this.avatar;
    }

}
