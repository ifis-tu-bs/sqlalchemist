/**
 * PreGame Screen
 */
game.StartScreen = me.ScreenObject.extend({
    onResetEvent: function() {
        //var rootContainer   = new game.fdom.RootContainer('/assets/data/img/gui/title_screen.png');
        var rootContainer   = new game.fdom.RootContainer('/assets/data/img/gui/preGameBackground.png');
        me.game.world.addChild(rootContainer);
        var isSignedIn = false;

        ajaxGetSession(function(xmlHttpRequest) {
            if(xmlHttpRequest.status != 200) {
                console.log("Error");
                return;
            }
            var session = JSON.parse(xmlHttpRequest.responseText);
            if(session.owner !== "") {
                isSignedIn = true;
            }
            console.log(session);
        });

        var startButton     = new game.fdom.ButtonElement(rootContainer, '31%','23%','35%','46%', 'Start', 'Button Start', false, function() {
            $(startButton.getNode()).fadeOut(50);
            setTimeout(function() {
                if(isSignedIn) {
                    me.state.change(me.state.MENU);
                } else {
                    me.game.world.removeChild(startButton);
                    me.state.change(STATE_LOGIN);
                }
            }, 100);
        });
        me.game.world.addChild(startButton);
    },
});
