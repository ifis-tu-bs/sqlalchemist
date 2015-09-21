game.ReadyScreen = me.ScreenObject.extend({

    /**
     *  action to perform on state change
     */
    onResetEvent : function() {

        //gets the playerstate
        function getStats(xmlHttpRequest) {
            console.log(xmlHttpRequest);

            var stat = JSON.parse(xmlHttpRequest.responseText);
            game.data.playerStat = stat;
            console.log(game.data.playerStat);

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
            console.log(game.data.scollLimit);

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
        /**me.game.world.addChild(
            new me.Sprite (
                0,0,
                me.loader.getImage('lab_screen')
            ),
            1
        );*/

        var background = new game.BackgroundElement('background', 100, 100, 0, 0);
        background.setImage("assets/data/img/gui/lab_screen.png", "back");
        me.game.world.addChild(background);


        //buttons for menu movement
        //me.game.world.addChild(new menu(1104,93),5);


        /**
         * these functions are called when buttons are clicked.
         * here: simple state change.
         */
        this.onRun = function(){
            if(game.data.sound){
                me.audio.play("door", false, null, game.data.soundVolume);
            }
            me.state.change(me.state.PLAY);
        };
        this.onCollector = function(){
            if(game.data.sound){
                me.audio.play("scroll", false, null, game.data.soundVolume);
            }
            me.state.change(STATE_COLLECTOR);
        };
        this.onSheet = function(){
            if(game.data.sound){
                me.audio.play("switch", false, null, game.data.soundVolume);
            }
            me.state.change(STATE_SHEET);
        };
        this.onScrollCol = function(){
            if(game.data.sound){
                me.audio.play("switch", false, null, game.data.soundVolume);
            }
            me.state.change(STATE_BELT);
        };
        this.onMenu = function(){
            if(game.data.sound){
                me.audio.play("door", false, null, game.data.soundVolume);
            }
            me.state.change(me.state.MENU);
        };
        this.onBooks = function(){
            if(game.data.sound){
                me.audio.play("page", false, null, game.data.soundVolume);
            }
            me.state.change(STATE_LEGEND);
        };
        this.stopDouble = function() {
            game.data.playing = false;
        };
        this.onCat = function() {
            if(game.data.sound && !game.data.playing){
                game.data.playing = true;
                me.audio.play("cat", false, this.stopDouble, game.data.soundVolume);
            }
        };


        //HIER TERRY BUTTON FUNCTION
        this.onTerry = function() {
            if(game.data.sound && !game.data.playing){
                game.data.playing = true;
                var number = Math.floor(Math.random() * 4 + 1);
                var sound = "lofi";
                sound = sound.concat(number);
                me.audio.play(sound, false, this.stopDouble, game.data.soundVolume);
            }
        };


        /**
         * Add all needed buttons to Lab screen
         * @param : id       : a unique alphanumeric string
         *          name     : text to display on screen
         *          callback : the callback function
         *          width    : the width of the element in percent of the width of the canvas
         *          height   : the height of the element in percent of the height of the canvas
         *          left     : the left margin of the element in percent of the width of the canvas
         *          top      : the top margin of the element in percent of the height of the canvas
         *          rows     : the number of rows
         */
        var runButton        = new game.ClickableElement('runButton', 'R U N', this.onRun, 15, 50, 33, 15, 4);
        var colButton        = new game.ClickableElement('colButton', 'Coll ector', this.onCollector, 20, 13, 60, 70, 2);
        var sheetButton      = new game.ClickableElement('sheetButton', 'Sh eet', this.onSheet, 9, 22, 18, 11, 3);
        var scrollColButton  = new game.ClickableElement('scrollColButton', 'Be lt', this.onScrollCol, 28, 25, 17, 75, 2);
        var backButton       = new game.ClickableElement('backButton', 'BACK', this.onMenu, 15, 34, 81, 14, 4);
        var bookButton       = new game.ClickableElement('bookButton', 'Bo ok', this.onBooks, 16, 37, 83, 59, 2);
        var catButton        = new game.ClickableElement('catButton', 'c a t', this.onCat, 10, 30, 49, 49, 3);
        var terryButton      = new game.ClickableElement('terryButton', 'terry', this.onTerry, 18, 40, 56, 12, 5);

        me.game.world.addChild(runButton);
        me.game.world.addChild(sheetButton);
        me.game.world.addChild(colButton);
        me.game.world.addChild(scrollColButton);
        me.game.world.addChild(backButton);
        me.game.world.addChild(bookButton);
        me.game.world.addChild(catButton);
        me.game.world.addChild(terryButton);


    }
});