/**
 * a HUD container and child items
 */

game.HUD = game.HUD || {};

//this Container is used in game.playScreen
game.HUD.Container = me.Container.extend({

    init: function() {
        // call the super constructor
        this._super(me.Container, 'init');

        // persistent across level change
        this.isPersistent = true;

        // trn off floating to use screen coordinates
        this.floating = true;

        // make sure our object is always drawn second
        this.z = 100;

        // give a name
        this.name = "HUD";

        // add our child score object at the bottom right corner
        this.addChild(new game.HUD.ScoreItem(1288, 707));

        this.addChild(new game.HUD.HealthScore(140, 10));

        this.addChild(
            new me.Sprite(
                1, 655,
                me.loader.getImage('belt_left')
            ),
            2
        );
        for(var i = 0; i < game.belt.beltSlots.length; i++){
            this.addChild(
                new me.Sprite(
                    95 + 94 * i, 655,
                    me.loader.getImage('belt_middle')
                ),
                2
            );
        }
        this.addChild(
            new me.Sprite(
                95 + 94 * game.belt.beltSlots.length, 655,
                me.loader.getImage('belt_right')
            ),
            2
        );

    }
});

game.HUDII = game.HUDII || {};

//this Container is used in game.playScreen
game.HUDII.Container = me.Container.extend({

    init: function() {
        // call the super constructor
        this._super(me.Container, 'init');

        // persistent across level change
        this.isPersistent = true;

        // make sure we use screen coordinates
        this.floating = false;

        // make sure our object is always drawn first
        this.z = 222;

        // give a name
        this.name = "HUDII";

        //Add Buttons
        this.addChild(new music(64, 1));
        this.addChild(new sound(1, 1));
        this.addChild(new exit(1255, 1));

        //Add BeltSlotButtons
        //226 + 94 * i, 620
        for(var i = 0; i < game.belt.beltSlots.length; i++) {
            this.addChild(new beltSlot(110 + 94 * i, 675, game.belt.beltSlots[i], i));
        }

    }

});

//needs to be KICKED
game.HUDIII = game.HUDIII || {};

//this Container is used in game.collectorScreen.  NO!!
game.HUDIII.Container = me.Container.extend({

    init: function() {
        // call the super constructor
        this._super(me.Container, 'init');

        // persistent across level change
        this.isPersistent = true;

        // make sure we use screen coordinates
        this.floating = false;

        // make sure our object is always drawn first
        this.z = 222;

        // give a name
        this.name = "HUDIII";

        //Add BeltSlotButtons
        for(var i = 0; i < game.belt.beltSlots.length; i++) {
            this.addChild(new beltSlotBelt(126 + 94 * i, 676, game.belt.beltSlots[i], i));
        }

    }

});

/**
 * a basic HUD item to display score in Game
 */
game.HUD.ScoreItem = me.Renderable.extend( {
    /**
     * constructor
     */
    init: function(x, y) {

        // call the parent constructor
        // (size does not matter here)
        this._super(me.Renderable, 'init', [x, y, 10, 10]);

        // create a font
        this.font = new me.BitmapFont("32x32_font", 32);
        this.font.set("right");

        // local copy of the global score
        this.score = 0;
    },

    /**
     * update function
     */
    update : function (dt) {
        //update trigger
        if (this.score !== game.data.score) {
            this.score = game.data.score;
            return true;
        }
        return false;
    },

    /**
     * draw the elements
     */
    draw : function (renderer) {
        this.font.draw (renderer, game.data.score, this.pos.x, this.pos.y);
    }
});

//Used in Game
game.HUD.HealthScore = me.Renderable.extend( {
    /**
     * constructor
     */
    init: function(x, y) {

        // call the parent constructor
        // (size does not matter here)
        this._super(me.Renderable, 'init', [x, y, 10, 10]);

        // create a font
        this.font = new me.BitmapFont("32x32_font", 32);


        this.font.set("left");

        // local copy of the global score
        this.score = game.stats.health;
        this.string = "";
    },

    /**
     * update function
     */
    update : function (dt) {
        //update trigger
        if (this.score !== game.stats.hp) {
            this.score = game.stats.hp;
            return true;
        }

        this.string = "";
        for (i = 0; i < game.stats.hp; i++){
            this.string = this.string.concat("$");
        }
        return false;
    },

    /**
     * draw the elements
     */
    draw : function (renderer) {
        this.font.draw (renderer, this.string , this.pos.x, this.pos.y);
    }
});

//Used in charactersheet, Profilescreen
game.SkinFront = me.Sprite.extend({
    init: function (x,y,filename, team) {
        this.currentSkin = game.skin.currentSkin;

        //change the size of the image for the Teams
        this.switch = 64;
        if (team){
            this.switch = 84;
        }

        this._super(me.Sprite, "init", [x, y, me.loader.getImage(filename.concat("_front")), this.switch, 64]);
        this.z = 1000;
    },

    /**
     * update function
     */
    update: function (dt) {
        //update trigger
        if (this.currentSkin !== game.skin.currentSkin) {
            this.currentSkin = game.skin.currentSkin;
            return true;
        }
        return false;
    }
});

//Used in Scrollcolletion
game.Scroll = me.Sprite.extend({
    init: function (x,y,filename) {
        this._super(me.Sprite, "init", [x, y, me.loader.getImage(filename), 32, 32]);
        this.z = 100;
    }
});

//Used in charactersheet, shows name and stats of the choosen skin
game.HUD.SkinName = me.Renderable.extend( {
    /**
     * constructor
     */
    init: function(x, y, name) {

        // call the parent constructor
        // (size does not matter here)
        this._super(me.Renderable, 'init', [x, y, 10, 10]);
        this.name = name;

        // create a font
        this.font = new me.Font("Trajan_Pro_Regular", 40, "black", "middle");

        // local copy of the
        this.currentSkin = 0;
    },

    /**
     * update function
     */
    update : function (dt) {
        //update trigger
        if (this.currentSkin !== game.skin.currentSkin) {
            this.currentSkin = game.skin.currentSkin;
            return true;
        }
        return false;
    },

    /**
     * draw the elements
     */
    draw : function (renderer) {
        renderer.drawFont(this.font, this.name.substring(0,20), this.pos.x, this.pos.y+100);
        renderer.drawFont(this.font, "Health:  " + game.stats.health , this.pos.x , this.pos.y+200);
        renderer.drawFont(this.font, "Speed:     " + game.stats.speed , this.pos.x , this.pos.y+300);
        renderer.drawFont(this.font, "Jump:      " + game.stats.jump , this.pos.x , this.pos.y+400);
        renderer.drawFont(this.font, "Defense: " + game.stats.defense , this.pos.x , this.pos.y+500);
    }
});

//Used in Settings
game.HUD.SettingsElements = me.Renderable.extend( {
    /**
     * constructor
     */
    init: function(x, y) {

        // call the parent constructor
        // (size does not matter here)
        this._super(me.Renderable, 'init', [x, y, 10, 10]);

        // create a font
        this.font = new me.Font("Trajan_Pro_Regular", 40, "black", "middle");

        this.sound = game.data.sound;
        this.music = game.data.music;

    },

    /**
     * update function
     */
    update : function (dt) {
        //update trigger
        if ( this.sound !== game.data.sound) {
            this.sound = game.data.sound;
            return true;
        }
        if ( this.music !== game.data.music) {
            this.music = game.data.music;
            return true;
        }
        return false;
    },

    /**
     * draw the elements
     */

    draw : function (renderer) {
        if (this.music) {
            renderer.drawFont(this.font, "Music:   ON", this.pos.x + 50, this.pos.y +15);
        } else {
            renderer.drawFont(this.font, "Music:  OFF", this.pos.x + 50, this.pos.y +15);
        }
        if(this.sound){
            renderer.drawFont(this.font, "Sound:  ON", this.pos.x + 50, this.pos.y + 115);
        } else {
            renderer.drawFont(this.font, "Sound: OFF", this.pos.x + 50, this.pos.y + 115);
        }
    }
});

//Used in Shop
game.HUD.LofiCoins = me.Renderable.extend( {
    /**
     * constructor
     */
    init: function(x, y) {

        // call the parent constructor
        // (size does not matter here)
        this._super(me.Renderable, 'init', [x, y, 10, 10]);

        this.font = new me.Font("Trajan_Pro_Regular", 45, "white", "right");
    },

    /**
     * draw the elements
     */
    draw : function (renderer) {
        renderer.drawFont(this.font, game.data.lofiCoins, this.pos.x, this.pos.y);
    }
});

//Used in Beltscreen, shows the amount of potions already created
game.HUD.PotionAmount = me.Renderable.extend( {
    /**
     * constructor
     */
    init: function(x, y) {

        // call the parent constructor
        // (size does not matter here)
        this._super(me.Renderable, 'init', [x, y, 10, 10]);

        // create a font
        this.font = new me.Font("Trajan_Pro_Regular", 32, "white", "middle");
    },

    /**renderer.drawFont(this.font, "USERNAME: " + this.user_object.username.toUpperCase(), this.pos.x + 760, this.pos.y);
     * draw the elements
     */
    draw : function (renderer) {
        for(var i = 1; i < 6; i++) {
            if (game.potion.potions[i].available){
                renderer.drawFont(this.font, "#"+game.potion.potions[i].amount , -17 + 105 * i, 265);
            }
            if (game.potion.potions[i + 5].available){
                renderer.drawFont(this.font, "#"+game.potion.potions[i + 5].amount ,570 + 105 * i, 265);
            }
            if (game.potion.potions[i + 10].available){
                renderer.drawFont(this.font, "#"+game.potion.potions[i + 10].amount , -17 + 105 * i, 460);
            }
            if (game.potion.potions[i + 15].available){
                renderer.drawFont(this.font, "#"+game.potion.potions[i + 15].amount ,570 + 105 * i, 460);
            }
        }

    }
});

game.HUD.Buy = me.Renderable.extend( {
    /**
     * constructor
     */
    init: function(x, y) {

        // call the parent constructor
        // (size does not matter here)
        this._super(me.Renderable, 'init', [x, y, 10, 10]);
        this.res = game.data.shop[game.data.shopId].desc.split("\\n");


        // create a font
        this.font = new me.Font("Trajan_Pro_Regular", 30, "black", "middle");
    },

    /**
     * draw the elements
     */
    draw : function (renderer) {
        renderer.drawFont(this.font, "Name: " + game.data.shop[game.data.shopId].name, this.pos.x - 100, this.pos.y - 50);
        renderer.drawFont(this.font, "Price: " + game.data.shop[game.data.shopId].price, this.pos.x - 100, this.pos.y - 100);
        for(var i = 0; i < this.res.length; i++) {
            renderer.drawFont(this.font,  this.res[i]   ,   this.pos.x  -  200, this.pos.y + 50 * i + 30);
        }

    }
});

game.HUD.BuyBelt = me.Renderable.extend( {
    /**
     * constructor
     */
    init: function(x, y) {

        // call the parent constructor
        // (size does not matter here)
        this._super(me.Renderable, 'init', [x, y, 10, 10]);


        // create a font
        this.font = new me.Font("Trajan_Pro_Regular", 30, "black", "middle");
    },

    /**
     * draw the elements
     */
    draw : function (renderer) {
        renderer.drawFont(this.font, "Name: " + game.data.beltShop[game.data.shopId].name, this.pos.x - 100, this.pos.y - 50);
        renderer.drawFont(this.font, "Price: " + game.data.beltShop[game.data.shopId].price, this.pos.x - 100, this.pos.y - 100);
        renderer.drawFont(this.font,  "Get a brand new beltslot."   ,   this.pos.x  -  100, this.pos.y + 30);


    }
});

game.HUD.Text = me.Renderable.extend( {
    /**
     * constructor
     */
    init: function(x, y, nextText) {

        // call the parent constructor
        // (size does not matter here)
        this._super(me.Renderable, 'init', [x, y, 10, 10]);
        this.res = nextText.split("<br>");

        // create a font
        this.font = new me.Font("Tahoma", 24, "white", "middle");
        console.log(this.font);
    },

    /**
     * draw the elements
     */
    draw : function (renderer) {
        for(var i = 0; i < this.res.length; i++) {
            renderer.drawFont(this.font,  this.res[i], this.pos.x, this.pos.y + 30 * i);
        }

    }
});

game.HUD.Homework = me.Renderable.extend( {
    /**
     * constructor
     */
    init: function(x, y) {

        // call the parent constructor
        // (size does not matter here)
        this._super(me.Renderable, 'init', [x, y, 10, 10]);

        // create a font
        this.font = new me.Font("Trajan_Pro_Regular", 50, "white", "right");

    }
});

/**Two badly placed functions
 * which are useed by the HUDs
 */
/**
 * Used to get the name of the image according to the potion
 * should be kicked and the name of the images change according
 * to the names used in the Database
 * @param potion
 * @returns the name of the image
 */
function getPotionName(potion){
    var rank = 0;
    var kind = 0;
    var res = 0;
    if(potion == null){
        return "IS_EMPTY"
    }else {
        switch (potion.type) {
            case 0:
                kind = "HEALTHPOTION";
                break;
            case 1:
                kind = "SPEEDPOTION";
                break;
            case 2:
                kind = "JUMPPOTION";
                break;
            case 3:
                kind = "DEFENSEPOTION";
        }

        switch (potion.power) {
            case 1 :
                rank = "WEAK_";
                break;
            case 2 :
                rank = "MODERATE_";
                break;
            case 3 :
                rank = "GREAT_";
                break;
            case 4 :
                rank = "POWERFUL_";
                break;
            case 5 :
                rank = "INSANE_";
        }
        res = rank.toString().concat(kind);
        return res;
    }
}
/**
 * Used in Profile and Highscore to convert seconds into
 * @param seconds
 * @returns a string of the structure
 *          years:month:days:hours:minutes:seconds
 */
function convertTime(seconds){
    var minutes = Math.floor(seconds/60);
    var hours =  Math.floor(minutes/60);
    var days = Math.floor(hours/24);
    var month = Math.floor(days/30);
    var years = Math.floor(month/12);
    var second = seconds%60;
    if(second < 10){
        second = "0"+ second;
    }
    minutes = minutes % 60;
    if(minutes < 10){
        minutes = "0"+ minutes;
    }
    hours = hours % 24;
    days = days % 30;
    month = month % 12;
    var res = "";
    if(years >= 1){
        var a = years + ":";
        res = "Cheater!!";

    }
    if(month >= 1){
        var m = month + ":";
        if(month < 10){
            m = "0"+ m
        }
        res = res.concat(m);
    }
    if(days >= 1){
        var d = days + ":";
        if(days < 10){
            d = "0"+ d
        }
        res = res.concat(d);
    }
    if(hours >= 1){
        var h = hours + ":";
        if(hours < 10){
            h = "0"+ h
        }
        res = res.concat(h);
    }
    var start = minutes + ":" + second
    res = res.concat(start);
    return res
}

game.HUD.OverlayAlert = me.Renderable.extend( {
    /**
     * constructor
     */
    init: function(x, y, alertText) {

        // call the parent constructor
        // (size does not matter here)
        this._super(me.Renderable, 'init', [x, y, 10, 10]);

        // create our font
        this.font = new me.Font("Trajan_Pro_Regular", 40, "black", "middle");

        // save Alert Text to display in this Object
        this.alertText = alertText;
    },

    /**
     * draw the AlertText given to this Object
     */
    draw : function (renderer) {
        renderer.drawFont(this.font, this.alertText, this.pos.x, this.pos.y);
    }
});