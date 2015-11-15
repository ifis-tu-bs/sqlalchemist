package models;

import com.avaje.ebean.annotation.ConcurrencyMode;
import com.avaje.ebean.annotation.EntityConcurrencyMode;

import com.avaje.ebean.Model;

import javax.persistence.*;

/**
 * @author fabiomazzone
 */
@Entity
@Table(name = "shopitem")
@EntityConcurrencyMode(ConcurrencyMode.NONE)
public class ShopItem extends Model{
    // unique ID
    @Id
    private long id;

    public static final int TYPE_AVATAR = 0;
    public static final int TYPE_BELT = 1;
    private final int type;

    private final String title;

    private final String description;

    private final String thumbnailUrl;

    private final int price;

    @OneToOne
    private Avatar avatar;

    public static final Model.Finder<Long, ShopItem> find = new Model.Finder<>(ShopItem.class);

//////////////////////////////////////////////////
//  Constructor
//////////////////////////////////////////////////

    /**
     *
     * @param name          title of the item
     * @param thumbnailUrl  url of the thumbnail
     * @param price         price of the item
     * @param avatar        optional avatar
     */
    public ShopItem(String name, String description, String thumbnailUrl, int type, Avatar avatar, int price) {

        this.title = name;
        this.description = description;
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

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
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
