package bootstrap;

public class BootstrapDB {
    public static void init() {
        PotionBootstrap.init();
        ScrollBootstrap.init();
        AvatarBootstrap.init();
        ShopItemBootstrap.init();
        MapBootstrap.init();
        StoryChallengeBootstrap.init();
        UserBootstrap.init();
        TaskSetBootstrap.init();
        HomeWorkBootstrap.init();
    }
}
