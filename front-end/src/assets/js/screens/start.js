/**
 * PreGame Screen
 */
game.StartScreen = me.ScreenObject.extend({
    onResetEvent: function() {
        //var rootContainer   = new game.fdom.RootContainer('/assets/data/img/gui/title_screen.png');
        var rootContainer   = new game.fdom.RootContainer('/assets/data/img/gui/login_screen.png');
        me.game.world.addChild(rootContainer);

        var parchment     = new game.fdom.ContainerElement(rootContainer, '84%','82%','8%','1%', 'StartScreen Parchment');
        me.game.world.addChild(parchment);
        parchment.hide();

        function displayStartScreen() {
            var isSessionValid = false;

            function validateCookie(xmlHttpRequest) {
                console.log(xmlHttpRequest);
                var response = JSON.parse(xmlHttpRequest.responseText);
                console.log(response);
                if(response.status === true) {
                    isSessionValid = true;
                }
            }

            if(typeof ($.cookie('SQL-Alchemist-Session')) !== 'undefined') {
                ajaxValidateCookie($.cookie('SQL-Alchemist-Session'), validateCookie);
            }

            var startButton     = new game.fdom.ButtonElement(rootContainer, '31%','23%','35%','46%', 'Start', 'Button Start', function() {
                $(startButton.getNode()).fadeOut(50);
                setTimeout(function() {
                    if(isSessionValid) {
                        console.log("Switch to Main Menu");
                        me.state.change(me.state.MENU);
                    } else {
                        me.game.world.removeChild(startButton);
                        displayLoginScreen();
                    }
                }, 100);
            });
            me.game.world.addChild(startButton);
        }

        function displayLoginScreen() {
            var title = new game.fdom.TitleElement(parchment, '20%','10%','40%','15%', 'Login', 'Title Login');
            me.game.world.addChild(title);

            var login = new game.fdom.ButtonElement(parchment, '28%','17%','35%','75%', 'Login', 'Button Login', function() {
                alert("login");
            });
            me.game.world.addChild(login);

            $(parchment.getNode()).fadeIn(100);
        }

        displayStartScreen();
        //displayLoginScreen();
    },
});
