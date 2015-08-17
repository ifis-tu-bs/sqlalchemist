package models;

import com.avaje.ebean.annotation.ConcurrencyMode;
import com.avaje.ebean.annotation.EntityConcurrencyMode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.Logger;
import play.Play;
import play.db.ebean.Model;
import play.libs.Json;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * This class contains the collected scrolls of the profiles
 *
 * @author fabiomazzone
 */
@Entity
@Table(name = "Scroll_Collection")
@EntityConcurrencyMode(ConcurrencyMode.NONE)
public class ScrollCollection extends Model {
    @Id
    long id;

    @OneToOne
    Profile profile;

    @OneToOne
    Scroll scroll;

    boolean isActive = false;

    Calendar added;

    private static final Finder<Long, ScrollCollection> find = new Finder<>(Long.class, ScrollCollection.class);

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
        if(scroll.isRecipe()) {
            this.isActive = true;
        } else {
            this.isActive = false;
        }
        this.added = Calendar.getInstance();
    }

//////////////////////////////////////////////////
//  getter & setter methods
//////////////////////////////////////////////////

    /**
     * a setter method that sets the Scroll as active
     */
    private void setActive() {
        this.isActive = true;
    }

    /**
     * a setter method that sets a scroll to active
     *
     * @param profile   the owner as Profile
     * @param scroll    the scroll
     */
    public static void setActive(Profile profile, Scroll scroll) {
        ScrollCollection scrollCollection = getByProfileAndScroll(profile, scroll);
        if (scrollCollection == null) {
            Logger.warn("ScrollCollection.setActive - no ScrollCollection Entity found");
            return;
        }
        scrollCollection.setActive();
        scrollCollection.update();
    }

    /**
     * a getter for the isActive attribute
     *
     * @param profile   the owner as Profile
     * @param scroll    the scroll
     * @return          returns isActive as a boolean
     */
    public static boolean isActive(Profile profile, Scroll scroll) {
        ScrollCollection scrollCollection = getByProfileAndScroll(profile, scroll);
        return scrollCollection != null && scrollCollection.isActive;
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

        node.put("scroll",  this.scroll.toJson());
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
        List<ScrollCollection> scrollCollectionList = getScrollCollection(profile);

        for(ScrollCollection scrollCollection : scrollCollectionList) {
            if(!scrollCollection.scroll.isRecipe()) {
                arrayNode.add(scrollCollection.toJson());
            }
        }

        objectNode.put("scrolls", arrayNode);

        return objectNode;
    }


//////////////////////////////////////////////////
//  action
//////////////////////////////////////////////////

    /**
     * checks if the scroll is collected from the owner
     *
     * @param profile   owner as profile
     * @param scroll    the scroll
     * @return          returns a boolean
     */
    public static boolean contains(Profile profile, Scroll scroll) {
        ScrollCollection scrollCollection = getByProfileAndScroll(profile, scroll);
        return scrollCollection != null;
    }

    /**
     * adds a scroll to the collection of an owner
     *
     * @param profile   the owner as profile
     * @param scroll    the scroll
     */
    public static void add(Profile profile, Scroll scroll) {
        ScrollCollection scrollCollection = new ScrollCollection(profile, scroll);
        scrollCollection.save();
    }

//////////////////////////////////////////////////
//  object find methods
//////////////////////////////////////////////////

    /**
     * get the scrollCollection object matches to the combination of profile and scroll if it exists
     *
     * @param profile   the owner of the object as profile
     * @param scroll    the scroll
     * @return          returns the scrollCollection object
     */
    public static ScrollCollection getByProfileAndScroll(Profile profile, Scroll scroll) {
        ScrollCollection scrollCollection = find.where().eq("profile", profile).eq("scroll", scroll).findUnique();
        if (scrollCollection == null) {
            return null;
        }
        return scrollCollection;
    }

    /**
     * get a list of all scrollCollection objects that are own by the given profile
     *
     * @param profile   the owner of the objects as profile
     * @return          returns a list of all scrollCollection objects
     */
    public static List<ScrollCollection> getScrollCollection(Profile profile) {
        List<ScrollCollection> scrollCollections = find.where().eq("profile", profile).findList();

        if(scrollCollections == null) {
            Logger.warn("ScrollCollection.getScrolls - can't find any scroll");
            return null;
        }

        return scrollCollections;
    }

    /**
     * get a list of all active scrollCollection objects that are own by the given profile
     *
     * @param profile   the owner of the objects as profile
     * @return          returns a list of all scrollCollection objects
     */
    public static List<Scroll> getActiveScrolls(Profile profile) {
        List<ScrollCollection> scrollCollectionList = find.where().eq("profile", profile).eq("isActive", true).findList();
        if (scrollCollectionList == null) {
            Logger.warn("ScrollCollection.getActiveScrolls - no Scrolls found");
            return null;
        }

        List<Scroll> scrollList = new ArrayList<Scroll>();

        for(ScrollCollection scrollCollection : scrollCollectionList) {
            scrollList.add(scrollCollection.scroll);
        }

        return scrollList;
    }

    public static int getLimit(Profile profile) {
        List<ScrollCollection> scrollCollectionList = getScrollCollection(profile);
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);
        int count = 0;

        for(ScrollCollection scrollCollectionItem : scrollCollectionList) {
            if(yesterday.before(scrollCollectionItem.added)) {
                count++;
            }
        }

        int ScrollLimit = Play.application().configuration().getInt("Game.ScrollLimit");
        return ScrollLimit - count;
    }

    public static void reset(Profile profile) {
        List<ScrollCollection> scrollCollectionList = find.where().eq("profile", profile).findList();

        for(ScrollCollection scrollCollection : scrollCollectionList) {
            scrollCollection.delete();
        }

    }
}
