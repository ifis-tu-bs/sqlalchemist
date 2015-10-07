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



            this.sheetName      = new game.TextOutputElement('sheetName',      36, 5.6, 48, 33.1, 5);
            this.sheetAttribute = new game.TextOutputElement('sheetAttribute', 36, 50, 24, 50, 8);
            this.sheetValues    = new game.TextOutputElement('sheetValues',    36, 50, 78, 50, 8);
            me.game.world.addChild(sheetAttribute);
            me.game.world.addChild(sheetValues);
            me.game.world.addChild(sheetName);
            this.sheetAttribute.writeHTML("HEALTH: " + "<br><br>" +
                                          "SPEED: " + "<br><br>" +
                                          "JUMP: " + "<br><br>" +
                                          "DEFENSE: ", 'sheetAttributePara');











            if (!team) {
                var avatar = new game.BackgroundElement('avatar', 4.84848, 8.33333, 46.969697, 16.927083, 'none');
                avatar.setImage("assets/data/img/avatare/" + filename + "_front.png", "skin");
                $("#avatar").fadeIn(100);
                me.game.world.addChild(avatar);
            } else {
                var avatar = new game.BackgroundElement('avatar', 4.84848, 8.33333, 46.212121, 16.927083, 'none');
                avatar.setImage("assets/data/img/avatare/" + filename + "_front.png", "skin");
                $("#avatar").fadeIn(100);
                me.game.world.addChild(avatar);
            }


        }

        ajaxSendChallengeStoryRequest(getStats);


        // Back to Lab Buttons
        var backButton1  = new game.ClickableElement('backButton1', 'B a c k', onLab, 15, 95,  1, 2.5, 4);
        var backButton2  = new game.ClickableElement('backButton2', 'B a c k', onLab, 15, 95, 84, 2.5, 4);
        me.game.world.addChild(backButton1);
        me.game.world.addChild(backButton2);
        $("#backButton1").fadeIn(100);
        $("#backButton2").fadeIn(100);

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



        this.onLeft = function(){
            console.log("clicked! skinLeft");

            //cycle through the game.skins.available and find next skin available
            do {
                game.skin.currentSkin = (game.skin.currentSkin - 1 + game.skin.skins.length) % game.skin.skins.length;
                console.log(game.skin.currentSkin);
            }while(game.skin.skins[game.skin.currentSkin].available === 0);

            //updates the avatar stats
            function getAvatarId(xmlHttpRequest) {

                var avatarId = JSON.parse(xmlHttpRequest.responseText);
                console.log(avatarId);
                console.log(game.skin.currentSkin);
                game.stats.health = avatarId.attributes.health;
                game.stats.speed = avatarId.attributes.speed;
                game.stats.jump = avatarId.attributes.jump;
                game.stats.defense = avatarId.attributes.defense;
                me.state.change(STATE_SHEET);

            }

            ajaxSendProfileAvatarIdRequest(game.skin.currentSkin ,getAvatarId);
        };


        this.onRight = function(){
            //cycle through the game.skins.available and find next skin available
            do {
                game.skin.currentSkin = (game.skin.currentSkin + 1) % game.skin.skins.length ;
                console.log(game.skin.currentSkin);
            } while(game.skin.skins[game.skin.currentSkin].available === 0);

            //updates the avatar stats
            function getAvatarId(xmlHttpRequest) {

                var avatarId = JSON.parse(xmlHttpRequest.responseText);
                console.log(avatarId);
                console.log(game.skin.currentSkin);
                game.stats.health = avatarId.attributes.health;
                game.stats.speed = avatarId.attributes.speed;
                game.stats.jump = avatarId.attributes.jump;
                game.stats.defense = avatarId.attributes.defense;
                me.state.change(STATE_SHEET);

            }

            ajaxSendProfileAvatarIdRequest(game.skin.currentSkin,getAvatarId);
        };

        var skinLeft  = new game.ClickableElement('skinLeft', '', this.onLeft, 4.848485, 8.333333,  33.33333, 16.927083, 1);
        var skinRight = new game.ClickableElement('skinRight', '', this.onRight, 4.848485, 8.333333,  60.606061, 16.927083, 1);
        skinLeft.setImage("assets/data/img/buttons/character_arrow_left.png", "left");
        skinRight.setImage("assets/data/img/buttons/character_arrow_right.png", "right");
        me.game.world.addChild(skinLeft);
        me.game.world.addChild(skinRight);
        $("#skinLeft").fadeIn(100);
        $("#skinRight").fadeIn(100);



    }
});




