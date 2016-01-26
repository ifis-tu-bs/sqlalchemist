
import akka.actor.*;
import play.*;

import bootstrap.*;

import play.api.libs.concurrent.Akka;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;


public class Global extends GlobalSettings {

  ActorSystem system;
  ActorRef userCleanUpActor;

  public void onStart(Application app) {
    Logger.info("Application has started");
    BootstrapDB.init();
    system = Akka.system(app.getWrappedApplication());
    userCleanUpActor = system.actorOf(UserCleanupActor.props);
    system.scheduler().schedule(
        Duration.create(10, TimeUnit.DAYS),
        Duration.create(1, TimeUnit.DAYS),
        userCleanUpActor,
        "tick",
        system.dispatcher(),
        null
    );

  }

  public void onStop(Application app) {
    Logger.info("Application shutdown...");
  }

}
