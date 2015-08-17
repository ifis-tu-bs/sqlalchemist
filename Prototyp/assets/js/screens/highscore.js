game.HighscoreScreen = me.ScreenObject.extend({

    /**
     *  action to perform on state change
     */
    onResetEvent : function() {

        /**
         * Load screen-image for Rankings-Screen
         */
        me.game.world.addChild(
            new me.Sprite (
                0,0,
                me.loader.getImage('ranking_screen')
            ),
            1
        );

        me.game.world.addChild(new backFromRankings(1080,20),2);
        var headers = new game.TextOutputElement('headers', 55, 9, 15, 11, 1);
        me.game.world.addChild(headers);


        /**
         * Ajax GET /highscore/* Methods
         * returned value: xmlHttpRequest
         * on success: onLoad function drawHighscore will be called
         */
        stopDouble = function() {
            game.data.playing = false;
        };
        function scoreReply() {
            if (game.data.sound && !game.data.playing){
                game.data.playing = true;
                me.audio.play("page", false, stopDouble, game.data.soundVolume);
            }
            ajaxSendHighscorePointsRequest(drawScore);
        }
        function drawScore(xmlHttpRequest) {
            //console.log(xmlHttpRequest.responseText);
            headers.clear();
            var header = "SCORE";
            drawHighscore(xmlHttpRequest, 1, header);
        }

        function coinsReply() {
            if (game.data.sound && !game.data.playing){
                game.data.playing = true;
                me.audio.play("page", false, stopDouble, game.data.soundVolume);
            }
            ajaxSendHighscoreCoinsRequest(drawCoins);
        }
        function drawCoins(xmlHttpRequest) {
            headers.clear();
            var header = "LOFI-COINS";
            drawHighscore(xmlHttpRequest, 2, header);
        }

        function timeSpentReply() {
            if (game.data.sound && !game.data.playing){
                game.data.playing = true;
                me.audio.play("page", false, stopDouble, game.data.soundVolume);
            }
            ajaxSendHighscoreTimeRequest(drawTimeSpent);
        }
        function drawTimeSpent(xmlHttpRequest) {
            headers.clear();
            var header = "TIME SPENT";
            drawHighscore(xmlHttpRequest, 3, header);
        }

        function runsReply() {
            if (game.data.sound && !game.data.playing){
                game.data.playing = true;
                me.audio.play("page", false, stopDouble, game.data.soundVolume);
            }
            ajaxSendHighscoreRunsRequest(drawRuns);
        }
        function drawRuns(xmlHttpRequest) {
            headers.clear();
            var header = "RUNS NEEDED";
            drawHighscore(xmlHttpRequest, 4, header);
        }

        function sqlStatementsReply() {
            if (game.data.sound && !game.data.playing){
                game.data.playing = true;
                me.audio.play("page", false, stopDouble, game.data.soundVolume);
            }
            ajaxSendHighscoreSQLRequest(drawSqlStatements);
        }
        function drawSqlStatements(xmlHttpRequest) {
            headers.clear();
            var header = "STATEMENTS";
            drawHighscore(xmlHttpRequest, 5, header);
        }

        function sqlRateReply() {
            if (game.data.sound && !game.data.playing){
                game.data.playing = true;
                me.audio.play("page", false, stopDouble, game.data.soundVolume);
            }
            ajaxSendHighscoreRateRequest(drawSQLRate);
        }
        function drawSQLRate(xmlHttpRequest) {
            headers.clear();
            var header = "SUCCESS RATE";
            drawHighscore(xmlHttpRequest, 6, header)
        }

        /**
         * Create all necessary OutputElements for highscores.
         */
        var ranks       = new game.TextOutputElement('ranks', 15, 70, 5, 22, 12);
        var usernames   = new game.TextOutputElement('usernames', 35, 70, 20.5, 22, 12);
        var values      = new game.TextOutputElement('values', 15, 70, 56, 23, 12);
        var ownRank     = new game.TextOutputElement('ownRank', 15, 11, 3.5, 87, 2);
        var ownUsername = new game.TextOutputElement('ownUsername', 35, 11, 20.5, 87, 2);
        var ownValue    = new game.TextOutputElement('ownValue', 15, 9, 55.5, 88, 2);
        var position    = new game.TextOutputElement('position', 50, 5, 17, 80.5, 1);
        position.writeHTML("your position:");

        me.game.world.addChild(ranks);
        me.game.world.addChild(usernames);
        me.game.world.addChild(values);
        me.game.world.addChild(ownRank);
        me.game.world.addChild(ownUsername);
        me.game.world.addChild(ownValue);
        me.game.world.addChild(position);


        /**
         * On success of Ajax-Call this function will be called and highscore will be drawn.
         * @param xmlHttpRequest : contains list of 10 best users.
         */
        function drawHighscore(xmlHttpRequest, i, header){
            headers.writeHTML(header,'headerPara');

            var highscore_object = JSON.parse(xmlHttpRequest.responseText);
            var ownPlace = highscore_object.ownRank;
            var ownID    = highscore_object.own.username;
            usernames.clear();
            values.clear();
            ranks.clear();
            ownRank.clear();
            ownUsername.clear();
            ownValue.clear();

            var usernamesText = "";
            var valuesText = "";


            for (var x = 0; x < highscore_object.highScore.length; x++) {
                usernamesText += (highscore_object.highScore[x].username + " <br> ");
                switch (i){
                    case 1:
                        valuesText += (highscore_object.highScore[x].totalScore + " <br> ");
                        break;
                    case 2:
                        valuesText += (highscore_object.highScore[x].totalCoins + " <br> ");
                        break;
                    case 3:
                        valuesText += (convertTime(highscore_object.highScore[x].playedTime) + " <br> ");
                        break;
                    case 4:
                        valuesText += (highscore_object.highScore[x].playedRuns + " <br> ");
                        break;
                    case 5:
                        valuesText += (highscore_object.highScore[x].solvedSQL + " <br> ");
                        break;
                    case 6:
                        valuesText += (highscore_object.highScore[x].quote + "%" + " <br> ");
                        break;
                }
            }

            /**
             * Get values of own Rank
             * @type {string}
             */
            var ownValues = "";
            switch (i){
                case 1:
                    ownValues = highscore_object.own.totalScore;
                    break;
                case 2:
                    ownValues = highscore_object.own.totalCoins;
                    break;
                case 3:
                    ownValues = convertTime(highscore_object.own.playedTime);
                    break;
                case 4:
                    ownValues = highscore_object.own.playedRuns;
                    break;
                case 5:
                    ownValues = highscore_object.own.solvedSQL;
                    break;
                case 6:
                    var percent = "%";
                    ownValues = highscore_object.own.quote + percent;
                    break;
            }


            ranks.writeHTML("" +
                "1. <br>" +
                "2. <br>" +
                "3. <br>" +
                "4. <br>" +
                "5. <br>" +
                "6. <br>" +
                "7. <br>" +
                "8. <br>" +
                "9. <br>" +
                "10. <br>", 'ranksPara');


            usernames.writeHTML(usernamesText,'usernamesPara');
            values.writeHTML(valuesText,'valuesPara');
            ownRank.writeHTML(ownPlace + ".", 'ownRankPara');
            ownUsername.writeHTML(ownID, 'ownUsernamePara');
            ownValue.writeHTML(ownValues, 'ownValuePara');
        }

        /**
         * Create necessary ClickableElements for Sign-UP
         * @param : id       : a unique alphanumeric string
         *          name     : text to display on screen
         *          callback : the callback function
         *          width    : the width of the element in percent of the width of the canvas
         *          height   : the height of the element in percent of the height of the canvas
         *          left     : the left margin of the element in percent of the width of the canvas
         *          top      : the top margin of the element in percent of the height of the canvas
         *          rows     : the number of rows
         */
        var mostScore     = new game.ClickableElement('mostScore', 'score', scoreReply, 10, 4, 83.5, 25, 1);
        var mostCoins     = new game.ClickableElement('mostCoins', 'lofi-coins', coinsReply, 16, 4, 81, 35, 1);
        var timeSpent     = new game.ClickableElement('timeSpent', 'time spent', timeSpentReply, 16, 4, 81, 45, 1);
        var numberOfRuns  = new game.ClickableElement('numberOfRuns', 'runs', runsReply, 9, 4, 84, 55, 1);
        var sqlStatements = new game.ClickableElement('sqlStatements', 'statements', sqlStatementsReply, 18, 4, 80, 65, 1);
        var sqlRate       = new game.ClickableElement('sqlRate', 'success rate', sqlRateReply, 19, 4, 79.5, 75, 1);

        /**
         * add children to container
         */
        me.game.world.addChild(mostCoins);
        me.game.world.addChild(mostScore);
        me.game.world.addChild(timeSpent);
        me.game.world.addChild(numberOfRuns);
        me.game.world.addChild(sqlStatements);
        me.game.world.addChild(sqlRate);

        scoreReply();

        if(game.data.cheat) {
            setTimeout(function () {
                coinsReply();
                setTimeout(function () {
                    timeSpentReply();
                    setTimeout(function () {
                        runsReply();
                        setTimeout(function () {
                            sqlStatementsReply();
                            setTimeout(function () {
                                sqlRateReply();
                                setTimeout(function () {
                                    me.state.change(me.state.HIGHSCORE);
                                }, 15000)
                            }, 15000)
                        }, 15000)
                    }, 15000)
                }, 15000)
            }, 15000)
        }


    }
});