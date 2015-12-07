game.LoginScreen = me.ScreenObject.extend({

    onResetEvent: function() {
        var rootContainer   = new game.fdom.RootContainer('/assets/data/img/gui/preGameBackground.png');
        me.game.world.addChild(rootContainer);

        var parchment     = new game.fdom.ContainerElement(rootContainer, '84%','83%','8%','0%', 'StartScreen Parchment');
        parchment.setBackgroundImage('/assets/data/img/gui/parchment.png');
        me.game.world.addChild(parchment);
        parchment.hide();

        var title = new game.fdom.TitleElement(parchment, '20%','10%','40%','10%', 'Login', 'Title LoginScreen');
        me.game.world.addChild(title);

        var loginForm = new game.fdom.FormElement(parchment, '100%','100%','100%','100%', 'Form LoginScreen', function() {
            var loginFormData = JSON.stringify({email: formEmailInputField.getNode().value, password: formPasswordInputField.getNode().value});
            console.log(loginFormData);
            ajaxSendLoginRequest(loginFormData, function(xmlHttpRequest) {
                if(xmlHttpRequest.status == 200) {
                    console.log("Login Successfull");
                    /*ajaxSendUserSettingsRequest(function(xmlHttpRequest) {
                        if( xmlHttpRequest.status == 200 ) {
                            var settings = JSON.parse(xmlHttpRequest.responseText);

                            game.data.music = profile.settings.music;
                            game.data.sound = profile.settings.sound;

                            if(game.data.music ){
                                me.audio.playTrack("menu",game.data.musicVolume);
                            }
                            if(game.data.sound) {
                                me.audio.play("switch", false, null, game.data.soundVolume);
                            }*/
                            $(rootContainer.getNode()).fadeOut(100);
                            setTimeout(function() {
                                me.state.change(me.state.MENU);
                            }, 50);

//                        }
//                     });
                } else if(xmlHttpRequest.status == 401) {
                    console.log("Wrong EMAIL or Password");
                } else if(xmlHttpRequest.staus == 400) {
                    console.log("Bad Request");
                }
            });
        });
        me.game.world.addChild(loginForm);

        var formEmailInputField = new game.fdom.InputFieldElement(loginForm, '64%','10%','17%','31%', 'Email Address', 'InputField LoginScreen');
        me.game.world.addChild(formEmailInputField);

        var formPasswordInputField = new game.fdom.InputPasswordFieldElement(loginForm, '64%','10%','17%','47%', 'Password', 'InputPasswordField LoginScreen');
        me.game.world.addChild(formPasswordInputField);

        // Login Button
        var loginButton = new game.fdom.ButtonElement(loginForm, '28%','17%','35%','74%', 'Enter', 'Button LoginScreen Enter', true);
        me.game.world.addChild(loginButton);

        // Password Reset Button
        var passwordReset = new game.fdom.ButtonElement(loginForm, '27%','5%','54%','65%', 'Forgot Password? Click Here!', 'Button LoginScreen PasswordReset', false, function() {
            $(parchment.getNode()).fadeOut(100);
            setTimeout(function() {
                me.state.change(STATE_FORGOTPASSWORD);
            }, 50);
        });
        me.game.world.addChild(passwordReset);
        // SignUp Button
        var signUpButton = new game.fdom.ButtonElement(loginForm, '31%','5%','17%','65%', 'No Login Yet? You Can Sign Up Here!', 'Button LoginScreen SignUp', false, function() {
            $(parchment.getNode()).fadeOut(100);
            setTimeout(function() {
                me.state.change(STATE_SIGNUP);
            }, 50);
        });
        me.game.world.addChild(signUpButton);

        $(parchment.getNode()).fadeIn(100);
    }
});
