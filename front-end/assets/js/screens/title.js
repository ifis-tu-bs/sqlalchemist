game.TitleScreen = me.ScreenObject.extend({

    /**
     *  action to perform on state change
     */
    onResetEvent : function() {

        /**
         * these functions are called when buttons are clicked.
         * here: simple state changes.
         */

        onLab = function(){
            if(game.data.sound){
                me.audio.play("door", false, null, game.data.soundVolume);
            }
            if (!game.data.profile.student) {
                $("#background").fadeOut(100);
            } else {
                $("#background2").fadeOut(100);
            }
            setTimeout( function() {
                me.state.change(me.state.READY);
            }, 100);
        };

        onTrivia = function(){
            if(game.data.sound){
                me.audio.play("door", false, null, game.data.soundVolume);
            }
            if (!game.data.profile.student) {
                $("#background").fadeOut(100);
            } else {
                $("#background2").fadeOut(100);
            }
            setTimeout( function() {
                me.state.change(STATE_TRIVIA);
            }, 100);
        };

        onProfile = function(){
            if(game.data.sound){
                me.audio.play("scroll", false, null, game.data.soundVolume);
            }
            if (!game.data.profile.student) {
                $("#background").fadeOut(100);
            } else {
                $("#background2").fadeOut(100);
            }
            setTimeout( function() {
                me.state.change(STATE_PROFILE);
            }, 100);
        };

        onSettings = function(){
            if(game.data.sound){
                me.audio.play("scroll", false, null, game.data.soundVolume);
            }
            if (!game.data.profile.student) {
                $("#background").fadeOut(100);
            } else {
                $("#background2").fadeOut(100);
            }
            setTimeout( function() {
                me.state.change(me.state.SETTINGS);
            }, 100);
        };

        onHighscore = function(){
            if(game.data.sound){
                me.audio.play("page", false, null, game.data.soundVolume);
            }
            if (!game.data.profile.student) {
                $("#background").fadeOut(100);
            } else {
                $("#background2").fadeOut(100);
            }
            setTimeout( function() {
                me.state.change(me.state.HIGHSCORE);
            }, 100);

        };

        onShop = function(){
            if(game.data.sound){
                me.audio.play("door", false, null, game.data.soundVolume);
            }
            if (!game.data.profile.student) {
                $("#background").fadeOut(100);
            } else {
                $("#background2").fadeOut(100);
            }
            setTimeout( function() {
                me.state.change(STATE_SHOP);
            }, 100);
        };

        onHomework = function(){
            if(game.data.sound){
                me.audio.play("switch", false, null, game.data.soundVolume);
            }
            if (!game.data.profile.student) {
                $("#background").fadeOut(100);
            } else {
                $("#background2").fadeOut(100);
            }
            setTimeout( function() {
                me.state.change(STATE_HOMEWORK);
            }, 100);
        };

        onLogout = function(xmlHttpRequest){
            console.log("Logout Response" + xmlHttpRequest.responseText);
            if (!game.data.profile.student) {
                $("#background").fadeOut(100);
            } else {
                $("#background2").fadeOut(100);
            }
            setTimeout( function() {
                window.location.reload();
            }, 100);
        };

        logoutReply = function(){
            ajaxSendLogoutRequest(onLogout);
        };

        /**
         * Draw TitleScreen. If student == true, draw screen with HomeworkScreen with corresponding buttons, else draw
         * simple HomeScreen
         * @param xmlHttpRequest contains server response
         */
        onProfileReply = function(xmlHttpRequest){

            game.data.profile = JSON.parse(xmlHttpRequest.responseText);
            if (!game.data.profile.student){

                var background = new game.BackgroundElement('background', 100, 100, 0, 0, 'none');
                background.setImage("assets/data/img/gui/home_screen_1.png", "back");
                me.game.world.addChild(background);

                $("#background").fadeIn(100);

                var settingsButton  = new game.ClickableElement('settingsButton', 'Sett ings', onSettings, 15, 22, 70, 61, 2);
                var highscoreButton = new game.ClickableElement('highscoreButton', 'Rank ings', onHighscore, 25, 31, 38, 54, 2);
                var shopButton      = new game.ClickableElement('shopButton', 'Sh op', onShop, 14, 22, 16.5, 61, 2);
                me.game.world.addChild(settingsButton);
                me.game.world.addChild(highscoreButton);
                me.game.world.addChild(shopButton);
                $("#settingsButton").fadeIn(100);
                $("#highscoreButton").fadeIn(100);
                $("#shopButton").fadeIn(100);

            } else {

                var background2 = new game.BackgroundElement('background2', 100, 100, 0, 0, 'none');
                background2.setImage("assets/data/img/gui/home_screen_2.png", "back2");
                me.game.world.addChild(background2);

                $("#background2").fadeIn(100);

                var settingsButtonStudent  = new game.ClickableElement('settingsButtonStudent', 'Sett ings', onSettings, 16, 20.5, 59.5, 78, 2);
                var highscoreButtonStudent = new game.ClickableElement('highscoreButtonStudent', 'Rank ings', onHighscore, 25, 25, 56, 50, 2);
                var shopButtonStudent      = new game.ClickableElement('shopButtonStudent', 'Sh op', onShop, 14, 20.5, 26, 78, 2);
                var homeworkButton         = new game.ClickableElement('homeworkButton', 'Home work', onHomework, 25, 21, 21, 54, 2);
                me.game.world.addChild(settingsButtonStudent);
                me.game.world.addChild(highscoreButtonStudent);
                me.game.world.addChild(shopButtonStudent);
                me.game.world.addChild(homeworkButton);
                $("#settingsButtonStudent").fadeIn(100);
                $("#highscoreButtonStudent").fadeIn(100);
                $("#shopButtonStudent").fadeIn(100);
                $("#homeworkButton").fadeIn(100);
            }

        };

        ajaxSendProfileRequest(onProfileReply);

        /**
         * Add all needed buttons to both title screens
         */
        var logoutButton    = new game.ClickableElement('logoutButton', 'Logout', logoutReply, 15, 7.5, 43, 1.5, 2);
        var labButton       = new game.ClickableElement('labButton', 'Story Mode', onLab, 17, 40, 8, 9, 4);
        var triviaButton    = new game.ClickableElement('triviaButton', 'Trivia Mode', onTrivia, 28, 27, 36, 16, 2);
        var profileButton   = new game.ClickableElement('profileButton', 'Pro file', onProfile, 18, 42, 74, 9, 4);
        me.game.world.addChild(logoutButton);
        me.game.world.addChild(labButton);
        me.game.world.addChild(triviaButton);
        me.game.world.addChild(profileButton);
        $("#logoutButton").fadeIn(100);
        $("#labButton").fadeIn(100);
        $("#triviaButton").fadeIn(100);
        $("#profileButton").fadeIn(100);

    }
});



