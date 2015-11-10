game.BuyScreen = me.ScreenObject.extend({

    onResetEvent : function() {

        //console.log(game.data.shopId);
        //console.log(game.data.shop[game.data.shopId]);

        /**
         * Create background-div and add images to it.
         */
        var backgroundShop = new game.BackgroundElement('backgroundShopId', 100, 100, 0, 0, 'none');
        backgroundShop.setImage("assets/data/img/gui/shop_screen.png", "backgroundshop");
        me.game.world.addChild(backgroundShop);
        $("#backgroundShopId").fadeIn(100);
        var backgroundFaded = new game.BackgroundElement('backgroundFadedId', 100, 100, 0, 0, 'none');
        backgroundFaded.setImage("assets/data/img/gui/faded_lab_screen.png", "backgroundfaded");
        me.game.world.addChild(backgroundFaded);
        $("#backgroundFadedId").fadeIn(100);
        var backgroundTag = new game.BackgroundElement('backgroundTagId', 88.863636, 68.489583, 0, 15.885417, 'none');
        backgroundTag.setImage("assets/data/img/gui/price_tag.png", "backgroundtag");
        me.game.world.addChild(backgroundTag);
        $("#backgroundTagId").fadeIn(100);

        /**
         * display amount of available coins.
         */
        var lofiCoinsFrame = new game.BackgroundElement('lofiCoinsId', 18.257576, 12.369792, 0, 82.291667, 'none');
        lofiCoinsFrame.setImage("assets/data/img/gui/LofiCoinBox.png", "lofiCoinsImg");
        me.game.world.addChild(lofiCoinsFrame);
        $("#lofiCoinsId").fadeIn(100);

        var lofiCoins = new game.TextOutputElement('lofiCoins', 15, 6, 2.7, 86, 1);
        me.game.world.addChild(lofiCoins);
        lofiCoins.writeHTML(game.data.lofiCoins, 'lofiCoinsPara');

        /**
         * Create elements to draw information about the item which is to be bought.
         * @type {Array|*}
         */
        var discription = game.data.shop[game.data.shopId].desc.split("\\n");

        var nameAndPrice    = new game.TextOutputElement('nameAndPrice', 50, 11, 36.136364, 28.0625, 3);
        var discriptiontext = new game.TextOutputElement('discriptiontext', 60, 25.666666, 26, 43.1, 7);
        me.game.world.addChild(nameAndPrice);
        me.game.world.addChild(discriptiontext);

        /**
         * Write descriptions, name and price to according item.
         */
        if (game.data.spriteId > 41) {
            nameAndPrice.writeHTML("Name: " + game.data.beltShop[game.data.shopId].name  + "<br><br>" +
                                   "Price: " + game.data.beltShop[game.data.shopId].price, 'namePara');
            discriptiontext.writeHTML("Get a brand new beltslot! The more slots you have in your belt, the more potions " +
                                      "you can take into the dungeon!", 'discPara');

            var buyBeltPotion = new game.BackgroundElement('buyBeltPotion', 7.272727, 12.5, 26.015152, 26.041667, 'none');
            buyBeltPotion.setImage("assets/data/img/potion/GREAT_JUMPPOTION.png", "skin");
            $("#buyBeltPotion").fadeIn(100);
            me.game.world.addChild(buyBeltPotion);

        } else {
            var filename = game.data.shop[game.data.shopId].thumbnailUrl;
            if (game.data.shop[game.data.shopId].avatar.isTeam) {
                var avatar = new game.BackgroundElement('avatar', 9.545455, 12.5, 26.015152, 26.041667, 'none');
                avatar.setImage("assets/data/img/avatare/" + filename + "_front.png", "skin");
                $("#avatar").fadeIn(100);
                me.game.world.addChild(avatar);
            } else {
                var avatar = new game.BackgroundElement('avatar', 7.272727, 12.5, 27.272727, 26.041667, 'none');
                avatar.setImage("assets/data/img/avatare/" + filename + "_front.png", "skin");
                $("#avatar").fadeIn(100);
                me.game.world.addChild(avatar);
            }

            nameAndPrice.writeHTML("Name: " + game.data.shop[game.data.shopId].name  + "<br><br>" +
                                   "Price: " + game.data.shop[game.data.shopId].price, 'namePara');

            for (var i = 0; i < discription.length; i++) {
                discriptiontext.writeHTML(discription[i] + "<br>", 'discPara');
            }

        }

        /**
         * Function to go back to the shop.
         */
        this.toShop = function() {
            $("#backToShop").fadeOut(100);
            $("#buyButton").fadeOut(100);
            $("#lofiCoinsId").fadeOut(100);
            $("#lofiCoins").fadeOut(100);
            $("#avatar").fadeOut(100);
            $("#buyBeltPotion").fadeOut(100);
            $("[id*='background']").fadeOut(100);
            //$("#backgroundShopId").fadeOut(100);
            //$("#backgroundFadedId").fadeOut(100);
            //$("#backgroundTagId").fadeOut(100);
            setTimeout( function() {
                me.state.change(STATE_SHOP);
            }, 100);
        };

        stopDouble = function() {
            game.data.playing = false;
        };

        /**
         * push information about bought item onto the server via ajax PUSH-Request.
         */
        this.onBuy = function() {
            function bought(xmlHttpRequest) {

                //console.log(xmlHttpRequest);
                if(xmlHttpRequest.status === 400 && game.data.sound && !game.data.playing){
                    game.data.playing = true;
                    me.audio.play("fail", false, stopDouble, game.data.soundVolume);
                }

                var session = JSON.parse(xmlHttpRequest.responseText);

                /**
                 * If the user is not able to purchase some item, an other sound will be played.s
                 */
                //console.log(game.data.lofiCoins);
                if (game.data.lofiCoins > session.coins) {
                    me.audio.play("cash", false, null, game.data.soundVolume);
                    me.state.change(STATE_SHOP);
                } else if (game.data.sound && !game.data.playing){
                    game.data.playing = true;
                    me.audio.play("fail", false, stopDouble, game.data.soundVolume);
                }
                game.data.lofiCoins = session.coins;

            }
            ajaxSendShopIdRequest(game.data.spriteId, bought);
        };

        /**
         * Create necessary buttons to go back to the shop or to buy an item.
         */
        var backToShop = new game.ClickableElement('backToShop','Back',this. toShop, 20, 7, 30, 73, 1);
        var buyButton  = new game.ClickableElement('buyButton','Buy', this.onBuy, 20, 7, 60, 73, 1);
        me.game.world.addChild(backToShop);
        me.game.world.addChild(buyButton);
        $("#backToShop").fadeIn(100);
        $("#buyButton").fadeIn(100);
    }

});