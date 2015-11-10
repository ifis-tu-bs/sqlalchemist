package models;

import com.avaje.ebean.Model;

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
    private Long id;

    @Column(name = "avatar_name", unique = true)
    private final String name;

    @Column(name = "Avatardesc")
    private final String desc;

    @Column(name = "avatar_filename", unique = true)
    private final String avatarFilename;

    private final boolean isTeam;

    @Embedded
    private final PlayerStats playerStats;

    public static final Finder<Long, Avatar> find = new Model.Finder<>(Long.class, Avatar.class);

//////////////////////////////////////////////////
//  Constructor
//////////////////////////////////////////////////

    /**
     * Avatar constructor
     * @param name                  name of the avatar
     * @param desc                  avatar description
     * @param avatarFilename        avatarFilename
     * @param playerStats           avatar PlayerStats
     */
    public Avatar(
            String name,
            String desc,
            String avatarFilename,
            boolean isTeam,
            PlayerStats playerStats) {
        this.name           = name;
        this.desc           = desc;
        this.avatarFilename= avatarFilename;
        this.isTeam         = isTeam;
        this.playerStats    = playerStats;

    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        if(this.name.contains(" 2"))
            return this.name.replace(" 2", "");
        return this.name;
    }

    public String getDesc() {
        return this.desc;
    }

    public String getAvatarFilename() {
        return this.avatarFilename;
    }

    public boolean isTeam() {
        return this.isTeam;
    }

    public PlayerStats getPlayerStats() {
        return this.playerStats;
    }

}
