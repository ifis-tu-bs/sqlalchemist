game.LoginScreen = me.ScreenObject.extend({

    onResetEvent: function() {
        var rootContainer   = new game.fdom.RootContainer('/assets/data/img/gui/preGameBackground.png');
        me.game.world.addChild(rootContainer);

        var parchment     = new game.fdom.ContainerElement(rootContainer, '84%','83%','8%','0%', 'StartScreen Parchment');
        parchment.setBackgroundImage('/assets/data/img/gui/parchment.png');
        me.game.world.addChild(parchment);
        parchment.hide();

        var title = new game.fdom.TitleElement(parchment, '20%','20%','40%','15%', 'Login', 'Title LoginScreen');
        me.game.world.addChild(title);

        var loginForm = new game.fdom.FormElement(parchment, '100%','100%','100%','100%', 'Form LoginScreen', function() {
            $(formPasswordInputField.getNode()).removeClass("invalid");
            $(formEmailInputField.getNode()).removeClass("invalid");
            var loginFormData = JSON.stringify({email: formEmailInputField.getNode().value, password: formPasswordInputField.getNode().value});
            ajaxSendLoginRequest(loginFormData, function(xmlHttpRequest) {
                if(xmlHttpRequest.status == 200) {
                    var user = JSON.parse(xmlHttpRequest.responseText);
                    game.data.user = user;
                    game.data.session.owner = user.username;
                    $(rootContainer.getNode()).fadeOut(100);
                    setTimeout(function() {
                        me.state.change(me.state.MENU);
                    }, 50);
                } else if(xmlHttpRequest.status == 401) {
                    var notificationElement = new game.fdom.NotificationElement(rootContainer, "log in failed", xmlHttpRequest.responseText);
                    me.game.world.addChild(notificationElement);
                } else if(xmlHttpRequest.status == 400) {
                    var errorMessage = JSON.parse(xmlHttpRequest.responseText);
                    if(typeof errorMessage.email !== 'undefined') {
                        $(formEmailInputField.getNode()).addClass("invalid");
                    }
                    if(typeof errorMessage.password !== 'undefined') {
                        $(formPasswordInputField.getNode()).addClass("invalid");
                    }
                }
            });
        });
        me.game.world.addChild(loginForm);

        var formEmailInputField = new game.fdom.InputFieldElement(loginForm, '64%','10%','17%','31%', 'email', 'InputField LoginScreen');
        me.game.world.addChild(formEmailInputField);

        var formPasswordInputField = new game.fdom.InputPasswordFieldElement(loginForm, '64%','10%','17%','47%', 'password', 'InputPasswordField LoginScreen');
        me.game.world.addChild(formPasswordInputField);

        // Login Button
        var loginButton = new game.fdom.ButtonElement(loginForm, '28%','17%','35%','71%', 'Enter', 'Button LoginScreen Enter', true);
        me.game.world.addChild(loginButton);

        // Password Reset Button
        var passwordReset = new game.fdom.ButtonElement(loginForm, '30%','6%','54%','65%', 'Forgot Password? Click Here!', 'Button LoginScreen PasswordReset', false, function() {
            $(parchment.getNode()).fadeOut(100);
            setTimeout(function() {
                me.state.change(STATE_FORGOTPASSWORD);
            }, 50);
        });
        me.game.world.addChild(passwordReset);
        // SignUp Button
        var signUpButton = new game.fdom.ButtonElement(loginForm, '36%','6%','14%','65%', 'No Login Yet? You Can Sign Up Here!', 'Button LoginScreen SignUp', false, function() {
            $(parchment.getNode()).fadeOut(100);
            setTimeout(function() {
                me.state.change(STATE_SIGNUP);
            }, 50);
        });
        me.game.world.addChild(signUpButton);

        $(parchment.getNode()).fadeIn(100);
    }
});
