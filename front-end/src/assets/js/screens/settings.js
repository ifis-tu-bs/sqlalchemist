game.SettingsScreen = me.ScreenObject.extend({

    onResetEvent : function() {
        /**
         *  set settings background
         */
        var rootContainer = new game.fdom.RootContainer('/assets/data/img/gui/settings_screen.png');
        me.game.world.addChild(rootContainer);

        var title = new game.fdom.TitleElement(rootContainer, '30%','10%','35%','5%', 'Settings', 'Title SettingsScreen');
        me.game.world.addChild(title);

        var backButton = new game.fdom.ButtonElement(rootContainer, '18%','20%','75%','0%', '', 'Button SettingsScreen Back', false, function() {
            $(rootContainer.getNode()).fadeOut(100);
            console.log("Push Settings");
            setTimeout(function() {
                me.state.change(me.state.MENU);
            }, 50);
        });
        me.game.world.addChild(backButton);

        var settingsContainerElement = new game.fdom.ContainerElement(rootContainer, '35%','27%','10%','20%', 'Container SettingsScreen Settings');
        me.game.world.addChild(settingsContainerElement);

        var settingsTextSound = new game.fdom.TitleElement(settingsContainerElement, '30%','24%','20%','10%', 'sound: ', 'Text SettingsScreen Sound ');
        me.game.world.addChild(settingsTextSound);

        var settingsTextMusic = new game.fdom.TitleElement(settingsContainerElement, '30%','24%','20%','50%', 'music: ', 'Text SettingsScreen Music');
        me.game.world.addChild(settingsTextMusic);

        var settingsSound = new game.fdom.CheckBoxElement(settingsContainerElement, '18%','24%','50%','10%', '', 'CheckBox SettingsScreen Sound');
        me.game.world.addChild(settingsSound);

        var settingsMusic = new game.fdom.CheckBoxElement(settingsContainerElement, '18%','24%','50%','47%', '', 'CheckBox SettingsScreen Music');
        me.game.world.addChild(settingsMusic);

        /*var oldPassword       = new game.TextInputElement('input','text', 'wOld', 'fOld', 35, 10, 48, 35, 2);
        var newPassword       = new game.TextInputElement('input','text', 'wNew', 'fNew', 35, 10, 48, 48, 2);
        var newPasswordAck    = new game.TextInputElement('input','text', 'wNewAck', 'fNewAck', 35, 10, 48, 61, 2);
        var changeHeader      = new game.TextOutputElement('changeHeader', 50, 5, 41, 26, 1);
        var resetHeader       = new game.TextOutputElement('resetHeader', 50, 20, 41, 36, 4);
        var keyBindingsHeader = new game.TextOutputElement('keyBindingsHeader', 50, 5, 40, 26, 1);
        var keyBindings       = new game.TextOutputElement('keyBindings', 30, 25, 45, 36, 5);
        var keyBindingsDisc   = new game.TextOutputElement('keyBindingsDisc', 25, 25, 62, 36, 5);
        var deleteUserHeader  = new game.TextOutputElement('deleteUserHeader', 50, 20, 41, 36, 4);
        var verifyUserHeader  = new game.TextOutputElement('verifyUserHeader', 50, 25, 41, 36, 5);
        me.game.world.addChild(oldPassword);
        me.game.world.addChild(newPassword);
        me.game.world.addChild(newPasswordAck);
        me.game.world.addChild(changeHeader);
        me.game.world.addChild(resetHeader);
        me.game.world.addChild(keyBindingsHeader);
        me.game.world.addChild(keyBindings);
        me.game.world.addChild(keyBindingsDisc);
        me.game.world.addChild(deleteUserHeader);
        me.game.world.addChild(verifyUserHeader);

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
        deleteUserHeader.writeHTML("Are you sure about" + "<br>" +
                                   "deleting your account?" + "<br>" +
                                   "All your data" + "<br>" +
                                   "will be deleted!");
        verifyUserHeader.writeHTML("As a student, you have to sign" + "<br>" +
                                   "up at the HMS first!" + "<br>" +
                                   "If you signed up here first," + "<br>" +
                                   "then you have to verify" + "<br>" +
                                   "yourself as student.");

        oldPassword.hide();
        newPassword.hide();
        newPasswordAck.hide();
        changeHeader.hide();
        resetHeader.hide();
        deleteUserHeader.hide();
        verifyUserHeader.hide();

        var submitVerification = new game.ClickableElement('submitVerification', "verify!", this.sendVerificationRequest, 33, 5, 48, 72, 1);
        me.game.world.addChild(submitVerification);

        var studentRequest;
        if (game.data.profile.student) {
            studentRequest = new game.ClickableElement('verifyGreen', "verify as student?", this.sendStudentRequest, 33, 5, 48, 72, 1);
            me.game.world.addChild(studentRequest);
        } else {
            studentRequest = new game.ClickableElement('studentRequest', "verify as student?", this.sendStudentRequest, 33, 5, 48, 72, 1);
            me.game.world.addChild(studentRequest);
        }

        SubmitResetClicked = function(){

            resetReply = function(xmlHttpRequest){
                fadeOutElements();
                setTimeout( function() {
                    window.location.reload();
                }, 100);
            };
            ajaxSendChallengeResetRequest(resetReply);
        };


        var submitReset = new game.ClickableElement('submitReset', 'Yes I\'m sure', SubmitResetClicked, 24, 5, 53.5, 65, 1);
        me.game.world.addChild(submitReset);
        submitReset.hide();

        /**
         * check if password change is valid and POST via ajax call
         *
         * @constructor
         *
        this.SubmitPasswordClicked = function () {

            var oldPasswordData    = document.getElementById("fOld").value;
            var newPasswordData    = document.getElementById("fNew").value;
            var newPasswordAckData = document.getElementById("fNewAck").value;
            var passwordJSON;

            if (newPasswordData == newPasswordAckData) {
                passwordJSON = JSON.stringify({password_old: oldPasswordData, password_new: newPasswordData});
            } else {
                alert("entered passwords do not match!");
            }

            /**
             *  POST new password via ajax function ajaxSendUsersRequest(jsonData, onload);
             *  JSON.user:
             *  "password_old" :String
             *  "password_new" :String
             *  on load function: confirmation for password change
             *
            function userReply() {
                fadeOutElements();
                setTimeout( function() {
                    me.state.change(me.state.SETTINGS);
                }, 100);
            }

            ajaxSendUsersRequest(passwordJSON, userReply);
        };

        /**
         *  params for ClickableElement: (id, name, callback, width, height, left, top)
         *
        var submitPassword = new game.ClickableElement('submitPassword', 'submit', this.SubmitPasswordClicked, 15, 5, 58, 75, 1);
        me.game.world.addChild(submitPassword);
        submitPassword.hide();

        /**
         * open necessary elements for password change
         * @constructor
         *
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
            deleteUserHeader.hide();
            submitDeletion.hide();
            verifyUserHeader.hide();
            submitVerification.hide();


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

        var changePassword = new game.ClickableElement('changePassword', 'change password', ChangePasswordClicked, 23, 10, 13, 45, 2);
        me.game.world.addChild(changePassword);


        this.resetStoryClicked = function(){
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
            deleteUserHeader.hide();
            submitDeletion.hide();
            verifyUserHeader.hide();
            submitVerification.hide();
        };

        var storyReset = new game.ClickableElement('storyReset', 'reset story mode', this.resetStoryClicked, 23, 10, 13, 58, 2);
        me.game.world.addChild(storyReset);


        this.submitDeletionClicked = function () {
            function deletionReply(xmlHttpRequest) {
                console.log(xmlHttpRequest);
                fadeOutElements();
                setTimeout( function() {
                    window.location.reload();
                }, 100);
            }
            ajaxSendUsersDeleteRequest(deletionReply);
        };

        var submitDeletion = new game.ClickableElement('submitDeletion', 'Yes I\'m sure', this.submitDeletionClicked, 24, 5, 53.5, 65, 1);
        me.game.world.addChild(submitDeletion);
        submitDeletion.hide();

        this.deleteUserClicked = function () {
            keyBindingsHeader.hide();
            keyBindings.hide();
            keyBindingsDisc.hide();
            oldPassword.hide();
            newPassword.hide();
            newPasswordAck.hide();
            changeHeader.hide();
            resetHeader.hide();
            submitPassword.hide();
            submitReset.hide();
            studentRequest.hide();
            verifyUserHeader.hide();
            deleteUserHeader.display();
            submitDeletion.display();
            submitVerification.hide();
        };

        var deleteUser = new game.ClickableElement('deleteUser', "delete your account", this.deleteUserClicked, 23, 10, 13, 71, 2);
        me.game.world.addChild(deleteUser);
*/
    }

});
