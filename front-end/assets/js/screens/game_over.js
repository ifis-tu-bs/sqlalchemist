game.GameOverScreen = me.ScreenObject.extend({

    onResetEvent : function() {

        /**
         * Check if last maps was a bossmap and go to according state.
         */
        if (game.data.text === 7) {
            $("#backgroundGameOverId").fadeOut(100);
            $("#next").fadeOut(100);
            setTimeout( function() {
                me.state.change(STATE_TEXT);
            }, 100);
        } else {
            if (game.data.playerStat.isTutorial && game.persistent.depth > 1) {
                game.data.text = 13;
            } else if (game.data.playerStat.isTutorial && game.persistent.depth === 1 && game.data.text === 13) {
                game.data.text = 12;
            }

            /**
             * Create background-div and add image to it.
             */
            var backgroundGameOver = new game.BackgroundElement('backgroundGameOverId', 100, 100, 0, 0, 'none');
            backgroundGameOver.setImage("assets/data/img/gui/game_over_screen.png", "backgroundgameover");
            me.game.world.addChild(backgroundGameOver);
            $("#backgroundGameOverId").fadeIn(100);

            var slots = [];
            var coins = 0;

            for (var i = 0; i < game.data.scrolls.length; i++) {
                //console.log(slots, "scrolls:" + game.data.scrolls[i]);
                if (game.data.scrolls[i] !== -1) {
                    slots.push({
                        "id": game.data.scrolls[i]
                    });
                }
            }

            /**
             * onLoad-function from ajax push call to push information about all collected scrolls onto the server.
             * @param xmlHttpRequest
             */
            function collectedOnload(xmlHttpRequest) {
                //console.log(xmlHttpRequest);
                var gotten = JSON.parse(xmlHttpRequest.responseText);
                //console.log(gotten);
                for (var i = 0; i < gotten.scrolls.length; i++) {
                    if (gotten.scrolls[i].id > 20) {
                        game.data.scrolls[i] = gotten.scrolls[i].name;
                    } else {
                        game.data.scrolls[i] = gotten.scrolls[i].potion.name;
                    }
                }
                this.coins = gotten.coins;

                /**
                 * Create all necessary TextOutputElements to print according information.
                 */
                this.depthOut = new game.TextOutputElement('depthId', 19, 5.3, 68.181818, 26.7, 1);
                me.game.world.addChild(depthOut);
                this.depthOut.writeHTML("Depth: " + game.persistent.depth, 'depthPara');

                this.scoreOut = new game.TextOutputElement('scoreId', 23, 5.3, 45, 26.7, 1);
                me.game.world.addChild(scoreOut);
                this.scoreOut.writeHTML("Score: " + game.data.score, 'scorePara');

                this.lofiCoinsOut = new game.TextOutputElement('lofiCoinsGoId', 30.5, 5.3, 14, 26.7, 1);
                me.game.world.addChild(lofiCoinsOut);
                this.lofiCoinsOut.writeHTML("Lofi-Coins: " + this.coins, 'lofiCoinsOutPara');

                this.scrollsObtained = new game.TextOutputElement('obtainedId', 73, 26.5, 14, 37.109375, 5);
                me.game.world.addChild(scrollsObtained);

                if(game.persistent.depth === 52){
                    function profileReply(xmlHttpRequest){
                        var playerstate_JSON = JSON.parse(xmlHttpRequest.responseText);
                        var id       = playerstate_JSON.id;
                        var username = playerstate_JSON.username;
                        function profileID_Reply(xmlHttpRequest) {

                            var profile_JSON = JSON.parse(xmlHttpRequest.responseText);
                            //console.log("Kahn",profile_JSON);
                            var playedRuns = profile_JSON.highScore.playedRuns;

                            this.scrollsObtained.writeHTML("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
                            "                               Well done " + username + "!!!"+"<br>" + "<br>" + "&nbsp;You have beaten the game in " + (playedRuns+1) + "&nbsp;Runs!" + "<br>" +
                                                           "&nbsp;reset the story in the settings" + "<br>" +  "&nbsp;if you want to finish it quicker.");
                        }
                        ajaxSendProfileIdRequest(id, profileID_Reply);
                    }
                    ajaxSendProfileRequest(profileReply);



                }else if (game.data.scollLimit !== 0) {

                    if (game.data.scrolls.length !== 0) {
                        this.scrollsObtained.writeHTML("Scrolls obtained: " + "<br>", 'obtainedPara');

                        for (i = 0; i < game.data.scrolls.length; i++) {
                            this.scrollsObtained.writeHTML("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + game.data.scrolls[i] + "<br>",
                                                           'obtainedPara');
                        }

                    } else {
                        this.scrollsObtained.writeHTML("<br>" + "No Scrolls For You!!!" + "<br><br>" + "Sorry :(",
                                                       'noScrollsPara');
                    }

                    this.coinLimit = new game.TextOutputElement('coinLimitGoId', 73, 5.3, 14, 71.614583, 1);
                    me.game.world.addChild(coinLimit);
                    this.coinLimit.writeHTML("You still can collect " + (game.data.scollLimit - game.data.scrolls.length)
                                             + " scrolls today.", 'coinLimitPara');

                } else {
                    this.scrollsObtained.writeHTML("<br><br>" + "You collected all Scrolls for today, come back tomorrow" +
                                                   " to earn new Scrolls!", 'allScrollsPara');
                }
            }

            var collected = JSON.stringify({
                depth: game.persistent.depth,
                score: game.data.score,
                scrolls: game.data.scrolls
            });
            //console.log("collected: " + collected);
            ajaxSendProfileCollectedRequest(collected, collectedOnload);

            /**
             * Stop last audio track and play according audio track to GameOverScreen.
             */
            if (game.data.music) {
                me.audio.stopTrack();
                if(game.persistent.depth === 52){
                    me.audio.playTrack("result", game.data.musicVolume);
                }else{
                    me.audio.playTrack("gameover", game.data.musicVolume);
                }

            }

            /**
             * Create button and according  callback function to go to the next screen.
             */
            this.onLab = function () {
                $("#backgroundGameOverId").fadeOut(100);
                $("#next").fadeOut(100);
                setTimeout( function() {
                    me.state.change(me.state.READY);
                }, 100);
            };

            var nextButton = new game.ClickableElement('next', 'NEXT', this.onLab, 25, 10, 37, 80.5, 1);
            me.game.world.addChild(nextButton);
            $("#next").fadeIn(100);

            //maxdepth verwenden
            if (Math.floor((game.persistent.depth - 1) % 5) === 0
                && game.persistent.depth > game.data.playerStat.characterState.maxDepth
                && game.persistent.depth !== 1) {
                game.persistent.boss = true;
            }
        }

    },

    onDestroyEvent: function(){
        /**
         * Stop game over audio track at state change to the menu.
         */
        if(game.data.music ){
            me.audio.stopTrack();
            me.audio.playTrack("menu",game.data.musicVolume);
        }
    }

});
