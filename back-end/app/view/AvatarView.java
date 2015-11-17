package view;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Avatar;
import play.libs.Json;

/**
 * @author fabiomazzone
 */
public class AvatarView {
    /**
     * Json method
     * @return returns the Avatar-object as a Json model
     */
    public static ObjectNode toJson(Avatar avatar) {
        ObjectNode node = Json.newObject();



        node.put("id",              avatar.getId());
        node.put("name",            avatar.getTitle());
        node.put("desc",            avatar.getDescription());
        node.put("avatarFilename",  avatar.getAvatarFilename());
        node.put("isTeam",          avatar.isTeam());
        node.set("attributes",      PlayerStatsView.toJson(avatar.getPlayerStats()));

        return node;
    }
}
