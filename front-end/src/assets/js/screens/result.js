game.ResultScreen = me.ScreenObject.extend({

    onResetEvent : function() {
        /**
         * Create background-div and add image to it.
         */
        var backgroundResult = new game.BackgroundElement('backgroundResultId', 100, 100, 0, 0, 'none');
        backgroundResult.setImage("assets/data/img/gui/result_screen.png", "backgroundresult");
        me.game.world.addChild(backgroundResult);
        $("#backgroundResultId").fadeIn(100);

        game.data.cheat = false;

        /**
         * Create button with  callback function to go back to according state where user came from.
         * @returns {boolean}
         */
        this.onNext = function () {
            console.log("clicked! Next with:" + game.task.kind);
            switch(game.task.kind) {
                case 0 : {
                    $("#backgroundResultId").fadeOut(100);
                    $("#nextFromResult").fadeOut(100);
                    setTimeout( function() {
                        me.state.change(me.state.READY);
                    }, 100);
                    return true;
                }
                case 1 : {
                    $("#backgroundResultId").fadeOut(100);
                    $("#nextFromResult").fadeOut(100);
                    setTimeout( function() {
                        me.state.change(me.state.READY);
                    }, 100);
                    return true;
                }
                case 2 : {
                    $("#backgroundResultId").fadeOut(100);
                    $("#nextFromResult").fadeOut(100);
                    setTimeout( function() {
                        me.state.change(STATE_TRIVIA);
                    }, 100);
                    return true;
                }
                case 3 : {
                    $("#backgroundResultId").fadeOut(100);
                    $("#nextFromResult").fadeOut(100);
                    setTimeout( function() {
                        me.state.change(STATE_HOMEWORK);
                    }, 100);
                    return true;
                }
            }
        };

        var nextButton = new game.ClickableElement('nextFromResult', 'NEXT', this.onNext, 25, 10, 37, 80.5, 1);
        me.game.world.addChild(nextButton);
        $("#nextFromResult").fadeIn(100);


        /**
         * Get results and print them in according TextOutPutElements.
         * @type {number}
         */
        this.time = Math.floor((game.task.finishTime - game.task.startTime)/1000);
        this.difficulty = game.task.difficulty + 1;
        this.score = game.data.gainScore;

        var resultTime = new game.TextOutputElement('resultTimeId', 19, 5.3, 29, 26, 1);
        me.game.world.addChild(resultTime);
        resultTime.writeHTML("Time: " + convertTime(this.time), 'resultTimePara');

        var resultScore = new game.TextOutputElement('resultScoreId', 25, 5.3, 54, 26, 1);
        me.game.world.addChild(resultScore);
        resultScore.writeHTML("Score: " + this.score, 'resultScorePara');

        var resultDifficulty = new game.TextOutputElement('resultDifficultyId', 25, 5.3, 20, 39, 1);
        me.game.world.addChild(resultDifficulty);
        resultDifficulty.writeHTML("Difficulty: " + this.difficulty, 'resultDifficultyPara');

        var resultCoins = new game.TextOutputElement('resultCoinsId', 35, 5.3, 56, 39, 1);
        me.game.world.addChild(resultCoins);
        resultCoins.writeHTML("Lofi-Coins: " + game.task.gainCoins, 'resultCoinsPara');

        var result = new game.TextOutputElement('resultId', 73, 21.2, 14, 52, 4);
        me.game.world.addChild(result);

        switch(game.task.kind) {
            case 0 : {
                result.writeHTML("You Crafted: " + "<br><br>" + "1 x " + game.potion.potions[game.task.potionId].name, 'resultCraftedPara');
                break;
            }
            case 1 : {
                result.writeHTML("You Got: " + "<br><br>" + game.task.name, 'resultGotPara');
            }
        }

    }
});
