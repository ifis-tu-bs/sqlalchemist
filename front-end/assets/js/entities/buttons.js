/*
 *  The buttons which contain pictures or a update fuction.
 */

// used in trivia does save the information for the task
var task = me.GUI_Object.extend(
    {
        init:function (x, y, difficulty)
        {
            var settings = {};
            this.difficulty = difficulty;
            settings.image = "task";
            settings.spritewidth = 165;
            settings.spriteheight = 165;
            // super constructor
            this._super(me.GUI_Object, "init", [x, y, settings]);
            // define the object z order
            this.z = 4;
        },

        // when the object is clicked
        onClick:function (event)
        {
            game.task.kind = 2;
            game.task.difficulty = this.difficulty;
            me.state.change(STATE_TASK);

        }
    });


// used in the CharacterSheet
var skinRight = me.GUI_Object.extend(
    {
        init:function (x, y)
        {
            var settings = {}
            settings.image = "character_arrow_right";
            settings.spritewidth = 64;
            settings.spriteheight = 64;
            // super constructor
            this._super(me.GUI_Object, "init", [x, y, settings]);
            // define the object z order
            this.z = 4;
        },

        // when the object is clicked
        onClick:function (event)
        {
            //cycle through the game.skins.available and find next skin available
            do {
                game.skin.currentSkin = (game.skin.currentSkin + 1) % game.skin.skins.length ;
                console.log(game.skin.currentSkin);
            } while(game.skin.skins[game.skin.currentSkin].available === 0);

            //updates the avatar stats
            function getAvatarId(xmlHttpRequest) {

                var avatarId = JSON.parse(xmlHttpRequest.responseText);
                console.log(avatarId);
                console.log(game.skin.currentSkin);
                game.stats.health = avatarId.attributes.health;
                game.stats.speed = avatarId.attributes.speed;
                game.stats.jump = avatarId.attributes.jump;
                game.stats.defense = avatarId.attributes.defense;
                me.state.change(STATE_SHEET);

            }

            ajaxSendProfileAvatarIdRequest(game.skin.currentSkin,getAvatarId);
        }
    });

// used in the CharacterSheet
var skinLeft = me.GUI_Object.extend(
    {
        init:function (x, y)
        {
            var settings = {}
            settings.image = "character_arrow_left";
            settings.spritewidth = 64;
            settings.spriteheight = 64;
            // super constructor
            this._super(me.GUI_Object, "init", [x, y, settings]);
            // define the object z order
            this.z = 4;


        },

        // when the object is clicked
        onClick:function (event)
        {
            console.log("clicked! skinLeft");

            //cycle through the game.skins.available and find next skin available
            do {
                game.skin.currentSkin = (game.skin.currentSkin - 1 + game.skin.skins.length) % game.skin.skins.length;
                console.log(game.skin.currentSkin);
            }while(game.skin.skins[game.skin.currentSkin].available === 0);

            //updates the avatar stats
            function getAvatarId(xmlHttpRequest) {

                var avatarId = JSON.parse(xmlHttpRequest.responseText);
                console.log(avatarId);
                console.log(game.skin.currentSkin);
                game.stats.health = avatarId.attributes.health;
                game.stats.speed = avatarId.attributes.speed;
                game.stats.jump = avatarId.attributes.jump;
                game.stats.defense = avatarId.attributes.defense;
                me.state.change(STATE_SHEET);

            }

            ajaxSendProfileAvatarIdRequest(game.skin.currentSkin ,getAvatarId);

        }
    });

// used in Game
var exit = me.GUI_Object.extend(
    {
        init:function (x, y)
        {
            var settings = {};
            settings.image = "ButtonsExit";
            settings.spritewidth = 64;
            settings.spriteheight = 64;
            // super constructor
            this._super(me.GUI_Object, "init", [x, y, settings]);
            // define the object z order
            this.z = -1;
        },

        // when the object is clicked
        onClick:function (event)
        {
            me.state.change(me.state.GAMEOVER);
        },
        // catches the event exit which is declared in play.js
        update :function (){
            if (me.input.isKeyPressed("exit")) {
                me.state.change(me.state.GAMEOVER);
            }
        }
    });

// used in Settings, Game
var music = me.GUI_Object.extend(
    {
        init:function (x, y)
        {
            var settings = {}
            settings.image = "ButtonsMusic";
            settings.spritewidth = 64;
            settings.spriteheight = 64;
            // super constructor
            this._super(me.GUI_Object, "init", [x, y, settings]);
            // define the object z order
            this.z = 4;
        },

        // output something in the console
        // when the object is clicked
        onClick:function (event)
        {
            if (game.data.music){
                game.data.music = false;
                me.audio.stopTrack();
            }else{
                game.data.music = true;
                if(me.state.isCurrent(me.state.PLAY)){
                    me.audio.playTrack(game.data.recentTitle ,game.data.musicVolume);
                }else{
                    me.audio.playTrack("Menu",game.data.musicVolume);
                }
            }
        },
        update :function (){
            if (me.input.isKeyPressed("music")) {
                if (game.data.music){
                    game.data.music = false;
                    me.audio.stopTrack();
                }else{
                    game.data.music = true;
                    me.audio.playTrack("level3",game.data.musicVolume);

                }
            }
        }
    });

// used in Settings, Game
var sound = me.GUI_Object.extend(
    {
        init:function (x, y)
        {
            var settings = {}
            settings.image = "ButtonsSound";
            settings.spritewidth = 64;
            settings.spriteheight = 64;
            // super constructor
            this._super(me.GUI_Object, "init", [x, y, settings]);
            // define the object z order
            this.z = 4;
        },

        // output something in the console
        // when the object is clicked
        onClick:function (event)
        {
            if (game.data.sound){
                game.data.sound = false;
            }else{
                game.data.sound = true;
                me.audio.play("cling", false, null, game.data.soundVolume);
            }

        },
        update :function (){
            if (me.input.isKeyPressed("sound")) {
                if (game.data.sound){
                    game.data.sound = false;
                }else{
                    game.data.sound = true;
                    me.audio.play("cling", false, null, game.data.soundVolume);
                }
            }
        }
    });

// used in Game
var beltSlot = me.GUI_Object.extend(
    {
        init:function (x, y, potion, slot)
        {
            this.potion = potion;
            this.slot = slot;
            this.width = 64;
            var settings = {};
            settings.image = getPotionName(potion);
            settings.spritewidth = 64;
            settings.spriteheight = this.width;
            // super constructor
            this._super(me.GUI_Object, "init", [x, y, settings]);
            // define the object z order
            this.z = 200;
        },

        // output something in the console
        // when the object is clicked
        onClick:function (event)
        {
            console.log("clicked!belt1!");
            console.log(game.belt.beltSlots[this.slot], this.slot,game.belt.beltSlots)
            //cycle through the game.skins.available and find next skin available
            if(game.belt.beltSlots[this.slot] !== null){
                if(game.potion.usePotion(this.potion, this.slot)) {
                    game.belt.beltSlots[this.slot] = null;
                    this.width = 1;
                }
            }else{
                console.log(game.belt.beltSlots[this.slot], this.slot,game.belt.beltSlots)
            }

        },
        update :function (){
            if (me.input.isKeyPressed(this.slot+1)) {
                if(game.belt.beltSlots[this.slot] !== null){
                    if(game.potion.usePotion(this.potion, this.slot)) {
                        game.belt.beltSlots[this.slot] = null;
                        this.width = 1;
                    }
                }
            }
        }
    });

// used in Shop
var beltSlotBelt = me.GUI_Object.extend(
    {
        init:function (x, y, potion, slot)
        {
            this.potion = potion;
            this.slot = slot;
            this.width = 64;
            var settings = {};
            settings.image = getPotionName(potion);
            settings.spritewidth = 64;
            settings.spriteheight = this.width;
            // super constructor
            this._super(me.GUI_Object, "init", [x, y, settings]);
            // define the object z order
            this.z = 200;
        },

        // output something in the console
        // when the object is clicked
        onClick:function (event)
        {
            console.log("clicked!beltSlotbelt!" + this.potion + this.slot);

            function onloadBelt(xmlHttpRequest) {

                console.log(xmlHttpRequest);
                me.state.change(STATE_BELT);

            }
            function setBelt() {


                this.slots = [];


                for(var i = 1; i <= game.belt.beltSlots.length; i++) {
                    console.log(i-1);
                    if(game.belt.beltSlots[i-1] === null) {
                        this.slots.push({
                            "slot" : i,
                            "potion"  : "empty"
                        });
                    }else{
                        this.slots.push({
                            "slot" : i,
                            "potion"  : game.belt.beltSlots[i-1].id
                        });
                    }
                    console.log(this.slots);
                }
                this.user_json = JSON.stringify({slots : this.slots});


                console.log(this.user_json);
                ajaxSendProfileBeltSetRequest(this.user_json,onloadBelt);
            }


            //puts attched potion into the collection
            if( game.belt.beltSlots[this.slot] !== null){
                game.belt.beltSlots[this.slot] = null;
                this.potion.amount ++;
                console.log(this.potion.amount);
                setBelt();
            }

            me.state.change(STATE_BELT);

        }
    });

// used in Potioncollector
var potionArrow = me.GUI_Object.extend(
    {
        init:function (x, y, potion)
        {
            this.potion = potion;
            var settings = {};
            settings.image = "spinning_scrolls";
            settings.spritewidth = 32;
            settings.spriteheight = 32;
            // super constructor
            this._super(me.GUI_Object, "init", [x, y, settings]);
            // define the object z order
            this.z = 200;
        },

        // output something in the console
        // when the object is clicked
        onClick:function (event)
        {

            game.task.potionId = this.potion.id;
            game.task.kind = 0;
            game.task.name = this.potion.name;
            console.log("Potion: " + game.task.potionId, "Name: " + game.task.name ,"Result" + game.potion.potions[game.task.potionId].name)
            me.state.change(STATE_TASK);

        }
    });

var showPotion = me.GUI_Object.extend(
    {
        init:function (x, y, potion)
        {
            this.potion = potion;
            this.width = 64;
            var settings = {};
            settings.image = getPotionName(potion);
            settings.spritewidth = 64;
            settings.spriteheight = this.width;
            // super constructor
            this._super(me.GUI_Object, "init", [x, y, settings]);
            // define the object z order
            this.z = 200;
        },

        // output something in the console
        // when the object is clicked
        onClick:function (event)
        {
            function onloadBelt(xmlHttpRequest) {

                console.log(xmlHttpRequest);
                me.state.change(STATE_BELT);
            }
            function setBelt() {


                this.slots = [];


                for(var i = 1; i <= game.belt.beltSlots.length; i++) {
                    console.log(i-1);
                    if(game.belt.beltSlots[i-1] === null) {
                        this.slots.push({
                            "slot" : i,
                            "potion"  : "empty"
                        });
                    }else{
                        this.slots.push({
                            "slot" : i,
                            "potion"  : game.belt.beltSlots[i-1].id
                        });
                    }
                    console.log(this.slots);
                }
                this.user_json = JSON.stringify({slots : this.slots});


                console.log(this.user_json);
                ajaxSendProfileBeltSetRequest(this.user_json,onloadBelt);
            }



            console.log(this.potion.amount + "arrow");
            var i = 0;

            while(i < game.belt.beltSlots.length && game.belt.beltSlots[i] !== null ){
                console.log("giveMeThe i: " + i + "and slot: " + game.belt.beltSlots[i]);
                i++;
            };



            //puts attched potion into the collection
            if(i < game.belt.beltSlots.length && this.potion.amount > 0){
                game.belt.beltSlots[i] = this.potion;
                this.potion.amount --;
                setBelt();
            }

        }
    });


// used in Shop
var showSkin = me.GUI_Object.extend(
    {
        init:function (x, y, skin, size, id, i)
        {
            this.skin = skin;
            this.shopId = i;
            this.spriteId = id;
            this.width = size;
            var settings = {};
            settings.image = skin.concat("_front");
            settings.spritewidth = this.width;
            settings.spriteheight = 64;
            // super constructor
            this._super(me.GUI_Object, "init", [x, y, settings]);
            // define the object z order
            this.z = 200;
        },

        // output something in the console
        // when the object is clicked
        onClick:function (event)
        {
            console.log(this.skin);
            game.data.spriteId  = this.spriteId;
            game.data.shopId  = this.shopId;

            me.state.change(STATE_BUY);

        }
    });

var buyBelt = me.GUI_Object.extend(
    {
        init:function (x, y, id, i)
        {
            this.shopId = i;
            this.spriteId = id;
            var settings = {};
            settings.image = "belt";
            settings.spritewidth = 55;
            settings.spriteheight = 330;
            // super constructor
            this._super(me.GUI_Object, "init", [x, y, settings]);
            // define the object z order
            this.z = 200;
        },

        // output something in the console
        // when the object is clicked
        onClick:function (event)
        {
            console.log(this.shopId + "Belt" + this.spriteId);
            game.data.spriteId  = this.spriteId;
            game.data.shopId = this.shopId;

            me.state.change(STATE_BUY);

        }
    });

//used in resultScreen
var NEXT = me.GUI_Object.extend(
    {
        init:function (x, y){
            var settings = {};
            settings.image = "story_button";
            settings.spritewidth = 240;
            settings.spriteheight = 330;
            // super constructor
            this._super(me.GUI_Object, "init", [x, y, settings]);
            // define the object z order
            this.z = 4;
        },

        // output something in the console
        // when the object is clicked
        onClick:function (event) {
            console.log("clicked! Next with:" + game.task.kind);
            switch(game.task.kind) {
                case 0 :
                {
                    me.state.change(me.state.READY);
                    return true;
                    break;
                }
                case 1 :
                {
                    me.state.change(me.state.READY);
                    return true;
                    break;
                }
                case 2 :
                {
                    me.state.change(STATE_TRIVIA);
                    return true;
                    break;
                }
                case 3 :
                {
                    me.state.change(STATE_HOMEWORK);
                    return true;
                }
            }
        }
    });


var backToMenu = me.GUI_Object.extend(
    {
        init:function (x, y)
        {
            var settings = {};
            settings.image = "new_back_button";
            settings.spritewidth = 240;
            settings.spriteheight = 136;
            // super constructor
            this._super(me.GUI_Object, "init", [x, y, settings]);
            // define the object z order
            this.z = 4;
        },

        // output something in the console
        // when the object is clicked
        onClick:function (event)
        {
            console.log("clicked back to menu!");
            if(game.data.sound){
                if(me.state.isCurrent(STATE_SHOP)){
                    me.audio.play("scroll", false, null, game.data.soundVolume);
                }else{
                    me.audio.play("switch", false, null, game.data.soundVolume);
                }
            }
            me.state.change(me.state.MENU);
        }
    });

//used in HighscoreScreen to go back to menu
var backFromRankings = me.GUI_Object.extend(
    {
        init:function (x, y)
        {
            var settings = {};
            settings.image = "back_button_ink";
            settings.spritewidth = 185;
            settings.spriteheight = 150;
            // super constructor
            this._super(me.GUI_Object, "init", [x, y, settings]);
            // define the object z order
            this.z = 4;
        },

        // output something in the console
        // when the object is clicked
        onClick:function (event)
        {
            console.log("clicked back to menu!");
            if(game.data.sound){
                me.audio.play("page", false, null, game.data.soundVolume);
            }
            me.state.change(me.state.MENU);
        }
    });

var backToTheLab = me.GUI_Object.extend(
    {
        init:function (x, y)
        {
            var settings = {};
            settings.image = "new_back_button";
            settings.spritewidth = 240;
            settings.spriteheight = 136;
            // super constructor
            this._super(me.GUI_Object, "init", [x, y, settings]);
            // define the object z order
            this.z = 4;
        },

        // output something in the console
        // when the object is clicked
        onClick:function (event)
        {
            console.log("clicked bac to lab!");
            if(game.data.sound){
                me.audio.play("switch", false, null, game.data.soundVolume);
            }
            me.state.change(me.state.READY);
        }
    });

var upgrade = me.GUI_Object.extend(
    {
        init:function (x, y, name, id)
        {
            var settings = {};
            settings.image = "spinning_scrolls";
            settings.spritewidth = 32;
            settings.spriteheight = 32;
            // super constructor
            this._super(me.GUI_Object, "init", [x, y, settings]);
            // define the object z order
            this.z = 5;
            this.id = id;
            this.name = name;
        },

        // output something in the console
        // when the object is clicked
        onClick:function (event)
        {
            game.task.potionId = this.id;
            game.task.kind = 1;
            game.task.name = this.name;
            console.log("Upgrade: " + game.task.potionId, "Name: " + game.task.name , "Result" + game.scroll.enchantments[game.task.potionId].name)
            me.state.change(STATE_TASK);
        }
    });

//tutorial Buttons

var skipTutorial = me.GUI_Object.extend(
    {
        init:function (x, y)
        {
            var settings = {};
            settings.image = "skip_all";
            settings.spritewidth = 214;
            settings.spriteheight = 63;
            // super constructor
            this._super(me.GUI_Object, "init", [x, y, settings]);
            // define the object z order
            this.z = 4;
        },

        // output something in the console
        // when the object is clicked
        onClick:function (event)
        {

            function skip(xmlHttpRequest) {

                console.log(xmlHttpRequest);
                //add sound name
                me.audio.pause(game.data.audio);
                game.data.text = game.data.playerStat.texts.length;
                me.state.change(me.state.READY);

            }
            ajaxNextChallengeRequest(skip)
        }
    });

var nextTutorial = me.GUI_Object.extend(
    {
        init:function (x, y, state, trigger)
        {
            var settings = {};
            this.state = state;
            this.trigger = trigger;
            settings.image = "skip_one";
            settings.spritewidth = 214;
            settings.spriteheight = 63;
            // super constructor
            this._super(me.GUI_Object, "init", [x, y, settings]);
            // define the object z order
            this.z = 4;
        },

        // output something in the console
        // when the object is clicked
        onClick:function (event)
        {
            me.audio.pause(game.data.audio);
            if(this.trigger){
                me.state.change(this.state);
                game.data.text++;
            }else{
                if(!game.data.wait){
                    game.data.wait = true;
                    game.data.text++;
                }
            }
        }
    });

var backFromLegend = me.GUI_Object.extend(
    {
        init:function (x, y)
        {
            var settings = {};
            settings.image = "back_button_ink";
            settings.spritewidth = 185;
            settings.spriteheight = 150;
            // super constructor
            this._super(me.GUI_Object, "init", [x, y, settings]);
            // define the object z order
            this.z = 4;
        },

        // output something in the console
        // when the object is clicked
        onClick:function (event)
        {
            if(game.data.sound){
                me.audio.play("page", false, null, game.data.soundVolume);
            }
            me.state.change(me.state.READY);
        }
    });


var tutorialButton = me.GUI_Object.extend(
    {
        init:function (x, y, image, width, height,to)
        {
            var settings = {};
            settings.image = image;
            settings.spritewidth = width;
            settings.spriteheight = height;
            this.to = to;
            // super constructor
            this._super(me.GUI_Object, "init", [x, y, settings]);
            // define the object z order
            this.z = 4;
        },

        // output something in the console
        // when the object is clicked
        onClick:function (event)
        {
            console.log("clicked!");
            if(game.data.wait) {
                game.data.wait = false;
                switch (this.to) {
                    case 0 :
                    {
                        me.state.change(STATE_TEXT);
                        break;
                    }
                    case 1 :
                    {
                        me.state.change(me.state.PLAY);
                        break;
                    }
                    case 2 :
                    {
                        me.state.change(me.state.MENU);
                        break;
                    }
                    case 3 :
                    {
                        me.state.change(STATE_COLLECTOR);
                    }
                }
            }

        }
    });


