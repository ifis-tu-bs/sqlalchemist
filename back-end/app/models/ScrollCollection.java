package models;

import dao.ScrollCollectionDAO;

import com.avaje.ebean.annotation.ConcurrencyMode;
import com.avaje.ebean.annotation.EntityConcurrencyMode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.avaje.ebean.Model;
import play.libs.Json;

import javax.persistence.*;
import java.util.Calendar;
import java.util.List;

/**
 * This class contains the collected scrolls of the profiles
 *
 * @author fabiomazzone
 */
@Entity
@Table(
        name = "scrollcollection",
        uniqueConstraints = @UniqueConstraint(columnNames = {"profile_id", "scroll_id"})
)
@EntityConcurrencyMode(ConcurrencyMode.NONE)
public class ScrollCollection extends Model {
    @Id
    private long id;

    @ManyToOne
    private final Profile profile;

    @ManyToOne
    private final Scroll scroll;

    private boolean isActive;

    private final Calendar added;

    public static final Finder<Long, ScrollCollection> find = new Finder<>(ScrollCollection.class);

//////////////////////////////////////////////////
//  constructor
//////////////////////////////////////////////////


    /**
     * this is the constructor for a ScrollCollection object
     *
     * @param profile   the owner of the Scroll as profile
     * @param scroll    the scroll
     */
    public ScrollCollection(Profile profile, Scroll scroll) {
        this.profile    = profile;
        this.scroll     = scroll;
        this.isActive   = scroll.isRecipe();
        this.added      = Calendar.getInstance();
    }

//////////////////////////////////////////////////
//  getter & setter methods
//////////////////////////////////////////////////
    public Scroll getScroll() {
      return this.scroll;
    }

    public boolean isActive() {
      return this.isActive;
    }

    public Calendar getAdded() {
      return this.added;
    }

    /**
     * a setter method that sets the Scroll as active
     */
    public void setActive() {
        this.isActive = true;
    }

//////////////////////////////////////////////////
//  json method
//////////////////////////////////////////////////

    /**
     * convert the current object to an Json object
     *
     * @return  returns the object as Json object
     */
    public ObjectNode toJson() {
        ObjectNode node = Json.newObject();

        node.set("scroll",  this.scroll.toJson());
        node.put("isActive",this.isActive);

        return node;
    }

    /**
     * convert all objects from the owner to a json object
     *
     * @param profile   the owner as profile
     * @return          returns all objects that the given profile owns as Json object
     */
    public static ObjectNode toJsonAll(Profile profile) {
        ArrayNode arrayNode     = JsonNodeFactory.instance.arrayNode();
        ObjectNode objectNode   = Json.newObject();
        List<ScrollCollection> scrollCollectionList = ScrollCollectionDAO.getScrollCollection(profile);

        scrollCollectionList.stream().filter(scrollCollection -> !scrollCollection.scroll.isRecipe()).forEach(scrollCollection -> arrayNode.add(scrollCollection.toJson()));

        objectNode.set("scrolls", arrayNode);

        return objectNode;
    }
}
