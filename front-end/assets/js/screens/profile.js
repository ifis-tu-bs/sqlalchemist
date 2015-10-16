game.ProfileScreen = me.ScreenObject.extend({

    /**
     *  action to perform on state change
     */
    onResetEvent : function() {

        /**
         * Create background-div and add image to it.
         */
        var backgroundProfile = new game.BackgroundElement('backgroundProfileId', 100, 100, 0, 0, 'none');
        backgroundProfile.setImage("assets/data/img/gui/profile_screen.png", "backgroundprofile");
        me.game.world.addChild(backgroundProfile);
        $("#backgroundProfileId").fadeIn(100);

        /**
         * Create button for state change back to the menu with corresponding callback function.
         */
        this.backToMenu = function () {
            $("#backgroundProfileId").fadeOut(100);
            $("#backFromProfile").fadeOut(100);
            $("#avatar").fadeOut(100);
            setTimeout( function() {
                me.state.change(me.state.MENU);
            }, 100);
        };
        var backFromProfile = new game.ClickableElement('backFromProfile','', this.backToMenu, 18.1818, 17.7083, 72, -5, 1);
        backFromProfile.setImage("assets/data/img/buttons/new_back_button.png", "back");
        me.game.world.addChild(backFromProfile);
        $("#backFromProfile").fadeIn(100);


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
                console.log("Kahn",profile_JSON);
                var filename = profile_JSON.avatar.avatarFilename;
                var isTeam = profile_JSON.avatar.isTeam;
                if (!isTeam) {
                    var avatar = new game.BackgroundElement('avatar', 4.84848, 8.33333, 26.4394, 18.75, 'none');
                    avatar.setImage("assets/data/img/avatare/" + filename + "_front.png", "skin");
                    $("#avatar").fadeIn(100);
                    me.game.world.addChild(avatar);
                } else {
                    var avatar = new game.BackgroundElement('avatar', 6.36363, 8.33333, 25.6818, 18.75, 'none');
                    avatar.setImage("assets/data/img/avatare/" + filename + "_front.png", "skin");
                    $("#avatar").fadeIn(100);
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
