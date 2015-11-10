game.BeltScreen = me.ScreenObject.extend({

    onResetEvent: function() {

        /**
         * Create background-div and add image to it.
         */
        var backgroundBelt = new game.BackgroundElement('backgroundBeltId', 100, 100, 0, 0, 'none');
        backgroundBelt.setImage("assets/data/img/gui/belt_screen.png", "backgroundbelt");
        me.game.world.addChild(backgroundBelt);
        $("#backgroundBeltId").fadeIn(100);

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
            setTimeout( function() {
                me.state.change(me.state.READY);
            }, 100);
        };

        var backFromBelt = new game.ClickableElement('backFromBeltId','', this.backToMenu, 18.1818, 17.7083, 1.893939, -5, 1);
        backFromBelt.setImage("assets/data/img/buttons/new_back_button.png", "back");
        me.game.world.addChild(backFromBelt);
        $("#backFromBeltId").fadeIn(100);


        /**
         * Callback function of AJAX-call to get all information about collected scrolls and available belt slots.
         * @param xmlHttpRequest
         */
        function getAmount(xmlHttpRequest) {
            var amount = JSON.parse(xmlHttpRequest.responseText);
            //console.log(amount);

            //get Potions 1-20
            for (var i = 0; i < amount.characterState.inventory.potions.length; i++) {
                game.potion.potions[i + 1].amount = amount.characterState.inventory.potions[i].count;
            }

            /**
             * Draw belt according to its length.
             */
            var beltLeftImage = new game.BackgroundElement('beltLeftImageId', 7.121212, 12.239583, 8.863636, 78.125, 'none');
            beltLeftImage.setImage("assets/data/img/stuff/belt_left.png", "beltLeft");
            me.game.world.addChild(beltLeftImage);
            $("#beltLeftImageId").fadeIn(100);

            for (var i = 0; i < game.belt.beltSlots.length; i++) {

                var beltMiddleImage = new game.BackgroundElement('beltMiddleImageId' + i, 7.121212, 12.239583, 15.984848 + 7.121212 * i, 78.125, 'none');
                beltMiddleImage.setImage("assets/data/img/stuff/belt_middle.png", "beltMiddle");
                me.game.world.addChild(beltMiddleImage);
                $("#beltMiddleImageId" + i).fadeIn(100);
            }

            var beltRightImage = new game.BackgroundElement('beltRightImageId', 7.121212, 12.239583, 15.984848 + 7.121212 * game.belt.beltSlots.length, 78.125, 'none');
            beltRightImage.setImage("assets/data/img/stuff/belt_right.png", "beltRight");
            me.game.world.addChild(beltRightImage);
            $("#beltRightImageId").fadeIn(100);

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
                        me.state.change(STATE_TASK);
                    }, 100);
                }
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
                        me.state.change(STATE_BELT);
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
                    }

                    //console.log(this.potion.amount + "arrow");
                    var i = 0;

                    while (i < game.belt.beltSlots.length && game.belt.beltSlots[i] !== null) {
                        console.log("giveMeThe i: " + i + "and slot: " + game.belt.beltSlots[i]);
                        i++;
                    }

                    //puts attched potion into the collection
                    if (i < game.belt.beltSlots.length && this.potion.amount > 0) {
                        game.belt.beltSlots[i] = this.potion;
                        this.potion.amount--;
                        this.setBelt();
                    }
                }

            };

            /**
             * Set buttons and images for all potions which the the user collected during the game. Information about
             * the amount of all potion the user owns are also drawn. By clicking on the potion buttons, the user can
             * put the according potion into his belt.
             */
            for (var i = 1; i < 6; i++) {
                if (game.potion.potions[i].available) {
                    var potionName = getPotionName(game.potion.potions[i]);

                    var brewPotion = new game.ClickableElement('brewPotionId' + i, '', this.brewPotion(game.potion.potions[i]),
                                                                2.424242, 4.166667, 2.19697 + 7.954545 * i, 30.078125, 1);
                    brewPotion.setImage("assets/data/img/stuff/spinning_scroll_red_32.png", "brew");
                    me.game.world.addChild(brewPotion);
                    $("#brewPotionId" + i).fadeIn(100);

                    var setPotion = new game.ClickableElement('setPotionId' + i, '', this.setPotion(game.potion.potions[i]),
                                                               4.848485, 8.333333, 0.984848 + 7.954545 * i, 35.026042, 1);
                    setPotion.setImage("assets/data/img/potion/" + potionName + ".png", "potion");
                    me.game.world.addChild(setPotion);
                    $("#setPotionId" + i).fadeIn(100);

                    var potionAmount = new game.TextOutputElement('potionAmountId' + i, 5, 4, -1.787879 + 7.954545 * i, 36.505208, 1);
                    me.game.world.addChild(potionAmount);
                    if (game.potion.potions[i].amount > 9){
                        potionAmount.writeHTML("#9+");
                    } else {
                        potionAmount.writeHTML("#" + game.potion.potions[i].amount);
                    }
                }
                if (game.potion.potions[i + 5].available) {
                    var potionName = getPotionName(game.potion.potions[i + 5]);

                    var brewPotion = new game.ClickableElement('brewPotionId' + i + 5, '', this.brewPotion(game.potion.potions[i + 5]),
                                                                2.424242, 4.166667, 46.666667 + 7.954545 * i, 30.078125, 1);
                    brewPotion.setImage("assets/data/img/stuff/spinning_scroll_red_32.png", "brew");
                    me.game.world.addChild(brewPotion);
                    $("#brewPotionId" + i + 5).fadeIn(100);

                    var setPotion = new game.ClickableElement('setPotionId' + i + 5, '', this.setPotion(game.potion.potions[i + 5]),
                                                               4.848485, 8.333333, 45.454545 + 7.954545 * i, 35.026042, 1);
                    setPotion.setImage("assets/data/img/potion/" + potionName + ".png", "potion");
                    me.game.world.addChild(setPotion);
                    $("#setPotionId" + i + 5).fadeIn(100);

                    var potionAmount = new game.TextOutputElement('potionAmountId' + i + 5, 5, 4, 42.681818 + 7.954545 * i, 36.505208, 1);
                    me.game.world.addChild(potionAmount);
                    if (game.potion.potions[i + 5].amount > 9){
                        potionAmount.writeHTML("#9+");
                    } else {
                        potionAmount.writeHTML("#" + game.potion.potions[i + 5].amount);
                    }
                }
                if (game.potion.potions[i + 10].available) {
                    var potionName = getPotionName(game.potion.potions[i + 10]);

                    var brewPotion = new game.ClickableElement('brewPotionId' + i + 10, '', this.brewPotion(game.potion.potions[i + 10]),
                                                                2.424242, 4.166667, 2.19697 + 7.954545 * i, 55.46875, 1);
                    brewPotion.setImage("assets/data/img/stuff/spinning_scroll_red_32.png", "brew");
                    me.game.world.addChild(brewPotion);
                    $("#brewPotionId" + i + 10).fadeIn(100);

                    var setPotion = new game.ClickableElement('setPotionId' + i + 10, '', this.setPotion(game.potion.potions[i + 10]),
                                                               4.848485, 8.333333, 0.984848 + 7.954545 * i, 60.416667, 1);
                    setPotion.setImage("assets/data/img/potion/" + potionName + ".png", "potion");
                    me.game.world.addChild(setPotion);
                    $("#setPotionId" + i + 10).fadeIn(100);

                    var potionAmount = new game.TextOutputElement('potionAmountId' + i + 10, 5, 4, -1.787879 + 7.954545 * i, 61.895833, 1);
                    me.game.world.addChild(potionAmount);
                    if (game.potion.potions[i + 10].amount > 9){
                        potionAmount.writeHTML("#9+");
                    } else {
                        potionAmount.writeHTML("#" + game.potion.potions[i + 10].amount);
                    }
                }
                if (game.potion.potions[i + 15].available) {
                    var potionName = getPotionName(game.potion.potions[i + 15]);

                    var brewPotion = new game.ClickableElement('brewPotionId' + i + 15, '', this.brewPotion(game.potion.potions[i + 15]),
                                                                2.424242, 4.166667, 46.666667 + 7.954545 * i, 55.46875, 1);
                    brewPotion.setImage("assets/data/img/stuff/spinning_scroll_red_32.png", "brew");
                    me.game.world.addChild(brewPotion);
                    $("#brewPotionId" + i + 15).fadeIn(100);

                    var setPotion = new game.ClickableElement('setPotionId' + i + 15, '', this.setPotion(game.potion.potions[i + 15]),
                                                               4.848485, 8.333333, 45.454545 + 7.954545 * i, 60.416667, 1);
                    setPotion.setImage("assets/data/img/potion/" + potionName + ".png", "potion");
                    me.game.world.addChild(setPotion);
                    $("#setPotionId" + i + 15).fadeIn(100);

                    var potionAmount = new game.TextOutputElement('potionAmountId' + i + 15, 5, 4, 42.681818 + 7.954545 * i, 61.895833, 1);
                    me.game.world.addChild(potionAmount);
                    if (game.potion.potions[i + 15].amount > 9){
                        potionAmount.writeHTML("#9+");
                    } else {
                        potionAmount.writeHTML("#" + game.potion.potions[i + 15].amount);
                    }
                }
            }

            /**
             * Function to put potion fromthe belt back into the collection.
             * @param potion, the potion to put back
             * @param slot, beltslot from which the potion is put back in the collection
             * @returns {Function}
             */
            this.setBeltPotion = function(potion, slot){
                return function() {
                    this.potion = potion;
                    this.slot = slot;

                    //console.log("clicked!beltSlotbelt!" + this.potion + this.slot);

                    function onloadBelt(xmlHttpRequest) {
                        //console.log(xmlHttpRequest);

                        $("#backgroundBeltId").fadeOut(50);
                        $("#backFromBeltId").fadeOut(50);
                        $("[id*='ImageId']").fadeOut(50);
                        $("[id*='brewPotionId']").fadeOut(50);
                        $("[id*='setPotionId']").fadeOut(50);
                        $("[id*='potionAmountId']").fadeOut(50);
                        $("[id*='beltPotionId']").fadeOut(50);
                        me.state.change(STATE_BELT);

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

                    $("#backgroundBeltId").fadeOut(50);
                    $("#backFromBeltId").fadeOut(50);
                    $("[id*='ImageId']").fadeOut(50);
                    $("[id*='brewPotionId']").fadeOut(50);
                    $("[id*='setPotionId']").fadeOut(50);
                    $("[id*='potionAmountId']").fadeOut(50);
                    $("[id*='beltPotionId']").fadeOut(50);
                    me.state.change(STATE_BELT);
                }
            };

            /**
             * Create buttons to put the potions back into the collection.
             */
            for (var i = 0; i < game.belt.beltSlots.length; i++) {
                var potionName = getPotionName(game.belt.beltSlots[i]);

                var beltPotion = new game.ClickableElement('beltPotionId' + i, '', this.setBeltPotion(game.belt.beltSlots[i], i),
                                                            4.848485, 8.333333, 17.121212 + 7.121212 * i, 80.729167, 1);
                beltPotion.setImage("assets/data/img/potion/" + potionName + ".png", "potion");
                me.game.world.addChild(beltPotion);
                $("#beltPotionId" + i).fadeIn(100);
            }

        }

        ajaxSendChallengeStoryRequest(getAmount);
    }
});