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
    private StoryChallenge next;

    public static final Finder<Long, StoryChallenge> find = new Finder<>(Long.class, StoryChallenge.class);

//////////////////////////////////////////////////
//  Constructor
//////////////////////////////////////////////////

    public StoryChallenge(
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

    public List<Text> getTexts() {
      return this.texts;
    }

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

    public StoryChallenge getNext() {
        return this.next;
    }

    public void setNext(StoryChallenge next) {
      this.next = next;
    }
}
