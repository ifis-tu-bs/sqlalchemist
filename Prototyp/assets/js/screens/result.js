game.ResultScreen = me.ScreenObject.extend({

    /**
     *  action to perform on state change
     */
    onResetEvent : function() {

        // lab_screen
        me.game.world.addChild(
            new me.Sprite (
                0,0,
                me.loader.getImage('result_screen')
            ),
            1
        );

        game.data.cheat = false;
        // Creates a Button that brings you back to the last Screen
        me.game.world.addChild(new NEXT(580,590),2);
        // The result text
        me.game.world.addChild(new game.HUD.Result(450, 300),3);
    }
});