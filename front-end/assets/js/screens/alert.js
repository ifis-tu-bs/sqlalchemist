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

        var background = new game.BackgroundElement('background', 100, 100, 0, 0, 'none');
        background.setImage("assets/data/img/gui/game_over_screen.png", "back");
        me.game.world.addChild(background);

        $("#background").fadeIn(100);

        me.game.world.addChild(new game.HUD.OverlayAlert(300, 150, game.alert.alertText));

        this.onAlertOkClick = function() {
            $("#alertOk").fadeOut(100);
            $("#background").fadeOut(100);
            setTimeout( function() {
                me.state.change(game.alert.toState);
            }, 100);

        };

        var alertOk = new game.ClickableElement("alertOk", "Ok", this.onAlertOkClick, 40, 12, 33, 25, 1);
        me.game.world.addChild(alertOk);
        $("#alertOk").fadeIn(100);

    }
});

function setUpAlert(state, text) {

        game.alert.isAlert = true;
        game.alert.toState = state;
        game.alert.alertText = text;

}
