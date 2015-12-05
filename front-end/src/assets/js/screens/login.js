game.LoginScreen = me.ScreenObject.extend({

    onResetEvent: function() {
        var rootContainer   = new game.fdom.RootContainer('/assets/data/img/gui/preGameBackground.png');
        me.game.world.addChild(rootContainer);

        var parchment     = new game.fdom.ContainerElement(rootContainer, '84%','82%','8%','1%', 'StartScreen Parchment');
        me.game.world.addChild(parchment);
        parchment.hide();

        var title = new game.fdom.TitleElement(parchment, '20%','10%','40%','10%', 'Login', 'Title resetPasword');
        me.game.world.addChild(title);

        var loginForm = new game.fdom.FormElement(parchment, '100%','100%','100%','100%', 'Form Login');
        me.game.world.addChild(loginForm);

        var formEmailInputField = new game.fdom.InputFieldElement(loginForm, '60%','10%','20%','27%', 'Email Address', 'InputField Login');
        me.game.world.addChild(formEmailInputField);

        var formPasswordInputField = new game.fdom.InputPasswordFieldElement(loginForm, '60%','10%','20%','47%', 'Password', 'InputPasswordField Login');
        me.game.world.addChild(formPasswordInputField);

        // Login Button
        var loginButton = new game.fdom.ButtonElement(loginForm, '28%','17%','35%','75%', 'Login', 'Button Login', function() {
            var loginFormData = JSON.stringify({email: formEmailInputField.getNode().value, password: formPasswordInputField.getNode().value});
            console.log(loginFormData);
            ajaxSendLoginRequest(loginFormData, function(xmlHttpRequest) {
                console.log(xmlHttpRequest);
            });
        });
        me.game.world.addChild(loginButton);

        // Password Reset Button
        var passwordReset = new game.fdom.ButtonElement(loginForm, '25%','5%','54%','65%', 'Reset Password', 'Button passwordReset', function() {
            $(parchment.getNode()).fadeOut(100);
            setTimeout(function() {
                me.state.change(STATE_FORGOTPASSWORD);
            }, 50);
        });
        me.game.world.addChild(passwordReset);
        // SignUp Button
        var signUpButton = new game.fdom.ButtonElement(loginForm, '25%','5%','21%','65%', 'SignUp', 'Button signUp', function() {
            $(parchment.getNode()).fadeOut(100);
            setTimeout(function() {
                me.state.change(STATE_SIGNUP);
            }, 50);
        });
        me.game.world.addChild(signUpButton);

        $(parchment.getNode()).fadeIn(100);
    },

    /**
     * Gets the settings on Login and starts the menu-music.
     */
    onDestroyEvent: function() {

        //get the users settings
        function getSettings(xmlHttpRequest){

            var profile = JSON.parse(xmlHttpRequest.responseText);
            game.data.music = profile.settings.music;
            game.data.sound = profile.settings.sound;
            game.data.lofiCoins = profile.coins;

             //starts background music
            if(game.data.music ){
                me.audio.playTrack("menu",game.data.musicVolume);
            }
            if(game.data.sound) {
                me.audio.play("switch", false, null, game.data.soundVolume);
            }


        }
        ajaxSendProfileRequest(getSettings);
    }
});
