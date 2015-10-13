game.LoginScreen = me.ScreenObject.extend({
    /**
     *  Action to perform on state change: STATE_LOGIN
     */
    onResetEvent: function() {

        /**
         * Load screen-image for Login
         */
        var background = new game.BackgroundElement('background', 100, 100, 0, 0, 'none');
        background.setImage("assets/data/img/gui/login_screen.png", "back");
        me.game.world.addChild(background);

        $("#background").fadeIn("slow");

        /**
         * Function will be called when server responded.
         * @param xmlHttpRequest
         */
        function onLogin(xmlHttpRequest) {
            //console.log("STATUS :" + xmlHttpRequest.status);
            if (xmlHttpRequest.status == 401){
                alert("wrong id or password!");
            } else{
                $("#background").fadeOut(100);
                setTimeout( function() {
                    me.state.change(me.state.MENU);
                }, 100);
            }
        }

        /**
         * Ajax POST /login
         * returned value: xmlHttpRequest
         * on success: user session is created
         */
        this.loginReply = function () {
            var userid     = document.getElementById("fId").value;
            var pw         = document.getElementById("fPassword").value;
            this.user_json = JSON.stringify({id: userid, password: pw});
            ajaxSendLoginRequest(this.user_json, onLogin);
        };


        /**
         * Create all necessary TextInputElements for Login
         * @param : tag       : 'input' or 'textarea'
         *          type      : the type of a 'input' element or the name of a 'textarea' element as string
         *          wrapperId : a unique alphanumeric string
         *          fieldId   : a unique alphanumeric string
         *          width     : the width of the element in percent of the width of the canvas
         *          height    : the height of the element in percent of the height of the canvas
         *          left      : the left margin of the element in percent of the width of the canvas
         *          top       : the top margin of the element in percent of the height of the canvas
         *          rows      : the number of rows
         */
        var userid   = new game.TextInputElement('input', 'text', 'wId', 'fId', 55, 12, 22, 25, 2);
        var password = new game.TextInputElement('input', 'text', 'wPassword', 'fPassword', 55, 12, 22, 42, 2);


        /**
         * Insert Text in TextInputElement as placeholder and workaround for clearing it by clicking in TextInputElement
         */
        userid.insertText('e-mail');
        password.insertText('password');

        this.clearUserIDField = function () {
            userid.clear();
            userid.removeEvent('click', this.clearUserIDField);
        };
        userid.addEvent('click', this.clearUserIDField);

        this.clearPasswordField = function () {
            password.clear();
            password.changeType('password');
            password.removeEvent('click', this.clearPasswordField);
        };
        password.addEvent('focus', this.clearPasswordField);


        this.toSignUp = function () {
            $("#background").fadeOut(100);
            setTimeout( function() {
                me.state.change(STATE_SIGNUP);
            }, 100);
        };

        this.toPasswordReset = function () {
            $("#background").fadeOut(100);
            setTimeout( function() {
                me.state.change(STATE_FORGOTPASSWORD);
            }, 100);
        };


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
        var loginButton     = new game.ClickableElement('loginButton', 'Enter', this.loginReply, 20, 6.5, 42.5, 64.5, 1);
        var signUpFirst     = new game.ClickableElement('signUpFirst', 'No Login yet? You can Sign Up here!', this.toSignUp, 25, 2, 25, 58.5, 1);
        var forgotPassword  = new game.ClickableElement('forgotPassword', 'Forgot Password? Click here!', this.toPasswordReset, 22, 2, 52, 58.5, 1);


        /**
         * add children to container
         */
        me.game.world.addChild(userid);
        me.game.world.addChild(password);

        me.game.world.addChild(loginButton);
        me.game.world.addChild(signUpFirst);
        me.game.world.addChild(forgotPassword);


    },

    /**
     * Gets the Settings on Login and starts the Menu-Music
     */
    onDestroyEvent: function() {

        //get the users settings
        function getSettings(xmlHttpRequest){

            var profile = JSON.parse(xmlHttpRequest.responseText);
            game.data.music = profile.settings.music;
            game.data.sound = profile.settings.sound;
            game.data.lofiCoins = profile.coins;

             //starts background music
            if(game.data.music ){
                me.audio.playTrack("menu",game.data.musicVolume);
            }
            if(game.data.sound) {
                me.audio.play("switch", false, null, game.data.soundVolume);
            }


        }
        ajaxSendProfileRequest(getSettings);
    }
});
