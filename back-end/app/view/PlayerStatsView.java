package view;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.PlayerStats;
import play.libs.Json;

/**
 * @author fabiomazzone
 */
public class PlayerStatsView {
    /**
     *
     * @return returns the Avatar-Object as a Json model
     */
    public static ObjectNode toJson(PlayerStats playerStats) {
        ObjectNode node = Json.newObject();

        node.put("health",  playerStats.getHealth());
        node.put("defense", playerStats.getDefense());
        node.put("speed",   playerStats.getSpeed());
        node.put("jump",    playerStats.getJump());
        node.put("slots",   playerStats.getSlot());

        return node;
    }
}
