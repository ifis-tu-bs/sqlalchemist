game.ShopScreen = me.ScreenObject.extend({
    /**
     *  action to perform on state change
     */
    onResetEvent: function () {

        // workaround f�r android bug CB-4404
        if (me.device.android || me.device.android2) {
            if (me.device.isFullscreen) {
                me.device.exitFullscreen();
                document.body.style.minHeight = document.body.clientHeight + 'px';
            }
        }

        me.game.world.addChild(
            new me.Sprite(
                0, 0,
                me.loader.getImage('shop_screen')
            ),
            1
        );
        function getCash(xmlHttpRequest) {
            var profile = JSON.parse(xmlHttpRequest.responseText);
            game.data.lofiCoins = profile.coins;

            me.game.world.addChild(new game.HUD.LofiCoins(230, 660),3);

        }
        ajaxSendProfileRequest(getCash);

        me.game.world.addChild(new backToMenu(1050,-50));

        function getShopInfo(xmlHttpRequest) {

            console.log(xmlHttpRequest);
            var shop = JSON.parse(xmlHttpRequest.responseText);
            console.log(shop);
            game.data.shop = shop;


            //add the skins into the Screen to bring you to the buying screen
            for (var i = 0; i < 7; i++) {
                if (!shop[i].bought) {
                    this.size = 64;
                    this.difference = 0;
                    if (shop[i].avatar.isTeam) {
                        this.size = 84;
                        this.difference = 10;
                    }
                    me.game.world.addChild(new showSkin(312 - this.difference, 140 + 84 * (i - 0), shop[i].thumbnailUrl, this.size, shop[i].id, i));
                }
            }
            for (var i = 7; i < 14; i++) {
                if (!shop[i].bought) {
                    this.size = 64;
                    this.difference = 0;
                    if (shop[i].avatar.isTeam) {
                        this.size = 84;
                        this.difference = 10;
                    }
                    me.game.world.addChild(new showSkin(436 - this.difference, 140 + 84 * (i - 7), shop[i].thumbnailUrl, this.size, shop[i].id, i));
                }
            }
            for (var i = 14; i < 21; i++) {
                if (!shop[i].bought) {
                    this.size = 64;
                    this.difference = 0;
                    if (shop[i].avatar.isTeam) {
                        this.size = 84;
                        this.difference = 10;
                    }
                    me.game.world.addChild(new showSkin(518 - this.difference, 140 + 84 * (i - 14), shop[i].thumbnailUrl, this.size, shop[i].id, i));
                }
            }
            for (var i = 21; i < 27; i++) {
                if (!shop[i].bought) {
                    this.size = 64;
                    this.difference = 0;
                    if (shop[i].avatar.isTeam) {
                        this.size = 84;
                        this.difference = 10;
                    }
                    me.game.world.addChild(new showSkin(685 - this.difference, 224 + 84 * (i - 21), shop[i].thumbnailUrl, this.size, shop[i].id, i));
                }
            }
            for (var i = 27; i < 32; i++) {
                if (!shop[i].bought) {
                    this.size = 64;
                    this.difference = 0;
                    if (shop[i].avatar.isTeam) {
                        this.size = 84;
                        this.difference = 10;
                    }
                    me.game.world.addChild(new showSkin(768 - this.difference, 308 + 84 * (i - 27), shop[i].thumbnailUrl, this.size, shop[i].id, i));
                }
            }
            for (var i = 32; i < 38; i++) {
                if (!shop[i].bought) {
                    this.size = 64;
                    this.difference = 0;
                    if (shop[i].avatar.isTeam) {
                        this.size = 84;
                        this.difference = 10;
                    }
                    me.game.world.addChild(new showSkin(930 - this.difference, 224 + 84 * (i - 32), shop[i].thumbnailUrl, this.size, shop[i].id, i));
                }
            }

        }

        ajaxSendShopRequest(getShopInfo);

        function getBeltInfo(xmlHttpRequest) {
            console.log("BELT");
            console.log(xmlHttpRequest);
            var belt = JSON.parse(xmlHttpRequest.responseText);
            console.log(belt);
            game.data.beltShop = belt;
            console.log("IN");

            var t = 0;

            while( belt[t].bought && t < belt.length ){
                //console.log(belt[t].id,!belt[t].bought,t );
                t++;
            }
            me.game.world.addChild(new buyBelt(850, 40 , belt[t].id, t));
        }

        ajaxSendShopBeltRequest(getBeltInfo);
    }
});