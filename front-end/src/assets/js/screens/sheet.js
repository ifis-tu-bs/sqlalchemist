game.SheetScreen = me.ScreenObject.extend({
    /**
     *  action to perform on state change
     */
    onResetEvent: function() {

        /**
         * Create background-div and add image to it.
         */
        var backgroundSheet = new game.BackgroundElement('backgroundSheetId', 100, 100, 0, 0, 'none');
        backgroundSheet.setImage("assets/data/img/gui/sheet_screen.png", "backgroundsheet");
        me.game.world.addChild(backgroundSheet);
        $("#backgroundSheetId").fadeIn(100);

        function fadeOutElements(){
            $("#backgroundSheetId").fadeOut(100);
            $("#avatar1").fadeOut(100);
            $("#avatar2").fadeOut(100);
            $("#backButton1").fadeOut(100);
            $("#backButton2").fadeOut(100);
            $("[id^=upgrade]").fadeOut(100);
            $("#skin").fadeOut(100);
            $("#skinLeft").fadeOut(100);
            $("#skinRight").fadeOut(100);
        }

        /**
         * Create back-buttons with matching function and effects
         */
        this.onLab = function () {
            fadeOutElements();
            setTimeout( function() {
                me.state.change(me.state.READY);
            }, 100);
        };

        var backButton1  = new game.ClickableElement('backButton1', 'B a c k', this.onLab, 15, 95,  1, 2.5, 4);
        var backButton2  = new game.ClickableElement('backButton2', 'B a c k', this.onLab, 15, 95, 84, 2.5, 4);
        me.game.world.addChild(backButton1);
        me.game.world.addChild(backButton2);


        var sheetName      = new game.TextOutputElement('sheetName',      42, 11.25, 28.5, 30, 2);
        var sheetAttribute = new game.TextOutputElement('sheetAttribute', 20, 45,    30.5, 45, 8);
        var sheetValues    = new game.TextOutputElement('sheetValues',    5,  45,    63.5, 45, 8);

        avatar2 = new game.BackgroundElement('avatar2', 6.363636, 8.33333, 46.212121, 16.927083, 'none');
        avatar1 = new game.BackgroundElement('avatar1', 4.84848, 8.33333, 46.969697, 16.927083, 'none');

        me.game.world.addChild(sheetAttribute);
        me.game.world.addChild(sheetValues);
        me.game.world.addChild(sheetName);

        var repaint = false;
        /**
         * Ajax-Call to get the characters stats.
         * @param xmlHttpRequest: contains the characterState
         */
        function getStats(xmlHttpRequest){

            var stat = JSON.parse(xmlHttpRequest.responseText);
            console.log("Stat:",stat);

            var avatarName = stat.characterState.currentAvatar.name;
            var filename = stat.characterState.currentAvatar.avatarFilename;
            var team = stat.characterState.currentAvatar.isTeam;
            game.data.sprite = stat.characterState.currentAvatar.avatarFilename;
            //console.log(stat.characterState.currentAvatar.id);


            /**
             * set values to according TextOutPutElements
             */
            sheetAttribute.clear();
            sheetValues.clear();
            sheetName.clear();

            sheetName.writeHTML(avatarName, 'sheetNamePara');
            sheetAttribute.writeHTML("Health: " + "<br><br>" +
                                          "Speed: " + "<br><br>" +
                                          "Jump: " + "<br><br>" +
                                          "Defense: ", 'sheetAttributePara');
            sheetValues.writeHTML(game.stats.health + "<br><br>" +
                                       game.stats.speed + "<br><br>" +
                                       game.stats.jump + "<br><br>" +
                                       game.stats.defense, 'sheetValuesPara');

            /**
             * The position of the avatars need to be set differently. If the current avatar is a team or not.
             */
            var avatar;
            if(!repaint){
                repaint = true;
                avatar1 = new game.BackgroundElement('avatar1', 4.84848, 8.33333, 46.969697, 16.927083, 'none');
                avatar2 = new game.BackgroundElement('avatar2', 6.363636, 8.33333, 46.212121, 16.927083, 'none');

                if (!team) {

                    avatar1.setImage("assets/data/img/avatare/" + filename + "_front.png", "skin");
                    avatar1.display();
                } else {

                    avatar2.setImage("assets/data/img/avatare/" + filename + "_front.png", "skin");
                    avatar2.display();
                }
                me.game.world.addChild(avatar1);
                me.game.world.addChild(avatar2);
            }else{
                if (!team) {

                    avatar2.hide();
                    $("#avatar1 #skin").attr("src","assets/data/img/avatare/" + filename + "_front.png");
                    avatar1.display();
                } else {

                    avatar1.hide();
                    $("#avatar2 #skin").attr("src","assets/data/img/avatare/" + filename + "_front.png");
                    avatar2.display();
                }
            }

        }
        ajaxSendChallengeStoryRequest(getStats);


        /**
         * @type {number[]} Array to set position of enchantment
         */
        var kind = [0,0,0,0];

        function onUpgrade(name, id){
            return function () {
                game.task.name = name;
                game.task.potionId = id;
                game.task.kind = 1;
                /*console.log("Upgrade: " + game.task.potionId,
                            "Name: " + game.task.name,
                            "Result" + game.scroll.enchantments[game.task.potionId].name);*/
                me.state.change(STATE_TASK);
            };
        }

       for (var i = 0; i < game.scroll.enchantments.length; i++){
            var j = Math.floor(i/10);
            /*console.log(game.scroll.enchantments[i].name,
                        game.scroll.enchantments[i].id,
                        "used: "      + game.scroll.enchantments[i].used,
                        "availible: " + game.scroll.enchantments[i].available,
                        "position: "  + kind[j]);*/
            if(!game.scroll.enchantments[i].used && game.scroll.enchantments[i].available && kind[j] === 0){

                var name = game.scroll.enchantments[i].name;
                var id   = game.scroll.enchantments[i].id;
                var upgrade = new game.ClickableElement('upgrade' + i, '', onUpgrade(name, id), 3.5, 5.5,
                                                        57.575757, 50 + 11 * j, 1);
                upgrade.setImage("assets/data/img/collectables/upgrade_scroll.png", "scroll");
                me.game.world.addChild(upgrade);
                kind[j] = 1;
                //console.log("painted");
            }
        }

        /**
         * Create buttons to go trough all purchased skins. Callback function allows to cycle in left/right direction
         * trough all available skins and set the the chosen skin one to the current.
         */
        this.onLeft = function(){
            //console.log("clicked! skinLeft");
            do {
                game.skin.currentSkin = (game.skin.currentSkin - 1 + game.skin.skins.length) % game.skin.skins.length;
                //console.log(game.skin.currentSkin);
            } while(game.skin.skins[game.skin.currentSkin].available === 0);

            /**
             * Ajax-call to get according skin-stats.
             * @param xmlHttpRequest contains according stats.
             */
            function getAvatarId(xmlHttpRequest) {

                var avatarId = JSON.parse(xmlHttpRequest.responseText);
                //console.log(avatarId);
                //console.log(game.skin.currentSkin);
                game.stats.health = avatarId.attributes.health;
                game.stats.speed = avatarId.attributes.speed;
                game.stats.jump = avatarId.attributes.jump;
                game.stats.defense = avatarId.attributes.defense;

                ajaxSendChallengeStoryRequest(getStats);
            }
            ajaxSendProfileAvatarIdRequest(game.skin.currentSkin, getAvatarId);
        };

        var skinLeft  = new game.ClickableElement('skinLeft', '', this.onLeft, 4.848485, 8.333333,  33.33333, 16.927083, 1);
        skinLeft.setImage("assets/data/img/buttons/character_arrow_left.png", "left");
        me.game.world.addChild(skinLeft);


        this.onRight = function(){
            do {
                game.skin.currentSkin = (game.skin.currentSkin + 1) % game.skin.skins.length ;
                //console.log(game.skin.currentSkin);
            } while(game.skin.skins[game.skin.currentSkin].available === 0);

            function getAvatarId(xmlHttpRequest) {
                var avatarId = JSON.parse(xmlHttpRequest.responseText);
                //console.log(avatarId);
                //console.log(game.skin.currentSkin);
                game.stats.health = avatarId.attributes.health;
                game.stats.speed = avatarId.attributes.speed;
                game.stats.jump = avatarId.attributes.jump;
                game.stats.defense = avatarId.attributes.defense;

                ajaxSendChallengeStoryRequest(getStats);
            }
            ajaxSendProfileAvatarIdRequest(game.skin.currentSkin, getAvatarId);
        };

        var skinRight = new game.ClickableElement('skinRight', '', this.onRight, 4.848485, 8.333333,  60.606061, 16.927083, 1);
        skinRight.setImage("assets/data/img/buttons/character_arrow_right.png", "right");
        me.game.world.addChild(skinRight);
    }
});
