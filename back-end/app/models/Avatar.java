package models;

import com.fasterxml.jackson.databind.node.ObjectNode;

import play.db.ebean.Model;
import play.libs.Json;

import javax.persistence.*;

/**
 *  Mapping-Class for Avatar on the File System
 *
 *  @version 1.0
 *  @author fabiomazzone
 */
@Entity
@Table(name = "avatar")
public class Avatar extends Model {

    @Id
    private long id;

    @Column(name = "avatar_name", unique = true)
    private final String name;

    @Column(name = "Avatardesc")
    private final String desc;

    @Column(name = "avatar_filename", unique = true)
    private final String avatarFilename;

    private final boolean isTeam;

    @Column(name = "soundURL")
    private final String soundURL;

    @Embedded
    private final PlayerStats playerStats;

    public static final Finder<Long, Avatar> find = new Finder<>(Long.class, Avatar.class);

//////////////////////////////////////////////////
//  Constructor
//////////////////////////////////////////////////

    /**
     * Avatar constructor
     * @param name                  name of the avatar
     * @param desc                  avatar description
     * @param avatarFilename        avatarFilename
     * @param soundURL              sound url
     * @param playerStats           avatar PlayerStats
     */
    public Avatar(
            String name,
            String desc,
            String avatarFilename,
            String soundURL,
            boolean isTeam,
            PlayerStats playerStats) {
        super();

        this.name           = name;
        this.desc           = desc;
        this.avatarFilename= avatarFilename;
        this.soundURL       = soundURL;
        this.isTeam         = isTeam;
        this.playerStats    = playerStats;

    }

//////////////////////////////////////////////////
//  Json Method
//////////////////////////////////////////////////

    /**
     * Json method
     * @return returns the Avatar-object as a Json model
     */
    public ObjectNode toJson() {
        ObjectNode node = Json.newObject();

        node.put("id",              this.id);
        node.put("name",            this.getName());
        node.put("desc",            this.desc);
        node.put("avatarFilename",  this.avatarFilename);
        node.put("soundURL",        this.soundURL);
        node.put("isTeam",          this.isTeam);
        node.put("attributes",      this.playerStats.toJson());

        return node;
    }


    /**
     *
     * @return
     */
    public PlayerStats getPlayerStats() {
        return this.playerStats;
    }

    public String getName() {
        return this.name.contains("2") ? this.name.replace(" 2", "").trim() : this.name;
    }

    public String getAvatar() {
        return this.avatarFilename;
    }

    public String getDesc() {
        return this.desc;
    }

    public long getId() {
        return this.id;
    }
}
