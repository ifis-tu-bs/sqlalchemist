package dao;

import models.Profile;
import models.Scroll;
import models.ScrollCollection;

import play.Logger;
import play.Play;

import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;

public class ScrollCollectionDAO {
  /**
   * a setter method that sets a scroll to active
   *
   * @param profile   the owner as Profile
   * @param scroll    the scroll
   */
  public static void setActive(Profile profile, Scroll scroll) {
      ScrollCollection scrollCollection = getByProfileAndScroll(profile, scroll);
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
   * @param profile   the owner as Profile
   * @param scroll    the scroll
   * @return          returns isActive as a boolean
   */
  public static boolean isActive(Profile profile, Scroll scroll) {
      ScrollCollection scrollCollection = getByProfileAndScroll(profile, scroll);
      return scrollCollection != null && scrollCollection.isActive();
  }


  //////////////////////////////////////////////////
  //  action
  //////////////////////////////////////////////////

      /**
       * checks if the scroll is collected from the owner
       *
       * @param profile   owner as profile
       * @param scroll    the scroll
       * @return          returns a boolean
       */
      public static boolean contains(Profile profile, Scroll scroll) {
          ScrollCollection scrollCollection = getByProfileAndScroll(profile, scroll);
          return scrollCollection != null;
      }

      /**
       * adds a scroll to the collection of an owner
       *
       * @param profile   the owner as profile
       * @param scroll    the scroll
       */
      public static void add(Profile profile, Scroll scroll) {
          ScrollCollection scrollCollection = new ScrollCollection(profile, scroll);
          scrollCollection.save();
      }

  //////////////////////////////////////////////////
  //  object find methods
  //////////////////////////////////////////////////

      /**
       * get the scrollCollection object matches to the combination of profile and scroll if it exists
       *
       * @param profile   the owner of the object as profile
       * @param scroll    the scroll
       * @return          returns the scrollCollection object
       */
      public static ScrollCollection getByProfileAndScroll(Profile profile, Scroll scroll) {
          ScrollCollection scrollCollection = ScrollCollection.find.where().eq("profile", profile).eq("scroll", scroll).findUnique();
          if (scrollCollection == null) {
              return null;
          }
          return scrollCollection;
      }

      /**
       * get a list of all scrollCollection objects that are own by the given profile
       *
       * @param profile   the owner of the objects as profile
       * @return          returns a list of all scrollCollection objects
       */
      public static List<ScrollCollection> getScrollCollection(Profile profile) {
          List<ScrollCollection> scrollCollections = ScrollCollection.find.where().eq("profile", profile).findList();

          if(scrollCollections == null) {
              Logger.warn("ScrollCollection.getScrolls - can't find any scroll");
              return new ArrayList<>();
          }

          return scrollCollections;
      }

      /**
       * get a list of all active scrollCollection objects that are own by the given profile
       *
       * @param profile   the owner of the objects as profile
       * @return          returns a list of all scrollCollection objects
       */
      public static List<Scroll> getActiveScrolls(Profile profile) {
          List<ScrollCollection> scrollCollectionList = ScrollCollection.find.where().eq("profile", profile).eq("isActive", true).findList();
          if (scrollCollectionList == null) {
              Logger.warn("ScrollCollection.getActiveScrolls - no Scrolls found");
              return null;
          }

          List<Scroll> scrollList = new ArrayList<>();

          for(ScrollCollection scrollCollection : scrollCollectionList) {
              scrollList.add(scrollCollection.getScroll());
          }

          return scrollList;
      }

      public static int getLimit(Profile profile) {
          List<ScrollCollection> scrollCollectionList = getScrollCollection(profile);
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

      public static void reset(Profile profile) {
          List<ScrollCollection> scrollCollectionList = ScrollCollection.find.where().eq("profile", profile).findList();

          for(ScrollCollection scrollCollection : scrollCollectionList) {
              scrollCollection.delete();
          }

      }
}
