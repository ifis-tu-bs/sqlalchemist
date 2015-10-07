game.ShopScreen = me.ScreenObject.extend({
    /**
     *  action to perform on state change
     */
    onResetEvent: function () {

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


        this.backToMenu = function () {

            //$("#avatar").fadeOut(100);
            setTimeout( function() {
                me.state.change(me.state.MENU);
            }, 100);
        };
        var backFromShop = new game.ClickableElement('backFromShop','', this.backToMenu, 18.1818, 17.7083, 77, -5, 1);
        backFromShop.setImage("assets/data/img/buttons/new_back_button.png", "back");
        me.game.world.addChild(backFromShop);
        $("#backFromShop").fadeIn(100);



        function getShopInfo(xmlHttpRequest) {

            function goBuy(id) {
                return function() {
                    game.data.shopId = id;
                    me.state.change(STATE_BUY);
                }
            }

            var shop = JSON.parse(xmlHttpRequest.responseText);
            console.log(shop);
            game.data.shop = shop;

            for (var i = 0; i < 7; i++) {
                if (shop[i].avatar.isTeam) {
                    this.difference = 0.5575;
                    this.size = 1.0151;
                } else if (!shop[i].bought) {
                    this.difference = 0;
                    this.size       = 0;
                }
                var shopItem = new game.ClickableElement('shopItem' + i, '', goBuy(i), 4.948 + this.size, 8.433, 23.436 - this.difference, 18.129167 + i * 10.9375, 1);
                shopItem.setImage("assets/data/img/avatare/" + shop[i].thumbnailUrl + "_front.png", "avatar");
                $("#shopItem" + i).fadeIn(100);
                me.game.world.addChild(shopItem);
            }

            for (var i = 7; i < 14; i++) {
                if (shop[i].avatar.isTeam) {
                    this.difference = 0.5575;
                    this.size = 1.0151;
                } else if (!shop[i].bought) {
                    this.difference = 0;
                    this.size       = 0;
                }
                var shopItem = new game.ClickableElement('shopItem' + i, '', goBuy(i), 4.848 + this.size, 8.333, 33.330 - this.difference, 18.129167 + (i - 7) * 10.9375, 1);
                shopItem.setImage("assets/data/img/avatare/" + shop[i].thumbnailUrl + "_front.png", "avatar");
                $("#shopItem" + i).fadeIn(100);
                me.game.world.addChild(shopItem);
            }

            for (var i = 14; i < 21; i++) {
                if (shop[i].avatar.isTeam) {
                    this.difference = 0.5575;
                    this.size = 1.0151;
                } else if (!shop[i].bought) {
                    this.difference = 0;
                    this.size       = 0;
                }
                var shopItem = new game.ClickableElement('shopItem' + i, '', goBuy(i), 4.848 + this.size, 8.333, 38.942 - this.difference, 18.129167 + (i - 14) * 10.9375, 1);
                shopItem.setImage("assets/data/img/avatare/" + shop[i].thumbnailUrl + "_front.png", "avatar");
                $("#shopItem" + i).fadeIn(100);
                me.game.world.addChild(shopItem);
            }

            for (var i = 21; i < 27; i++) {
                if (shop[i].avatar.isTeam) {
                    this.difference = 0.5575;
                    this.size = 1.0151;
                } else if (!shop[i].bought) {
                    this.difference = 0;
                    this.size       = 0;
                }
                var shopItem = new game.ClickableElement('shopItem' + i, '', goBuy(i), 4.848 + this.size, 8.333, 51.994 - this.difference, 29.06666 + (i - 21) * 10.9375, 1);
                shopItem.setImage("assets/data/img/avatare/" + shop[i].thumbnailUrl + "_front.png", "avatar");
                $("#shopItem" + i).fadeIn(100);
                me.game.world.addChild(shopItem);
            }

            for (var i = 27; i < 32; i++) {
                if (shop[i].avatar.isTeam) {
                    this.difference = 0.5575;
                    this.size = 1.0151;
                } else if (!shop[i].bought) {
                    this.difference = 0;
                    this.size       = 0;
                }
                var shopItem = new game.ClickableElement('shopItem' + i, '', goBuy(i), 4.848 + this.size, 8.333, 57.782 - this.difference, 40.00417 + (i - 27) * 10.9375, 1);
                shopItem.setImage("assets/data/img/avatare/" + shop[i].thumbnailUrl + "_front.png", "avatar");
                $("#shopItem" + i).fadeIn(100);
                me.game.world.addChild(shopItem);
            }

            for (var i = 32; i < 38; i++) {
                if (shop[i].avatar.isTeam) {
                    this.difference = 0.5575;
                    this.size = 1.0151;
                } else if (!shop[i].bought) {
                    this.difference = 0;
                    this.size       = 0;
                }
                var shopItem = new game.ClickableElement('shopItem' + i, '', goBuy(i), 4.848 + this.size, 8.333, 70.2545 - this.difference, 29.06666 + (i - 32) * 10.9375, 1);
                shopItem.setImage("assets/data/img/avatare/" + shop[i].thumbnailUrl + "_front.png", "avatar");
                $("#shopItem" + i).fadeIn(100);
                me.game.world.addChild(shopItem);
            }



            /**for (var i = 0; i < 7; i++) {
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
            }*/

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