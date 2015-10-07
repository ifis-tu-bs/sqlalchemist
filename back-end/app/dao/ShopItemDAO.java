package dao;

import models.Avatar;
import models.ShopItem;

import play.Logger;

import javax.persistence.PersistenceException;
import java.util.List;

public class ShopItemDAO {
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

          return create(avatar.getName(), avatar.getDesc(), avatar.getAvatar(), ShopItem.TYPE_AVATAR, avatar, price);
      }

      /**
       *
       * @param name          name for the item
       * @param thumbnailUrl  thumbnailUrl
       * @param price         price
       * @return  returns the ShopItem
       */
      public static ShopItem create(String name, String thumbnailUrl, int price) {
          return create(name, null, thumbnailUrl, ShopItem.TYPE_BELT, null, price);
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
          return ShopItem.find.byId(id);
      }

      public static ShopItem getByName(String name) {
          ShopItem shopItem;
          if ((shopItem = ShopItem.find.where().eq("name", name).findUnique()) == null) {
              Logger.warn("ShopItem.getByName(" + name + ") - No ShopItem found !");
              return null;
          }
          return shopItem;
      }

      public static List<ShopItem> getByPrice(int price) {
          List<ShopItem> shopItems = ShopItem.find.where().eq("price", price).findList();

          return shopItems;
      }

      public static List<ShopItem> getAvatarList() {
          return ShopItem.find.where().eq("type", ShopItem.TYPE_AVATAR).ne("price", 0).orderBy("price asc").findList();
      }

      public static List<ShopItem> getBeltList() {
          return ShopItem.find.where().eq("type", ShopItem.TYPE_BELT).orderBy("price asc").findList();
      }
}