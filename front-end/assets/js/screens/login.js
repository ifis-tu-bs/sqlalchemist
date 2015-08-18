game.LoginScreen = me.ScreenObject.extend({
    /**
     *  Action to perform on state change: STATE_LOGIN
     */
    onResetEvent: function() {

        // workaround f�r android bug CB-4404
        if (me.device.android || me.device.android2) {
            if (me.device.isFullscreen) {
                me.device.exitFullscreen();
                document.body.style.minHeight = document.body.clientHeight + 'px';
            }
        }

        /**
         * Load screen-image for Login
         */
        me.game.world.addChild(
            new me.Sprite (
                0,0,
                me.loader.getImage('login_screen')
            ),
            1
        );


        /**
         * Function will be called when server responded.
         * @param xmlHttpRequest
         */
        function onLogin(xmlHttpRequest) {

            console.log("STATUS :" + xmlHttpRequest.status);
            if (xmlHttpRequest.status == 401){
                alert("wrong id or password!");
            } else{
                toMainMenu();
            }
        }

        /**
         * Ajax POST /login
         * returned value: xmlHttpRequest
         * on success: user session is created
         */
        function loginReply() {
            var userid     = document.getElementById("fId").value;
            var pw         = document.getElementById("fPassword").value;
            this.user_json = JSON.stringify({id: userid, password: pw});
            ajaxSendLoginRequest(this.user_json, onLogin);
        }


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

        clearUserIDField = function () {
            userid.clear();
            userid.removeEvent('click', clearUserIDField);
        };
        userid.addEvent('click', clearUserIDField);

        clearPasswordField = function () {
            password.clear();
            password.changeType('password');
            password.removeEvent('click', clearPasswordField);
        };
        password.addEvent('focus', clearPasswordField);


        function toSignUp() {
            me.state.change(STATE_SIGNUP);
        }
        function toPasswordReset() {
            me.state.change(STATE_FORGOTPASSWORD);
        }


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
        var loginButton     = new game.ClickableElement('loginButton', 'Enter', loginReply, 20, 6.5, 42.5, 64.5, 1);
        var signUpFirst     = new game.ClickableElement('signUpFirst', 'No Login yet? You can Sign Up here!', toSignUp, 25, 2, 25, 58.5, 1);
        var forgotPassword  = new game.ClickableElement('forgotPassword', 'Forgot Password? Click here!', toPasswordReset, 22, 2, 52, 58.5, 1);


        /**
         * add children to container
         */
        me.game.world.addChild(userid);
        me.game.world.addChild(password);

        me.game.world.addChild(loginButton);
        me.game.world.addChild(signUpFirst);
        me.game.world.addChild(forgotPassword);


    },

    //Gets the Settings on Login and starts the Menu-Music
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
