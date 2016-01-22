game.ProfileScreen = me.ScreenObject.extend({

    /**
     *  action to perform on state change
     */
    onResetEvent : function() {

        /**
         * Create background-div and add image to it.
         */
        var rootContainer = new game.fdom.RootContainer('/assets/data/img/gui/settings_screen.png');
        me.game.world.addChild(rootContainer);

        var title = new game.fdom.TitleElement(rootContainer, '100%','19%','0%','5%', 'Profile', 'Title ProfileScreen');
        me.game.world.addChild(title);

        var avatarBox = new game.fdom.ImageElement(rootContainer, '17.8%', '26.7%', '16.2%', '4.1%', 'Image ProfileScreen AvatarBox', 'assets/data/img/stuff/schokofrosch.png');
        avatarBox.hide();
        $(avatarBox.getNode()).fadeIn(100);
        me.game.world.addChild(avatarBox);

        /**
         * Create button for state change back to the menu with corresponding callback function.
         */
        var backToLabButton = new game.fdom.ButtonElement(rootContainer, '20%','20%','73%','2%', '', 'Button ProfileScreen Back', false, function() {
            $("#backgroundProfileId").fadeOut(100);
            $("#backFromProfile").fadeOut(100);
            $("#avatar").fadeOut(100);
            $(rootContainer.getNode()).fadeOut(100);
            setTimeout( function() {
                me.state.change(me.state.MENU);
            }, 100);
        });
        backToLabButton.hide();
        $(backToLabButton.getNode()).fadeIn(100);
        me.game.world.addChild(backToLabButton);

        /**
         * Create TextOutputElements to draw some useful information
         */
        var attribute = new game.TextOutputElement('attribute', 36, 58, 14, 34, 9);
        var values    = new game.TextOutputElement('valuesProfile', 36, 58, 48, 33.1, 9);
        me.game.world.addChild(attribute);
        me.game.world.addChild(values);


        /**
         * This function will be called when Ajax-Call ProfileRequest succeeds.
         * @param xmlHttpRequest contains the playerState
         */
        function profileReply(xmlHttpRequest){
            var playerstate_JSON = JSON.parse(xmlHttpRequest.responseText);
            var id       = playerstate_JSON.id;
            var username = playerstate_JSON.username;

            var attributeText = "username: <br>" +
                                "current coins: <br>" +
                                "total coins: <br>" +
                                "total score:  <br>" +
                                "played runs: <br>" +
                                "time spent: <br>" +
                                "solved statements: <br>" +
                                "success rate: <br>";
            var valuesText = username + "<br>";

            attribute.writeHTML(attributeText, "attributePara");

            /**
             * This function will be called when Ajax-Call ProfileIdRequest succeeds.
             * @param xmlHttpRequest contains profile information by id:
             *        current avatar, current coins and current highscoreStats
             */
            function profileID_Reply(xmlHttpRequest) {

                var profile_JSON = JSON.parse(xmlHttpRequest.responseText);
                //console.log("Kahn",profile_JSON);
                var filename = profile_JSON.avatar.avatarFilename;
                var isTeam = profile_JSON.avatar.isTeam;
                var avatar;
                if (!isTeam) {
                    avatar = new game.BackgroundElement('avatar', 4.84848, 8.33333, 23.4394, 18.75, 'none');
                    avatar.setImage("assets/data/img/avatare/" + filename + "_front.png", "skin");
                    me.game.world.addChild(avatar);
                } else {
                    avatar = new game.BackgroundElement('avatar', 6.36363, 8.33333, 22.6818, 18.75, 'none');
                    avatar.setImage("assets/data/img/avatare/" + filename + "_front.png", "skin");
                    me.game.world.addChild(avatar);
                }

                var currentCoins = profile_JSON.coins;
                var totalCoins = profile_JSON.highScore.totalCoins;
                var totalPoints = profile_JSON.highScore.totalScore;
                var playedRuns = profile_JSON.highScore.playedRuns;
                var playedTime = convertTime(profile_JSON.highScore.playedTime);
                var solvedSQL = profile_JSON.highScore.solvedSQL;
                var quote = profile_JSON.highScore.quote;

                var valuesStats = currentCoins + "<br>" +
                                  totalCoins + "<br>" +
                                  totalPoints + "<br>" +
                                  playedRuns + "<br>" +
                                  playedTime + "<br>" +
                                  solvedSQL + "<br>" +
                                  quote + "%" + "<br>";
                values.writeHTML(valuesText + valuesStats, "valuesPara");
                }

            ajaxSendProfileIdRequest(id, profileID_Reply);
        }

        ajaxSendProfileRequest(profileReply);

    }
});
