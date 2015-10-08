game.BuyScreen = me.ScreenObject.extend({

    /**
     *  action to perform on state change
     */
    onResetEvent : function() {

        console.log(game.data.shopId);
        console.log(game.data.shop[game.data.shopId]);

        me.game.world.addChild(
            new me.Sprite (
                0,0,
                me.loader.getImage('shop_screen')
            ),
            0
        );

        me.game.world.addChild(new game.HUD.LofiCoins(230, 660),3);

        me.game.world.addChild(
            new me.Sprite (
                0,0,
                me.loader.getImage('faded_lab_screen')
            ),
           1
        );

        me.game.world.addChild(
            new me.Sprite (
                -107,122,
                me.loader.getImage('price_tag')
            ),
            2
        );

        // Creates a Button that brings you back to the last Screen

        if(game.data.spriteId > 41){
            me.game.world.addChild( new game.HUD.BuyBelt(543, 300),3);
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

            var discription = game.data.shop[game.data.shopId].desc.split("\\n");

            var nameAndPrice    = new game.TextOutputElement('nameAndPrice', 50, 11, 36.136364, 28.0625, 3);
            var discriptiontext = new game.TextOutputElement('discriptiontext', 60, 25.666666, 26, 43.1, 7);
            me.game.world.addChild(nameAndPrice);
            me.game.world.addChild(discriptiontext);

            nameAndPrice.writeHTML("Name: " + game.data.shop[game.data.shopId].name  + "<br><br>" +
                                   "Price: " + game.data.shop[game.data.shopId].price, 'namePara');

            for(var i = 0; i < discription.length; i++) {
                discriptiontext.writeHTML(discription[i] + "<br>", 'discPara');
            }

        }

        this.toShop = function() {
            $("#backToShop").fadeOut(100);
            $("#buyButton").fadeOut(100);
            setTimeout( function() {
                me.state.change(STATE_SHOP);
            }, 100);
        };

        stopDouble = function() {
            game.data.playing = false;
        };

        this.onBuy = function() {
            function bought(xmlHttpRequest) {

                console.log(xmlHttpRequest);
                if(xmlHttpRequest.status === 400 && game.data.sound && !game.data.playing){
                    game.data.playing = true;
                    me.audio.play("fail", false, stopDouble, game.data.soundVolume);
                }


                var session = JSON.parse(xmlHttpRequest.responseText);

                console.log(game.data.lofiCoins);
                if (game.data.lofiCoins > session.coins) {
                    me.audio.play("cash", false, null, game.data.soundVolume);
                    me.state.change(STATE_SHOP);
                } else if (game.data.sound && !game.data.playing){
                    console.log("else");
                    game.data.playing = true;
                    me.audio.play("fail", false, stopDouble, game.data.soundVolume);
                }
                game.data.lofiCoins = session.coins;

            }
            ajaxSendShopIdRequest(game.data.spriteId, bought);
        };

        var backToShop = new game.ClickableElement('backToShop','Back',this. toShop, 20, 7, 30, 73, 1);
        var buyButton  = new game.ClickableElement('buyButton','Buy', this.onBuy, 20, 7, 60, 73, 1);
        me.game.world.addChild(backToShop);
        me.game.world.addChild(buyButton);
        $("#backToShop").fadeIn(100);
        $("#buyButton").fadeIn(100);
    }

});