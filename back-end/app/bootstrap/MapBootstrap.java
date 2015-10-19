package bootstrap;

import dao.MapDAO;

import play.Logger;

class MapBootstrap {
  //////////////////////////////////////////////////
  //  Init Method
  //////////////////////////////////////////////////

      /**
       * This method initialize the 'Map' table with some data
       */
      public static void init() {
          Logger.info("Initialize 'Map' data");


          MapDAO.create(0, "assets/data/map/tutorial.tmx", true);

          MapDAO.create(1, "assets/data/map/0bossmap.tmx", true);
          MapDAO.create(1, "assets/data/map/0map0.tmx", false);
          MapDAO.create(1, "assets/data/map/0map1.tmx", false);
          MapDAO.create(1, "assets/data/map/0map2.tmx", false);
          MapDAO.create(1, "assets/data/map/0map3.tmx", false);
          MapDAO.create(1, "assets/data/map/0map4.tmx", false);
          MapDAO.create(1, "assets/data/map/0map5.tmx", false);

          MapDAO.create(2, "assets/data/map/1bossmap.tmx", true);
          MapDAO.create(2, "assets/data/map/1map0.tmx", false);
          MapDAO.create(2, "assets/data/map/1map1.tmx", false);
          MapDAO.create(2, "assets/data/map/1map2.tmx", false);
          MapDAO.create(2, "assets/data/map/1map3.tmx", false);
          MapDAO.create(2, "assets/data/map/1map4.tmx", false);
          MapDAO.create(2, "assets/data/map/1map5.tmx", false);

          MapDAO.create(3, "assets/data/map/2bossmap.tmx", true);
          MapDAO.create(3, "assets/data/map/2map0.tmx", false);
          MapDAO.create(3, "assets/data/map/2map1.tmx", false);
          MapDAO.create(3, "assets/data/map/2map2.tmx", false);
          MapDAO.create(3, "assets/data/map/2map3.tmx", false);
          MapDAO.create(3, "assets/data/map/2map4.tmx", false);
          MapDAO.create(3, "assets/data/map/2map5.tmx", false);

          MapDAO.create(4, "assets/data/map/3bossmap.tmx", true);
          MapDAO.create(4, "assets/data/map/3map0.tmx", false);
          MapDAO.create(4, "assets/data/map/3map1.tmx", false);
          MapDAO.create(4, "assets/data/map/3map2.tmx", false);
          MapDAO.create(4, "assets/data/map/3map3.tmx", false);
          MapDAO.create(4, "assets/data/map/3map4.tmx", false);
          MapDAO.create(4, "assets/data/map/3map5.tmx", false);

          MapDAO.create(5, "assets/data/map/4bossmap.tmx", true);
          MapDAO.create(5, "assets/data/map/4map0.tmx", false);
          MapDAO.create(5, "assets/data/map/4map1.tmx", false);
          MapDAO.create(5, "assets/data/map/4map2.tmx", false);
          MapDAO.create(5, "assets/data/map/4map3.tmx", false);
          MapDAO.create(5, "assets/data/map/4map4.tmx", false);
          MapDAO.create(5, "assets/data/map/4map5.tmx", false);

          MapDAO.create(6, "assets/data/map/5bossmap.tmx", true);
          MapDAO.create(6, "assets/data/map/5map0.tmx", false);
          MapDAO.create(6, "assets/data/map/5map1.tmx", false);
          MapDAO.create(6, "assets/data/map/5map2.tmx", false);
          MapDAO.create(6, "assets/data/map/5map3.tmx", false);
          MapDAO.create(6, "assets/data/map/5map4.tmx", false);
          MapDAO.create(6, "assets/data/map/5map5.tmx", false);

          MapDAO.create(7, "assets/data/map/6bossmap.tmx", true);
          MapDAO.create(7, "assets/data/map/6map0.tmx", false);
          MapDAO.create(7, "assets/data/map/6map1.tmx", false);
          MapDAO.create(7, "assets/data/map/6map2.tmx", false);
          MapDAO.create(7, "assets/data/map/6map3.tmx", false);
          MapDAO.create(7, "assets/data/map/6map4.tmx", false);
          MapDAO.create(7, "assets/data/map/6map5.tmx", false);

          MapDAO.create(8, "assets/data/map/7bossmap.tmx", true);
          MapDAO.create(8, "assets/data/map/7map0.tmx", false);
          MapDAO.create(8, "assets/data/map/7map1.tmx", false);
          MapDAO.create(8, "assets/data/map/7map2.tmx", false);
          MapDAO.create(8, "assets/data/map/7map3.tmx", false);
          MapDAO.create(8, "assets/data/map/7map4.tmx", false);
          MapDAO.create(8, "assets/data/map/7map5.tmx", false);

          MapDAO.create(9, "assets/data/map/8bossmap.tmx", true);
          MapDAO.create(9, "assets/data/map/8map0.tmx", false);
          MapDAO.create(9, "assets/data/map/8map1.tmx", false);
          MapDAO.create(9, "assets/data/map/8map2.tmx", false);
          MapDAO.create(9, "assets/data/map/8map3.tmx", false);
          MapDAO.create(9, "assets/data/map/8map4.tmx", false);
          MapDAO.create(9, "assets/data/map/8map5.tmx", false);

          MapDAO.create(10, "assets/data/map/9bossmap.tmx", true);
          MapDAO.create(10, "assets/data/map/9map0.tmx", false);
          MapDAO.create(10, "assets/data/map/9map1.tmx", false);
          MapDAO.create(10, "assets/data/map/9map2.tmx", false);
          MapDAO.create(10, "assets/data/map/9map3.tmx", false);
          MapDAO.create(10, "assets/data/map/9map4.tmx", false);
          MapDAO.create(10, "assets/data/map/9map5.tmx", false);

          Logger.info("Done initializing");
      }


}
