package models;

import com.avaje.ebean.annotation.ConcurrencyMode;
import com.avaje.ebean.annotation.EntityConcurrencyMode;
import com.fasterxml.jackson.databind.node.ObjectNode;
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

    public static final int TEXT_TYPE_TERRY_SUCCESSFUL = 200;
    public static final int TEXT_TYPE_TERRY_FAILURE = 201;
    public static final int TEXT_TYPE_TERRY_URGE = 202;
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

    public static final Finder<Long, Text> find = new Finder<>(Long.class, Text.class);


//////////////////////////////////////////////////
//  Constructor
//////////////////////////////////////////////////

    public Text(
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
}
