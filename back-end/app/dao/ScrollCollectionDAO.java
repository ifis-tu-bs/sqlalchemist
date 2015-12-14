package dao;

import models.Scroll;
import models.ScrollCollection;

import models.User;
import play.Logger;
import play.Play;

import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import java.util.stream.Collectors;

public class ScrollCollectionDAO {
  /**
   * a setter method that sets a scroll to active
   *
   * @param user      the owner
   * @param scroll    the scroll
   */
  public static void setActive(User user, Scroll scroll) {
      ScrollCollection scrollCollection = getByProfileAndScroll(user, scroll);
      if (scrollCollection == null) {
          Logger.warn("ScrollCollection.setActive - no ScrollCollection Entity found");
          return;
      }
      scrollCollection.setActive();
      scrollCollection.update();
  }

  /**
   * a getter for the isActive attribute
   *
   * @param user   the owner as Profile
   * @param scroll    the scroll
   * @return          returns isActive as a boolean
   */
  public static boolean isActive(User user, Scroll scroll) {
      ScrollCollection scrollCollection = getByProfileAndScroll(user, scroll);
      return scrollCollection != null && scrollCollection.isActive();
  }


  //////////////////////////////////////////////////
  //  action
  //////////////////////////////////////////////////

      /**
       * checks if the scroll is collected from the owner
       *
       * @param user   owner as profile
       * @param scroll    the scroll
       * @return          returns a boolean
       */
      public static boolean contains(User user, Scroll scroll) {
          ScrollCollection scrollCollection = getByProfileAndScroll(user, scroll);
          return scrollCollection != null;
      }

      /**
       * adds a scroll to the collection of an owner
       *
       * @param user   the owner as profile
       * @param scroll    the scroll
       */
      public static void add(User user, Scroll scroll) {
          ScrollCollection scrollCollection = new ScrollCollection(user, scroll);
          scrollCollection.save();
      }

  //////////////////////////////////////////////////
  //  object find methods
  //////////////////////////////////////////////////

      /**
       * get the scrollCollection object matches to the combination of profile and scroll if it exists
       *
       * @param user   the owner of the object as
       * @param scroll    the scroll
       * @return          returns the scrollCollection object
       */
      private static ScrollCollection getByProfileAndScroll(User user, Scroll scroll) {
          ScrollCollection scrollCollection = ScrollCollection.find.where().eq("user", user).eq("scroll", scroll).findUnique();
          if (scrollCollection == null) {
              return null;
          }
          return scrollCollection;
      }

      /**
       * get a list of all scrollCollection objects that are own by the given profile
       *
       * @param user   the owner of the objects as profile
       * @return          returns a list of all scrollCollection objects
       */
      public static List<ScrollCollection> getScrollCollection(User user) {
          List<ScrollCollection> scrollCollections = ScrollCollection.find.where().eq("user", user).findList();

          if(scrollCollections == null) {
              Logger.warn("ScrollCollection.getScrolls - can't find any scroll");
              return new ArrayList<>();
          }

          return scrollCollections;
      }

      /**
       * get a list of all active scrollCollection objects that are own by the given profile
       *
       * @param user   the owner of the objects as profile
       * @return          returns a list of all scrollCollection objects
       */
      public static List<Scroll> getActiveScrolls(User user) {
          List<ScrollCollection> scrollCollectionList = ScrollCollection.find.where().eq("user", user).eq("isActive", true).findList();
          if (scrollCollectionList == null) {
              Logger.warn("ScrollCollection.getActiveScrolls - no Scrolls found");
              return new ArrayList<>();
          }

          return scrollCollectionList.stream().map(ScrollCollection::getScroll).collect(Collectors.toList());
      }

      public static int getLimit(User user) {
          List<ScrollCollection> scrollCollectionList = getScrollCollection(user);
          Calendar yesterday = Calendar.getInstance();
          yesterday.add(Calendar.DATE, -1);
          int count = 0;

          for(ScrollCollection scrollCollectionItem : scrollCollectionList) {
              if(yesterday.before(scrollCollectionItem.getAdded())) {
                  count++;
              }
          }

          int ScrollLimit = Play.application().configuration().getInt("Game.ScrollLimit");
          return ScrollLimit - count;
      }

      public static void reset(User user) {
          Logger.info("ScrollCollection Reset !!!!");
          List<ScrollCollection> scrollCollectionList = ScrollCollection.find.where().eq("user", user).findList();
          Logger.info("Size: " + scrollCollectionList.size());
          scrollCollectionList.forEach(models.ScrollCollection::delete);
      }
}
