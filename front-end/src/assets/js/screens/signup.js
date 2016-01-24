game.SignUpScreen = me.ScreenObject.extend({

    /**
     *  Action to perform on state change: STATE_SIGNUP
     */
    onResetEvent: function() {

        var rootContainer   = new game.fdom.RootContainer('/assets/data/img/gui/preGameBackground.png');
        me.game.world.addChild(rootContainer);

        var parchment     = new game.fdom.ContainerElement(rootContainer, '84%','83%','8%','0%', 'StartScreen Parchment');
        parchment.setBackgroundImage('/assets/data/img/gui/parchment.png');
        me.game.world.addChild(parchment);
        parchment.hide();

        var title = new game.fdom.TitleElement(parchment, '26%','20%','38%','10%', 'Sign Up', 'Title SignUpScreen resetPasword');
        me.game.world.addChild(title);

        var signUpForm = new game.fdom.FormElement(parchment, '100%','100%','100%','100%', 'Form SignUpScreen',  function() {
            $(formEmailInputField.getNode()).removeClass("invalid");
            $(formUsernameInputField.getNode()).removeClass("invalid");
            $(formPasswordInputField.getNode()).removeClass("invalid");
            $(formPasswordRepeatInputField.getNode()).removeClass("invalid");

            var email    = formEmailInputField.getNode().value;
            var username = formUsernameInputField.getNode().value;
            var password = formPasswordInputField.getNode().value;
            var passwordC= formPasswordRepeatInputField.getNode().value;

            if(password !== passwordC) {
                var notificationElement = new game.fdom.NotificationElement(rootContainer, "SignUp failed", "entered passwords do not match");
                me.game.world.addChild(notificationElement);
                $(formPasswordInputField.getNode()).addClass("invalid");
                $(formPasswordRepeatInputField.getNode()).addClass("invalid");
                return;
            }

            var signUpData = JSON.stringify({
                email: email,
                username: username,
                password: password
            });
            ajaxCreateUser(signUpData, function(xmlHttpRequest) {
                if(xmlHttpRequest.status == 400) {
                    var errorMessage = JSON.parse(xmlHttpRequest.responseText);
                    if(typeof errorMessage.email !== 'undefined') {
                        $(formEmailInputField.getNode()).addClass("invalid");
                    }
                    if(typeof errorMessage.username !== 'undefined') {
                        $(formUsernameInputField.getNode()).addClass("invalid");
                    }
                    if(typeof errorMessage.password !== 'undefined') {
                        $(formPasswordInputField.getNode()).addClass("invalid");
                        $(formPasswordRepeatInputField.getNode()).addClass("invalid");
                    }
                } else if(xmlHttpRequest.status == 200) {
                    var user = JSON.parse(xmlHttpRequest.responseText);
                    game.data.user = user;
                    game.data.session.owner = user.username;
                    $(rootContainer.getNode()).fadeOut(100);
                    setTimeout(function() {
                        me.state.change(me.state.MENU);
                    }, 50);
                }
            });
        });
        me.game.world.addChild(signUpForm);

        var formEmailInputField = new game.fdom.InputFieldElement(signUpForm, '60%','10%','20%','24%', 'email', 'InputField SignUpScreen Email');
        me.game.world.addChild(formEmailInputField);

        var formUsernameInputField = new game.fdom.InputFieldElement(signUpForm, '60%','10%','20%','38%', 'username', 'InputField SignUpScreen Username');
        me.game.world.addChild(formUsernameInputField);

        var formPasswordInputField = new game.fdom.InputPasswordFieldElement(signUpForm, '60%','10%','20%','51%', 'password', 'InputPasswordField SignUpScreen Password');
        me.game.world.addChild(formPasswordInputField);

        var formPasswordRepeatInputField = new game.fdom.InputPasswordFieldElement(signUpForm, '60%','10%','20%','64%', 'password confirmation', 'InputPasswordField SignUpScreen Password');
        me.game.world.addChild(formPasswordRepeatInputField);

        // Password Reset Button
        var SignUp = new game.fdom.ButtonElement(signUpForm, '17%','15%','59%','77%', 'Enter', 'Button SignUpScreen Submit', true);
        me.game.world.addChild(SignUp);
        // Back Button
        var backButton = new game.fdom.ButtonElement(signUpForm, '14%','15%','23%','77%', 'Back', 'Button SignUpScreen Back', false, function() {
            $(parchment.getNode()).fadeOut(100);
            setTimeout(function() {
                me.state.change(STATE_LOGIN);
            }, 50);
        });
        me.game.world.addChild(backButton);

        $(parchment.getNode()).fadeIn(100);

    }

});
