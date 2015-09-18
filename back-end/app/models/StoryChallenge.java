package models;


import com.avaje.ebean.annotation.ConcurrencyMode;
import com.avaje.ebean.annotation.EntityConcurrencyMode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import helper.SimpleText;
import play.Logger;
import play.libs.Json;

import javax.persistence.*;
import java.util.List;

/**
 * Mapping-Class for StoryChallenge
 *
 * @version 0.5
 * @author fabiomazzone
 */
@Entity
@Table(name = "story_challenge")
@EntityConcurrencyMode(ConcurrencyMode.NONE)
public class StoryChallenge extends Challenge {
    public static final int SOLVE_TYPE_RUNNER = 0;

    public static final int TYPE_TUTORIAL = 0;
    public static final int TYPE_STORY = 1;

    @ManyToMany
    List<Text> texts;

    @ManyToMany
    List<Map> maps;

    private int level;

    @OneToOne
    public StoryChallenge next;

    private static final Finder<Long, StoryChallenge> find = new Finder<>(Long.class, StoryChallenge.class);

//////////////////////////////////////////////////
//  Constructor
//////////////////////////////////////////////////

    protected StoryChallenge(
            String          name,
            boolean         tutorial,
            List<Map>       maps,
            StoryChallenge  next,
            int             level) {

        super(name,
                SOLVE_TYPE_RUNNER,
                0,
                tutorial ? TYPE_TUTORIAL: TYPE_STORY);


        this.maps = maps;
        this.next = next;
        this.level = level;

    }



    public int getLevel() {
        return level;
    }

//////////////////////////////////////////////////
//  Json Method
//////////////////////////////////////////////////

    public ObjectNode toJson() {
        ObjectNode node     = Json.newObject();
        ArrayNode textsNode = JsonNodeFactory.instance.arrayNode();
        ArrayNode mapNode   = JsonNodeFactory.instance.arrayNode();

        for(Text text : this.texts) {
            textsNode.add(text.toJson());
        }
        for(Map map : this.maps) {
            mapNode.add(map.toJson());
        }

        node.put("level",           this.level);
        node.put("texts",           textsNode);
        node.put("maps",            mapNode);

        node.put("isTutorial",      this.type == TYPE_TUTORIAL);

        node.put("characterState",  this.player.toJsonCharacterState());

        return node;
    }

//////////////////////////////////////////////////
//  Getter & Setter
//////////////////////////////////////////////////

    public boolean setTexts(List<SimpleText> texts) {
        for(int i = 0; i < texts.size(); i++) {
            SimpleText simpleText = texts.get(i);
            Text text = Text.create(this.type, i, simpleText.prerequisite, simpleText.text, simpleText.sound_url, simpleText.lines);

            if(text != null) {
                this.texts.add(text);
            } else {
                return false;
            }
        }
        return true;
    }

//////////////////////////////////////////////////
//  Create Method
//////////////////////////////////////////////////

    public static StoryChallenge create(
            String          name,
            boolean         isTutorial,
            List<SimpleText>texts,
            List<Map>       maps,
            StoryChallenge  next,
            int             level) {

        if(name == null
                || texts == null || texts.size() == 0
                || maps == null || maps.size() == 0 ) {
            Logger.warn("StoryChallenge.create - null Values not Accepted");
            return null;
        }

        StoryChallenge storyChallenge = new StoryChallenge(
                name,
                isTutorial,
                maps,
                next,
                level);

        try {
            storyChallenge.save();

            if(!storyChallenge.setTexts(texts)) {
                Logger.warn("StoryChallenge.create - Can't save all Texts");

                if(storyChallenge.texts != null) {
                    for(Text text : storyChallenge.texts) {
                        text.delete();
                    }
                }

                storyChallenge.delete();
            }
            storyChallenge.update();
        } catch (PersistenceException pe) {
            Logger.warn(pe.getMessage());
        }
        return storyChallenge;
    }

//////////////////////////////////////////////////
//  Object Getter Methods
//////////////////////////////////////////////////

    public static StoryChallenge getForProfile(Profile profile) {
        StoryChallenge challenge = null;
        if (!profile.isTutorialDone()) {
            challenge = find.where().eq("type", TYPE_TUTORIAL).findList().get(0);
            profile.setCurrentStory(challenge);
        } else {
            challenge = profile.getCurrentStory();
        }


        profile.update();
        if(challenge != null)
            challenge.player = profile;

        return challenge;
    }

    public static StoryChallenge getFirstLevel() {
        StoryChallenge challenge = find.where().eq("type", TYPE_TUTORIAL).findList().get(0);
        if(challenge == null) {
            Logger.warn("StoryChallenge.getFirstLevel - No Element found");
            return null;
        }
        challenge = challenge.getNext();
        if(challenge == null) {
            Logger.warn("StoryChallenge.getFirstLevel - Challenge is null");
            return null;
        }
        return challenge;
    }

    public StoryChallenge getNext() {

        return next;
    }
}
