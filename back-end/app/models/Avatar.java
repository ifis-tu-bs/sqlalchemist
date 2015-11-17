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

    @Column(unique = true)
    private final String title;

    private final String description;

    @Column(unique = true)
    private final String avatarFilename;

    private final boolean isTeam;

    @Embedded
    private final PlayerStats playerStats;

    public static final Finder<Long, Avatar> find = new Finder<>(Avatar.class);

//////////////////////////////////////////////////
//  Constructor
//////////////////////////////////////////////////

    /**
     * Avatar constructor
     * @param title                  title of the avatar
     * @param desc                  avatar description
     * @param avatarFilename        avatarFilename
     * @param playerStats           avatar PlayerStats
     */
    public Avatar(
            String title,
            String desc,
            String avatarFilename,
            boolean isTeam,
            PlayerStats playerStats) {
        this.title = title;
        this.description = desc;
        this.avatarFilename= avatarFilename;
        this.isTeam         = isTeam;
        this.playerStats    = playerStats;

    }

    public long getId() {
        return this.id;
    }

    public String getTitle() {
        if(this.title.contains(" 2"))
            return this.title.replace(" 2", "");
        return this.title;
    }

    public String getDescription() {
        return this.description;
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
