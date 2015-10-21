/*
 *  The buttons which contain a update function.
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
            console.log(game.belt.beltSlots[this.slot], this.slot,game.belt.beltSlots);
            //cycle through the game.skins.available and find next skin available
            if(game.belt.beltSlots[this.slot] !== null){
                if(game.potion.usePotion(this.potion, this.slot)) {
                    game.belt.beltSlots[this.slot] = null;
                    this.width = 1;
                }
            } else {
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

var tutorialButton = me.GUI_Object.extend(
    {
        init:function (x, y, image, width, height, to)
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


