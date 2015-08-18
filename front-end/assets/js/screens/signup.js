game.SignUpScreen = me.ScreenObject.extend({

    /**
     *  Action to perform on state change: STATE_SIGNUP
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
         * Load screen-image for Sign-Up
         */
        me.game.world.addChild(
            new me.Sprite (
                0,0,
                me.loader.getImage('sign_up_screen')
            ),
            1
        );


        /**
         * Create all necessary TextInputElements for Sign-UP
         * @param : tag    : 'input' or 'textarea'
         *          type   : the type of a 'input' element or the name of a 'textarea' element as string
         *          id     : a unique alphanumeric string
         *          width  : the width of the element in percent of the width of the canvas
         *          height : the height of the element in percent of the height of the canvas
         *          left   : the left margin of the element in percent of the width of the canvas
         *          top    : the top margin of the element in percent of the height of the canvas
         *          rows   : the number of rows
         */
        var useridSignUp           = new game.TextInputElement('input', 'text', 'wIdSignUp', 'fIdSignUp', 55, 7, 22, 24, 2);
        var usernameSignUp         = new game.TextInputElement('input', 'text', 'wUsernameSignUp', 'fUsernameSignUp', 55, 7, 22, 34, 2);
        var passwordSignUp         = new game.TextInputElement('input', 'text', 'wPasswordSignUp', 'fPasswordSignUp', 55, 7, 22, 44, 2);
        var passwordAckSignUp      = new game.TextInputElement('input', 'text', 'wPasswordAckSignUp', 'fPasswordAckSignUp', 55, 7, 22, 54, 2);


        /**
         * Insert Text in TextInputElement as placeholder and workaround for clearing it by clicking in TextInputElement
         */
        useridSignUp.insertText('e-mail');
        usernameSignUp.insertText('username');
        passwordSignUp.insertText('password');
        passwordAckSignUp.insertText('password confirmation');


        clearUserIDField = function () {
            useridSignUp.clear();
            useridSignUp.removeEvent('click', clearUserIDField);
        };
        useridSignUp.addEvent('click', clearUserIDField);


        clearUsernameField = function () {
            usernameSignUp.clear();
            usernameSignUp.removeEvent('click', clearUsernameField);
        };
        usernameSignUp.addEvent('click', clearUsernameField);


        clearPasswordField = function () {
            passwordSignUp.clear();
            passwordSignUp.changeType('password');
            passwordSignUp.removeEvent('click', clearPasswordField);
        };
        passwordSignUp.addEvent('focus', clearPasswordField);


        clearPasswordAckField = function () {
            passwordAckSignUp.clear();
            passwordAckSignUp.changeType('password');
            passwordAckSignUp.removeEvent('click', clearPasswordAckField);
        };
        passwordAckSignUp.addEvent('focus', clearPasswordAckField);

        function toStart() {
            me.state.change(me.state.MENU);
        }



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
                /*  ~~~~~~~~~~~~ This is how to invoke the ajax-Call ~~~~~~~~~~~~
                 *
                 *  For POST, the first PARAM is the JSON.stringify() String (MUST BE JSON)
                 *            the second PARAM is the function to be called when the Server responded. in this Case: onload
                 *
                 *  For GET,  the only PARAM is the function
                 *
                 *  If defining the Request for a special Id, it will always be the first PARAM
                 *
                 */
                ajaxSendSignupRequest(this.user_object, onSubmit);
            }
        };


        function toLogin() {
            me.state.change(STATE_LOGIN);
        }

        /**
         * Create all necessary ClickableElements for Sign-UP
         * @param : id       : a unique alphanumeric string
         *          name     : text to display on screen
         *          callback : the callback function
         *          width    : the width of the element in percent of the width of the canvas
         *          height   : the height of the element in percent of the height of the canvas
         *          left     : the left margin of the element in percent of the width of the canvas
         *          top      : the top margin of the element in percent of the height of the canvas
         *          rows     : the number of rows
         */

        var submitButton      = new game.ClickableElement('submitButton', 'Enter', this.submitReply, 20, 6.5, 58.5, 64.5, 1);
        var backToLoginButton = new game.ClickableElement('backToLoginButton', 'Back', toLogin, 20, 6.5, 19.5, 64.5, 1);

        /**
         * add children to container
         */
        me.game.world.addChild(useridSignUp);
        me.game.world.addChild(usernameSignUp);
        me.game.world.addChild(passwordSignUp);
        me.game.world.addChild(passwordAckSignUp);

        me.game.world.addChild(submitButton);
        me.game.world.addChild(backToLoginButton);
    }


});
