package models;

import com.avaje.ebean.Model;

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

    private final int level;

    @Column(unique = true)
    private final String path;

    private final boolean isBossMap;

    public static final Finder<Long, Map> find = new Finder<>(Map.class);

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
