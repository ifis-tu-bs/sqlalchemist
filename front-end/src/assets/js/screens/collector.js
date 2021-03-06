
game.CollectorScreen = me.ScreenObject.extend({

    /**
     *  action to perform on state change
     */
    onResetEvent: function() {


        /**
         * Create background-div and add image and title to it.
         */
        var rootContainer = new game.fdom.RootContainer('/assets/data/img/gui/wood_screen.png');
        rootContainer.hide();
        $(rootContainer.getNode()).fadeIn(100);
        me.game.world.addChild(rootContainer);

        var title = new game.fdom.TitleElement(rootContainer, '70%','19%','14.5%','7%', 'Scroll Collection', 'Title ScrollCollection');
        me.game.world.addChild(title);

        var collectorParchment = new game.fdom.ImageElement(rootContainer, '98%', '96.2%', '0%', '0%', 'Image CollectorScreen Parchment', 'assets/data/img/gui/scrollcollection_screen_transparent.png');
        collectorParchment.hide();
        $(collectorParchment.getNode()).fadeIn(100);
        me.game.world.addChild(collectorParchment);

        /**
         * Create element with according callback function to get back into the Laboratory.
         */
         var backToLabButton = new game.fdom.ButtonElement(rootContainer, '20%','20%','76%','2%', '', 'Button CollectorScreen Back', false, function() {
            if (game.data.sound) {
                me.audio.play("scroll", false, null, game.data.soundVolume);
            }
            $("[id*='scrolls']").fadeOut(100);
            $("[id*='scrollsRed']").fadeOut(100);
            $("[id*='checkSymbol']").fadeOut(100);
            $("[id*='collectionBox']").fadeOut(100);
            $("[id$='text']").fadeOut(100);
            $("#backgroundCollectorId").fadeOut(100);
            $("#backFromCollector").fadeOut(100);
            $(rootContainer.getNode()).fadeOut(100);
            setTimeout( function() {
                me.state.change(me.state.READY);
            }, 100);
        });

        backToLabButton.hide();
        $(backToLabButton.getNode()).fadeIn(100);
        me.game.world.addChild(backToLabButton);


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
            var collectionBox = new game.BackgroundElement('collectionBox' + j, 26.060606, 42.1875, 20.772727 + j * 30.878788, 35.5, 'none');
            collectionBox.setImage("assets/data/img/stuff/scroll_collection_box.png", "boxImg");
            me.game.world.addChild(collectionBox);

            for (var k = 0; k < 5; k++) {
                //console.log(game.scroll.enchantments,game.potion.potions);
                /*console.log(k + j * 5,game.scroll.enchantments[game.level.scrolls[k + j * 5][0]].available,
                  game.scroll.enchantments[game.level.scrolls[k + j * 5][1]].available,
                  game.scroll.enchantments[game.level.scrolls[k + j * 5][2]].available,
                  game.scroll.enchantments[game.level.scrolls[k + j * 5][3]].available,
                  game.potion.potions[game.level.scrolls[k + j * 5][4]].available,
                  game.potion.potions[game.level.scrolls[k + j * 5][5]].available)*/

                if (game.scroll.enchantments[game.level.scrolls[k + j * 5][0]].used &&
                    game.scroll.enchantments[game.level.scrolls[k + j * 5][1]].used &&
                    game.scroll.enchantments[game.level.scrolls[k + j * 5][2]].used &&
                    game.scroll.enchantments[game.level.scrolls[k + j * 5][3]].used &&
                    game.potion.potions[game.level.scrolls[k + j * 5][4]].available &&
                    game.potion.potions[game.level.scrolls[k + j * 5][5]].available){
                    level++;

                    var checkSymbol = new game.BackgroundElement('check' + k, 4.5, 6.5,
                        41 + j * 30.878788, 40 + 6.510417 * k, 'none');
                    checkSymbol.setImage("assets/data/img/stuff/check_symbol.png", "checksymbol");
                    me.game.world.addChild(checkSymbol);
                    //console.log('check' + k);
                    checkSymbol.display();

                }

                for (var i = 0; i < 6; i++) {
                    if (i < 4) {
                        if (game.scroll.enchantments[game.level.scrolls[k + j * 5][i]].available) {

                            var scrolls = new game.BackgroundElement('scrolls' + i + k * 4 + j * 20, 2.424243, 4.166668,
                                23.272727 + 3.030303 * i + j * 30.878788, 41.666667 + 6.510417 * k, 'none');
                            scrolls.setImage("assets/data/img/stuff/spinning_scroll_32.png", "scrollImg");
                            me.game.world.addChild(scrolls);
                            //console.log('scrolls' + i + k * 4 + j * 20);

                            progress++;
                            /*console.log(game.scroll.enchantments[game.level.scrolls[k + j * 5][i]].available,
                                game.scroll.enchantments[game.level.scrolls[k+ j * 5][i]].name, k + j * 5);*/
                        }
                    } else {
                        if (game.potion.potions[game.level.scrolls[k + j * 5][i]].available){

                            var scrollsRed = new game.BackgroundElement('scrollsRed' + i + k * 2 + j * 10, 2.424243, 4.166668,
                                23.272727 + 3.030303 * i + j * 30.878788, 41.666667 + 6.510417 * k, 'none');
                            scrollsRed.setImage("assets/data/img/stuff/spinning_scroll_red_32.png", "scrollRedImg");
                            me.game.world.addChild(scrollsRed);
                            //console.log('scrollsRed' + i + k * 2 + j * 10);

                            progress++;
                            /*console.log(game.potion.potions[game.level.scrolls[k + j * 5][i]].available,
                                    game.potion.potions[game.level.scrolls[k + j * 5][i]].name, k + j * 5);*/
                        }
                    }
                    //console.log(game.level.scrolls[k + j * 5][i],  "  k+ j * 5:"+(k + j * 5 ),"  i:"+i);
                }
            }


        }

        /**
         * Create TextOutPutElements to draw information about the daily scroll-limit, the collected scrolls and statistics.
         */
        var scrollProgress = new game.TextOutputElement('protext', 60, 18, 19.151515, 26.041667, 3);
        me.game.world.addChild(scrollProgress);
        scrollProgress.writeHTML(level + " of 10 levels cleared." + "<br>" + progress + " of 60 scrolls collected.", 'protextbody');

        var scrollLimit = new game.TextOutputElement('limtext', 86, 10, 7, 77, 2);
        me.game.world.addChild(scrollLimit);
        scrollLimit.writeHTML("You can only collect 6 scrolls a day! " + game.data.scollLimit + " Scrolls left", 'limtextbody');

        var info = new game.TextOutputElement('infotext', 80, 10, 10, 85, 3);
        me.game.world.addChild(info);
        info.writeHTML("To finish a level you have to use all of its enchantments and beat its boss.", 'infotextbody');
    }
});
