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

    @Column(unique = true, name = "avatar_name")
    private final String name;

    private final String description;

    @Column(unique = true)
    private final String avatarFilename;

    private final boolean isTeam;

    @Embedded
    private final PlayerStats attributes;

//////////////////////////////////////////////////
//  Constructor
//////////////////////////////////////////////////

    /**
     * Avatar constructor
     * @param name              name of the avatar
     * @param description       avatar description
     * @param avatarFilename    avatarFilename
     * @param attributes        avatar PlayerStats
     */
    public Avatar(
            String name,
            String description,
            String avatarFilename,
            boolean isTeam,
            PlayerStats attributes) {

        this.name           = name;
        this.description    = description;
        this.avatarFilename = avatarFilename;
        this.isTeam         = isTeam;
        this.attributes     = attributes;

    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        if(this.name.contains(" 2"))
            return this.name.replace(" 2", "");
        return this.name;
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

    public PlayerStats getAttributes() {
        return this.attributes;
    }

}
