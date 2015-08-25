game.ProfileScreen = me.ScreenObject.extend({

    /**
     *  action to perform on state change
     */
    onResetEvent : function() {

        // profile_screen
        me.game.world.addChild(
            new me.Sprite (
                0,0,
                me.loader.getImage('profile_screen')
            ),
            1
        );

        //Back to Menubutton
        me.game.world.addChild(new backToMenu(950,-40),2);

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
                //console.log(profile_JSON);
                var filename = profile_JSON.avatar.avatarFilename;
                var isTeam = profile_JSON.avatar.isTeam;
                if (isTeam == false) {
                    me.game.world.addChild(new game.SkinFront(349, 144, filename, isTeam));
                } else {
                    me.game.world.addChild(new game.SkinFront(339, 144, filename, isTeam));
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


            /**
             *
             */
            ajaxSendProfileIdRequest(id, profileID_Reply);
        }

        /**
         *
         */
        ajaxSendProfileRequest(profileReply);

    }
});
