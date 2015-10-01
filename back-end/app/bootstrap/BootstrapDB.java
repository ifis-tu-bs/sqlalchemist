package bootstrap;

public class BootstrapDB {
  public static void init() {
    
    PotionBootstrap.init();
    AvatarBootstrap.init();

    MapBootstrap.init();
    StoryChallengeBootstrap.init();
  }
}
