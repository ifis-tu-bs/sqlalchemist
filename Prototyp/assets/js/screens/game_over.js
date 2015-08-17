game.GameOverScreen = me.ScreenObject.extend({

    /**
     *  action to perform on state change
     */
    onResetEvent : function() {

        if(game.data.text === 7) {
            me.state.change(STATE_TEXT);
        }else{
            if (game.data.playerStat.isTutorial && game.persistent.depth > 1) {
                game.data.text = 13;
            } else if(game.data.playerStat.isTutorial && game.persistent.depth === 1 && game.data.text === 13 ){
                game.data.text = 12;
            }

            // Game_Over_screen
            me.game.world.addChild(
                new me.Sprite(
                    0, 0,
                    me.loader.getImage('game_over_screen')
                ),
                1
            );

            var slots = [];
            var coins = 0;

            for (var i = 0; i < game.data.scrolls.length; i++) {
                console.log(slots, "scrolls:" + game.data.scrolls[i]);
                if (game.data.scrolls[i] !== -1) {
                    slots.push({
                        "id": game.data.scrolls[i]
                    });
                }
            }

            function collectedOnload(xmlHttpRequest) {
                console.log(xmlHttpRequest);
                var gotten = JSON.parse(xmlHttpRequest.responseText);
                console.log(gotten);
                for (var i = 0; i < gotten.scrolls.length; i++) {
                    if (gotten.scrolls[i].id > 20) {
                        game.data.scrolls[i] = gotten.scrolls[i].name;
                    } else {
                        game.data.scrolls[i] = gotten.scrolls[i].potion.name;
                    }
                }
                coins = gotten.coins;
                //HUD that draws and updates your Gameover-Text
                me.game.world.addChild(new game.HUD.GameOver(300, 150, coins), 3);

            }

            var collected = JSON.stringify({
                depth: game.persistent.depth,
                score: game.data.score,
                scrolls: game.data.scrolls
            });
            console.log("collected: " + collected);
            ajaxSendProfileCollectedRequest(collected, collectedOnload);


            // stop the last audio track and play the audio track

            if (game.data.music) {
                me.audio.stopTrack();
                me.audio.playTrack("gameover", game.data.musicVolume);
            }

            onLab = function () {
                me.state.change(me.state.READY);
            };

            var nextButton = new game.ClickableElement('next', 'NEXT', onLab, 25, 10, 37, 80.5, 1);
            me.game.world.addChild(nextButton);

            //maxdepth verwenden
            if (Math.floor((game.persistent.depth - 1) % 5) === 0 && game.persistent.depth > game.data.playerStat.characterState.maxDepth && game.persistent.depth !== 1) {
                game.persistent.boss = true;
            }
        }

    },

    onDestroyEvent: function(){

        if(game.data.music ){
            me.audio.stopTrack();
            me.audio.playTrack("menu",game.data.musicVolume);
        }
    }
});
