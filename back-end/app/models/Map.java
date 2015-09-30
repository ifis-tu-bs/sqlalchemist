package models;

import com.fasterxml.jackson.databind.node.ObjectNode;

import play.db.ebean.Model;
import play.libs.Json;

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
    public long id;

    @Column(name = "level")
    public int level;

    @Column(name = "path", unique = true)
    public String path;

    @Column(name = "boss_map")
    public boolean isBossMap = false;

    public static final Finder<Long, Map> find = new Finder<>(Long.class, Map.class);

//////////////////////////////////////////////////
//  Constructor
//////////////////////////////////////////////////

    /**
     *
     * @param path      Path to File Source
     * @param isBossMap isBossMap
     */
    public Map(
            int level,
            String  path,
            boolean isBossMap) {
        super();
        this.level      = level;
        this.path       = path;
        this.isBossMap  = isBossMap;
    }

//////////////////////////////////////////////////
//  Json Method
//////////////////////////////////////////////////

    /**
     *
     * @return returns the Map-Object as a Json model
     */
    public ObjectNode toJson() {
        ObjectNode node = Json.newObject();

        node.put("id",          this.id);
        node.put("level",       this.level);
        node.put("path",        this.path);
        node.put("isBossMap",   this.isBossMap);

        return node;
    }
}
