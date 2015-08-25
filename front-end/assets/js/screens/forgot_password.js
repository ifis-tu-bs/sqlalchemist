game.ForgotPasswordScreen = me.ScreenObject.extend({

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
                me.loader.getImage('reset_password_screen')
            ),
            1
        );


        /**
        * Function will be called when server responded.
        * @param : xmlHttpRequest
        */
        function onReset() {
            toLogin();
        }

        /**
        * Ajax POST /login
        * returned value: xmlHttpRequest
        * on success: user session is created
        */
        function resetPasswordReply() {
            var userid     = document.getElementById("fId").value;
            this.user_json = JSON.stringify({id: userid});

            ajaxSendUsersResetPasswordRequest(this.user_json, onReset);

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
        var userid   = new game.TextInputElement('input', 'text', 'wId', 'fId', 55, 12, 22, 35, 2);


        /**
         * Insert Text in TextInputElement as placeholder and workaround for clearing it by clicking in TextInputElement
         */
        userid.insertText('e-mail');


        clearUserIDField = function () {
            userid.clear();
            userid.removeEvent('click', clearUserIDField);
        };
        userid.addEvent('click', clearUserIDField);


        function toLogin() {
            me.state.change(STATE_LOGIN);
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
        var resetButton       = new game.ClickableElement('resetButton', 'Reset', resetPasswordReply, 20, 6.5, 59.5, 64.5, 1);
        var backToLoginButton = new game.ClickableElement('backToLoginButton', 'Back', toLogin, 20, 6.5, 19.5, 64.5, 1);

        /**
         * add children to container
         */
        me.game.world.addChild(userid);
        me.game.world.addChild(resetButton);
        me.game.world.addChild(backToLoginButton);


    }
});
