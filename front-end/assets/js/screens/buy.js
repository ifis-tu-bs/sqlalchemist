game.BuyScreen = me.ScreenObject.extend({

    /**
     *  action to perform on state change
     */
    onResetEvent : function() {

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
        }else{
            if (game.data.shop[game.data.shopId].avatar.isTeam == true) {
                me.game.world.addChild(new game.SkinFront(350,200,game.data.shop[game.data.shopId].thumbnailUrl, game.data.shop[game.data.shopId].avatar.isTeam));
            } else {
                me.game.world.addChild(new game.SkinFront(360,200,game.data.shop[game.data.shopId].thumbnailUrl, game.data.shop[game.data.shopId].avatar.isTeam));

            }

            // The result text
            me.game.world.addChild( new game.HUD.Buy(543, 300),3);
        }


        toShop = function() {
            me.state.change(STATE_SHOP);
        };

        stopDouble = function() {
            game.data.playing = false;
        };

        onBuy = function() {
            function bought(xmlHttpRequest) {

                console.log(xmlHttpRequest);
                if(xmlHttpRequest.status === 400 && game.data.sound && !game.data.playing){
                    game.data.playing = true;
                    me.audio.play("fail", false, stopDouble, game.data.soundVolume);
                }


                var session = JSON.parse(xmlHttpRequest.responseText);

                console.log(game.data.lofiCoins);
                if (game.data.sound && game.data.lofiCoins > session.coins) {
                    me.audio.play("cash", false, null, game.data.soundVolume);
                    me.state.change(STATE_SHOP);
                }else if (game.data.sound && !game.data.playing){
                    console.log("else");
                    game.data.playing = true;
                    me.audio.play("fail", false, stopDouble, game.data.soundVolume);
                }
                game.data.lofiCoins = session.coins;

            }
            ajaxSendShopIdRequest(game.data.spriteId, bought);
        };

        var backToShop = new game.ClickableElement('backToShop','Back', toShop, 20, 7, 30, 73, 1);
        var buyButton  = new game.ClickableElement('buyButton','Buy', onBuy, 20, 7, 60, 73, 1);

        me.game.world.addChild(backToShop);
        me.game.world.addChild(buyButton);
    }

});