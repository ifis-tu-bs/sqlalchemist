game.BeltScreen = me.ScreenObject.extend({

    onResetEvent: function() {
        console.log("background");
        me.game.world.addChild(
            new me.Sprite(
                0, 0,
                me.loader.getImage('belt_screen')
            ),
            1
        );

        //gets the playerstate
        function getAmount(xmlHttpRequest) {
            console.log("getstats");
            var amount = JSON.parse(xmlHttpRequest.responseText);
            console.log(amount);

            //get Potions 1-20
            for (var i = 0; i < amount.characterState.inventory.potions.length; i++) {
                game.potion.potions[i + 1].amount = amount.characterState.inventory.potions[i].count;
            }
            //the belt background
            me.game.world.addChild(
                new me.Sprite(
                    117, 600,
                    me.loader.getImage('belt_left')
                ),
                2
            );
            for (var i = 0; i < game.belt.beltSlots.length; i++) {
                me.game.world.addChild(
                    new me.Sprite(
                        211 + 94 * i, 600,
                        me.loader.getImage('belt_middle')
                    ),
                    2
                );
            }
            me.game.world.addChild(
                new me.Sprite(
                    211 + 94 * game.belt.beltSlots.length, 600,
                    me.loader.getImage('belt_right')
                ),
                2
            );


            console.log("inventory");
            //added the buttons with the differnt Potions, which bring you to the task
            for (var i = 1; i < 6; i++) {
                if (game.potion.potions[i].available) {
                    me.game.world.addChild(new potionArrow(29 + 105 * i, 231, game.potion.potions[i]));
                    me.game.world.addChild(new showPotion(13 + 105 * i, 269, game.potion.potions[i]));
                }
                if (game.potion.potions[i + 5].available) {
                    me.game.world.addChild(new potionArrow(616 + 105 * i, 231, game.potion.potions[i + 5]));
                    me.game.world.addChild(new showPotion(600 + 105 * i, 269, game.potion.potions[i + 5]));
                }
                if (game.potion.potions[i + 10].available) {
                    me.game.world.addChild(new potionArrow(29 + 105 * i, 426, game.potion.potions[i + 10]));
                    me.game.world.addChild(new showPotion(13 + 105 * i, 464, game.potion.potions[i + 10]));
                }
                if (game.potion.potions[i + 15].available) {
                    me.game.world.addChild(new potionArrow(616 + 105 * i, 426, game.potion.potions[i + 15]));
                    me.game.world.addChild(new showPotion(600 + 105 * i, 464, game.potion.potions[i + 15]));
                }
            }


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