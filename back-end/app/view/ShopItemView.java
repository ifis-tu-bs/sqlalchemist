package view;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import models.ShopItem;
import models.User;
import play.libs.Json;

import java.util.List;

/**
 * @author fabiomazzone
 */
public class ShopItemView {
    /**
     *
     * @return returns the ShopItem-Object as a Json model
     */
    public static ObjectNode toJson(User user, ShopItem shopItem) {
        ObjectNode node = Json.newObject();

        node.put("id",              shopItem.getId());
        node.put("name",            shopItem.getTitle());
        node.put("desc",            shopItem.getDescription());
        node.put("type",            shopItem.getType());
        node.put("thumbnailUrl",    shopItem.getThumbnailUrl());
        node.put("price",           shopItem.getPrice());
        if(shopItem.isTypeAvatar()) {
            node.set("avatar",      AvatarView.toJson(shopItem.getAvatar()));
        }
        node.put("bought",          user.getShopItems().contains(shopItem));

        return node;
    }

    public static ArrayNode toJson(User user, List<ShopItem> shopItems) {
        ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();

        for(ShopItem shopItem : shopItems) {
            arrayNode.add(ShopItemView.toJson(user, shopItem));
        }

        return arrayNode;
    }
}
