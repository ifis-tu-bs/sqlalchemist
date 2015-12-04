game.TriviaScreen = me.ScreenObject.extend({
    /**
     *  action to perform on state change
     */
    onResetEvent: function() {

        /**
         * Create background-div and add image to it.
         */
        var backgroundTrivia = new game.BackgroundElement('backgroundTriviaId', 100, 100, 0, 0, 'none');
        backgroundTrivia.setImage("assets/data/img/gui/task_screen_new.png", "backgroundtrivia");
        me.game.world.addChild(backgroundTrivia);

        var triviaHeader = new game.TextOutputElement('triviaHeader', 100, 7, 0, 10, 1);
        me.game.world.addChild(triviaHeader);
        triviaHeader.write("Choose A Difficulty:");

        /**
         * Function which returns the specific task difficulty from the according button and redirects the user to the
         * TaskScreen.
         * @param difficulty
         * @returns {Function}
         * @constructor
         */
        this.CoinClick = function (difficulty) {
            return function() {
                game.task.kind = 2;
                game.task.difficulty = difficulty;
                //console.log(difficulty);

                for (var i = 0; i < 6; i++) {
                    $("#taskLevel" + i).fadeOut(100);
                }
                $("#backFromTrivia").fadeOut(100);
                $("#triviaHeader").fadeOut(100);
                setTimeout( function() {
                    me.state.change(STATE_TASK);
                }, 100);
            };
        };

        /**
         * Create buttons that return the specific difficulty of a task to the callback function.
         */
         var taskLevel;
        for (var i = 0; i < 2; i++) {
            taskLevel = new game.ClickableElement('taskLevel' + i + 1, '', this.CoinClick(i + 1), 12.5, 21.484375, 30.30303 + i * 24.242424, 26.041667, 1);
            taskLevel.setImage("assets/data/img/buttons/difficulty_coins/coin_" + (i + 1) + ".png", "coin");
            me.game.world.addChild(taskLevel);
        }

        for (var j = 0; j < 3; j++){
            taskLevel = new game.ClickableElement('taskLevel' + j + 3, '', this.CoinClick(j + 3), 12.5, 21.484375, 18.484848 + j * 23.560606, 52.083333, 1);
            taskLevel.setImage("assets/data/img/buttons/difficulty_coins/coin_" + (j + 3 + 1) + ".png", "coin");
            me.game.world.addChild(taskLevel);
        }

        /**
         * Create element to go back to the menu with according callback function.
         */
        triviaBackClick = function(){
            for (var i = 1; i < 6; i++) {
                $("#taskLevel" + i).fadeOut(100);
            }
            $("#backFromTrivia").fadeOut(100);
            $("#triviaHeader").fadeOut(100);
            setTimeout( function() {
                me.state.change(me.state.MENU);
            }, 100);
        };

        var backFromTrivia = new game.ClickableElement('backFromTrivia', 'Back', triviaBackClick, 15, 6.5, 39, 80, 1);
        me.game.world.addChild(backFromTrivia);

    }
});
