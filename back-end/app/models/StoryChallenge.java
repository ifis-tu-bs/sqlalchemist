package models;

import dao.TextDAO;

import helper.SimpleText;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.ConcurrencyMode;
import com.avaje.ebean.annotation.EntityConcurrencyMode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.libs.Json;
import view.MapView;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Mapping-Class for StoryChallenge
 *
 * @version 0.5
 * @author fabiomazzone
 */
@Entity
@Table(name = "storychallenge")
@EntityConcurrencyMode(ConcurrencyMode.NONE)
public class StoryChallenge extends Model {
    @Id
    private Long id;

    private final String name;

    private final boolean isTutorial;

    public static final int SOLVE_TYPE_RUNNER = 0;

    @ManyToMany
    private List<Text> texts;

    @ManyToMany
    final List<Map> maps;

    private final int level;

    @OneToOne
    private final StoryChallenge next;

    private final Date createdAt;

    @Transient
    User user;


    public static final Finder<Long, StoryChallenge> find = new Finder<>(StoryChallenge.class);

//////////////////////////////////////////////////
//  Constructor
//////////////////////////////////////////////////

    public StoryChallenge(
            String          name,
            boolean         isTutorial,
            List<Map>       maps,
            StoryChallenge  next,
            int             level) {

        this.name = name;


        this.isTutorial = isTutorial;

        this.maps = maps;

        this.next = next;
        this.level = level;

        this.createdAt = new Date();
    }



    public int getLevel() {
        return level;
    }

//////////////////////////////////////////////////
//  Json Method
//////////////////////////////////////////////////

    public ObjectNode toJson() {
        ObjectNode node = Json.newObject();
        ArrayNode textsNode = JsonNodeFactory.instance.arrayNode();
        ArrayNode mapNode = JsonNodeFactory.instance.arrayNode();

        node.put("name", this.getName());

        for (Text text : this.texts) {
            textsNode.add(text.toJson());
        }
        for (Map map : this.maps) {
            mapNode.add(MapView.toJson(map));
        }

        node.put("level",   this.level);
        node.set("texts",   textsNode);
        node.set("maps",    mapNode);

        node.put("isTutorial", this.isTutorial);

        node.set("characterState", this.user.toJsonCharacterState());

        node.put("createdAt", String.valueOf(this.getCreatedAt()));

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
            Text text = TextDAO.create(0, i, simpleText.prerequisite, simpleText.text, simpleText.sound_url, simpleText.lines);

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

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }
}
