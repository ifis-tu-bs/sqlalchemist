game.BeltScreen = me.ScreenObject.extend({

    onResetEvent: function() {

        me.game.world.addChild(
            new me.Sprite(
                0, 0,
                me.loader.getImage('belt_screen')
            ),
            1
        );

        /**var background = new game.BackgroundElement('background', 100, 100, 0, 0);
        background.setImage("assets/data/img/gui/belt_screen.png", "back");
        me.game.world.addChild(background);*/


        //gets the playerstate
        function getAmount(xmlHttpRequest) {
            var amount = JSON.parse(xmlHttpRequest.responseText);
            //console.log(amount);

            //get Potions 1-20
            for (var i = 0; i < amount.characterState.inventory.potions.length; i++) {
                game.potion.potions[i + 1].amount = amount.characterState.inventory.potions[i].count;
            }

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


            console.log("inventory");
            //added the buttons with the differnt Potions, which bring you to the task


            this.brewPotion = function (potion) {
                return function() {
                    console.log(potion);
                    game.task.potionId = potion.id;
                    game.task.kind = 0;
                    game.task.name = potion.name;
                    console.log("Potion: " + game.task.potionId, "Name: " + game.task.name ,"Result" + game.potion.potions[game.task.potionId].name)
                    me.state.change(STATE_TASK);
                }
            };

            this.setPotion = function(potion){
                return function() {
                    this.potion = potion;

                    function onloadBelt(xmlHttpRequest) {
                        //console.log(xmlHttpRequest);
                        me.state.change(STATE_BELT);
                    }

                    function setBelt() {
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
                        setBelt();
                    }
                }

            };


            for (var i = 1; i < 6; i++) {
                if (game.potion.potions[i].available) {
                    //me.game.world.addChild(new potionArrow(29 + 105 * i, 231, game.potion.potions[i]));
                    //me.game.world.addChild(new showPotion(13 + 105 * i, 269, game.potion.potions[i]));
                    var potionName = getPotionName(game.potion.potions[i]);

                    var brewPotion = new game.ClickableElement('brewPotionId' + i, '', this.brewPotion(game.potion.potions[i]), 2.424242, 4.166667, 2.19697 + 7.954545 * i, 30.078125, 1);
                    brewPotion.setImage("assets/data/img/stuff/spinning_scroll_red_32.png", "brew");
                    me.game.world.addChild(brewPotion);
                    $("#brewPotionId" + i).fadeIn(100);

                    var setPotion = new game.ClickableElement('setPotionId' + i, '', this.setPotion(game.potion.potions[i]), 2.424242, 4.166667, 0.984848 + 7.954545 * i, 35.026042, 1);
                    setPotion.setImage("assets/data/img/potion/" + potionName + ".png", "potion");
                    me.game.world.addChild(setPotion);
                    $("#setPotionId" + i).fadeIn(100);

                }
                if (game.potion.potions[i + 5].available) {
                    //me.game.world.addChild(new potionArrow(616 + 105 * i, 231, game.potion.potions[i + 5]));
                    //me.game.world.addChild(new showPotion(600 + 105 * i, 269, game.potion.potions[i + 5]));
                    var potionName = getPotionName(game.potion.potions[i]);

                    var brewPotion = new game.ClickableElement('brewPotionId' + i + 5, '', this.brewPotion(game.potion.potions[i + 5]), 2.424242, 4.166667, 46.666667 + 7.954545 * i, 30.078125, 1);
                    brewPotion.setImage("assets/data/img/stuff/spinning_scroll_red_32.png", "brew");
                    me.game.world.addChild(brewPotion);
                    $("#brewPotionId" + i + 5).fadeIn(100);

                    var setPotion = new game.ClickableElement('setPotionId' + i + 5, '', this.setPotion(game.potion.potions[i + 5]), 2.424242, 4.166667, 45.454545 + 7.954545 * i, 35.026042, 1);
                    setPotion.setImage("assets/data/img/potion/" + potionName + ".png", "potion");
                    me.game.world.addChild(setPotion);
                    $("#setPotionId" + i + 5).fadeIn(100);

                }
                if (game.potion.potions[i + 10].available) {
                    //me.game.world.addChild(new potionArrow(29 + 105 * i, 426, game.potion.potions[i + 10]));
                    //me.game.world.addChild(new showPotion(13 + 105 * i, 464, game.potion.potions[i + 10]));
                    var potionName = getPotionName(game.potion.potions[i]);

                    var brewPotion = new game.ClickableElement('brewPotionId' + i + 10, '', this.brewPotion(game.potion.potions[i + 10]), 2.424242, 4.166667, 2.19697 + 7.954545 * i, 55.46875, 1);
                    brewPotion.setImage("assets/data/img/stuff/spinning_scroll_red_32.png", "brew");
                    me.game.world.addChild(brewPotion);
                    $("#brewPotionId" + i + 10).fadeIn(100);

                    var setPotion = new game.ClickableElement('setPotionId' + i + 10, '', this.setPotion(game.potion.potions[i + 10]), 2.424242, 4.166667, 0.984848 + 7.954545 * i, 60.416667, 1);
                    setPotion.setImage("assets/data/img/potion/" + potionName + ".png", "potion");
                    me.game.world.addChild(setPotion);
                    $("#setPotionId" + i + 10).fadeIn(100);

                }
                if (game.potion.potions[i + 15].available) {
                    //me.game.world.addChild(new potionArrow(616 + 105 * i, 426, game.potion.potions[i + 15]));
                    //me.game.world.addChild(new showPotion(600 + 105 * i, 464, game.potion.potions[i + 15]));
                    var potionName = getPotionName(game.potion.potions[i]);

                    var brewPotion = new game.ClickableElement('brewPotionId' + i + 15, '', this.brewPotion(game.potion.potions[i + 15]), 2.424242, 4.166667, 46.666667 + 7.954545 * i, 55.46875, 1);
                    brewPotion.setImage("assets/data/img/stuff/spinning_scroll_red_32.png", "brew");
                    me.game.world.addChild(brewPotion);
                    $("#brewPotionId" + i + 15).fadeIn(100);

                    var setPotion = new game.ClickableElement('setPotionId' + i + 15, '', this.setPotion(game.potion.potions[i + 15]), 2.424242, 4.166667, 45.454545 + 7.954545 * i, 60.416667, 1);
                    setPotion.setImage("assets/data/img/potion/" + potionName + ".png", "potion");
                    me.game.world.addChild(setPotion);
                    $("#setPotionId" + i + 15).fadeIn(100);

                }
            }

            /**var

            var showPotion = me.GUI_Object.extend(
                {
                    init:function (x, y, potion)
                    {
                        this.potion = potion;

                        settings.image = getPotionName(potion);
                        settings.spritewidth = 64;
                        settings.spriteheight = this.width;
                        // super constructor
                        this._super(me.GUI_Object, "init", [x, y, settings]);
                        // define the object z order
                        this.z = 200;
                    },

                    // output something in the console
                    // when the object is clicked
                    onClick:function (event)
                    {
                        function onloadBelt(xmlHttpRequest) {

                            console.log(xmlHttpRequest);
                            me.state.change(STATE_BELT);
                        }
                        function setBelt() {


                            this.slots = [];


                            for(var i = 1; i <= game.belt.beltSlots.length; i++) {
                                console.log(i-1);
                                if(game.belt.beltSlots[i-1] === null) {
                                    this.slots.push({
                                        "slot" : i,
                                        "potion"  : "empty"
                                    });
                                }else{
                                    this.slots.push({
                                        "slot" : i,
                                        "potion"  : game.belt.beltSlots[i-1].id
                                    });
                                }
                                console.log(this.slots);
                            }
                            this.user_json = JSON.stringify({slots : this.slots});


                            console.log(this.user_json);
                            ajaxSendProfileBeltSetRequest(this.user_json,onloadBelt);
                        }

                        console.log(this.potion.amount + "arrow");
                        var i = 0;

                        while(i < game.belt.beltSlots.length && game.belt.beltSlots[i] !== null ){
                            console.log("giveMeThe i: " + i + "and slot: " + game.belt.beltSlots[i]);
                            i++;
                        };

                        //puts attched potion into the collection
                        if(i < game.belt.beltSlots.length && this.potion.amount > 0){
                            game.belt.beltSlots[i] = this.potion;
                            this.potion.amount --;
                            setBelt();
                        }

                    }
                });*/


            console.log("buttons");
            me.game.world.addChild(new backToTheLab(25, -50));
            //Add BeltSlotButtons
            for (var i = 0; i < game.belt.beltSlots.length; i++) {
                me.game.world.addChild(new beltSlotBelt(226 + 94 * i, 620, game.belt.beltSlots[i], i));
            }


            me.game.world.addChild(new game.HUD.PotionAmount(0, 0), 3);

        }

        ajaxSendChallengeStoryRequest(getAmount);




    }
});