game.SettingsScreen = me.ScreenObject.extend({

    onResetEvent : function() {

        /**
         *  set settings background
         */
        var backgroundsettings = new game.BackgroundElement('backgroundsettings', 100, 100, 0, 0, 'none');
        backgroundsettings.setImage("assets/data/img/gui/settings_screen.png", "back");
        me.game.world.addChild(backgroundsettings);

        var settingsBackgroundHeader = new game.TextOutputElement('settingsHeaderId', 40, 10, 30, 3, 1);
        me.game.world.addChild(settingsBackgroundHeader);
        settingsBackgroundHeader.writeHTML("SETTINGS");

        function fadeOutElements(){
            $("#studentRequest").fadeOut(100);
            $("#backgroundsettings").fadeOut(100);
            $("#storyReset").fadeOut(100);
            $("#changePassword").fadeOut(100);
            $("#backFromSettings").fadeOut(100);
            $("#soundButtonId").fadeOut(100);
            $("#musicButtonId").fadeOut(100);
            $("#soundsElement").fadeOut(100);
            $("#deleteUser").fadeOut(100);
            $("#submitDeletion").fadeOut(100);
            $("#verifyGreen").fadeOut(100);
            $("#submitVerification").fadeOut(100);
            $("#submitPassword").fadeOut(100);
            $("#submitReset").fadeOut(100);
            $("[id*=Header]").fadeOut(100);
            $("[id^=keyBindings]").fadeOut(100);
        }

        /**
         *  add all necessary buttons to screen
         */
        this.backToMenu = function () {
            fadeOutElements();
            setTimeout( function() {
                me.state.change(me.state.MENU);
            }, 100);
        };

        var backFromSettings = new game.ClickableElement('backFromSettings','', this.backToMenu, 14.01515, 19.53125, 77, 0, 1);
        backFromSettings.setImage("assets/data/img/buttons/back_button_ink.png", "back");
        me.game.world.addChild(backFromSettings);

        var oldPassword       = new game.TextInputElement('input','text', 'wOld', 'fOld', 35, 10, 48, 35, 2);
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

        this.sendStudentRequest = function() {

            keyBindingsHeader.hide();
            keyBindings.hide();
            keyBindingsDisc.hide();
            oldPassword.hide();
            newPassword.hide();
            newPasswordAck.hide();
            changeHeader.hide();
            resetHeader.hide();
            submitReset.hide();
            submitPassword.hide();
            studentRequest.hide();
            deleteUserHeader.hide();
            submitDeletion.hide();
            verifyUserHeader.display();
            submitVerification.display();

            this.sendVerificationRequest = function (){
                studentReply = function(xmlHttpRequest) {
                    studentRequest.changeColor('verifyGreen');
                    //console.log(xmlHttpRequest);
                };
                ajaxSendUserStudentRequest(studentReply);
            };

        };

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
         */
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
             */
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
         */
        var submitPassword = new game.ClickableElement('submitPassword', 'submit', this.SubmitPasswordClicked, 15, 5, 58, 75, 1);
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

        this.musicClicked = function () {
            $("[id*='musicImage']").remove();
            if (game.data.music) {
                musicButton.setImage("assets/data/img/buttons/ButtonsMusicOff.png", "musicImage");
                game.data.music = false;
                me.audio.stopTrack();
            } else {
                musicButton.setImage("assets/data/img/buttons/ButtonsMusic.png", "musicImage");
                game.data.music = true;
                if(me.state.isCurrent(me.state.PLAY)) {
                    me.audio.playTrack(game.data.recentTitle ,game.data.musicVolume);
                } else {
                    me.audio.playTrack("Menu",game.data.musicVolume);
                }
            }
        };

        var musicButton = new game.ClickableElement('musicButtonId', '', this.musicClicked, 4.848485, 8.333333, 30, 22, 1);
        me.game.world.addChild(musicButton);
        if (game.data.music) {
            musicButton.setImage("assets/data/img/buttons/ButtonsMusic.png", "musicImage");
        } else {
            musicButton.setImage("assets/data/img/buttons/ButtonsMusicOff.png", "musicImage");
        }

        this.soundClicked = function (){
            $("[id*='soundImage']").remove();
            if (game.data.sound) {
                soundButton.setImage("assets/data/img/buttons/ButtonsSoundOff.png", "soundImage");
                game.data.sound = false;
            } else {
                soundButton.setImage("assets/data/img/buttons/ButtonsSound.png", "soundImage");
                game.data.sound = true;
                me.audio.play("cling", false, null, game.data.soundVolume);
            }
        };

        var soundButton = new game.ClickableElement('soundButtonId', '', this.soundClicked, 4.848485, 8.333333, 30, 32, 1);
        me.game.world.addChild(soundButton);
        if (game.data.music) {
            soundButton.setImage("assets/data/img/buttons/ButtonsSound.png", "soundImage");
        } else {
            soundButton.setImage("assets/data/img/buttons/ButtonsSoundOff.png", "soundImage");
        }

        var soundsElement = new game.TextOutputElement('soundsElement', 15, 20, 15, 21, 4);
        me.game.world.addChild(soundsElement);
        soundsElement.writeHTML("music:" + "<br>" + "sound:", 'soundPara');
    },

    onDestroyEvent: function(){

        //console.log(game.data.music, game.data.sound);
        function changeSet(xmlHttpRequest) {
            //console.log(xmlHttpRequest);
        }

        function setSettings() {
            var music = game.data.music;
            var sound = game.data.sound;
            this.user_json_setting = JSON.stringify({music: music, sound: sound});
            //console.log("AJAX: ", this.user_json_setting);
            ajaxSendProfileSettingsSetRequest(this.user_json_setting, changeSet);
        }

        setSettings();

    }

});
