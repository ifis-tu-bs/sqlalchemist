game.TitleScreen = me.ScreenObject.extend({

    onResetEvent : function() {

        /**
         * these functions are called when buttons are clicked.
         * here: simple state changes.
         */

         if(game.data.user.settings.music) {
             game.data.music = true;
             if(!game.data.musicAlreadyPlaying) {
                 me.audio.playTrack("Menu",game.data.musicVolume);
                 game.data.musicAlreadyPlaying = true;

             }
         }
        if(game.data.user.settings.sound) {
            game.data.sound = true;
        }

        function fadeOutElements() {
            $("[id^=background]").fadeOut(100);
            $("[id$=Button]").fadeOut(100);
            $("[id$=Student]").fadeOut(100);
        }

        onLab = function(){
            if(game.data.sound){
                me.audio.play("door", false, null, game.data.soundVolume);
            }
            fadeOutElements();
            setTimeout( function() {
                me.state.change(me.state.READY);
            }, 100);
        };

        onTrivia = function(){
            if(game.data.sound){
                me.audio.play("door", false, null, game.data.soundVolume);
            }
            fadeOutElements();
            setTimeout( function() {
                me.state.change(STATE_TRIVIA);
            }, 100);
        };

        onProfile = function(){
            if(game.data.sound){
                me.audio.play("scroll", false, null, game.data.soundVolume);
            }
            fadeOutElements();
            setTimeout( function() {
                me.state.change(STATE_PROFILE);
            }, 100);
        };

        onSettings = function(){
            if(game.data.sound){
                me.audio.play("scroll", false, null, game.data.soundVolume);
            }
            fadeOutElements();
            setTimeout( function() {
                me.state.change(me.state.SETTINGS);
            }, 100);
        };

        onHighscore = function(){
            if(game.data.sound){
                me.audio.play("page", false, null, game.data.soundVolume);
            }
            fadeOutElements();
            setTimeout( function() {
                me.state.change(me.state.HIGHSCORE);
            }, 100);

        };

        onShop = function(){
            if(game.data.sound){
                me.audio.play("door", false, null, game.data.soundVolume);
            }
            fadeOutElements();
            setTimeout( function() {
                me.state.change(STATE_SHOP);
            }, 100);
        };

        onHomework = function(){
            if(game.data.sound){
                me.audio.play("switch", false, null, game.data.soundVolume);
            }
            fadeOutElements();
            setTimeout( function() {
                me.state.change(STATE_HOMEWORK);
            }, 100);
        };

        onLogout = function(xmlHttpRequest){
            //console.log("Logout Response" + xmlHttpRequest.responseText);
            fadeOutElements();
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
                background.setImage("assets/data/img/gui/menu_no_homework.png", "back");
                me.game.world.addChild(background);
                $("#background").fadeIn(100);

                var labButton       = new game.ClickableElement('labButton', '', onLab, 21.742424, 52.34375, 3.333333, 5.208333, 1);
                var triviaButton    = new game.ClickableElement('triviaButton', '', onTrivia, 37.348485, 39.453125, 31.287879, 14.84375, 1);
                var settingsButton  = new game.ClickableElement('settingsButton', '', onSettings, 14.621212, 33.072917, 51.212121, 56.380208, 1);
                var highscoreButton = new game.ClickableElement('highscoreButton', '', onHighscore, 23.939394, 26.692708, 73.863636, 61.848958, 1);
                var shopButton      = new game.ClickableElement('shopButton', '', onShop, 13.712121, 32.942708, 32.575758, 56.380208, 1);
                var profileButton   = new game.ClickableElement('profileButton', '', onProfile, 21.439394, 50.651042, 74.924242, 4.6875, 1);
                me.game.world.addChild(labButton);
                me.game.world.addChild(triviaButton);
                me.game.world.addChild(settingsButton);
                me.game.world.addChild(highscoreButton);
                me.game.world.addChild(shopButton);
                me.game.world.addChild(profileButton);
                labButton.setImage("assets/data/img/buttons/menubuttons/storymode.png", "storymodeImage");
                triviaButton.setImage("assets/data/img/buttons/menubuttons/quizmode.png", "quizmodeImage");
                settingsButton.setImage("assets/data/img/buttons/menubuttons/settings.png", "settingsImage");
                highscoreButton.setImage("assets/data/img/buttons/menubuttons/rankings.png", "rankingsImage");
                shopButton.setImage("assets/data/img/buttons/menubuttons/shop.png", "shopImage");
                profileButton.setImage("assets/data/img/buttons/menubuttons/profile.png", "profileImage");

            } else {

                var background2 = new game.BackgroundElement('background2', 100, 100, 0, 0, 'none');
                background2.setImage("assets/data/img/gui/menu_homework.png", "back2");
                me.game.world.addChild(background2);
                $("#background2").fadeIn(100);

                var labButtonStudent       = new game.ClickableElement('labButtonStudent', '', onLab, 21.742424, 52.34375, 3.333333, 5.208333, 1);
                var triviaButtonStudent    = new game.ClickableElement('triviaButtonStudent', '', onTrivia, 37.348485, 39.453125, 31.287879, 14.84375, 1);
                var settingsButtonStudent  = new game.ClickableElement('settingsButtonStudent', '', onSettings, 14.621212, 33.072917, 54.924242, 56.380208, 1);
                var highscoreButtonStudent = new game.ClickableElement('highscoreButtonStudent', '', onHighscore, 23.939394, 26.692708, 73.863636, 61.848958, 1);
                var shopButtonStudent      = new game.ClickableElement('shopButtonStudent', '', onShop, 13.712121, 32.942708, 37.575758, 56.380208, 1);
                var homeworkButton         = new game.ClickableElement('homeworkButton', '', onHomework, 27.80303, 28.645833, 5.30303, 66.666667, 1);
                var profileButtonStudent   = new game.ClickableElement('profileButtonStudent', '', onProfile, 21.439394, 50.651042, 74.621212, 4.6875, 1);
                me.game.world.addChild(labButtonStudent);
                me.game.world.addChild(triviaButtonStudent);
                me.game.world.addChild(settingsButtonStudent);
                me.game.world.addChild(highscoreButtonStudent);
                me.game.world.addChild(shopButtonStudent);
                me.game.world.addChild(homeworkButton);
                me.game.world.addChild(profileButtonStudent);
                labButtonStudent.setImage("assets/data/img/buttons/menubuttons/storymode.png", "storymodeImage");
                triviaButtonStudent.setImage("assets/data/img/buttons/menubuttons/quizmode.png", "quizmodeImage");
                settingsButtonStudent.setImage("assets/data/img/buttons/menubuttons/settings.png", "settingsImage");
                highscoreButtonStudent.setImage("assets/data/img/buttons/menubuttons/rankings.png", "rankingsImage");
                shopButtonStudent.setImage("assets/data/img/buttons/menubuttons/shop.png", "shopImage");
                homeworkButton.setImage("assets/data/img/buttons/menubuttons/homeworkshelf.png", "shopImage");
                profileButtonStudent.setImage("assets/data/img/buttons/menubuttons/profile.png", "profileImage");
            }

        };

        ajaxSendProfileRequest(onProfileReply);

        var logoutButton = new game.ClickableElement('logoutButton', '', logoutReply, 14.1, 13.8, 43, 0, 2);
        logoutButton.setImage("assets/data/img/buttons/Logout.png", "logout");
        me.game.world.addChild(logoutButton);
        $("#logoutButton").fadeIn(100);
    }
});
