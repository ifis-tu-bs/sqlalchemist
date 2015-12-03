game.HighscoreScreen = me.ScreenObject.extend({

    /**
     *  action to perform on state change
     */
    onResetEvent : function() {

        /**
         * Set screen-image for Rankings-Screen
         */
        var backgroundRanking = new game.BackgroundElement('backgroundRankingId', 100, 100, 0, 0, 'none');
        backgroundRanking.setImage("assets/data/img/gui/ranking_screen.png", "backgroundRanking");
        me.game.world.addChild(backgroundRanking);
        $("#backgroundRankingId").fadeIn(100);

        this.backToMenu = function() {
            $("#backgroundRankingId").fadeOut(100);
            $("#backgroundRanking").fadeOut(100);
            $("#backFromRankings").fadeOut(100);
            $("#mostScore").fadeOut(100);
            $("#mostCoins").fadeOut(100);
            $("#timeSpent").fadeOut(100);
            $("#numberOfRuns").fadeOut(100);
            $("#sqlStatements").fadeOut(100);
            $("#sqlRate").fadeOut(100);
            setTimeout(function () {
                me.state.change(me.state.MENU);
            }, 100);
        };

        var backFromRankings = new game.ClickableElement('backFromRankings','', this.backToMenu, 14.01515, 19.53125, 82, 0, 1);
        backFromRankings.setImage("assets/data/img/buttons/back_button_ink.png", "backFromRankingsImage");
        me.game.world.addChild(backFromRankings);

        /**
         * Create header element
         */
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
        this.scoreReply = function () {
            if (game.data.sound && !game.data.playing){
                game.data.playing = true;
                me.audio.play("page", false, stopDouble, game.data.soundVolume);
            }
            ajaxSendHighscorePointsRequest(drawScore);
        };
        function drawScore(xmlHttpRequest) {
            //console.log(xmlHttpRequest.responseText);
            headers.clear();
            var header = "SCORE";
            drawHighscore(xmlHttpRequest, 1, header);
        }

        this.coinsReply= function () {
            if (game.data.sound && !game.data.playing){
                game.data.playing = true;
                me.audio.play("page", false, stopDouble, game.data.soundVolume);
            }
            ajaxSendHighscoreCoinsRequest(drawCoins);
        };
        function drawCoins(xmlHttpRequest) {
            headers.clear();
            var header = "LOFI-COINS";
            drawHighscore(xmlHttpRequest, 2, header);
        }

        this.timeSpentReply = function () {
            if (game.data.sound && !game.data.playing){
                game.data.playing = true;
                me.audio.play("page", false, stopDouble, game.data.soundVolume);
            }
            ajaxSendHighscoreTimeRequest(drawTimeSpent);
        };
        function drawTimeSpent(xmlHttpRequest) {
            headers.clear();
            var header = "TIME SPENT";
            drawHighscore(xmlHttpRequest, 3, header);
        }

        this.runsReply = function () {
            if (game.data.sound && !game.data.playing){
                game.data.playing = true;
                me.audio.play("page", false, stopDouble, game.data.soundVolume);
            }
            ajaxSendHighscoreRunsRequest(drawRuns);
        };
        function drawRuns(xmlHttpRequest) {
            headers.clear();
            var header = "RUNS NEEDED";
            drawHighscore(xmlHttpRequest, 4, header);
        }

        this.sqlStatementsReply = function () {
            if (game.data.sound && !game.data.playing){
                game.data.playing = true;
                me.audio.play("page", false, stopDouble, game.data.soundVolume);
            }
            ajaxSendHighscoreSQLRequest(drawSqlStatements);
        };
        function drawSqlStatements(xmlHttpRequest) {
            headers.clear();
            var header = "STATEMENTS";
            drawHighscore(xmlHttpRequest, 5, header);
        }

        this.sqlRateReply = function () {
            if (game.data.sound && !game.data.playing){
                game.data.playing = true;
                me.audio.play("page", false, stopDouble, game.data.soundVolume);
            }
            ajaxSendHighscoreRateRequest(drawSQLRate);
        };
        function drawSQLRate(xmlHttpRequest) {
            headers.clear();
            var header = "SUCCESS RATE";
            drawHighscore(xmlHttpRequest, 6, header);
        }

        /**
         * Create all necessary OutputElements for highscores.
         */
        var ranks       = new game.TextOutputElement('ranks', 15, 70, 5, 22, 12);
        var usernames   = new game.TextOutputElement('usernames', 35, 70, 20.5, 22, 12);
        var values      = new game.TextOutputElement('values', 15, 70, 56, 23, 12);
        var ownRank     = new game.TextOutputElement('ownRank', 15, 11, 3.5, 87, 2);
        var ownUsername = new game.TextOutputElement('ownUsername', 35, 11, 20.5, 87, 2);
        var ownValue    = new game.TextOutputElement('ownValue', 15, 9, 55, 88, 2);
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

            /**
             * Get values of the 10 best users
             */
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
             * Get values of current user's rank
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

            /**
             * Insert values into TextOutpuElemts
             */
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
         * Create necessary ClickableElements for Rankings
         */
        var mostScore     = new game.ClickableElement('mostScore', 'score', this.scoreReply, 10, 4, 83.5, 25, 1);
        var mostCoins     = new game.ClickableElement('mostCoins', 'lofi-coins', this.coinsReply, 16, 4, 81, 35, 1);
        var timeSpent     = new game.ClickableElement('timeSpent', 'time spent', this.timeSpentReply, 16, 4, 81, 45, 1);
        var numberOfRuns  = new game.ClickableElement('numberOfRuns', 'runs', this.runsReply, 9, 4, 84, 55, 1);
        var sqlStatements = new game.ClickableElement('sqlStatements', 'statements', this.sqlStatementsReply, 18, 4, 80, 65, 1);
        var sqlRate       = new game.ClickableElement('sqlRate', 'success rate', this.sqlRateReply, 19, 4, 79.5, 75, 1);
        me.game.world.addChild(mostScore);
        me.game.world.addChild(mostCoins);
        me.game.world.addChild(timeSpent);
        me.game.world.addChild(numberOfRuns);
        me.game.world.addChild(sqlStatements);
        me.game.world.addChild(sqlRate);

        this.scoreReply();

    }
});
