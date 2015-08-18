import play.*;

/**
 * asd
 * Created by fabiomazzone on 27/04/15.
 */
public class Global extends GlobalSettings {
    /**
     *
     * @param app
     */
    public void onStart(Application app) {
        Logger.info("Application has started");
        super.onStart(app);

    }

    /**
     *
     * @param app
     */
    public void onStop(Application app){
        Logger.info("Application shutdown");
        super.onStop(app);
    }
}
