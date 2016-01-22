import play.*;

import bootstrap.*;
import play.mvc.Action;
import play.mvc.Http;

import java.lang.reflect.Method;

public class Global extends GlobalSettings {
  public void onStart(Application app) {
    Logger.info("Application has started");
    BootstrapDB.init();
  }

  public void onStop(Application app) {
    Logger.info("Application shutdown...");
  }

}
