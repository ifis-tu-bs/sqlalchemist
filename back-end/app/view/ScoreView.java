package view;

import models.Score;
import models.User;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.libs.Json;

import java.util.List;

/**
 * @author fabiomazzone
 */
public class ScoreView {
    public static ObjectNode toJson(User user) {
        Score score = user.getScore();
        ObjectNode highScoreJson = Json.newObject();

        highScoreJson.put("id",             user.getId());
        highScoreJson.put("username",       user.getUsername());
        highScoreJson.put("totalCoins",     score.getTotalCoins());
        highScoreJson.put("totalScore",     score.getTotalScore());
        highScoreJson.put("playedTime",     score.getPlayedTime());
        highScoreJson.put("playedRuns",     score.getPlayedRuns());
        highScoreJson.put("solvedSQL",      score.getSolvedSQL());
        highScoreJson.put("quote",          score.getSuccessRate());

        return highScoreJson;
    }

    public static ObjectNode toJsonAll(List<User> users, User user) {
        ArrayNode scoreArray = JsonNodeFactory.instance.arrayNode();

        for (User user1 : users) {
            scoreArray.add(toJson(user1));
        }

        ObjectNode node = Json.newObject();

        node.set("highScore",   scoreArray);
        node.put("ownRank",     user.getScore().getOwnRank() + 1);
        node.set("own",         toJson(user));
        return node;
    }
}
