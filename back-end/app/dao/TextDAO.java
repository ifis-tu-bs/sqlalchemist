package dao;

import helper.Random;

import models.Text;

import play.Logger;

import java.util.List;
import javax.persistence.PersistenceException;

public class TextDAO {
  //////////////////////////////////////////////////
  //  Create Method
  //////////////////////////////////////////////////

      public static Text create(
              int type,
              int chronology,
              int prerequisite,
              String text,
              String sound_url,
              int lines) {

          if(text == null) {
              return null;
          }

          Text item = new Text(type, text, sound_url, prerequisite, chronology, lines);

          try {
              item.save();
          } catch (PersistenceException pe) {
              Logger.warn("Text.create - caught PersistenceException: " + pe.getMessage());
              Text text_comp = Text.find.where().eq("text", text).findUnique();
              if(text_comp != null) {
                  Logger.warn("Text.create - found alternative \"Text\"");
                  return text_comp;
              }
              Logger.error("Text.create - no alternative \"Text\" found");
              return null;
          }

          return item;
      }


  //////////////////////////////////////////////////
  //  Object Getter Methods
  //////////////////////////////////////////////////

      private static Text getTextByType(int type) {
          List<Text> texts = Text.find.where().eq("type", type).findList();

          int size = texts.size();
          if(size != 0) {
              int i = Random.randomInt(0, size - 1);
              return texts.get(i);
          }
          Logger.warn("Text.getTextByType - found no texts");
          return null;
      }

      /**
       *
       */
      public static Text getTerrySuccessful() {
          return getTextByType(Text.TEXT_TYPE_TERRY_SUCCESSFUL);
      }
      /**
       *
       */
      public static Text getTerryFailure() {
          return getTextByType(Text.TEXT_TYPE_TERRY_FAILURE);
      }

      /**
       *
       */
      public static Text getTerryUrge() {
          return getTextByType(Text.TEXT_TYPE_TERRY_URGE);
      }
}
