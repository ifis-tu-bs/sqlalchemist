game.SettingsScreen = me.ScreenObject.extend({

    onResetEvent : function() {
        /**
         *  set settings background
         */
        var rootContainer = new game.fdom.RootContainer('/assets/data/img/gui/wood_screen.png');
        me.game.world.addChild(rootContainer);
        rootContainer.hide();
        $(rootContainer.getNode()).fadeIn(100);

        var title = new game.fdom.TitleElement(rootContainer, '30%','20%','35%','5%', 'Settings', 'Title SettingsScreen');
        me.game.world.addChild(title);

        var settingsParchment = new game.fdom.ImageElement(rootContainer, '96.1%', '89.7%', '0%', '10.3%', 'Image SettingsScreen Parchment', 'assets/data/img/gui/settings_screen_transparent.png');
        settingsParchment.hide();
        $(settingsParchment.getNode()).fadeIn(100);
        me.game.world.addChild(settingsParchment);

        var backButton = new game.fdom.ButtonElement(rootContainer, '18%','20%','75%','0%', '', 'Button SettingsScreen Back', false, function() {
            $(rootContainer.getNode()).fadeOut(100);
            var settingsData = { music: settingsMusic.isChecked(), sound: settingsSound.isChecked()};
            game.data.user.settings.music = settingsMusic.isChecked();
            game.data.user.settings.sound = settingsSound.isChecked();
            ajaxUpdateUser(game.data.user.username, JSON.stringify({settings: settingsData}), function(xmlHttpRequest) {
                var user = JSON.parse(xmlHttpRequest.responseText);

                game.data.user = user;
            });
            setTimeout(function() {
                me.state.change(me.state.MENU);
            }, 50);
        });
        me.game.world.addChild(backButton);

        /* ------------------------------------------------------------------ */
        var settingsContainerElement = new game.fdom.ContainerElement(rootContainer, '24%','21%','10%','20%', 'Container SettingsScreen Settings');
        me.game.world.addChild(settingsContainerElement);

        var settingsTextSound = new game.fdom.TitleElement(settingsContainerElement, '45%','50%','15%','11%', 'sound: ', 'Text SettingsScreen Sound ');
        me.game.world.addChild(settingsTextSound);

        var settingsTextMusic = new game.fdom.TitleElement(settingsContainerElement, '45%','50%','15%','61%', 'music: ', 'Text SettingsScreen Music');
        me.game.world.addChild(settingsTextMusic);

        var settingsSound = new game.fdom.CheckBoxElement(settingsContainerElement, '20%','30%','65%','10%', '', 'CheckBox SettingsScreen Sound');
        me.game.world.addChild(settingsSound);

        var settingsMusic = new game.fdom.CheckBoxElement(settingsContainerElement, '20%','30%','65%','60%', '', 'CheckBox SettingsScreen Music');
        me.game.world.addChild(settingsMusic);

        $(settingsMusic.getNode()).on('click', function() {
            if(settingsMusic.isChecked()) {
                game.data.music = true;
                game.data.musicAlreadyPlaying = true;
                me.audio.playTrack("Menu",game.data.musicVolume);
            } else {
                game.data.music = false;
                game.data.musicAlreadyPlaying = false;
                me.audio.stopTrack();
            }
        });

        $(settingsSound.getNode()).on('click', function() {
            if(settingsSound.isChecked()) {
                game.data.sound = true;
                me.audio.play("cling", false, null, game.data.soundVolume);
            } else {
                game.data.sound = false;
            }
        });

        if(game.data.user.settings.sound) {
            $(settingsSound.getNode()).addClass('Checked');
        }
        if(game.data.user.settings.music) {
            $(settingsMusic.getNode()).addClass('Checked');
        }

        /* ------------------------------------------------------------------ */
        var menuContainer = new game.fdom.ContainerElement(rootContainer, '24%','46%','10%','41%', 'Container SettingsScreen Menu');
        me.game.world.addChild(menuContainer);

        var changePasswordButton = new game.fdom.ButtonElement(menuContainer, '100%','25%','0%','6%', 'change password', 'Button SettingsScreen Menu changePassword', false, function() {
            changeView(changePasswordContainer);
        });
        me.game.world.addChild(changePasswordButton);

        var resetStoryModeButton = new game.fdom.ButtonElement(menuContainer, '100%','25%','0%','39%', 'reset story mode', 'Button SettingsScreen Menu resetStory', false, function() {
            changeView(resetStoryModeContainer);
        });
        me.game.world.addChild(resetStoryModeButton);

        var deleteUserButton = new game.fdom.ButtonElement(menuContainer, '100%','25%','0%','73%', 'delete your account', 'Button SettingsScreen Menu deleteUser', false, function() {
            changeView(deleteUserContainer);
        });
        me.game.world.addChild(deleteUserButton);


        /* ------------------------------------------------------------------ */
        var defaultContainer = new game.fdom.ContainerElement(rootContainer, '55%','67%','34%','20%', 'Container SettingsScreen View');
        me.game.world.addChild(defaultContainer);

        var defaultContainerViewTitle = new game.fdom.TitleElement(defaultContainer, '86%','15%','7%','2.5%', 'ingame key bindings:', 'Text SettingsScreen View DefaultView Title');
        me.game.world.addChild(defaultContainerViewTitle);

        var keyBindingsTable = new game.fdom.ContainerElement(defaultContainer, "70%", "45%", "15%", "15%", "Table SettingsScreen");
        me.game.world.addChild(keyBindingsTable);

        // Create Table;
        keyBindingsTableRaw = [
            ["music toggle",    "[m]"],
            ["sound toggle",    "[n]"],
            ["leave dungeon",   "[esc]"],
            ["jump",            "[space]"],
            ["use potion",      "num[1-7]"]

        ];

        var tableElement = document.createElement("table");
        keyBindingsTable.getNode().appendChild(tableElement);

        for (var i = 0; i < keyBindingsTableRaw.length; i++) {
            var rowElement = document.createElement("tr");
            tableElement.appendChild(rowElement);
            for (var j = 0; j < keyBindingsTableRaw[i].length; j++) {
                var colElement = document.createElement("td");
                rowElement.appendChild(colElement);
                $(colElement).text(keyBindingsTableRaw[i][j]);
            }
        }

        var yIDInputField = new game.fdom.InputFieldElement(defaultContainer, '50%','10%','25%','68%', "GITZ Y-ID", "InputField");
        me.game.world.addChild(yIDInputField);

        var verifyButton = new game.fdom.ButtonElement(defaultContainer, '50%','10%','27%','88%', 'Verify as Student', 'Button SettingsScreen View DefaultView VerifyButton', false, function() {
            if(!game.data.user.student) {
                ajaxVerifyStudent(game.data.user.username, JSON.stringify({yID: yIDInputField.getNode().value }), function(xmlHttpRequest) {
                    if(xmlHttpRequest.status == 200) {
                        console.log("Verify as Student");
                        $(verifyButton.getNode()).addClass("Verified");
                        $(verifyButton.getNode()).text("already verified");
                    }
                });
            } else {
                console.log("alredy Verified");
            }
        });
        me.game.world.addChild(verifyButton);

        if(game.data.user.student) {
            $(verifyButton.getNode()).addClass("Verified");
            $(verifyButton.getNode()).text("already verified");
        }

        /* ------------------------------------------------------------------ */
        var changePasswordContainer = new game.fdom.ContainerElement(rootContainer, '55%','67%','34%','20%', 'Container SettingsScreen View');
        me.game.world.addChild(changePasswordContainer);
        changePasswordContainer.hide();

        var changePasswordForm = new game.fdom.FormElement(changePasswordContainer, '100%','100%','100%','100%', 'Form SettingsScreen ChangePasswordView',  function() {
            $(formPasswordOldInputField.getNode()).removeClass("invalid");
            $(formPasswordInputField.getNode()).removeClass("invalid");
            $(formPasswordRepeatInputField.getNode()).removeClass("invalid");

            var passwordOld = formPasswordOldInputField.getNode().value;
            var password    = formPasswordInputField.getNode().value;
            var passwordC   = formPasswordRepeatInputField.getNode().value;
            if (password != passwordC) {
                var notificationElement = new game.fdom.NotificationElement(rootContainer, "Sorry!", "Entered passwords do not match!");
                me.game.world.addChild(notificationElement);
                $(formPasswordInputField.getNode()).addClass("invalid");
                $(formPasswordRepeatInputField.getNode()).addClass("invalid");
                return;
            }
            var changePasswordData = {oldPassword: passwordOld, newPassword: password};

            ajaxUpdatePassword(game.data.user.username, JSON.stringify(changePasswordData), function(xmlHttpRequest) {
                if (xmlHttpRequest.status == 400) {
                    var errorMessage = JSON.parse(xmlHttpRequest.responseText);
                    if(typeof errorMessage.oldPassword !== 'undefined') {
                        $(formPasswordOldInputField.getNode()).addClass("invalid");
                    }
                    if(typeof errorMessage.newPassword !== 'undefined') {
                        $(formPasswordInputField.getNode()).addClass("invalid");
                        $(formPasswordRepeatInputField.getNode()).addClass("invalid");
                    }
                } else if(xmlHttpRequest.status == 200) {
                    var notificationElement = new game.fdom.NotificationElement(rootContainer, "Yo!", "Password changed successfully!");
                    me.game.world.addChild(notificationElement);
                }

            });

        });
        me.game.world.addChild(changePasswordForm);

        var changePasswordViewTitle = new game.fdom.TitleElement(changePasswordForm, '60%','16%','21.5%','10%', 'change password:', 'Text SettingsScreen View ChangePasswordView');
        me.game.world.addChild(changePasswordViewTitle);

        var formPasswordOldInputField = new game.fdom.InputPasswordFieldElement(changePasswordForm, '60%','10%','20%','30%', 'old password', 'InputPasswordField SettingsScreen Password');
        me.game.world.addChild(formPasswordOldInputField);

        var formPasswordInputField = new game.fdom.InputPasswordFieldElement(changePasswordForm, '60%','10%','20%','47%', 'password', 'InputPasswordField SettingsScreen Password');
        me.game.world.addChild(formPasswordInputField);

        var formPasswordRepeatInputField = new game.fdom.InputPasswordFieldElement(changePasswordForm, '60%','10%','20%','63%', 'repeat password', 'InputPasswordField SettingsScreen Password');
        me.game.world.addChild(formPasswordRepeatInputField);

        var changePasswordSubmit = new game.fdom.ButtonElement(changePasswordForm, '60%','10%','21.5%','85%', 'submit', 'Button SettingsScreen View ChangePasswordView', true);
        me.game.world.addChild(changePasswordSubmit);

        /* ------------------------------------------------------------------ */
        var resetStoryModeContainer = new game.fdom.ContainerElement(rootContainer, '55%','67%','34%','20%', 'Container SettingsScreen View');
        me.game.world.addChild(resetStoryModeContainer);
        resetStoryModeContainer.hide();

        var resetStoryModeContainerText1 = new game.fdom.TitleElement(resetStoryModeContainer, '65%','16%','20%','27%', 'Are you sure you want do reset the story?', 'Text SettingsScreen View ResetStoryModeView');
        me.game.world.addChild(resetStoryModeContainerText1);

        var resetStoryModeContainerText2 = new game.fdom.TitleElement(resetStoryModeContainer, '65%','16%','20%','43%', 'Your whole progress will be deleted!', 'Text SettingsScreen View ResetStoryModeView');
        me.game.world.addChild(resetStoryModeContainerText2);

        var resetStoryModePushButton = new game.fdom.ButtonElement(resetStoryModeContainer, '65%','10%','21.5%','75%', "Yes I'm sure", 'Button SettingsScreen View ResetStoryModeView ResetStoryMode', false, function() {
            ajaxSendChallengeResetRequest(function(xmlHttpRequest) {
                var notificationElement;
                if(xmlHttpRequest.status == 400) {
                    notificationElement = new game.fdom.NotificationElement(rootContainer, "Sorry!", "Could not reset your progress!");
                    me.game.world.addChild(notificationElement);
                    return;
                } else if (xmlHttpRequest.status == 200) {
                    notificationElement = new game.fdom.NotificationElement(rootContainer, "Yo!", "please reload the page");
                    me.game.world.addChild(notificationElement);
                    changeView(resetStoryModeContainer);
                }
            });
        });
        me.game.world.addChild(resetStoryModePushButton);

        /* ------------------------------------------------------------------ */
        var deleteUserContainer = new game.fdom.ContainerElement(rootContainer, '55%','67%','34%','20%', 'Container SettingsScreen View');
        me.game.world.addChild(deleteUserContainer);
        deleteUserContainer.hide();

        var deleteUserContainerText1 = new game.fdom.TitleElement(deleteUserContainer, '65%','16%','20%','27%', 'Are you sure about deleting your account?', 'Text SettingsScreen View ResetStoryModeView');
        me.game.world.addChild(deleteUserContainerText1);

        var deleteUserContainerText2 = new game.fdom.TitleElement(deleteUserContainer, '65%','16%','20%','43%', 'All your data will be deleted!', 'Text SettingsScreen View ResetStoryModeView');
        me.game.world.addChild(deleteUserContainerText2);

        var deleteUserPushButton = new game.fdom.ButtonElement(deleteUserContainer, '65%','10%','21.5%','75%', "Yes I'm sure", 'Button SettingsScreen View ResetStoryModeView ResetStoryMode', false, function() {
            ajaxDeleteUser(game.data.user.username, function(xmlHttpRequest) {
                if(xmlHttpRequest.status == 400) {
                    var notificationElement = new game.fdom.NotificationElement(rootContainer, "Sorry!", "Could not delete your account! Try again, later!");
                    me.game.world.addChild(notificationElement);
                    return;
                }
                game.data.session = JSON.parse(xmlHttpRequest.responseText);
                game.data.user = {};
                $(rootContainer.getNode()).fadeOut(100);
                setTimeout( function() {
                    me.state.change(STATE_SIGNUP);
                }, 50);
            });
        });
        me.game.world.addChild(deleteUserPushButton);


        var currentView = defaultContainer;
        function changeView(newView) {
            $(currentView.getNode()).fadeOut(100);
            if(newView == currentView) {
                setTimeout( function () {
                    currentView = defaultContainer;
                    $(currentView.getNode()).fadeIn(100);
                }, 50);
                return;
            }
            currentView = newView;
            $(currentView.getNode()).fadeIn(100);
        }
    }
});
