game.ResultScreen = me.ScreenObject.extend({

    onResetEvent : function() {
        /**
         * Create background-div and add image to it.
         */
        var rootContainer = new game.fdom.RootContainer('/assets/data/img/gui/result_screen.png');
        me.game.world.addChild(rootContainer);

        var title = new game.fdom.TitleElement(rootContainer, '100%','25%','0%','10%', 'Result', 'Title ResultScreen');
        me.game.world.addChild(title);

        game.data.cheat = false;

        this.fadeOutElements = function () {
            $("#backgroundResultId").fadeOut(100);
            $("#nextFromResult").fadeOut(100);
            $(rootContainer.getNode()).fadeOut(100);
        };

        /**
         * Create button with  callback function to go back to according state where user came from.
         * @returns {boolean}
         */
        var nextButton = new game.fdom.ButtonElement(rootContainer, '28%','17%','35%','74%', "Next", 'Button ResultScreen Next', false, function() {
            //console.log("clicked! Next with:" + game.task.kind);
            if (!game.data.playerStat.isTutorial) {
                switch(game.task.kind) {
                    case 0 : {
                        this.fadeOutElements();
                        setTimeout( function() {
                            me.state.change(me.state.READY);
                        }, 100);
                        return true;
                    }
                    case 1 : {
                        this.fadeOutElements();
                        setTimeout( function() {
                            me.state.change(me.state.READY);
                        }, 100);
                        return true;
                    }
                    case 2 : {
                        this.fadeOutElements();
                        setTimeout( function() {
                            me.state.change(STATE_TRIVIA);
                        }, 100);
                        return true;
                    }
                    case 3 : {
                        this.fadeOutElements();
                        setTimeout( function() {
                            me.state.change(STATE_HOMEWORK);
                        }, 100);
                        return true;
                    }
                }
            } else {
                game.potion.potions[16].amount++;
                this.fadeOutElements();
                setTimeout( function() {
                    me.state.change(STATE_TEXT);
                }, 100);
            }

        });

        me.game.world.addChild(nextButton);


        /**
         * Get results and print them in according TextOutPutElements.
         * @type {number}
         */
        this.time = Math.floor((game.task.finishTime - game.task.startTime)/1000);

        var resultTime = new game.TextOutputElement('resultTimeId', 19, 5.3, 29, 26, 1);
        me.game.world.addChild(resultTime);
        resultTime.writeHTML("Time: " + convertTime(this.time), 'resultTimePara');

        var resultScore = new game.TextOutputElement('resultScoreId', 25, 5.3, 54, 26, 1);
        me.game.world.addChild(resultScore);
        resultScore.writeHTML("Score: " + game.data.gainScore, 'resultScorePara');

        var resultDifficulty = new game.TextOutputElement('resultDifficultyId', 25, 5.3, 20, 39, 1);
        me.game.world.addChild(resultDifficulty);
        resultDifficulty.writeHTML("Difficulty: " + game.task.difficulty, 'resultDifficultyPara');

        var resultCoins = new game.TextOutputElement('resultCoinsId', 35, 5.3, 56, 39, 1);
        me.game.world.addChild(resultCoins);
        resultCoins.writeHTML("Lofi-Coins: " + game.task.gainCoins, 'resultCoinsPara');

        var result = new game.TextOutputElement('resultId', 73, 21.2, 14, 52, 4);
        me.game.world.addChild(result);

        switch (game.task.kind) {
            case 0 : {
                result.writeHTML("You crafted: " + "<br><br>" + "1 x " + game.potion.potions[game.task.potionId].name, 'resultCraftedPara');
                break;
            }
            case 1 : {
                result.writeHTML("You got: " + "<br><br>" + game.task.name, 'resultGotPara');
            }
        }

    }
});
