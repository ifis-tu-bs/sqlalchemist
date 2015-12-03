/**
 * The ReadyScreen is really important for the game.
 * All of the players information will be saved locally every time the player passes the lab.
 */

game.ReadyScreen = me.ScreenObject.extend({

    /**
     *  action to perform on state change
     */
    onResetEvent : function() {



        function fadeOutElements() {
            $("#runButton").fadeOut(100);
            $("#colButton").fadeOut(100);
            $("#sheetButton").fadeOut(100);
            $("#scrollColButton").fadeOut(100);
            $("#backButton").fadeOut(100);
            $("#bookButton").fadeOut(100);
            $("#catButton").fadeOut(100);
            $("#terryButton").fadeOut(100);
            $("#backgroundLab").fadeOut(100);
        }


        //gets the playerstate
        function getStats(xmlHttpRequest) {
            console.log(xmlHttpRequest);

            var stat = JSON.parse(xmlHttpRequest.responseText);
            game.data.playerStat = stat;
            //console.log(game.data.playerStat);

            //attributes
            game.stats.health = stat.characterState.attributes.health;
            game.stats.speed = stat.characterState.attributes.speed;
            game.stats.jump = stat.characterState.attributes.jump;
            game.stats.defense = stat.characterState.attributes.defense;
            game.persistent.maxDepth = stat.characterState.maxDepth;

            //sprite
            game.data.sprite = stat.characterState.currentAvatar.avatarFilename;
            game.skin.currentSkin = stat.characterState.currentAvatar.id;
            //get scrolls
            for (var i = 0; i < stat.characterState.scrollCollection.scrolls.length; i++) {

                if (stat.characterState.scrollCollection.scrolls[i].scroll.id > 19) {
                    game.scroll.enchantments[stat.characterState.scrollCollection.scrolls[i].scroll.posId - 20].available = true;
                    game.scroll.enchantments[stat.characterState.scrollCollection.scrolls[i].scroll.posId - 20].used = stat.characterState.scrollCollection.scrolls[i].isActive;
                    game.scroll.enchantments[stat.characterState.scrollCollection.scrolls[i].scroll.posId - 20].id = stat.characterState.scrollCollection.scrolls[i].scroll.id;
                    game.scroll.enchantments[stat.characterState.scrollCollection.scrolls[i].scroll.posId - 20].name = stat.characterState.scrollCollection.scrolls[i].scroll.name;
                        //console.log("ent:"+ (stat.characterState.scrollCollection.scrolls[i].scroll.posId - 20));
                }
                    //console.log("ent:"+ (stat.characterState.scrollCollection.scrolls[i].scroll.posId - 20), i);

            }
            game.data.scollLimit = stat.characterState.scrollLimit;

            //get Potions 1-20
            for (var i = 0; i < stat.characterState.inventory.potions.length; i++) {
                game.potion.potions[i + 1].amount = stat.characterState.inventory.potions[i].count;
                game.potion.potions[i + 1].name = stat.characterState.inventory.potions[i].potion.name;
                game.potion.potions[i + 1].id = stat.characterState.inventory.potions[i].potion.id;
                game.potion.potions[i + 1].type = stat.characterState.inventory.potions[i].potion.type;
                game.potion.potions[i + 1].power = stat.characterState.inventory.potions[i].potion.powerLevel;
                //console.log("pot"+(i+1));

                if (stat.characterState.inventory.potions[i].scroll !== "empty") {
                    game.potion.potions[i + 1].available = true;
                }
            }

            //get avatars
            for (var i = 0; i < stat.characterState.avatars_bought.length; i++) {
                game.skin.skins[stat.characterState.avatars_bought[i].id].available = 1;
                console.log(i, stat.characterState.avatars_bought[i].id, game.skin.skins[stat.characterState.avatars_bought[i].id].name)
            }

            //start the Text
            if ((stat.isTutorial && game.data.text !== 14) || game.persistent.boss) {

                if (game.persistent.boss) {
                    game.data.text = 0;
                }
                me.state.change(STATE_TEXT);
            }

        }

        ajaxSendChallengeStoryRequest(getStats);

        //load belt
        function getBelt(xmlHttpRequest) {
            console.log("belt" + xmlHttpRequest);
            var belt = JSON.parse(xmlHttpRequest.responseText);
            console.log(belt.slots.length);
            console.log(belt);
            console.log("!!!");
            for (var i = 0; i < belt.slots.length; i++) {
                if (i <= game.belt.beltSlots.length) {
                    console.log("No push" + belt.slots[i].potion);
                    if (belt.slots[i].potion === "empty") {
                        game.belt.beltSlots[i] = null;
                    } else {
                        game.belt.beltSlots[i] = game.potion.potions[belt.slots[i].potion.id];
                    }
                } else {
                    console.log("push!!" + belt.slots[i].potion);
                    if (belt.slots[i].potion === "empty") {
                        game.belt.beltSlots.push(null);
                    } else {
                        game.belt.beltSlots.push(game.potion.potions[belt.slots[i].potion.id]);
                    }
                }
                console.log(game.belt.beltSlots);
            }
        }

        ajaxSendProfileBeltRequest(getBelt);

        // lab_screen

        var background = new game.BackgroundElement('backgroundLab', 100, 100, 0, 0);
        background.setImage("assets/data/img/gui/new_lab.png", "back");
        me.game.world.addChild(background);

        /**
         * these functions are called when buttons are clicked.
         * here: simple state change.
         */
        this.onRun = function(){
            if(game.data.sound){
                me.audio.play("door", false, null, game.data.soundVolume);
            }
            fadeOutElements();
            setTimeout( function() {
                me.state.change(me.state.PLAY);
            }, 100);
        };
        this.onCollector = function(){
            if(game.data.sound){
                me.audio.play("scroll", false, null, game.data.soundVolume);
            }
            fadeOutElements();
            setTimeout( function() {
                me.state.change(STATE_COLLECTOR);
            }, 100);
        };
        this.onSheet = function(){
            if(game.data.sound){
                me.audio.play("switch", false, null, game.data.soundVolume);
            }
            fadeOutElements();
            setTimeout( function() {
                me.state.change(STATE_SHEET);
            }, 100);
        };
        this.onScrollCol = function(){
            if(game.data.sound){
                me.audio.play("switch", false, null, game.data.soundVolume);
            }
            fadeOutElements();
            setTimeout( function() {
                me.state.change(STATE_BELT);
            }, 100);
        };
        this.onMenu = function(){
            if(game.data.sound){
                me.audio.play("door", false, null, game.data.soundVolume);
            }
            fadeOutElements();
            setTimeout( function() {
                me.state.change(me.state.MENU);
            }, 100);
        };
        this.onBooks = function(){
            if(game.data.sound){
                me.audio.play("page", false, null, game.data.soundVolume);
            }
            fadeOutElements();
            setTimeout( function() {
                me.state.change(STATE_LEGEND);
            }, 100);
        };
        function stopDouble() {
            game.data.playing = false;
        };
        this.onCat = function() {
            if(game.data.sound && !game.data.playing){
                game.data.playing = true;
                me.audio.play("cat", false, stopDouble, game.data.soundVolume);
            }
        };

        this.onTerry = function() {
            if(game.data.sound && !game.data.playing){
                game.data.playing = true;
                var number = Math.floor(Math.random() * 4 + 1);
                var sound = "lofi";
                sound = sound.concat(number);
                me.audio.play(sound, false, stopDouble, game.data.soundVolume);
            }
        };


        /**
         * Add all needed buttons to Lab screen
         */
        var runButton        = new game.ClickableElement('runButton', '', this.onRun, 17.2, 49.6, 31.7, 14.5, 1);
        var colButton        = new game.ClickableElement('colButton', '', this.onCollector, 20, 13, 60, 70, 2);
        var sheetButton      = new game.ClickableElement('sheetButton', '', this.onSheet, 12, 27.2, 16.3, 8.3, 1);
        var scrollColButton  = new game.ClickableElement('scrollColButton', '', this.onScrollCol, 29.7, 23.7, 17.6, 76.3, 1);
        var backButton       = new game.ClickableElement('backButton', '', this.onMenu, 14.6, 43.2, 83.5, 9.8, 1);
        var bookButton       = new game.ClickableElement('bookButton', '', this.onBooks, 18.6, 46.5, 81.74, 53.5, 1);
        var catButton        = new game.ClickableElement('catButton', '', this.onCat, 9.24, 20.96, 46.89, 52.99, 1);
        var terryButton      = new game.ClickableElement('terryButton', '', this.onTerry, 19.32, 66.15, 53.3, 10.29, 1);
        runButton.setImage("assets/data/img/buttons/menubuttons/dungeon_door.png", "dungeonImage");
        colButton.setImage("assets/data/img/buttons/menubuttons/scroll_collection.png", "scrollsImage");
        sheetButton.setImage("assets/data/img/buttons/menubuttons/mirror.png", "sheetImage");
        scrollColButton.setImage("assets/data/img/buttons/menubuttons/potion_belt_cut.png", "beltImage");
        backButton.setImage("assets/data/img/buttons/menubuttons/fat_lady.png", "fatLadyImage");
        bookButton.setImage("assets/data/img/buttons/menubuttons/table.png", "tableImage");
        catButton.setImage("assets/data/img/buttons/menubuttons/cat.png", "catImage");
        terryButton.setImage("assets/data/img/buttons/menubuttons/schrank.png", "terryImage");
        me.game.world.addChild(runButton);
        me.game.world.addChild(sheetButton);
        me.game.world.addChild(colButton);
        me.game.world.addChild(scrollColButton);
        me.game.world.addChild(backButton);
        me.game.world.addChild(bookButton);
        me.game.world.addChild(catButton);
        me.game.world.addChild(terryButton);

        console.log("AAAAAAAAAAA",game.data.to );
        if(game.data.to != null){
            console.log(game.data.to );
            var state = game.data.to;
            game.data.to = null;
            me.state.change(state);
        }

    }
});