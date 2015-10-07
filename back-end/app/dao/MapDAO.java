package dao;

import models.Map;

import play.Logger;

import javax.persistence.PersistenceException;

import java.util.List;

public class MapDAO {
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
              Map map_res = Map.find.where().eq("path", path).findUnique();
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
          return Map.find.byId(id);
      }

      public static List<Map> getByLevel(int level) {
          return Map.find.where().eq("level", level).findList();
      }

}