/**
 * PreGame Screen
 */
game.StartScreen = me.ScreenObject.extend({
    onResetEvent: function() {
        var rootContainer   = new game.fdom.RootContainer('/assets/data/img/gui/preGameBackground.png');
        me.game.world.addChild(rootContainer);
        var isSignedIn = false;

        ajaxGetSession(function(xmlHttpRequest) {
            if(xmlHttpRequest.status != 200) {
                console.log("Error");
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
                    }
                });
            }
        });


        var titleBanner     = new game.fdom.ImageElement(rootContainer, '46%', '25%', '27%', '15%', 'Image StartScreen TitleBanner', '/assets/data/img/buttons/StartScreenBanner.png');
        titleBanner.hide();
        var startButton     = new game.fdom.ButtonElement(rootContainer, '31%','23%','35%','46%', 'START', 'Button StartScreen Start', false, function() {
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
        });
        startButton.hide();

        $(startButton.getNode()).fadeIn(50);
        $(titleBanner.getNode()).fadeIn(50);
        me.game.world.addChild(startButton);
    },
});
