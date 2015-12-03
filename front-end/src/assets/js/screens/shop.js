game.ShopScreen = me.ScreenObject.extend({
    /**
     *  action to perform on state change
     */
    onResetEvent: function () {

        /**
         * Create background-div and add image to it.
         */
        var backgroundShop = new game.BackgroundElement('backgroundShopId', 100, 100, 0, 0, 'none');
        backgroundShop.setImage("assets/data/img/gui/shop_screen.png", "backgroundshop");
        me.game.world.addChild(backgroundShop);

        /**
         * Ajax Call to get information about the LofiCoin amount the user owns and draw them in an OutputElement.
         * @param xmlHttpRequest
         */
        function getCash(xmlHttpRequest) {
            var profile = JSON.parse(xmlHttpRequest.responseText);
            game.data.lofiCoins = profile.coins;

            var lofiCoins = new game.TextOutputElement('lofiCoins', 15, 6, 2.7, 86, 1);
            me.game.world.addChild(lofiCoins);
            lofiCoins.writeHTML(game.data.lofiCoins, 'lofiCoinsPara');
        }
        ajaxSendProfileRequest(getCash);

        function fadeOutElements(){
            $("[id*='shopItem']").fadeOut(100);
            $("#backgroundShopId").fadeOut(100);
            $("#lofiCoins").fadeOut(100);
            $("#backFromShop").fadeOut(100);
            $("#buyBeltSlotId").fadeOut(100);
        }

        /**
         * Create Element to go back to the menu.
         */
        this.backToMenu = function () {
            fadeOutElements();
            setTimeout( function() {
                me.state.change(me.state.MENU);
            }, 100);
        };

        var backFromShop = new game.ClickableElement('backFromShop','', this.backToMenu, 18.1818, 17.7083, 77, -5, 1);
        backFromShop.setImage("assets/data/img/buttons/new_back_button.png", "back");
        me.game.world.addChild(backFromShop);


        /**
         * Get information about all shopItems which are not bought yet.
         * @param xmlHttpRequest contains shop infos.
         */
        function getShopInfo(xmlHttpRequest) {

            var shop = JSON.parse(xmlHttpRequest.responseText);
            console.log(shop);
            game.data.shop = shop;

            /**
             * This function will be called when the user wants to buy an item and the user is guided to the BuyScreen.
             * @param id
             * @param spriteid
             * @returns {Function}
             */
            function goBuy(id, spriteid) {
                return function() {
                    game.data.shopId = id;
                    game.data.spriteId = spriteid;
                    fadeOutElements();
                    setTimeout( function() {
                        me.state.change(STATE_BUY);
                    }, 100);
                };
            }

            /**
             * Display all 500-coin-items that are not bought yet.
             */
             var shopItem;
             var i;
            for (i = 0; i < 7; i++) {
                if (!shop[i].bought) {
                    if (shop[i].avatar.isTeam) {
                        this.difference = 0.5575;
                        this.size = 1.0151;
                    } else {
                        this.difference = 0;
                        this.size       = 0;
                    }
                    shopItem = new game.ClickableElement('shopItem' + i, '', goBuy(i, shop[i].id), 4.948 + this.size,
                                                             8.433, 23.436 - this.difference, 18.129167 + i * 10.9375, 1);
                    shopItem.setImage("assets/data/img/avatare/" + shop[i].thumbnailUrl + "_front.png", "avatar");
                    me.game.world.addChild(shopItem);
                }
            }

            /**
             * Display seven first 2000-coin-items that are not bought yet.
             */
            for (i = 7; i < 14; i++) {
                if (!shop[i].bought) {
                    if (shop[i].avatar.isTeam) {
                        this.difference = 0.5575;
                        this.size = 1.0151;
                    } else {
                        this.difference = 0;
                        this.size       = 0;
                    }
                    shopItem = new game.ClickableElement('shopItem' + i, '', goBuy(i, shop[i].id), 4.848 + this.size,
                                                            8.333, 33.330 - this.difference, 18.129167 + (i - 7) * 10.9375, 1);
                    shopItem.setImage("assets/data/img/avatare/" + shop[i].thumbnailUrl + "_front.png", "avatar");
                    me.game.world.addChild(shopItem);
                }
            }

            /**
             * Display next seven 2000-coin-items that are not bought yet.
             */
            for (i = 14; i < 21; i++) {
                if (!shop[i].bought) {
                    if (shop[i].avatar.isTeam) {
                        this.difference = 0.5575;
                        this.size = 1.0151;
                    } else {
                        this.difference = 0;
                        this.size       = 0;
                    }
                    shopItem = new game.ClickableElement('shopItem' + i, '', goBuy(i, shop[i].id), 4.848 + this.size,
                                                            8.333, 38.942 - this.difference, 18.129167 + (i - 14) * 10.9375, 1);
                    shopItem.setImage("assets/data/img/avatare/" + shop[i].thumbnailUrl + "_front.png", "avatar");
                    me.game.world.addChild(shopItem);
                }
            }

            /**
             * Display first row of 8000-coin-items that are not bought yet.
             */
            for (i = 21; i < 27; i++) {
                if (!shop[i].bought) {
                    if (shop[i].avatar.isTeam) {
                        this.difference = 0.5575;
                        this.size = 1.0151;
                    } else {
                        this.difference = 0;
                        this.size = 0;
                    }
                    shopItem = new game.ClickableElement('shopItem' + i, '', goBuy(i, shop[i].id), 4.848 + this.size,
                                                            8.333, 51.994 - this.difference, 29.06666 + (i - 21) * 10.9375, 1);
                    shopItem.setImage("assets/data/img/avatare/" + shop[i].thumbnailUrl + "_front.png", "avatar");
                    me.game.world.addChild(shopItem);
                }
            }

            /**
             * Display next row of 8000-coin-items that are not bought yet.
             */
            for (i = 27; i < 32; i++) {
                if (!shop[i].bought) {
                    if (shop[i].avatar.isTeam) {
                        this.difference = 0.5575;
                        this.size = 1.0151;
                    } else {
                        this.difference = 0;
                        this.size       = 0;
                    }
                    shopItem = new game.ClickableElement('shopItem' + i, '', goBuy(i, shop[i].id), 4.848 + this.size,
                                                             8.333, 57.782 - this.difference, 40.00417 + (i - 27) * 10.9375, 1);
                    shopItem.setImage("assets/data/img/avatare/" + shop[i].thumbnailUrl + "_front.png", "avatar");
                    me.game.world.addChild(shopItem);
                }
            }

            /**
             * Display 20000-coin-items that are not bought yet.
             */
            for (i = 32; i < 38; i++) {
                if (!shop[i].bought) {
                    if (shop[i].avatar.isTeam) {
                        this.difference = 0.5575;
                        this.size = 1.0151;
                    } else {
                        this.difference = 0;
                        this.size = 0;
                    }
                    shopItem = new game.ClickableElement('shopItem' + i, '', goBuy(i, shop[i].id), 4.848 + this.size,
                                                            8.333, 70.2545 - this.difference, 29.06666 + (i - 32) * 10.9375, 1);
                    shopItem.setImage("assets/data/img/avatare/" + shop[i].thumbnailUrl + "_front.png", "avatar");
                    me.game.world.addChild(shopItem);
                }
            }

        }

        ajaxSendShopRequest(getShopInfo);

        /**
         * Call to check if or how many BeltSlots the user already bought.
         * @param xmlHttpRequest
         */
        function getBeltInfo(xmlHttpRequest) {

            /**
             * If the user wants to buy a new BeltSlot this function will be called an the user is guided to the BuyScreen.
             * @param id: id of the shopItem.
             * @param i: next slot to be bought.
             * @returns {Function}
             */
            function goBuyBeltSlot(id, i) {
                return function () {
                    this.shopId = i;
                    this.spriteId = id;
                    console.log(this.shopId + "Belt" + this.spriteId);
                    game.data.spriteId = this.spriteId;
                    game.data.shopId = this.shopId;

                    fadeOutElements();
                    setTimeout( function() {
                        me.state.change(STATE_BUY);
                    }, 100);
                };
            }

            //console.log("BELT");
            //console.log(xmlHttpRequest);
            var belt = JSON.parse(xmlHttpRequest.responseText);
            //console.log(belt);
            game.data.beltShop = belt;
            //console.log("IN");

            var t = 0;

            while( belt[t].bought && t < belt.length ){
                //console.log(belt[t].id,!belt[t].bought,t );
                t++;
            }

            /**
             * Button to buy a new BeltSlot.
             */
            var buyBeltSlot = new game.ClickableElement('buyBeltSlotId', '', goBuyBeltSlot(belt[t].id, t),
                                                         7.121212, 44.010417, 63.5, 5.208333, 1);
            buyBeltSlot.setImage("assets/data/img/buttons/shopbelt.png", "beltSlotShop");
            me.game.world.addChild(buyBeltSlot);
        }

        ajaxSendShopBeltRequest(getBeltInfo);

    }
});
