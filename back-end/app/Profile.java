public class Profile {


//////////////////////////////////////////////////
//  Constructor
//////////////////////////////////////////////////
/*

    public boolean shopItemInList(ShopItem shopItem) {
        return this.shopItems.contains(shopItem);
    }

    public void setCurrentScroll(Scroll currentScroll) {
        this.currentScroll = currentScroll;
    }

    public void setUser(User user) {
        this.user = user;
    }

//////////////////////////////////////////////////
//  JSON
//////////////////////////////////////////////////

    public static ObjectNode toJsonHighScoreAll(List<Profile> profiles, Profile profile) {
        ArrayNode newArray = JsonNodeFactory.instance.arrayNode();

        for (Profile profileI : profiles) {
            newArray.add(profileI.toJsonHighScore());
        }

        ObjectNode node = Json.newObject();

        node.set("highScore",   newArray);
        node.put("ownRank",     profile.ownRank + 1);
        node.set("own",         profile.toJsonHighScore());
        return node;
    }

//////////////////////////////////////////////////
//  Ranking Methods
//////////////////////////////////////////////////

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public void setStoryDone() {
        this.storyDone = false;
    }

    public void setShopItems(ArrayList<ShopItem> shopItems) {
        this.shopItems = shopItems;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public void setCoinScale(float coinScale) {
        this.coinScale = coinScale;
    }

    public void setScrollLimit(int scrollLimit) {
        this.scrollLimit = scrollLimit;
    }

    public void setDepthReset(int depthReset) {
        this.depth = depthReset;
    }

    public void setQuote(int quote) {
        this.quote = quote;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public Date getEdited_at() {
        return edited_at;
    }

    public TaskSet getCurrentTaskSet() {
        return currentTaskSet;
    }

    public void setCurrentTaskSet(TaskSet currentTaskSet) {
        this.currentTaskSet = currentTaskSet;
    }

    public void setCurrentHomeWork(HomeWork currentHomeWork) {
        this.currentHomeWork = currentHomeWork;
    }

    public HomeWork getCurrentHomeWork() {
        return currentHomeWork;
    }*/
}
