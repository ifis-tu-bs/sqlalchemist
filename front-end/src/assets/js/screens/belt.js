game.BeltScreen = me.ScreenObject.extend({

    onResetEvent: function() {

        /**
         * Create background-div and add image to it.
         */
        var rootContainer = new game.fdom.RootContainer('/assets/data/img/gui/belt_screen.png');
        me.game.world.addChild(rootContainer);


        var potionLegend = new game.fdom.ImageElement(rootContainer, '2.4%', '4.1%', '26.5%', '4%', 'Image BeltScreen PotionLegend', 'assets/data/img/potion/weak_greypotion.png');
        potionLegend.hide();
        $(potionLegend.getNode()).fadeIn(100);
        me.game.world.addChild(potionLegend);

        var potionLegendText = new game.fdom.TitleElement(rootContainer, '23%','4.1%','3%','4%', 'to attach potion to belt, click: ', 'Title BeltScreen PotionLegendText');
        me.game.world.addChild(potionLegendText);

        var scrollLegend = new game.fdom.ImageElement(rootContainer, '2.4%', '4.1%', '26.5%', '9%', 'Image BeltScreen ScrollLegend', 'assets/data/img/stuff/spinning_scroll_red_32.png');
        scrollLegend.hide();
        $(scrollLegend.getNode()).fadeIn(100);
        me.game.world.addChild(scrollLegend);

        var scrollLegendText = new game.fdom.TitleElement(rootContainer, '23%','4.1%','3%','9%', 'to brew potion, click: ', 'Title BeltScreen ScrollLegendText');
        me.game.world.addChild(scrollLegendText);


        /**
         * Create button and according callback-function to go back to teh Laboratory.
         */
        this.backToMenu = function () {
            $("#backgroundBeltId").fadeOut(100);
            $("#backFromBeltId").fadeOut(100);
            $("[id*='ImageId']").fadeOut(100);
            $("[id*='brewPotionId']").fadeOut(100);
            $("[id*='setPotionId']").fadeOut(100);
            $("[id*='potionAmountId']").fadeOut(100);
            $("[id*='beltPotionId']").fadeOut(100);
            $(rootContainer.getNode()).fadeIn(100);
            setTimeout( function() {
                me.state.change(me.state.READY);
            }, 100);
        };

        var backFromBelt = new game.ClickableElement('backFromBeltId','', this.backToMenu, 18.1818, 17.7083, 80, -5, 1);
        backFromBelt.setImage("assets/data/img/buttons/new_back_button.png", "back");
        me.game.world.addChild(backFromBelt);


        /**
         * Callback function of AJAX-call to get all information about collected scrolls and available belt slots.
         * @param xmlHttpRequest
         */
        function getAmount(xmlHttpRequest) {
            var amount = JSON.parse(xmlHttpRequest.responseText);
            var i;
            //console.log(amount);

            //get Potions 1-20
            for (i = 0; i < amount.characterState.inventory.potions.length; i++) {
                game.potion.potions[i + 1].amount = amount.characterState.inventory.potions[i].count;
            }

            /**
             * Draw belt according to its length.
             */
            var beltLeftImage = new game.BackgroundElement('beltLeftImageId', 7.121212, 12.239583, 8.863636, 78.125, 'none');
            beltLeftImage.setImage("assets/data/img/stuff/belt_left.png", "beltLeft");
            me.game.world.addChild(beltLeftImage);

            for (i = 0; i < game.belt.beltSlots.length; i++) {

                var beltMiddleImage = new game.BackgroundElement('beltMiddleImageId' + i, 7.121212, 12.239583, 15.984848 + 7.121212 * i, 78.125, 'none');
                beltMiddleImage.setImage("assets/data/img/stuff/belt_middle.png", "beltMiddle");
                me.game.world.addChild(beltMiddleImage);
            }

            var beltRightImage = new game.BackgroundElement('beltRightImageId', 7.121212, 12.239583, 15.984848 + 7.121212 * game.belt.beltSlots.length, 78.125, 'none');
            beltRightImage.setImage("assets/data/img/stuff/belt_right.png", "beltRight");
            me.game.world.addChild(beltRightImage);

            /**
             * This function will be called when the user wants to brew a potion. The user is guided to the Taskscreen.
             * @param potion
             * @returns {Function}
             */
            this.brewPotion = function (potion) {
                return function() {
                    //console.log(potion);
                    this.potion = potion;
                    game.task.potionId = this.potion.id;
                    game.task.kind = 0;
                    game.task.name = this.potion.name;
                    //console.log("Potion: " + game.task.potionId, "Name: " + game.task.name ,"Result" + game.potion.potions[game.task.potionId].name);

                    $("#backgroundBeltId").fadeOut(100);
                    $("#backFromBeltId").fadeOut(100);
                    $("[id*='ImageId']").fadeOut(100);
                    $("[id*='brewPotionId']").fadeOut(100);
                    $("[id*='setPotionId']").fadeOut(100);
                    $("[id*='potionAmountId']").fadeOut(100);
                    $("[id*='beltPotionId']").fadeOut(100);
                    setTimeout( function() {
                        game.data.to = STATE_BELT;
                        me.state.change(STATE_TASK);
                    }, 100);
                };
            };

            /**
             * Callback function to attach a potion into the belt and update the the PlayerState.
             * @param potion
             * @returns {Function}
             */
            this.setPotion = function(potion){
                return function() {
                    this.potion = potion;

                    function onloadBelt(xmlHttpRequest) {
                        //console.log(xmlHttpRequest);
                        //me.state.change(STATE_BELT);
                        //$("[id*='potionInBelt']").remove();
                        //potionAmount.clear();
                        repaintPotion();
                        repaintPotionAmount();
                    }

                    this.setBelt = function () {
                        this.slots = [];

                        for (var i = 1; i <= game.belt.beltSlots.length; i++) {
                            //console.log(i-1);
                            if (game.belt.beltSlots[i - 1] === null) {
                                this.slots.push({
                                    "slot": i,
                                    "potion": "empty"
                                });
                            } else {
                                this.slots.push({
                                    "slot": i,
                                    "potion": game.belt.beltSlots[i - 1].id
                                });
                            }
                            //console.log(this.slots);
                        }

                        this.user_json = JSON.stringify({slots: this.slots});

                        //console.log(this.user_json);
                        ajaxSendProfileBeltSetRequest(this.user_json, onloadBelt);
                    };

                    //console.log(this.potion.amount + "arrow");
                    var i = 0;

                    while (i < game.belt.beltSlots.length && game.belt.beltSlots[i] !== null) {
                        //console.log("giveMeThe i: " + i + "and slot: " + game.belt.beltSlots[i]);
                        i++;
                    }

                    //puts attched potion into the collection
                    if (i < game.belt.beltSlots.length && this.potion.amount > 0) {
                        game.belt.beltSlots[i] = this.potion;
                        this.potion.amount--;
                        this.setBelt();
                    }
                };

            };

            /**
             * Set buttons and images for all potions which the the user collected during the game. Information about
             * the amount of all potion the user owns are also drawn. By clicking on the potion buttons, the user can
             * put the according potion into his belt.
             */
            var potionName;
            for (i = 1; i < 6; i++) {
                var brewPotion;
                var setPotion;
                var potionAmount;
                if (game.potion.potions[i].available) {
                    potionName = getPotionName(game.potion.potions[i]);

                    brewPotion = new game.ClickableElement('brewPotionId' + i, '', this.brewPotion(game.potion.potions[i]),
                                                                2.424242, 4.166667, 2.19697 + 7.954545 * i, 30.078125, 1);
                    brewPotion.setImage("assets/data/img/stuff/spinning_scroll_red_32.png", "brew");
                    me.game.world.addChild(brewPotion);

                    setPotion = new game.ClickableElement('setPotionId' + i, '', this.setPotion(game.potion.potions[i]),
                                                               4.848485, 8.333333, 0.984848 + 7.954545 * i, 35.026042, 1);
                    setPotion.setImage("assets/data/img/potion/" + potionName + ".png", "potion");
                    me.game.world.addChild(setPotion);

                    potionAmount = new game.TextOutputElement('potionAmountId' + i, 5, 4, -1.787879 + 7.954545 * i, 36.505208, 1);
                    me.game.world.addChild(potionAmount);
                    if (game.potion.potions[i].amount > 9){
                        potionAmount.writeHTML("#9+","Amount");
                    } else {
                        potionAmount.writeHTML("#" + game.potion.potions[i].amount,"Amount"+ 1);
                    }
                }
                if (game.potion.potions[i + 5].available) {
                    potionName = getPotionName(game.potion.potions[i + 5]);

                    brewPotion = new game.ClickableElement('brewPotionId' + (i + 5), '', this.brewPotion(game.potion.potions[i + 5]),
                                                                2.424242, 4.166667, 46.666667 + 7.954545 * i, 30.078125, 1);
                    brewPotion.setImage("assets/data/img/stuff/spinning_scroll_red_32.png", "brew");
                    me.game.world.addChild(brewPotion);

                    setPotion = new game.ClickableElement('setPotionId' + (i + 5), '', this.setPotion(game.potion.potions[i + 5]),
                                                               4.848485, 8.333333, 45.454545 + 7.954545 * i, 35.026042, 1);
                    setPotion.setImage("assets/data/img/potion/" + potionName + ".png", "potion");
                    me.game.world.addChild(setPotion);

                    potionAmount = new game.TextOutputElement('potionAmountId' + (i + 5), 6 , 8, 42.681818 + 7.954545 * i, 36.505208, 2);
                    me.game.world.addChild(potionAmount);
                    if (game.potion.potions[i + 5].amount > 9){
                        potionAmount.writeHTML("#9+","Amount" + (i + 5));
                    } else {
                        potionAmount.writeHTML("#" + game.potion.potions[i + 5].amount,"Amount" + (i + 5));
                    }
                }
                if (game.potion.potions[i + 10].available) {
                    potionName = getPotionName(game.potion.potions[i + 10]);

                    brewPotion = new game.ClickableElement('brewPotionId' + (i + 10), '', this.brewPotion(game.potion.potions[i + 10]),
                                                                2.424242, 4.166667, 2.19697 + 7.954545 * i, 55.46875, 1);
                    brewPotion.setImage("assets/data/img/stuff/spinning_scroll_red_32.png", "brew");
                    me.game.world.addChild(brewPotion);

                    setPotion = new game.ClickableElement('setPotionId' + (i + 10), '', this.setPotion(game.potion.potions[i + 10]),
                                                               4.848485, 8.333333, 0.984848 + 7.954545 * i, 60.416667, 1);
                    setPotion.setImage("assets/data/img/potion/" + potionName + ".png", "potion");
                    me.game.world.addChild(setPotion);

                    potionAmount = new game.TextOutputElement('potionAmountId' + (i + 10), 5, 4, -1.787879 + 7.954545 * i, 61.895833, 1);
                    me.game.world.addChild(potionAmount);
                    if (game.potion.potions[i + 10].amount > 9){
                        potionAmount.writeHTML("#9+","Amount");
                    } else {
                        potionAmount.writeHTML("#" + game.potion.potions[i + 10].amount,"Amount" + (i + 10));
                    }
                }
                if (game.potion.potions[i + 15].available) {
                    potionName = getPotionName(game.potion.potions[i + 15]);

                    brewPotion = new game.ClickableElement('brewPotionId' + (i + 15), '', this.brewPotion(game.potion.potions[i + 15]),
                                                                2.424242, 4.166667, 46.666667 + 7.954545 * i, 55.46875, 1);
                    brewPotion.setImage("assets/data/img/stuff/spinning_scroll_red_32.png", "brew");
                    me.game.world.addChild(brewPotion);

                    setPotion = new game.ClickableElement('setPotionId' + (i + 15), '', this.setPotion(game.potion.potions[i + 15]),
                                                               4.848485, 8.333333, 45.454545 + 7.954545 * i, 60.416667, 1);
                    setPotion.setImage("assets/data/img/potion/" + potionName + ".png", "potion");
                    me.game.world.addChild(setPotion);

                    potionAmount = new game.TextOutputElement('potionAmountId' + (i + 15), 5, 4, 42.681818 + 7.954545 * i, 61.895833, 1);
                    me.game.world.addChild(potionAmount);
                    if (game.potion.potions[i + 15].amount > 9){
                        potionAmount.writeHTML("#9+","Amount");
                    } else {
                        potionAmount.writeHTML("#" + game.potion.potions[i + 15].amount,"Amount"+ (i + 15));
                    }
                }
            }

            /**
             * Function to put potion from the belt back into the collection.
             * @param potion, the potion to put back
             * @param slot, beltslot from which the potion is put back in the collection
             * @returns {Function}
             */
            this.setBeltPotion = function(potion, slot){
                return function() {
                    console.log("Set Belt Ption: ",potion,slot);
                    console.log("Belt2: ",game.belt.beltSlots);
                    //this.potion = potion;
                    this.potion = game.belt.beltSlots[slot];
                    this.slot = slot;

                    //console.log("clicked!beltSlotbelt!" + this.potion + this.slot);

                    function onloadBelt(xmlHttpRequest) {
                        //console.log(xmlHttpRequest);

                        repaintPotion();
                        repaintPotionAmount();
                        /*
                        $("#backgroundBeltId").fadeOut(50);
                        $("#backFromBeltId").fadeOut(50);
                        $("[id*='ImageId']").fadeOut(50);
                        $("[id*='brewPotionId']").fadeOut(50);
                        $("[id*='setPotionId']").fadeOut(50);
                        $("[id*='potionAmountId']").fadeOut(50);
                        $("[id*='beltPotionId']").fadeOut(50);
                        me.state.change(STATE_BELT);*/

                    }

                    /**
                     * update the information about the beltSlots.
                     */
                    this.setBelt = function () {

                        this.slots = [];

                        for (var i = 1; i <= game.belt.beltSlots.length; i++) {
                            //console.log(i-1);
                            if (game.belt.beltSlots[i-1] === null) {
                                this.slots.push({
                                    "slot" : i,
                                    "potion"  : "empty"
                                });
                            } else {
                                this.slots.push({
                                    "slot" : i,
                                    "potion"  : game.belt.beltSlots[i-1].id
                                });
                            }
                            //console.log(this.slots);
                        }
                        this.user_json = JSON.stringify({slots : this.slots});

                        //console.log(this.user_json);
                        ajaxSendProfileBeltSetRequest(this.user_json, onloadBelt);
                    };

                    if( game.belt.beltSlots[this.slot] !== null){
                        game.belt.beltSlots[this.slot] = null;
                        this.potion.amount ++;
                        //console.log(this.potion.amount);
                        this.setBelt();
                    }
                    /*
                    $("#backgroundBeltId").fadeOut(50);
                    $("#backFromBeltId").fadeOut(50);
                    $("[id*='ImageId']").fadeOut(50);
                    $("[id*='brewPotionId']").fadeOut(50);
                    $("[id*='setPotionId']")  .fadeOut(50);
                     $("[id*='potionAmountId']").fadeOut(50);
                     $("[id*='beltPotionId']").fadeOut(50);
                    me.state.change(STATE_BELT);
                    */

                };
            };

            /**
             * Create buttons to put the potions back into the collection.
             */
            for (i = 0; i < game.belt.beltSlots.length; i++) {
                potionName = getPotionName(game.belt.beltSlots[i]);

                var beltPotion = new game.ClickableElement('beltPotionId' + i, '', this.setBeltPotion(game.belt.beltSlots[i], i),
                    4.848485, 8.333333, 17.121212 + 7.121212 * i, 80.729167, 1);
                beltPotion.setImage("assets/data/img/potion/" + potionName + ".png", "potionInBelt");
                me.game.world.addChild(beltPotion);
            }

            //Update Potion Amount
            function repaintPotionAmount(){

                for (i = 1; i < 6; i++) {
                    var potionAmount;
                    if (game.potion.potions[i].available) {

                        if (game.potion.potions[i].amount > 9) {
                            $("#potionAmountId" + (i)+" #Amount"+ (i)).text("#9+");
                        } else {
                            $("#potionAmountId" + (i)+" #Amount"+ (i)).text("#" + game.potion.potions[i].amount);
                        }

                    }
                    if (game.potion.potions[i + 5].available) {

                        if (game.potion.potions[i + 5].amount > 9) {
                            $("#potionAmountId" + (i + 5)+" #Amount"+ (i + 5)).text("#9+");
                        } else {
                            $("#potionAmountId" + (i + 5)+" #Amount"+ (i + 5)).text("#" + game.potion.potions[i + 5].amount);
                        }
                    }
                    if (game.potion.potions[i + 10].available) {


                        if (game.potion.potions[i + 10].amount > 9) {
                            $("#potionAmountId" + (i + 10)+" #Amount"+ (i + 10)).text("#9+");
                        } else {
                            $("#potionAmountId" + (i + 10)+" #Amount"+ (i + 10)).text("#" + game.potion.potions[i + 10].amount);
                        }
                    }
                    if (game.potion.potions[i + 15].available) {


                        if (game.potion.potions[i + 15].amount > 9) {
                            $("#potionAmountId" + (i + 15)+" #Amount"+ (i + 15)).text("#9+");
                        } else {
                            $("#potionAmountId" + (i + 15)+" #Amount"+ (i + 15)).text("#" + game.potion.potions[i + 15].amount);
                        }
                    }
                }
            }

            //Update the Potion in the Belt
            function repaintPotion(){

                console.log("Belt: ",game.belt.beltSlots);

                /**
                 * Create buttons to put the potions back into the collection.
                 */
                for (i = 0; i < game.belt.beltSlots.length; i++) {
                    potionName = getPotionName(game.belt.beltSlots[i]);
                    console.log(potionName);
                    $("#beltPotionId"+ i+ " #potionInBelt").attr("src","assets/data/img/potion/" + potionName + ".png");
                }
            }
        }
        ajaxSendChallengeStoryRequest(getAmount);
    }
});
