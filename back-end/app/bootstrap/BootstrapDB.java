package bootstrap;

public class BootstrapDB {
    public static void init() {
        ScrollBootstrap.init();
        ShopItemBootstrap.init();
        MapBootstrap.init();
        StoryChallengeBootstrap.init();
        UserBootstrap.init();
        TaskSetBootstrap.init();
    }
}
