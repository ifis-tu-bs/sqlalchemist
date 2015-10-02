
game.StartScreen = me.ScreenObject.extend({
    /**
     *  action to perform on state change
     */

    onResetEvent: function() {

        me.game.world.addChild(
            new me.Sprite (
                0,0,
                me.loader.getImage('black')
            ),
            1
        );

        
        var background = new game.BackgroundElement('background', 100, 100, 0, 0, 'none');
        background.setImage("assets/data/img/gui/title_screen.png", "back");
        me.game.world.addChild(background);

        $("#background").fadeIn("slow");

        //get the users settings
        function getSettings(xmlHttpRequest) {


            var profile = JSON.parse(xmlHttpRequest.responseText);
            console.log(profile);
            game.data.music = profile.settings.music;
            game.data.sound = profile.settings.sound;
            game.data.lofiCoins = profile.coins;

            //starts background music
            if (game.data.music) {
                me.audio.playTrack("menu", game.data.musicVolume);

            }
            if (game.data.sound) {
                me.audio.play("switch", false, null, game.data.soundVolume);
            }
        }

        function checkSession(xmlHttpRequest){
            console.log(xmlHttpRequest);

            game.data.gotSession = true;
            var session = JSON.parse(xmlHttpRequest.responseText);
            console.log(session);
            ajaxSendProfileRequest(getSettings);

        }
        ajaxSendProfileRequest(checkSession);


        /**
         * these functions are called when buttons are clicked.
         * Here: simple state change.
         */


        this.onStart = function() {
            $("#background").fadeOut(100);
            $("#startButton").fadeOut(100);
            setTimeout( function() {
                if(game.data.gotSession){
                    me.state.change(me.state.MENU);
                } else {
                    me.state.change(STATE_LOGIN);
                }
            }, 100);
        };

        /**
         * Add all needed buttons to start screen
         * @param : id       : a unique alphanumeric string
         *          name     : text to display on screen
         *          callback : the callback function
         *          width    : the width of the element in percent of the width of the canvas
         *          height   : the height of the element in percent of the height of the canvas
         *          left     : the left margin of the element in percent of the width of the canvas
         *          top      : the top margin of the element in percent of the height of the canvas
         *          rows     : the number of rows
         */
        var startButton  = new game.ClickableElement('startButton', 'start', this.onStart, 20, 8.7, 43, 50, 1);
        me.game.world.addChild(startButton);
        $("#startButton").fadeIn(100);

    }
});