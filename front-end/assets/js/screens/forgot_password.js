game.ForgotPasswordScreen = me.ScreenObject.extend({

    /**
    *  Action to perform on state change: STATE_LOGIN
    */
    onResetEvent: function() {

        /**
        * Load screen-image for Login
        */
        var background = new game.BackgroundElement('background', 100, 100, 0, 0, 'none');
        background.setImage("assets/data/img/gui/reset_password_screen.png", "back");
        me.game.world.addChild(background);

        $("#background").fadeIn(100);


        toLogin = function () {
            $("#background").fadeOut(100);
            $("#resetButton").fadeOut(100);
            $("#backToLoginButton").fadeOut(100);
            setTimeout( function() {
                me.state.change(STATE_LOGIN);
            }, 100);
        };

        /**
        * Function will be called when server responded.
        * @param : xmlHttpRequest
        */
        onReset = function () {
            toLogin();
        };

        /**
        * Ajax POST /login
        * returned value: xmlHttpRequest
        * on success: user session is created
        */
        this.resetPasswordReply = function() {
            var userid     = document.getElementById("fEmail").value;
            this.user_json = JSON.stringify({id: userid});

            ajaxSendUsersResetPasswordRequest(this.user_json, onReset);
        };

        /**
         * Create all necessary TextInputElements for Login
         */
        var userid = new game.TextInputElement('input', 'text', 'wEmail', 'fEmail', 55, 12, 22, 35, 2);
        me.game.world.addChild(userid);

        /**
         * Insert Text in TextInputElement as placeholder and workaround for clearing it by clicking in TextInputElement
         */
        userid.insertText('e-mail');

        this.clearUserIDField = function () {
            userid.clear();
            userid.removeEvent('click', this.clearUserIDField);
        };
        userid.addEvent('click', this.clearUserIDField);


        /**
         * Create necessary ClickableElements for Sign-UP
         */
        var resetButton       = new game.ClickableElement('resetButton', 'Reset', this.resetPasswordReply, 20, 6.5, 59.5, 64.5, 1);
        var backToLoginButton = new game.ClickableElement('backToLoginButton', 'Back', toLogin, 20, 6.5, 19.5, 64.5, 1);
        me.game.world.addChild(resetButton);
        me.game.world.addChild(backToLoginButton);
        $("#resetButton").fadeIn(100);
        $("#backToLoginButton").fadeIn(100);


    }
});
