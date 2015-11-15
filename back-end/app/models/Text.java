package models;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.ConcurrencyMode;
import com.avaje.ebean.annotation.EntityConcurrencyMode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;

import javax.persistence.*;

/**
 *
 * @author fabiomazzone
 */
@Entity
@Table(name = "text")
@EntityConcurrencyMode(ConcurrencyMode.NONE)
public class Text extends Model {
    @Id
    private Long id;

    public static final int TEXT_TYPE_TERRY_SUCCESSFUL = 200;
    public static final int TEXT_TYPE_TERRY_FAILURE = 201;
    public static final int TEXT_TYPE_TERRY_URGE = 202;

    private final int type;

    private final int prerequisite;

    private final int chronology;

    @Column(columnDefinition = "Text")
    private final String text;

    private final String sound_url;

    private final int content_rows;

    public static final Finder<Long, Text> find = new Finder<>(Text.class);


//////////////////////////////////////////////////
//  Constructor
//////////////////////////////////////////////////

    public Text(
            int type,
            String text,
            String sound_url,
            int prerequisite,
            int chronology,
            int content_rows) {

        super();

        this.type           = type;
        this.text           = text;
        this.sound_url      = sound_url;
        this.prerequisite = prerequisite;
        this.chronology = chronology;
        this.content_rows = content_rows;
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
        node.put("content_rows",       this.content_rows);
        node.put("sound_url",   this.sound_url);

        return node;
    }
}
