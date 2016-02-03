/**
 * PreGame Screen
 */
game.StartScreen = me.ScreenObject.extend({
    onResetEvent: function() {
        var rootContainer   = new game.fdom.RootContainer('/assets/data/img/gui/preGameBackground.png');
        me.game.world.addChild(rootContainer);
        var isSignedIn = false;
        var gotSession = false;

        ajaxGetSession(function(xmlHttpRequest) {
            if(xmlHttpRequest.status != 200) {
                var notificationElement = new game.fdom.NotificationElement(rootContainer, "Server Error", "Your request cannot proceed");
                me.game.world.addChild(notificationElement);
                return;
            }
            var session = JSON.parse(xmlHttpRequest.responseText);
            game.data.session = session;
            if(session.owner !== "") {
                isSignedIn = true;
                ajaxGetUser(game.data.session.owner, function(xmlHttpRequest) {
                    if(xmlHttpRequest.status == 200) {
                        var user = JSON.parse(xmlHttpRequest.responseText);
                        game.data.user = user;
                        gotSession = true;
                    }
                });
                return;
            } else {
                gotSession = true;
                return;
            }
        });



        var titleBanner     = new game.fdom.ImageElement(rootContainer, '46%', '25%', '27%', '15%', 'Image StartScreen TitleBanner', '/assets/data/img/buttons/StartScreenBanner.png');
        titleBanner.hide();
        var startButton     = new game.fdom.ButtonElement(rootContainer, '35%','19%','34%','47%', 'START', 'Button StartScreen Start', false, function() {
            if (gotSession) {
                $(startButton.getNode()).fadeOut(50);
                $(titleBanner.getNode()).fadeOut(50);
                setTimeout(function() {
                    if(isSignedIn) {
                        me.state.change(me.state.MENU);
                    } else {
                        me.game.world.removeChild(startButton);
                        me.state.change(STATE_LOGIN);
                    }
                }, 100);
            } else {
                var notificationElement = new game.fdom.NotificationElement(rootContainer, "Please wait", "try again in a few seconds");
                me.game.world.addChild(notificationElement);
            }
        });
        startButton.hide();

        $(startButton.getNode()).fadeIn(50);
        $(titleBanner.getNode()).fadeIn(50);
        me.game.world.addChild(startButton);
    },
});
