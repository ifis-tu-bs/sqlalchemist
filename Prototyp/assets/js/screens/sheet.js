game.SheetScreen = me.ScreenObject.extend({
    /**
     *  action to perform on state change
     */
    onResetEvent: function() {
        me.game.world.addChild(
            new me.Sprite(
                0, 0,
                me.loader.getImage('sheet_screen')
            ),
            1
        );

        onLab = function () {
            me.state.change(me.state.READY);
        };

        function getStats(xmlHttpRequest){

            var stat = JSON.parse(xmlHttpRequest.responseText);
            console.log(stat);

            var avatarName = stat.characterState.currentAvatar.name;
            var filename = stat.characterState.currentAvatar.avatarFilename;
            var team = stat.characterState.currentAvatar.isTeam;
            game.data.sprite = stat.characterState.currentAvatar.avatarFilename;
            console.log(stat.characterState.currentAvatar.id);

            // Text and Image off the Avatare
            me.game.world.addChild(new game.HUD.SkinName(380, 150, avatarName));
            if (!team) {
                me.game.world.addChild(new game.SkinFront(620, 130, filename, team));
            } else {
                me.game.world.addChild(new game.SkinFront(610, 130, filename, team));
            }


        }

        ajaxSendChallengeStoryRequest(getStats);


        // Back to Lab Buttons
        var backButton1  = new game.ClickableElement('backButton1', 'B a c k', onLab, 15, 95,  1, 2.5, 4);
        var backButton2  = new game.ClickableElement('backButton2', 'B a c k', onLab, 15, 95, 84, 2.5, 4);

        me.game.world.addChild(backButton1);
        me.game.world.addChild(backButton2);

        var kind = [0,0,0,0];

        for (var i = 0; i < game.scroll.enchantments.length; i++){
            var j = Math.floor(i/10);
            console.log(game.scroll.enchantments[i].name, game.scroll.enchantments[i].id,"  used: " + game.scroll.enchantments[i].used," availible: " + game.scroll.enchantments[i].available, "position: " + kind[j]);
            if(!game.scroll.enchantments[i].used && game.scroll.enchantments[i].available && kind[j] === 0){
                me.game.world.addChild(new upgrade(760, 345 + 100*j,game.scroll.enchantments[i].name,game.scroll.enchantments[i].id));
                kind[j] = 1;
                console.log("painted");
            }
        }




        // Buttons to flip through the Avatars
        me.game.world.addChild(new skinLeft(440, 130));
        me.game.world.addChild(new skinRight(800, 130));
    }
});




