game.HomeworkScreen = me.ScreenObject.extend({
    /**
     *  action to perform on state change
     */
    onResetEvent: function () {

        // workaround f�r android bug CB-4404
        if (me.device.android || me.device.android2) {
            if (me.device.isFullscreen) {
                me.device.exitFullscreen();
                document.body.style.minHeight = document.body.clientHeight + 'px';
            }
        }

        me.game.world.addChild(
            new me.Sprite(
                0, 0,
                me.loader.getImage('homework_screen')
            ),
            1
        );

        backToLab = function(){
            me.state.change(me.state.MENU);
            if(game.data.sound){
                me.audio.play("switch", false, null, game.data.soundVolume);
            }
        };
        var returnButton = new game.ClickableElement('returnButton', 'Back', backToLab, 15, 6, 23, 74, 1);
        me.game.world.addChild(returnButton);


        toTask = function(){
            game.task.kind = 3;
            me.state.change(STATE_TASK);
        };
        var startHomework = new game.ClickableElement('startHomework', 'Start', toTask, 15, 6, 62, 74, 1);
        me.game.world.addChild(startHomework);

        homeworkReply = function(xmlHttpRequest){
            var homework_object = JSON.parse(xmlHttpRequest.responseText);

            var title = new game.TextOutputElement('title', 40, 45, 18, 25, 10);
            me.game.world.addChild(title);
            var expireDates = new game.TextOutputElement('expireDates', 15, 45, 60, 25, 10);
            me.game.world.addChild(expireDates);

            for (var i = 0; i < homework_object.length; i++) {
                var name = homework_object[i].name;
                var due_to = homework_object[i].expires_at;
                var submitted = homework_object[i].submitted;
                title.writeHTML(name + "<br>");
                expireDates.writeHTML(due_to + "<br>");

                me.game.world.addChild(
                    new me.Sprite(
                        1000, 192 + 35 * i,
                        me.loader.getImage('checkbox')
                    ),
                    2
                );
                if (submitted != true) {
                    me.game.world.addChild(new me.Sprite(995, 175 + 35 * i, me.loader.getImage('check_symbol')), 3);
                }
            }

        };

        ajaxSendProfileHomeworkRequest(homeworkReply);

    }
});