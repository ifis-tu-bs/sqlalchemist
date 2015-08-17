game.TriviaScreen = me.ScreenObject.extend({
    /**
     *  action to perform on state change
     */
    onResetEvent: function() {
        me.game.world.addChild(
            new me.Sprite (
                0,0,
                me.loader.getImage('difficulty_screen')
            ),
            1
        );
        //me.game.world.addChild(new backToMenu(1070,-50));


        // the 5 task buttons with the according difficulties as third parameter
        me.game.world.addChild(new task(400,200,1));
        me.game.world.addChild(new task(720,200,2));
        me.game.world.addChild(new task(244,400,3));
        me.game.world.addChild(new task(555,400,4));
        me.game.world.addChild(new task(876,400,5));

        triviaBackClick = function(){
            me.state.change(me.state.MENU);
        };

        var backFromTrivia = new game.ClickableElement('backFromTrivia', 'Back', triviaBackClick, 15, 6.5, 39, 77, 1);
        me.game.world.addChild(backFromTrivia);

    }
});
