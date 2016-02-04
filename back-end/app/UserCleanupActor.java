import akka.actor.*;

import dao.UserDAO;
import models.User;
import play.Logger;

import java.util.Calendar;
import java.util.List;

/**
 * Created by fabiomazzone on 25/01/16.
 */
public class UserCleanupActor extends UntypedActor{

  public static Props props = Props.create(UserCleanupActor.class);

  @Override
  public void onReceive(Object message) throws Exception {
    if(message instanceof String && message.equals("tick")) {
      Logger.info("User Cleanup - Run");

      Calendar calendar = Calendar.getInstance();

      calendar.add(Calendar.DAY_OF_YEAR, -7);

      List<User> userList = UserDAO.getAllUnverifiedUser();

      userList.stream().filter(user -> user.getCreatedAt().before(calendar)).forEach(user -> {
        Logger.info("Delete User: " + user.getUsername());
        user.delete();
      });

      Logger.info("User Cleanup - Done");
    }
  }
}
