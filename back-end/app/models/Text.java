package models;

import java.util.List;

import com.avaje.ebean.annotation.ConcurrencyMode;
import com.avaje.ebean.annotation.EntityConcurrencyMode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.helper.*;
import play.Logger;
import play.db.ebean.Model;
import play.libs.Json;

import javax.persistence.*;

/**
 *
 * @author fabiomazzone
 */
@Entity
@Table(name = "texts")
@EntityConcurrencyMode(ConcurrencyMode.NONE)
public class Text extends Model {
    @Id
    private
    long id;

    private static final int TEXT_TYPE_TERRY_SUCCESSFUL = 200;
    private static final int TEXT_TYPE_TERRY_FAILURE = 201;
    private static final int TEXT_TYPE_TERRY_URGE = 202;
    @Column(name = "type")
    private final int type;

    private int prerequisite;

    private int chronology;

    @Column(name = "text", columnDefinition = "Text")
    private final String text;

    @Column(name = "sound_url")
    private final String sound_url;

    @Column(name = "count_of_lines")
    private final int lines;

    private static final Finder<Long, Text> find = new Finder<>(Long.class, Text.class);


//////////////////////////////////////////////////
//  Constructor
//////////////////////////////////////////////////

    private Text(
            int type,
            String text,
            String sound_url,
            int prerequisite,
            int chronology,
            int lines) {

        super();

        this.type           = type;
        this.text           = text;
        this.sound_url      = sound_url;
        this.prerequisite = prerequisite;
        this.chronology = chronology;
        this.lines = lines;
    }

//////////////////////////////////////////////////
//  Json Method
//////////////////////////////////////////////////

    /**
     *
     * @return returns the Avatar-Object as a Json model
     */
    public ObjectNode toJson() {
        ObjectNode node = Json.newObject();

        node.put("id",          this.id);
        node.put("type",        this.type);
        node.put("prerequisite",this.prerequisite);
        node.put("chronology",  this.chronology);
        node.put("text",        this.text);
        node.put("lines",       this.lines);
        node.put("sound_url",   this.sound_url);

        return node;
    }

//////////////////////////////////////////////////
//  Create Method
//////////////////////////////////////////////////

    public static Text create(
            int type,
            int chronology,
            int prerequisite,
            String text,
            String sound_url,
            int lines) {

        if(text == null) {
            return null;
        }

        Text item = new Text(type, text, sound_url, prerequisite, chronology, lines);

        try {
            item.save();
        } catch (PersistenceException pe) {
            Logger.warn("Text.create - caught PersistenceException: " + pe.getMessage());
            Text text_comp = find.where().eq("text", text).findUnique();
            if(text_comp != null) {
                Logger.warn("Text.create - found alternative \"Text\"");
                return text_comp;
            }
            Logger.error("Text.create - no alternative \"Text\" found");
            return null;
        }

        return item;
    }


//////////////////////////////////////////////////
//  Object Getter Methods
//////////////////////////////////////////////////

    private static Text getTextByType(int type) {
        List<Text> texts = find.where().eq("type", type).findList();

        int size = texts.size();
        if(size != 0) {
            int i = Random.randomInt(0, size - 1);
            return texts.get(i);
        }
        Logger.warn("Text.getTextByType - found no texts");
        return null;
    }

    /**
     *
     */
    public static Text getTerrySuccessful() {
        return getTextByType(TEXT_TYPE_TERRY_SUCCESSFUL);
    }
    /**
     *
     */
    public static Text getTerryFailure() {
        return getTextByType(TEXT_TYPE_TERRY_FAILURE);
    }

    /**
     *
     */
    public static Text getTerryUrge() {
        return getTextByType(TEXT_TYPE_TERRY_URGE);
    }



}
