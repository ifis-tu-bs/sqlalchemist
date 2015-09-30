package bootstrap;

public class BootstrapDB {
  public static void init() {
    MapBootstrap.init();
    AvatarBootstrap.init();
    StoryChallengeBootstrap.init();
  }
}
