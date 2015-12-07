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
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "scroll_id"})
)
@EntityConcurrencyMode(ConcurrencyMode.NONE)
public class ScrollCollection extends Model {
    @ManyToOne
    private User user;

    @ManyToOne
    private Scroll scroll;

    private boolean isActive;

    private Calendar added;

    public static final Finder<Long, ScrollCollection> find = new Finder<>(ScrollCollection.class);

//////////////////////////////////////////////////
//  constructor
//////////////////////////////////////////////////


    /**
     * this is the constructor for a ScrollCollection object
     *
     * @param user   the owner of the Scroll as profile
     * @param scroll    the scroll
     */
    public ScrollCollection(User user, Scroll scroll) {
        this.user       = user;
        this.scroll     = scroll;
        this.isActive   = scroll.isRecipe();
        this.added      = Calendar.getInstance();
    }

//////////////////////////////////////////////////
//  getter & setter methods
//////////////////////////////////////////////////


    public User getUser() {
        return user;
    }

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
     * @param user   the owner as profile
     * @return          returns all objects that the given profile owns as Json object
     */
    public static ObjectNode toJsonAll(User user) {
        ArrayNode arrayNode     = JsonNodeFactory.instance.arrayNode();
        ObjectNode objectNode   = Json.newObject();
        List<ScrollCollection> scrollCollectionList = ScrollCollectionDAO.getScrollCollection(user);

        scrollCollectionList.stream().filter(scrollCollection -> !scrollCollection.scroll.isRecipe()).forEach(scrollCollection -> arrayNode.add(scrollCollection.toJson()));

        objectNode.set("scrolls", arrayNode);

        return objectNode;
    }
}
