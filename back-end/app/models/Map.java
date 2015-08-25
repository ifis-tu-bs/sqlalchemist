package models;

import com.fasterxml.jackson.databind.node.ObjectNode;
import play.Logger;
import play.db.ebean.Model;
import play.libs.Json;

import javax.persistence.*;
import java.util.List;

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
    long id;

    @Column(name = "level")
    int level;

    @Column(name = "path", unique = true)
    String path;

    @Column(name = "boss_map")
    boolean isBossMap = false;

    static final Finder<Long, Map> find = new Finder<>(Long.class, Map.class);

//////////////////////////////////////////////////
//  Constructor
//////////////////////////////////////////////////

    /**
     *
     * @param path      Path to File Source
     * @param isBossMap isBossMap
     */
    private Map(
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

//////////////////////////////////////////////////
//  Create Methods
//////////////////////////////////////////////////

    /**
     * @param level
     * @param path  Path to File Source
     * @return returns the Map-Object or Null on Failure
     */
    private static Map create(
            int level,
            String path) {
        return Map.create(level, path, false);
    }

    /**
     * @param level
     * @param path      Path to File Source
     * @param isBossMap isBossMap
     * @return returns the Map-Object or Null on Failure
     */
    public static Map create(
            int level,
            String  path,
            boolean isBossMap) {

        if(path == null) {
            return  null;
        }
        Map map = new Map(level, path, isBossMap);

        try {
            map.save();
        } catch (PersistenceException pe) {
            Map map_res = find.where().eq("path", path).findUnique();
            if(map_res != null && map_res.isBossMap == isBossMap) {
                Logger.warn("Map.create - Can't create Map(duplicate) " + map.toJson().toString());
                return map_res;
            }
            Logger.error("Map.create - Can't create Map: " + map.toJson());
            Logger.error("Map.create - Error: " + pe.getMessage());
            return null;
        }
        return map;
    }

//////////////////////////////////////////////////
//  Object Getter Methods
//////////////////////////////////////////////////

    /**
     *
     * @param id ID of the Map
     * @return returns the Map Object
     */
    public Map getById(long id) {
        return find.byId(id);
    }

    public static List<Map> getByLevel(int level) {
        return find.where().eq("level", level).findList();
    }


//////////////////////////////////////////////////
//  Init Method
//////////////////////////////////////////////////

    /**
     * This method initialize the 'Map' table with some data
     */
    public static void init() {
        Logger.info("Initialize 'Map' data");


        Map.create(0, "assets/data/map/tutorial.tmx", true);

        Map.create(1, "assets/data/map/0bossmap.tmx", true);
        Map.create(1, "assets/data/map/0map0.tmx");
        Map.create(1, "assets/data/map/0map1.tmx");
        Map.create(1, "assets/data/map/0map2.tmx");
        Map.create(1, "assets/data/map/0map3.tmx");
        Map.create(1, "assets/data/map/0map4.tmx");
        Map.create(1, "assets/data/map/0map5.tmx");

        Map.create(2, "assets/data/map/1bossmap.tmx", true);
        Map.create(2, "assets/data/map/1map0.tmx");
        Map.create(2, "assets/data/map/1map1.tmx");
        Map.create(2, "assets/data/map/1map2.tmx");
        Map.create(2, "assets/data/map/1map3.tmx");
        Map.create(2, "assets/data/map/1map4.tmx");
        Map.create(2, "assets/data/map/1map5.tmx");

        Map.create(3, "assets/data/map/2bossmap.tmx", true);
        Map.create(3, "assets/data/map/2map0.tmx");
        Map.create(3, "assets/data/map/2map1.tmx");
        Map.create(3, "assets/data/map/2map2.tmx");
        Map.create(3, "assets/data/map/2map3.tmx");
        Map.create(3, "assets/data/map/2map4.tmx");
        Map.create(3, "assets/data/map/2map5.tmx");

        Map.create(4, "assets/data/map/3bossmap.tmx", true);
        Map.create(4, "assets/data/map/3map0.tmx");
        Map.create(4, "assets/data/map/3map1.tmx");
        Map.create(4, "assets/data/map/3map2.tmx");
        Map.create(4, "assets/data/map/3map3.tmx");
        Map.create(4, "assets/data/map/3map4.tmx");
        Map.create(4, "assets/data/map/3map5.tmx");

        Map.create(5, "assets/data/map/4bossmap.tmx", true);
        Map.create(5, "assets/data/map/4map0.tmx");
        Map.create(5, "assets/data/map/4map1.tmx");
        Map.create(5, "assets/data/map/4map2.tmx");
        Map.create(5, "assets/data/map/4map3.tmx");
        Map.create(5, "assets/data/map/4map4.tmx");
        Map.create(5, "assets/data/map/4map5.tmx");

        Map.create(6, "assets/data/map/5bossmap.tmx", true);
        Map.create(6, "assets/data/map/5map0.tmx");
        Map.create(6, "assets/data/map/5map1.tmx");
        Map.create(6, "assets/data/map/5map2.tmx");
        Map.create(6, "assets/data/map/5map3.tmx");
        Map.create(6, "assets/data/map/5map4.tmx");
        Map.create(6, "assets/data/map/5map5.tmx");

        Map.create(7, "assets/data/map/6bossmap.tmx", true);
        Map.create(7, "assets/data/map/6map0.tmx");
        Map.create(7, "assets/data/map/6map1.tmx");
        Map.create(7, "assets/data/map/6map2.tmx");
        Map.create(7, "assets/data/map/6map3.tmx");
        Map.create(7, "assets/data/map/6map4.tmx");
        Map.create(7, "assets/data/map/6map5.tmx");

        Map.create(8, "assets/data/map/7bossmap.tmx", true);
        Map.create(8, "assets/data/map/7map0.tmx");
        Map.create(8, "assets/data/map/7map1.tmx");
        Map.create(8, "assets/data/map/7map2.tmx");
        Map.create(8, "assets/data/map/7map3.tmx");
        Map.create(8, "assets/data/map/7map4.tmx");
        Map.create(8, "assets/data/map/7map5.tmx");

        Map.create(9, "assets/data/map/8bossmap.tmx", true);
        Map.create(9, "assets/data/map/8map0.tmx");
        Map.create(9, "assets/data/map/8map1.tmx");
        Map.create(9, "assets/data/map/8map2.tmx");
        Map.create(9, "assets/data/map/8map3.tmx");
        Map.create(9, "assets/data/map/8map4.tmx");
        Map.create(9, "assets/data/map/8map5.tmx");

        Map.create(10, "assets/data/map/9bossmap.tmx", true);
        Map.create(10, "assets/data/map/9map0.tmx");
        Map.create(10, "assets/data/map/9map1.tmx");
        Map.create(10, "assets/data/map/9map2.tmx");
        Map.create(10, "assets/data/map/9map3.tmx");
        Map.create(10, "assets/data/map/9map4.tmx");
        Map.create(10, "assets/data/map/9map5.tmx");

        Logger.info("Done initializing");
    }

}
