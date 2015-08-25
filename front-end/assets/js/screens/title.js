
game.TitleScreen = me.ScreenObject.extend({

    /**
     *  action to perform on state change
     */
    onResetEvent : function() {

        /**
         * these functions are called when buttons are clicked.
         * here: simple state change.
         */

        onLab = function(){
            if(game.data.sound){
                me.audio.play("door", false, null, game.data.soundVolume);
            }
            me.state.change(me.state.READY);
        };
        onTrivia = function(){
            if(game.data.sound){
                me.audio.play("door", false, null, game.data.soundVolume);
            }
            me.state.change(STATE_TRIVIA);
        };
        onProfile = function(){
            if(game.data.sound){
                me.audio.play("scroll", false, null, game.data.soundVolume);
            }
            me.state.change(STATE_PROFILE);
        };
        onSettings = function(){
            if(game.data.sound){
                me.audio.play("scroll", false, null, game.data.soundVolume);
            }
            me.state.change(me.state.SETTINGS);
        };
        onHighscore = function(){
            if(game.data.sound){
                me.audio.play("page", false, null, game.data.soundVolume);
            }
            me.state.change(me.state.HIGHSCORE);
        };
        onShop = function(){
            if(game.data.sound){
                me.audio.play("door", false, null, game.data.soundVolume);
            }
            me.state.change(STATE_SHOP);
        };
        onHomework = function(){
            if(game.data.sound){
                me.audio.play("switch", false, null, game.data.soundVolume);
            }
            me.state.change(STATE_HOMEWORK);
        };

        onLogout = function(xmlHttpRequest){
            console.log("Logout Response" + xmlHttpRequest.responseText);
            window.location.reload();
        };
        logoutReply = function(){
            ajaxSendLogoutRequest(onLogout);
        };


        /**
         * Draw TitleScreen. If student == true, draw screen with HomeworkScreen, else draw simple HomeScreen
         * @param xmlHttpRequest contains server response
         */
        onProfileReply = function(xmlHttpRequest){

            var profileObject = JSON.parse(xmlHttpRequest.responseText);
            if (profileObject.student == false){

                me.game.world.addChild(
                    new me.Sprite (
                        0,0,
                        me.loader.getImage('home_screen_1')
                    ),
                    1
                );

                var settingsButton  = new game.ClickableElement('settingsButton', 'Sett ings', onSettings, 15, 22, 70, 61, 2);
                var highscoreButton = new game.ClickableElement('highscoreButton', 'Rank ings', onHighscore, 25, 31, 38, 54, 2);
                var shopButton      = new game.ClickableElement('shopButton', 'Sh op', onShop, 14, 22, 16.5, 61, 2);

                me.game.world.addChild(settingsButton);
                me.game.world.addChild(highscoreButton);
                me.game.world.addChild(shopButton);

            } else {
                me.game.world.addChild(
                    new me.Sprite (
                        0,0,
                        me.loader.getImage('home_screen_2')
                    ),
                    1
                );

                var settingsButtonStudent  = new game.ClickableElement('settingsButtonStudent', 'Sett ings', onSettings, 16, 20.5, 59.5, 78, 2);
                var highscoreButtonStudent = new game.ClickableElement('highscoreButtonStudent', 'Rank ings', onHighscore, 25, 25, 56, 50, 2);
                var shopButtonStudent      = new game.ClickableElement('shopButtonStudent', 'Sh op', onShop, 14, 20.5, 26, 78, 2);
                var homeworkButton         = new game.ClickableElement('homeworkButton', 'Home work', onHomework, 25, 21, 21, 54, 2);

                me.game.world.addChild(settingsButtonStudent);
                me.game.world.addChild(highscoreButtonStudent);
                me.game.world.addChild(shopButtonStudent);
                me.game.world.addChild(homeworkButton);
            }

        };

        ajaxSendProfileRequest(onProfileReply);

        /**
         * Add all needed buttons to both title screens
         * @param : id       : a unique alphanumeric string
         *          name     : text to display on screen
         *          callback : the callback function
         *          width    : the width of the element in percent of the width of the canvas
         *          height   : the height of the element in percent of the height of the canvas
         *          left     : the left margin of the element in percent of the width of the canvas
         *          top      : the top margin of the element in percent of the height of the canvas
         *          rows     : the number of rows
         */

        var logoutButton    = new game.ClickableElement('logoutButton', 'Logout', logoutReply, 15, 7.5, 43, 1.5, 2);
        var labButton       = new game.ClickableElement('labButton', 'Story Mode', onLab, 17, 40, 8, 9, 4);
        var triviaButton    = new game.ClickableElement('triviaButton', 'Trivia Mode', onTrivia, 28, 27, 36, 16, 2);
        var profileButton   = new game.ClickableElement('profileButton', 'Pro file', onProfile, 18, 42, 74, 9, 4);

        me.game.world.addChild(logoutButton);
        me.game.world.addChild(labButton);
        me.game.world.addChild(triviaButton);
        me.game.world.addChild(profileButton);

    }
});



