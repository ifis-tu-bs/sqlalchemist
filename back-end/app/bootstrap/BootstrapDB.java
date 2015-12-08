package bootstrap;

public class BootstrapDB {
    public static void init() {
        BootstrapRole.init();
        ScrollBootstrap.init();
        ShopItemBootstrap.init();
        MapBootstrap.init();
        StoryChallengeBootstrap.init();
        UserBootstrap.init();
        TaskSetBootstrap.init();
        HomeWorkBootstrap.init();
    }
}
