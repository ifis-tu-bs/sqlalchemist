package dao;

import helper.SimpleText;

import models.Map;
import models.Profile;
import models.StoryChallenge;

import play.Logger;

import javax.persistence.PersistenceException;
import java.util.List;

public class StoryChallengeDAO {
  //////////////////////////////////////////////////
  //  Create Method
  //////////////////////////////////////////////////

      public static StoryChallenge create(
              String          name,
              boolean         isTutorial,
              List<SimpleText>texts,
              List<Map>       maps,
              StoryChallenge  next,
              int             level) {

          if(name == null
                  || texts == null || texts.size() == 0
                  || maps == null || maps.size() == 0 ) {
              Logger.warn("StoryChallenge.create - null Values not Accepted");
              return null;
          }

          StoryChallenge storyChallenge = new StoryChallenge(
                  name,
                  isTutorial,
                  maps,
                  next,
                  level);

          try {
              storyChallenge.save();

              if(!storyChallenge.setTexts(texts)) {
                  Logger.warn("StoryChallenge.create - Can't save all Texts");

                  if(storyChallenge.getTexts() != null) {
                      storyChallenge.getTexts().forEach(models.Text::delete);
                  }

                  storyChallenge.delete();
              }
              storyChallenge.update();
          } catch (PersistenceException pe) {
              Logger.warn(pe.getMessage());
          }
          return storyChallenge;
      }

  //////////////////////////////////////////////////
  //  Object Getter Methods
  //////////////////////////////////////////////////
      public static StoryChallenge getForProfile(Profile profile) {
          StoryChallenge challenge;
          if (!profile.isTutorialDone()) {
              challenge = StoryChallenge.find.where().eq("isTutorial", true).findList().get(0);
              profile.setCurrentStory(challenge);
          } else {
              challenge = profile.getCurrentStory();
          }


          profile.update();
          if(challenge != null)
              challenge.setProfile(profile);

          return challenge;
      }

      public static StoryChallenge getFirstLevel() {
          StoryChallenge challenge = StoryChallenge.find.where().eq("isTutorial", true).findList().get(0);
          if(challenge == null) {
              Logger.warn("StoryChallenge.getFirstLevel - No Element found");
              return null;
          }
          challenge = challenge.getNext();
          if(challenge == null) {
              Logger.warn("StoryChallenge.getFirstLevel - Challenge is null");
              return null;
          }
          return challenge;
      }

    public static List<StoryChallenge> getAll() {
        return StoryChallenge.find.all();
    }
}
