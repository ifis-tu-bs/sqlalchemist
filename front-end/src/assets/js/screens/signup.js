game.SignUpScreen = me.ScreenObject.extend({

    /**
     *  Action to perform on state change: STATE_SIGNUP
     */
    onResetEvent: function() {
        
        /**
         * Load screen-image for Sign-Up
         */
        var background = new game.BackgroundElement('background', 100, 100, 0, 0, 'none');
        background.setImage("assets/data/img/gui/sign_up_screen.png", "back");
        me.game.world.addChild(background);
        $("#background").fadeIn(100);


        /**
         * Create all necessary TextInputElements for Sign-UP
         */
        var useridSignUp           = new game.TextInputElement('input', 'text', 'wIdSignUp', 'fIdSignUp', 55, 7, 22, 24, 2);
        var usernameSignUp         = new game.TextInputElement('input', 'text', 'wUsernameSignUp', 'fUsernameSignUp', 55, 7, 22, 34, 2);
        var passwordSignUp         = new game.TextInputElement('input', 'text', 'wPasswordSignUp', 'fPasswordSignUp', 55, 7, 22, 44, 2);
        var passwordAckSignUp      = new game.TextInputElement('input', 'text', 'wPasswordAckSignUp', 'fPasswordAckSignUp', 55, 7, 22, 54, 2);
        me.game.world.addChild(useridSignUp);
        me.game.world.addChild(usernameSignUp);
        me.game.world.addChild(passwordSignUp);
        me.game.world.addChild(passwordAckSignUp);


        /**
         * Insert Text in TextInputElement as placeholder and workaround for clearing it by clicking in TextInputElement
         */
        useridSignUp.insertText('e-mail');
        usernameSignUp.insertText('username');
        passwordSignUp.insertText('password');
        passwordAckSignUp.insertText('password confirmation');


        this.clearUserIDField = function () {
            useridSignUp.clear();
            useridSignUp.removeEvent('click', this.clearUserIDField);
        };
        useridSignUp.addEvent('click', this.clearUserIDField);


        this.clearUsernameField = function () {
            usernameSignUp.clear();
            usernameSignUp.removeEvent('click', this.clearUsernameField);
        };
        usernameSignUp.addEvent('click', this.clearUsernameField);


        this.clearPasswordField = function () {
            passwordSignUp.clear();
            passwordSignUp.changeType('password');
            passwordSignUp.removeEvent('click', this.clearPasswordField);
        };
        passwordSignUp.addEvent('focus', this.clearPasswordField);


        this.clearPasswordAckField = function () {
            passwordAckSignUp.clear();
            passwordAckSignUp.changeType('password');
            passwordAckSignUp.removeEvent('click', this.clearPasswordAckField);
        };
        passwordAckSignUp.addEvent('focus', this.clearPasswordAckField);

        toStart = function () {
            $("#background").fadeOut(100);
            $("#submitButton").fadeOut(100);
            $("#backToLoginButton").fadeOut(100);
            setTimeout( function() {
                me.state.change(me.state.MENU);
            }, 100);
        };


        /**
         *  action to perform when Submit-Button is clicked.
         */
        this.submitReply = function() {

            var id       = document.getElementById("fIdSignUp").value;
            var username = document.getElementById("fUsernameSignUp").value;
            var pw       = document.getElementById("fPasswordSignUp").value;
            var pwAck    = document.getElementById("fPasswordAckSignUp").value;


            /**
             * Parse TextInputElement values into JSON-String.
             */
            this.user_object = JSON.stringify({username: username, id: id, password: pw});


            function onSubmit(xmlHttpRequest) {
                //console.log("Response of POST" + xmlHttpRequest.responseText);
                /**
                 * Check if username or id already taken.
                 */
                var signUp_JSON = JSON.parse(xmlHttpRequest.responseText);
                if (signUp_JSON.id == 1) {
                    alert("id already taken");
                } else if (signUp_JSON.username == 1) {
                    alert("username already taken!");
                } else {
                    toStart();
                }

            }
            /**
             * Ckeck if entered Password is valid.
             */
            if (pw != pwAck) {
                alert("entered passwords do not match");
            } else if (pw == null) {
                alert("password may not be empty");
            } else {
                ajaxSendSignupRequest(this.user_object, onSubmit);
            }
        };


        this.toLogin = function() {
            $("#background").fadeOut(100);
            $("#submitButton").fadeOut(100);
            $("#backToLoginButton").fadeOut(100);
            setTimeout( function() {
                me.state.change(STATE_LOGIN);
            }, 100);
        };

        /**
         * Create all necessary ClickableElements for Sign-UP
         */

        var submitButton      = new game.ClickableElement('submitButton', 'Enter', this.submitReply, 20, 6.5, 58.5, 64.5, 1);
        var backToLoginButton = new game.ClickableElement('backToLoginButton', 'Back', this.toLogin, 20, 6.5, 19.5, 64.5, 1);
        me.game.world.addChild(submitButton);
        me.game.world.addChild(backToLoginButton);
        $("#submitButton").fadeIn(100);
        $("#backToLoginButton").fadeIn(100);
    }


});
