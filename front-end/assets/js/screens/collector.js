
game.CollectorScreen = me.ScreenObject.extend({

    /**
     *  action to perform on state change
     */
    onResetEvent: function() {
        me.game.world.addChild(
            new me.Sprite(
                0, 0,
                me.loader.getImage('scrollcollection_screen')
            ),
            1
        );


        backToLab = function(){
            if(game.data.sound){
                me.audio.play("scroll", false, null, game.data.soundVolume);
            }
            $("#backToLabButton").fadeOut(100);
            setTimeout( function() {
                me.state.change(me.state.READY);
            }, 100);
        };

        var backToLabButton = new game.ClickableElement('backToLabButton', 'Back', backToLab, 15, 7.3, 3, 6, 1);
        me.game.world.addChild(backToLabButton);
        $("#backToLabButton").fadeIn(100);

        var progress = 0;
        var level = 0;

        //hier kommt die Scrollcollection hin
        for (var j = 0; j < 2; j++) {
            for (var k = 0; k < 5; k++) {
                console.log(game.scroll.enchantments,game.potion.potions);
                //console.log(k + j * 5,game.scroll.enchantments[game.level.scrolls[k + j * 5][0]].available, game.scroll.enchantments[game.level.scrolls[k + j * 5][1]].available, game.scroll.enchantments[game.level.scrolls[k + j * 5][2]].available ,game.scroll.enchantments[game.level.scrolls[k + j * 5][3]].available , game.potion.potions[game.level.scrolls[k + j * 5][4]].available , game.potion.potions[game.level.scrolls[k + j * 5][5]].available )
                if(game.scroll.enchantments[game.level.scrolls[k + j * 5][0]].used && game.scroll.enchantments[game.level.scrolls[k + j * 5][1]].used && game.scroll.enchantments[game.level.scrolls[k + j * 5][2]].used && game.scroll.enchantments[game.level.scrolls[k + j * 5][3]].used && game.potion.potions[game.level.scrolls[k + j * 5][4]].available && game.potion.potions[game.level.scrolls[k + j * 5][5]].available){
                    level++;
                    me.game.world.addChild(new me.Sprite(225 + 40 * 6 + j * 500, 305 + 50 * k,me.loader.getImage('check_symbol')),3);
                }

                for (var i = 0; i < 6; i++) {
                    if(i < 4) {
                        if (game.scroll.enchantments[game.level.scrolls[k + j * 5][i]].available) {
                            me.game.world.addChild(new game.Scroll(228 + 40 * i + j * 500, 320 + 50 * k, "spinning_scrolls"), 3);
                            progress++;
                            console.log(game.scroll.enchantments[game.level.scrolls[k + j * 5][i]].available,
                                    game.scroll.enchantments[game.level.scrolls[k+ j * 5][i]].name, k + j * 5);
                        }
                    }
                    else{
                        if (game.potion.potions[game.level.scrolls[k + j * 5][i]].available){
                            me.game.world.addChild(new game.Scroll(228 + 40 * i + j * 500, 320 + 50 * k, "spinning_scrolls_red"), 3);
                            progress++;
                            console.log(game.potion.potions[game.level.scrolls[k + j * 5][i]].available,
                                    game.potion.potions[game.level.scrolls[k + j * 5][i]].name, k + j * 5);
                        }
                    }
                    //console.log(game.level.scrolls[k+ j * 5][i],  "  k+ j * 5:"+(k + j * 5 ),"  i:"+i);
                }
            }
            me.game.world.addChild(
                new me.Sprite(
                    195 + j * 500, 275,
                    me.loader.getImage('scroll_collection_box')
                ),
                2
            );
        }

        me.game.world.addChild(new game.HUD.Collector(200, 200, progress, level),3);
    }
});