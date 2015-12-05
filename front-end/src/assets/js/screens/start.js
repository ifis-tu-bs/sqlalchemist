/**
 * PreGame Screen
 */
game.StartScreen = me.ScreenObject.extend({
    onResetEvent: function() {
        //var rootContainer   = new game.fdom.RootContainer('/assets/data/img/gui/title_screen.png');
        var rootContainer   = new game.fdom.RootContainer('/assets/data/img/gui/preGameBackground.png');
        me.game.world.addChild(rootContainer);

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
                        me.state.change(STATE_LOGIN);
                    }
                }, 100);
            });
            me.game.world.addChild(startButton);
        }
        displayStartScreen();
    },
});
