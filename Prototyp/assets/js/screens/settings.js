/**
 *
 * @type {Object|void}
 */

game.SettingsScreen = me.ScreenObject.extend({

    /**
     *  action to perform on state change.
     */
    onResetEvent : function() {

        /**
         *  set settings_screen
         */
        me.game.world.addChild(
            new me.Sprite (
                0,0,
                me.loader.getImage('settings_screen')
            ),
            1
        );

        /**
         *  add all necessary buttons to screen
         */

        onMenu = function(){
            me.state.change(STATE_MENU);
        };
        me.game.world.addChild(new music(450,195),3);
        me.game.world.addChild(new sound(450,295),4);
        me.game.world.addChild(new game.HUD.SettingsElements(125, 195),5);
        me.game.world.addChild(new backToMenu(950,-40),2);


        var oldPassword         = new game.TextInputElement('input','text', 'wOld', 'fOld', 35, 10, 48, 35, 2);
        var newPassword         = new game.TextInputElement('input','text', 'wNew', 'fNew', 35, 10, 48, 48, 2);
        var newPasswordAck      = new game.TextInputElement('input','text', 'wNewAck', 'fNewAck', 35, 10, 48, 61, 2);
        var changeHeader        = new game.TextOutputElement('changeHeader', 50, 5, 41, 26, 1);
        var resetHeader         = new game.TextOutputElement('resetHeader', 50, 20, 41, 36, 4);
        var keyBindingsHeader   = new game.TextOutputElement('keyBindingsHeader', 50, 5, 40, 26, 1);
        var keyBindings         = new game.TextOutputElement('keyBindings', 30, 25, 45, 36, 5);
        var keyBindingsDisc     = new game.TextOutputElement('keyBindingsDisc', 25, 25, 62, 36, 5);


        me.game.world.addChild(oldPassword);
        me.game.world.addChild(newPassword);
        me.game.world.addChild(newPasswordAck);
        me.game.world.addChild(changeHeader);
        me.game.world.addChild(resetHeader);
        me.game.world.addChild(keyBindingsHeader);
        me.game.world.addChild(keyBindings);
        me.game.world.addChild(keyBindingsDisc);

        oldPassword.insertText('old password');
        newPassword.insertText('new password');
        newPasswordAck.insertText('password confirmation');
        changeHeader.writeHTML("change password:");
        resetHeader.writeHTML("Are you sure you want" + "<br>" +
                              "to reset the story?" + "<br>" +
                              "Your whole progress" + "<br>" +
                              "will be deleted!");
        keyBindingsHeader.writeHTML("<u>" + "ingame key bindings:" + "<u>");
        keyBindingsDisc.writeHTML("&quot;m&quot;" + "<br>" +
                                  "&quot;n&quot;" + "<br>" +
                                  "&quot;esc&quot;" + "<br>" +
                                  "&quot;space&quot;" + "<br>" +
                                  "&quot;num[1-7]&quot;");
        keyBindings.writeHTML("music on/off:" + "<br>" +
                              "sounds on/off:" + "<br>" +
                              "leave dungeon:" + "<br>" +
                              "jump:" + "<br>" +
                              "use potion:");

        oldPassword.hide();
        newPassword.hide();
        newPasswordAck.hide();
        changeHeader.hide();
        resetHeader.hide();

        sendStudentRequest = function() {
            studentReply = function(xmlHttpRequest) {
                studentRequest.changeColor('verifyGreen');
                console.log(xmlHttpRequest);
            };
            ajaxSendUserStudentRequest(studentReply);
        };
        var studentRequest = new game.ClickableElement('studentRequest', "verify as student", sendStudentRequest, 33, 5, 48, 72, 1);
        me.game.world.addChild(studentRequest);

        SubmitResetClicked = function(){

            resetReply = function(xmlHttpRequest){

                window.location.reload();

            };
            ajaxSendChallengeResetRequest(resetReply);
        };


        var submitReset = new game.ClickableElement('submitReset', 'Yes I\'m sure', SubmitResetClicked, 24, 5, 53.5, 65, 1)
        me.game.world.addChild(submitReset);
        submitReset.hide();

        /**
         * check if password change is valid and POST via ajax call
         *
         * @constructor
         */
        SubmitPasswordClicked = function () {

            var oldPasswordData    = document.getElementById("fOld").value;
            var newPasswordData    = document.getElementById("fNew").value;
            var newPasswordAckData = document.getElementById("fNewAck").value;

            if (newPasswordData == newPasswordAckData) {
                var passwordJSON = JSON.stringify({password_old: oldPasswordData, password_new: newPasswordData});
            } else {
                alert("entered passwords do not match!");
            }

            /**
             *  POST new password via ajax function ajaxSendUsersRequest(jsonData, onload);
             *  JSON.user:
             "password_old" :String
             "password_new" :String

             on load function: confirmation for password change
             */
            function userReply() {
                me.state.change(me.state.SETTINGS);


            }

            ajaxSendUsersRequest(passwordJSON, userReply);
        };

        /**
         *  params for ClickableElement: (id, name, callback, width, height, left, top)
         */
        var submitPassword = new game.ClickableElement('submitPassword', 'submit', SubmitPasswordClicked, 15, 5, 58, 75, 1);
        me.game.world.addChild(submitPassword);
        submitPassword.hide();

        /**
         * open necessary elements for password change
         * @constructor
         */
        ChangePasswordClicked = function() {
            keyBindingsHeader.hide();
            keyBindings.hide();
            keyBindingsDisc.hide();
            storyReset.display();
            oldPassword.display();
            newPassword.display();
            newPasswordAck.display();
            changeHeader.display();
            resetHeader.hide();
            submitReset.hide();
            submitPassword.display();
            studentRequest.hide();


            clearOldPasswordField = function () {
                oldPassword.clear();
                oldPassword.changeType('password');
                oldPassword.removeEvent('click', clearOldPasswordField);
            };
            oldPassword.addEvent('click', clearOldPasswordField);

            clearNewPasswordField = function () {
                newPassword.clear();
                newPassword.changeType('password');
                newPassword.removeEvent('click', clearNewPasswordField);
            };
            newPassword.addEvent('click', clearNewPasswordField);

            clearNewPasswordAckField = function () {
                newPasswordAck.clear();
                newPasswordAck.changeType('password');
                newPasswordAck.removeEvent('click', clearNewPasswordAckField);
            };
            newPasswordAck.addEvent('click', clearNewPasswordAckField);
        };

        var changePassword = new game.ClickableElement('changePassword', 'change password?', ChangePasswordClicked, 23, 10, 13, 53, 2);
        me.game.world.addChild(changePassword);


        resetStoryClicked = function(){
            keyBindingsHeader.hide();
            keyBindings.hide();
            keyBindingsDisc.hide();
            changePassword.display();
            oldPassword.hide();
            newPassword.hide();
            newPasswordAck.hide();
            changeHeader.hide();
            resetHeader.display();
            submitPassword.hide();
            submitReset.display();
            studentRequest.hide();
        };

        var storyReset = new game.ClickableElement('storyReset', 'reset story mode?', resetStoryClicked, 23, 10, 13, 70, 2);
        me.game.world.addChild(storyReset);

    },

    onDestroyEvent: function(){

        function changeSet(xmlHttpRequest) {
            console.log(xmlHttpRequest);
        }

        function setSettings() {
            var music = game.data.music;
            var sound = game.data.sound;
            this.user_json_setting = JSON.stringify({music: music, sound: sound});
            ajaxSendProfileSettingsSetRequest(this.user_json_setting, changeSet);
        }

        setSettings();



    }

});