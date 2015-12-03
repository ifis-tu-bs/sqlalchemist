import play.*;

import bootstrap.*;

public class Global extends GlobalSettings {
  public void onStart(Application app) {
    Logger.info("Application has started");
    BootstrapDB.init();
  }

  public void onStop(Application app) {
    Logger.info("Application shutdown...");
  }
}
