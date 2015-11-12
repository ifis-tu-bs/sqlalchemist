
game.CollectorScreen = me.ScreenObject.extend({

    /**
     *  action to perform on state change
     */
    onResetEvent: function() {


        /**
         * Create background-div and add image to it.
         */
        var backgroundCollector = new game.BackgroundElement('backgroundCollectorId', 100, 100, 0, 0, 'none');
        backgroundCollector.setImage("assets/data/img/gui/scrollcollection_screen.png", "backgroundCollectorImg");
        me.game.world.addChild(backgroundCollector);
        $("#backgroundCollectorId").fadeIn(100);

        /**
         * Create element with according callback function to get back into the Laboratory.
         */
        this.backToLab = function(){
            if (game.data.sound) {
                me.audio.play("scroll", false, null, game.data.soundVolume);
            }
            $("[id*='scrolls']").fadeOut(100);
            $("[id*='scrollsRed']").fadeOut(100);
            $("[id*='checkSymbol']").fadeOut(100);
            $("[id*='collectionBox']").fadeOut(100);
            $("#backgroundCollectorId").fadeOut(100);
            $("#backToLabButton").fadeOut(100);
            setTimeout( function() {
                me.state.change(me.state.READY);
            }, 100);
        };

        var backToLabButton = new game.ClickableElement('backToLabButton', 'Back', this.backToLab, 15, 7.3, 3, 6, 1);
        me.game.world.addChild(backToLabButton);
        $("#backToLabButton").fadeIn(100);

        /**
         * Init matrix to set symbols for enchantment and potion scrolls.
         * @type {number}
         */
        var progress = 0;
        var level = 0;

        /**
         * Go trought the matrix and set images for every collected potion or enchantment entity.
         */
        for (var j = 0; j < 2; j++) {

            /**
             * Set image for checkBoxes.
             */
            var collectionBox = new game.BackgroundElement('collectionBox' + j, 26.060606, 42.1875, 14.772727 + j * 37.878788, 35.5, 'none');
            collectionBox.setImage("assets/data/img/stuff/scroll_collection_box.png", "boxImg");
            $("#collectionBox" + j).fadeIn(100);
            me.game.world.addChild(collectionBox);

            for (var k = 0; k < 5; k++) {
                //console.log(game.scroll.enchantments,game.potion.potions);
                /*console.log(k + j * 5,game.scroll.enchantments[game.level.scrolls[k + j * 5][0]].available,
                  game.scroll.enchantments[game.level.scrolls[k + j * 5][1]].available,
                  game.scroll.enchantments[game.level.scrolls[k + j * 5][2]].available,
                  game.scroll.enchantments[game.level.scrolls[k + j * 5][3]].available,
                  game.potion.potions[game.level.scrolls[k + j * 5][4]].available,
                  game.potion.potions[game.level.scrolls[k + j * 5][5]].available)*/

                if(game.scroll.enchantments[game.level.scrolls[k + j * 5][0]].used
                    && game.scroll.enchantments[game.level.scrolls[k + j * 5][1]].used
                    && game.scroll.enchantments[game.level.scrolls[k + j * 5][2]].used
                    && game.scroll.enchantments[game.level.scrolls[k + j * 5][3]].used
                    && game.potion.potions[game.level.scrolls[k + j * 5][4]].available
                    && game.potion.potions[game.level.scrolls[k + j * 5][5]].available){
                    level++;

                    var checkSymbol = new game.BackgroundElement('check' + k, 4.5, 6.5,
                        35 + j * 37.878788, 40 + 6.510417 * k, 'none');
                    checkSymbol.setImage("assets/data/img/stuff/check_symbol.png", "checksymbol");
                    $("#checkSymbol" + k).fadeIn(100);
                    me.game.world.addChild(checkSymbol);
                    console.log('check' + k);
                    checkSymbol.display();

                    //me.game.world.addChild(new me.Sprite(225 + 40 * 6 + j * 500, 305 + 50 * k, me.loader.getImage('check_symbol')),3);
                }
                for (var i = 0; i < 6; i++) {
                    if (i < 4) {
                        if (game.scroll.enchantments[game.level.scrolls[k + j * 5][i]].available) {

                            var scrolls = new game.BackgroundElement('scrolls' + i, 2.424243, 4.166668,
                                17.272727 + 3.030303 * i + j * 37.878788, 41.666667 + 6.510417 * k, 'none');
                            scrolls.setImage("assets/data/img/stuff/spinning_scroll_32.png", "scrollImg");
                            $("#scrolls" + i).fadeIn(100);
                            me.game.world.addChild(scrolls);
                            console.log('scrolls' + i);
                            scrolls.display();

                            progress++;
                            /*console.log(game.scroll.enchantments[game.level.scrolls[k + j * 5][i]].available,
                                game.scroll.enchantments[game.level.scrolls[k+ j * 5][i]].name, k + j * 5);*/
                        }
                    } else {
                        if (game.potion.potions[game.level.scrolls[k + j * 5][i]].available){

                            var scrollsRed = new game.BackgroundElement('scrollsRed' + i, 2.424243, 4.166668,
                                17.272727 + 3.030303 * i + j * 37.878788, 41.666667 + 6.510417 * k, 'none');
                            scrollsRed.setImage("assets/data/img/stuff/spinning_scroll_red_32.png", "scrollRedImg");
                            $("#scrollsRed" + i).fadeIn(100);
                            me.game.world.addChild(scrollsRed);
                            console.log('scrollsRed' + i);
                            scrollsRed.display();

                            progress++;
                            /*console.log(game.potion.potions[game.level.scrolls[k + j * 5][i]].available,
                                    game.potion.potions[game.level.scrolls[k + j * 5][i]].name, k + j * 5);*/
                        }
                    }
                    //console.log(game.level.scrolls[k+ j * 5][i],  "  k+ j * 5:"+(k + j * 5 ),"  i:"+i);
                }
            }


        }

        /**
         * Create TextOutPutElements to draw information about the daily scroll-limit, the collected scrolls and statistics.
         */
        var scrollProgress = new game.TextOutputElement('protext', 60, 18, 19.151515, 26.041667, 3);
        me.game.world.addChild(scrollProgress);
        scrollProgress.writeHTML(level + " of 10 levels cleared." + "<br>" + progress + " of 60 scrolls collected.", 'protextbody');

        var scrollLimit = new game.TextOutputElement('limtext', 70, 12, 19.151515, 77, 2);
        me.game.world.addChild(scrollLimit);
        scrollLimit.writeHTML("You can only collect 3 scrolls a day!", 'limtextbody');
    }
});