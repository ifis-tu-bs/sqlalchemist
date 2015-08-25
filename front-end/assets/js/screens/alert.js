game.AlertScreen = me.ScreenObject.extend({
    init: function() {
        this.font = null;
        this.staticfont = null;
    },

    //test

    onResetEvent: function() {
        //We have no alert!
        if (!game.alert.isAlert || game.alert.alertText == null) {
            if (game.alert.toState != null) {
                //Go to given State
                me.state.change(game.alert.toState);
            }
            //Something is doing wrong in this alert call! Switch to start!
            me.state.change(STATE_START);
        }



        me.game.world.addChild(
            new me.Sprite(
                0, 0,
                me.loader.getImage('game_over_screen')
            ),
            1
        );

        me.game.world.addChild(new game.HUD.OverlayAlert(300, 150, game.alert.alertText));

        function onAlertOkClick() {
            me.state.change(game.alert.toState);
        }

        var alertOk = new game.ClickableElement("alertOk", "Ok", onAlertOkClick, 40, 12, 33, 25, 1);

        me.game.world.addChild(alertOk);

    }
});

function setUpAlert(state, text) {

        game.alert.isAlert = true;
        game.alert.toState = STATE_SIGNUP;
        game.alert.alertText = text;

}
