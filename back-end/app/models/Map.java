package models;

import play.db.ebean.Model;

import javax.persistence.*;

/**
 *  Mapping-Class for Maps on the File System
 *
 *  @version 1.0
 *  @author fabiomazzone
 */
@Entity
@Table(name = "map")
public class Map extends Model {
    @Id
    private long id;

    @Column(name = "level")
    private int level;

    @Column(name = "path", unique = true)
    private String path;

    @Column(name = "boss_map")
    private boolean isBossMap = false;

    public static final Finder<Long, Map> find = new Finder<>(Long.class, Map.class);

//////////////////////////////////////////////////
//  Constructor
//////////////////////////////////////////////////

    /**
     * @param path      Path to File Source
     * @param isBossMap isBossMap
     */
    public Map(
            int level,
            String path,
            boolean isBossMap) {
        super();
        this.level = level;
        this.path = path;
        this.isBossMap = isBossMap;
    }


    public long getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }

    public String getPath() {
        return path;
    }

    public boolean isBossMap() {
        return isBossMap;
    }
}
