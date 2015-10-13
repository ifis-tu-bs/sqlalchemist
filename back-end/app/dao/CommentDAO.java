package dao;

import models.Comment;
import models.Profile;

import play.Logger;

import javax.persistence.PersistenceException;

public class CommentDAO {

  //////////////////////////////////////////////////
  //  Create Method
  //////////////////////////////////////////////////

  public static Comment create(Profile profile, String text) {
    if(profile == null || text == null) {
      return null;
    }

    Comment comment = new Comment(profile, text);
    try{
      comment.save();
    } catch (PersistenceException pe) {
      Logger.warn("Comment.create - caught PersistenceException: " + pe.getMessage());
      Comment comment_comp = Comment.find.where().eq("text", text).findUnique();
      if(comment_comp != null) {
        Logger.warn("Comment.create - found alternative \"Comment\"");
        return comment_comp;
      }
      Logger.error("Comment.create - no alternative \"Comment\" found");
      return null;
    }
    return comment;
  }
}
