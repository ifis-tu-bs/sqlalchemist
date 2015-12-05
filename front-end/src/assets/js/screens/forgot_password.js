game.ForgotPasswordScreen = me.ScreenObject.extend({

    onResetEvent: function() {
        var rootContainer   = new game.fdom.RootContainer('/assets/data/img/gui/preGameBackground.png');
        me.game.world.addChild(rootContainer);

        var parchment     = new game.fdom.ContainerElement(rootContainer, '84%','82%','8%','1%', 'StartScreen Parchment');
        me.game.world.addChild(parchment);
        parchment.hide();

        var title = new game.fdom.TitleElement(parchment, '50%','10%','25%','10%', 'Reset Password', 'Title PasswordReset Login');
        me.game.world.addChild(title);

        var loginForm = new game.fdom.FormElement(parchment, '100%','100%','100%','100%', 'Form PasswordReset Login');
        me.game.world.addChild(loginForm);

        var formEmailInputField = new game.fdom.InputFieldElement(loginForm, '60%','10%','20%','49%', 'Email Address', 'InputField Login');
        me.game.world.addChild(formEmailInputField);

        // Password Reset Button
        var passwordReset = new game.fdom.ButtonElement(loginForm, '15%','9%','66%','76%', 'Reset', 'Button PasswordReset Reset', function() {
            console.log("passwordReset");
        });
        me.game.world.addChild(passwordReset);
        // Back Button
        var backButton = new game.fdom.ButtonElement(loginForm, '14%','9%','19%','76%', 'Back', 'Button PasswordReset Back', function() {
            $(parchment.getNode()).fadeOut(100);
            setTimeout(function() {
                me.state.change(STATE_LOGIN);
            }, 50);
        });
        me.game.world.addChild(backButton);

        $(parchment.getNode()).fadeIn(100);
    }
});
