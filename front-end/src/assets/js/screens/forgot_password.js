game.ForgotPasswordScreen = me.ScreenObject.extend({

    onResetEvent: function() {
        var rootContainer   = new game.fdom.RootContainer('/assets/data/img/gui/preGameBackground.png');
        me.game.world.addChild(rootContainer);

        var parchment     = new game.fdom.ContainerElement(rootContainer, '84%','83%','8%','0%', 'StartScreen Parchment');
        me.game.world.addChild(parchment);
        parchment.hide();

        var title = new game.fdom.TitleElement(parchment, '55%','20%','23%','27%', 'Reset Password', 'Title PasswordReset');
        me.game.world.addChild(title);

        var loginForm = new game.fdom.FormElement(parchment, '100%','100%','100%','100%', 'Form PasswordResetScreen', function() {
            $(formEmailInputField.getNode()).removeClass("invalid");
            var resetPasswordData = JSON.stringify({ email: formEmailInputField.getNode().value });
            ajaxCreatePasswordResetRequest(resetPasswordData, function(xmlHttpRequest) {
                if(xmlHttpRequest.status == 200) {
                    $(parchment.getNode()).fadeOut(100);
                    setTimeout(function() {
                        me.state.change(STATE_LOGIN);
                    }, 50);
                } else if( xmlHttpRequest.status == 400 ) {
                    var errorMessage = JSON.parse(xmlHttpRequest.responseText);
                    if(typeof errorMessage.email !== 'undefined') {
                        $(formEmailInputField.getNode()).addClass("invalid");
                        var notificationElement = new game.fdom.NotificationElement(rootContainer, "Sorry!", "entered email does not exist!");
                        me.game.world.addChild(notificationElement);
                    }
                    console.log(errorMessage);
                }
            });
        });
        me.game.world.addChild(loginForm);

        var formEmailInputField = new game.fdom.InputFieldElement(loginForm, '60%','10%','20%','49%', 'email', 'InputField PasswordResetScreen');
        me.game.world.addChild(formEmailInputField);

        // Password Reset Button
        var passwordReset = new game.fdom.ButtonElement(loginForm, '17%','15%','59%','68%', 'Reset', 'Button PasswordResetScreen Submit', true);
        me.game.world.addChild(passwordReset);
        // Back Button
        var backButton = new game.fdom.ButtonElement(loginForm, '14%','15%','23%','68%', 'Back', 'Button PasswordResetScreen Back', false, function() {
            $(parchment.getNode()).fadeOut(100);
            setTimeout(function() {
                me.state.change(STATE_LOGIN);
            }, 50);
        });
        me.game.world.addChild(backButton);

        $(parchment.getNode()).fadeIn(100);
    }
});
