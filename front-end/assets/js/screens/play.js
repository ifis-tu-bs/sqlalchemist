game.PlayScreen = me.ScreenObject.extend({
    /**
     *  action to perform on state change
     */
    onResetEvent: function() {

        if(game.data.music) {
            me.audio.stopTrack();
            me.audio.playTrack("credits", game.data.musicVolume);
        }
        //reset the Ingame Memory
        game.data.scrolls = [];
        game.data.collectedScrolls = 0;
        game.persistent.depth = 0;
        game.data.score = 0;

        var cheat = 2;
        game.persistent.depth += cheat*5;
        game.stats.defense += cheat;
        game.stats.health += cheat;
        game.stats.speed += cheat;
        game.stats.jump += cheat;

        for( var i = 0; i < 10; i++) {
            console.log("I:"+i);
            console.log(game.persistent.maxDepth,Math.floor(game.persistent.maxDepth / 5),i);
            console.log(game.scroll.enchantments[game.level.scrolls[i][0]].used, game.scroll.enchantments[game.level.scrolls[i][1]].used,game.scroll.enchantments[game.level.scrolls[i][2]].used,
                game.scroll.enchantments[game.level.scrolls[i][3]].used, game.potion.potions[game.level.scrolls[i][4]].available, game.potion.potions[game.level.scrolls[i][5]].available, (i + 1) < Math.ceil(game.persistent.maxDepth / 5));

            if (game.scroll.enchantments[game.level.scrolls[i][0]].used && game.scroll.enchantments[game.level.scrolls[i][1]].used && game.scroll.enchantments[game.level.scrolls[i][2]].used &&
                game.scroll.enchantments[game.level.scrolls[i][3]].used && game.potion.potions[game.level.scrolls[i][4]].available && game.potion.potions[game.level.scrolls[i][5]].available && (i + 1) < Math.ceil((game.persistent.maxDepth - 1) / 5)) {
                game.persistent.depth += 5;
            }
        }
        // load the start level
        if(game.data.playerStat.isTutorial){
            me.levelDirector.loadLevel("tutorial");
        }else if(game.data.cheat){
            game.data.cheat = false;
            me.levelDirector.loadLevel("tutorial");
        }else{
            me.levelDirector.loadLevel("start");
        }

        // reset the hp
        game.stats.hp = game.stats.health;
        game.data.runs++;

        // add our HUD to the game world
        this.HUD = new game.HUD.Container();
        this.HUDII = new game.HUDII.Container();
        me.game.world.addChild(this.HUD);
        me.game.world.addChild(this.HUDII);

        //Set the Potionsbuttons to the Keys
        me.input.bindKey(me.input.KEY.NUM1, 1, true);
        me.input.bindKey(me.input.KEY.NUM2, 2, true);
        me.input.bindKey(me.input.KEY.NUM3, 3, true);
        me.input.bindKey(me.input.KEY.NUM4, 4, true);
        me.input.bindKey(me.input.KEY.NUM5, 5, true);
        me.input.bindKey(me.input.KEY.NUM6, 6, true);
        me.input.bindKey(me.input.KEY.NUM7, 7, true);

        me.input.bindKey(me.input.KEY.M, "music", true);
        me.input.bindKey(me.input.KEY.N, "sound", true);
        me.input.bindKey(me.input.KEY.ESC, "exit", true);



        //Set Space to jump and cross the left mouse-click with it
        me.input.bindKey(me.input.KEY.SPACE, "jump", true);
        me.input.bindPointer(me.input.KEY.SPACE);

    },


    /**
     *  action to perform when leaving this screen (state change)
     */
    onDestroyEvent: function() {
        // remove the HUD from the game world
        me.game.world.removeChild(this.HUD);
        me.game.world.removeChild(this.HUDII);

        // remove the Keybinds
        me.input.unbindKey(me.input.KEY.SPACE);
        me.input.unbindPointer(me.input.mouse.LEFT);
        me.input.unbindKey(me.input.KEY.NUM1);
        me.input.unbindKey(me.input.KEY.NUM2);
        me.input.unbindKey(me.input.KEY.NUM3);
        me.input.unbindKey(me.input.KEY.NUM4);
        me.input.unbindKey(me.input.KEY.NUM5);
        me.input.unbindKey(me.input.KEY.NUM6);
        me.input.unbindKey(me.input.KEY.NUM7);
        me.input.unbindKey(me.input.KEY.ESC);
        me.input.unbindKey(me.input.KEY.N);
        me.input.unbindKey(me.input.KEY.M);
    }
});

