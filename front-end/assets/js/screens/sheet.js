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

        this.onLab = function () {
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


            this.sheetName      = new game.TextOutputElement('sheetName',      42, 11.25, 28.5, 30, 2);
            this.sheetAttribute = new game.TextOutputElement('sheetAttribute', 20, 45,    30.5, 45, 8);
            this.sheetValues    = new game.TextOutputElement('sheetValues',    10, 45,    58.5, 45, 8);
            me.game.world.addChild(sheetAttribute);
            me.game.world.addChild(sheetValues);
            me.game.world.addChild(sheetName);
            this.sheetName.writeHTML(avatarName, 'sheetNamePara');
            this.sheetAttribute.writeHTML("Health: " + "<br><br>" +
                                          "Speed: " + "<br><br>" +
                                          "Jump: " + "<br><br>" +
                                          "Defense: ", 'sheetAttributePara');
            this.sheetValues.writeHTML(game.stats.health + "<br><br>" +
            game.stats.speed + "<br><br>" +
            game.stats.jump + "<br><br>" +
            game.stats.defense, 'sheetValuesPara');


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
        var backButton1  = new game.ClickableElement('backButton1', 'B a c k', this.onLab, 15, 95,  1, 2.5, 4);
        var backButton2  = new game.ClickableElement('backButton2', 'B a c k', this.onLab, 15, 95, 84, 2.5, 4);
        me.game.world.addChild(backButton1);
        me.game.world.addChild(backButton2);
        $("#backButton1").fadeIn(100);
        $("#backButton2").fadeIn(100);

        var kind = [0,0,0,0];

        for (var i = 0; i < game.scroll.enchantments.length; i++){
            var j = Math.floor(i/10);
            console.log(game.scroll.enchantments[i].name,
                        game.scroll.enchantments[i].id,
                        "  used: " + game.scroll.enchantments[i].used,
                        " availible: " + game.scroll.enchantments[i].available,
                        "position: " + kind[j]);
            if(!game.scroll.enchantments[i].used && game.scroll.enchantments[i].available && kind[j] === 0){

                var name = game.scroll.enchantments[i].name;
                var id   = game.scroll.enchantments[i].id;

                function onUpgrade(name, enchantment){
                    return function () {
                        game.task.potionId = enchantment;
                        game.task.name = name;
                        game.task.kind = 1;
                        console.log("Upgrade: " + game.task.potionId, "Name: " + game.task.name , "Result" + game.scroll.enchantments[game.task.potionId].name)
                        me.state.change(STATE_TASK);
                    }

                }
                var upgrade = new game.ClickableElement('upgrade' + i, '', onUpgrade(i), 4.848485, 8.333333, 57.575757, 44.921875 + 13.020833 * j, 1);
                upgrade.setImage("assets/data/img/collectables/upgrade_scroll.png", "scroll");
                me.game.world.addChild(upgrade);
                $("#upgrade" + i).fadeIn(100);

                //me.game.world.addChild(new upgrade(760, 345 + 100*j,game.scroll.enchantments[i].name,game.scroll.enchantments[i].id));
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
            ajaxSendProfileAvatarIdRequest(game.skin.currentSkin, getAvatarId);
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
            ajaxSendProfileAvatarIdRequest(game.skin.currentSkin, getAvatarId);
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




