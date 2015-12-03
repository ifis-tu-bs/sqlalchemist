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
    if(potion === null){
        return "IS_EMPTY";
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
            m = "0"+ m;
        }
        res = res.concat(m);
    }
    if(days >= 1){
        var d = days + ":";
        if(days < 10){
            d = "0"+ d;
        }
        res = res.concat(d);
    }
    if(hours >= 1){
        var h = hours + ":";
        if(hours < 10){
            h = "0"+ h;
        }
        res = res.concat(h);
    }
    var start = minutes + ":" + second;
    res = res.concat(start);
    return res;
}
