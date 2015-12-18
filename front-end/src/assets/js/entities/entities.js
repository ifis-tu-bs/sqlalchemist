/**
 * The entities describe all the element which are used in the game.
 * They are added to each map with the tiled-editor.
 *
 */

//-----------------------------------------------------------------------------
/**
 * Our own LevelEntity
 * We need Random next maps, so we instanciate our own LevelEntity.
 * This one also changes the current background music randomly,
 * according the kind of the map.
 */
game.LevelEntity = me.LevelEntity.extend({

    /** @override */
    init : function (x, y, settings) {

        me.Entity.prototype.init.apply(this, [x, y, settings]);

        this.nextlevel = this.findNextLevel(Math.floor(game.persistent.depth / 5));

        console.log(game.persistent.depth % 5);

        //Play the background music
        var title;
        if (game.persistent.depth % 5 === 2 && game.persistent.depth !== 52) {
            title = "level".concat(Math.ceil(Math.random() * 7));
        } else if (game.persistent.depth % 5 === 1 && game.data.score !== 0 ) {
            title = "boss".concat(Math.ceil(Math.random() * 5));
        } else if (game.persistent.depth === 52) {
            title = "credits";
        }
        if( title !== undefined){
            game.data.recentTitle = title;
            if(game.data.music && game.persistent.depth !== 1) {
                me.audio.stopTrack();
                me.audio.playTrack(title, game.data.musicVolume);
            }
        }


        //fading settings
        this.fade = "#333333";
        this.duration = settings.duration;
        this.fading = false;

        this.name = "levelEntity";

        // a temp variable
        this.gotolevel = this.nextlevel;

        this.body.collisionType = me.collision.types.ACTION_OBJECT;
    },

    findNextLevel : function(difficulty) {

        var next;
        //Increase the depth for every level
        game.persistent.depth++;

        console.log(game.persistent.depth);
        //Every fifth map shall be a boooooosss map
        if (game.persistent.depth % 5 === 0) {
            //Return the bosslevel for current difficulty
            return (difficulty.toString() + "bossmap");
        }
        else if(game.persistent.depth === 51){
            return( "credits");
        }
        else {

            //give a random number between 0 and 5 as map id called: next
            do {
                next = Math.floor(Math.random() * 6);
            } while (next === game.persistent.currentLevel[0] || next === game.persistent.currentLevel[1] || next === game.persistent.currentLevel[2]);


            //Save Level Persistantly
            game.persistent.currentLevel[2] = game.persistent.currentLevel[1];
            game.persistent.currentLevel[1] = game.persistent.currentLevel[0];
            game.persistent.currentLevel[0] = next;

            //Return our map name
            console.log("NEXT: ",next);
            return (difficulty.toString() + "map" + next.toString());
        }
    }
});
/**
 * Player Entity
 */
game.PlayerEntity = me.Entity.extend({

    /**
     * constructor
     */
    init:function (x, y, settings) {
        //set name from sprite
        settings.image = game.data.sprite;
        // call the constructor
        this._super(me.Entity, 'init', [x, y, settings]);

        // set the default horizontal & vertical speed (accel vector)
        this.body.setVelocity(game.stats.speed + game.stats.addspeed, game.stats.jump + game.stats.addjump);

        // set the display to follow our position on both axis
        me.game.viewport.follow(this.pos , me.game.viewport.AXIS.BOTH);

        // set the Deadzone to zero so the player is always in the middle of the canvas
        me.game.viewport.setDeadzone(0, 0);

        // ensure the player is updated even when outside of the viewport
        this.alwaysUpdate = true;

        // boolean if player ist hurt
        this.hurt = false;

        // define a basic walking animation (using all frames)
        this.renderable.addAnimation("walk",  [0, 1, 2]);
        // set the walking animation as default
        this.renderable.setCurrentAnimation("walk");
    },

    /**
     * update the entity
     */
    update : function (dt) {

        // update the entity velocity
        this.body.vel.x += this.body.accel.x * me.timer.tick;
        this.body.setVelocity(game.stats.speed + game.stats.addspeed, game.stats.jump + game.stats.addjump);

        // eventlistener for the key named jump set in screen/play.js
        if (me.input.isKeyPressed('jump')) {
            // make sure we are not already jumping or falling
            if (!this.body.jumping && !this.body.falling) {
                // set current vel to the maximum defined value
                // gravity will then do the rest
                this.body.vel.y = -this.body.maxVel.y * me.timer.tick;
                // set the jumping flag
                this.body.jumping = true;
                // play some audio
                if(game.data.sound){
                    me.audio.play("jump", false, null, game.data.soundVolume);
                }
            }

        }

        // apply physics to the body (this moves the entity)
        this.body.update(dt);

        // handle collisions against other shapes
        me.collision.check(this);

        // return true if we moved or if the renderable was updated
        return (this._super(me.Entity, 'update', [dt]) || this.body.vel.x !== 0 || this.body.vel.y !== 0);
    },

    /**
     * Collision handler:
     *
     * Collisions are triggered frequently while Hitboxes overlap.
     * Hence there are variables set on a timeout preventing immediate retrigger.
     *
     */
    onCollision : function (response, other) {
        switch (response.b.body.collisionType) {
            case me.collision.types.WORLD_SHAPE:
                // Simulate a platform object
                if (other.type === "platform") {
                    if (this.body.falling &&
                        !me.input.isKeyPressed('down') &&
                            // Shortest overlap would move the player upward
                        (response.overlapV.y > 0) &&
                            // The velocity is reasonably fast enough to have penetrated to the overlap depth
                        (~~this.body.vel.y >= ~~response.overlapV.y)
                    ) {
                        // Disable collision on the x axis
                        response.overlapV.x = 0;
                        // Repond to the platform (it is solid)
                        return true;
                    }
                    // Do not respond to the platform (pass through)
                    return false;
                }
                break;

            case me.collision.types.ENEMY_OBJECT:
                if ((response.overlapV.y>0) && !this.body.jumping) {
                    // bounce (force jump)
                    this.body.falling = false;
                    this.body.vel.y = - this.body.maxVel.y * me.timer.tick;
                    // set the jumping flag
                    this.body.jumping = true;
                    // play some audio
                    if(game.data.sound){
                        me.audio.play("stomp", false, null, game.data.soundVolume);
                    }

                }
                else if(!self.hurt) {
                    // let's flicker in case we touched an enemy
                    self.hurt = true;

                    //Damage to take once only
                    var damage = Math.ceil(game.persistent.depth / 5) - game.stats.defense - game.stats.adddefense + game.persistent.damage;
                    if(damage > 0){
                        game.stats.hp -= (damage + (Math.floor(Math.random() * 1,13)));
                        this.renderable.flicker(1500);
                        //play audio
                        if(game.data.sound){
                            if(Math.floor(Math.random() * 4) === 3){
                                me.audio.play("thathurts", false, null, game.data.soundVolume);
                            }else{
                                me.audio.play("auh", false, null, game.data.soundVolume);
                            }
                        }
                    }
                    // change state if hitpoints drop to zero or below
                    if (game.stats.hp < 1 ){
                        setTimeout(function(){
                            me.state.change(me.state.GAMEOVER);
                        }, 300);
                    }
                    // after a given time it sets hurt back to false to allow another triggering
                    setTimeout(function(){
                        self.hurt = false;
                    }, 1500);
                }
                return false;


            default:
                // Do not respond to other objects (e.g. coins)
                return false;
        }

        // Make the object solid
        return true;
    }
});

/*----------------
 a Coin entity
 ----------------- */
game.CoinEntity = me.CollectableEntity.extend({
    // extending the init function is not mandatory
    // unless you need to add some extra initialization
    init: function(x, y, settings) {
        // call the parent constructor
        this._super(me.CollectableEntity, 'init', [x, y , settings]);

    },

    // this function is called by the engine, when
    // an object is touched by something (here collected)
    onCollision : function () {
        // do something when collected

        // give some score
        game.data.score += Math.ceil(game.persistent.depth/5);

        // play a "coin collected" sound
        if(game.data.sound){
            me.audio.play("cling", false, null, game.data.soundVolume);
        }

        // make sure it cannot be collected "again"
        this.body.setCollisionMask(me.collision.types.NO_OBJECT);

        // remove it
        me.game.world.removeChild(this);
    }
});



/* --------------------------
 a jumping and running enemy Entity
 ------------------------ */
game.EnemyEntity = me.Entity.extend({
    init: function(x, y, settings) {

        // save the area size defined in Tiled
        var width = settings.width;
        var height = settings.height;

        // adjust the size setting information to match the sprite size
        // so that the entity object is created with the right size
        settings.width = settings.spritewidth;
        settings.height = settings.spriteheight;

        //reads the stats of the enemy
        var jumppower = this.jumppower = settings.jump;
        var runpower  = this.runpower = settings.speed;
        game.persistent.damage = this.attack = settings.attack;

        // call the parent constructor
        this._super(me.Entity, 'init', [x, y , settings]);

        // set start/end position based on the initial area size
        y = this.pos.y;
        this.startY = y;
        this.endY   = y + height - settings.spriteheight;
        this.pos.y  = y + height - settings.spriteheight;

        // set start/end position based on the initial area size
        x = this.pos.x;
        this.startX = x;
        this.endX   = x + width - settings.spritewidth;
        this.pos.x  = x + width - settings.spritewidth;

        // manually update the entity bounds as we manually change the position
        this.updateBounds();

        // to remember which side we were jumping
        this.jump = false;

        // to remember which side we were walking
        this.walkLeft = false;

        // walking & jumping speed
        this.body.setVelocity( runpower, jumppower);

    },

    // manage the enemy movement
    update: function(dt) {

        if (this.alive) {
            if (this.jump && this.pos.y <= this.startY) {
                this.jump = false;
            } else if (!this.jump && this.pos.y >= this.endY) {
                this.jump = true;
            }
            // make it jump
            //this.body.vel.y = -this.body.maxVel.y * me.timer.tick;
            this.body.vel.y += (this.jump) ? -this.body.maxVel.y * me.timer.tick : this.body.maxVel.y * me.timer.tick;

        } else {
            this.body.vel.x = 0;
        }

        if (this.alive) {
            if (this.walkLeft && this.pos.x <= this.startX) {
                this.walkLeft = false;
            } else if (!this.walkLeft && this.pos.x >= this.endX) {
                this.walkLeft = true;
            }
            // make it walk
            this.renderable.flipX(this.walkLeft);
            this.body.vel.x += (this.walkLeft) ? -this.body.accel.x * me.timer.tick : this.body.accel.x * me.timer.tick;

        } else {
            this.body.vel.x = 0;
        }

        // update the body movement
        this.body.update(dt);

        // return true if we moved or if the renderable was updated
        return (this._super(me.Entity, 'update', [dt]) || this.body.vel.y !== 0 || this.body.vel.x !== 0);
    }
});


/*ScrollEntities are defined in the TMX with the
 *Parameter: typ(stat or potion), spritewidth, image, number
 *
 * The Entities willl only appear once and saved in game.scroll.. when collected
 * The Srcolls be set to invisibel
 */
game.ScrollEntity = me.CollectableEntity.extend({
    // extending the init function is not mandatory
    // unless you need to add some extra initialization
    init: function(x, y, settings) {
        //this variable are set by the tmx and tag the scroll
        this.scrollIndex = settings.scrollIndex;
        this.type = settings.type;

        //does remove scrolles that have already been collected
        if (settings.type === "enchantment") {
            if(game.scroll.enchantments[this.scrollIndex - 20].available){
                settings.image = "blank";

                me.game.world.removeChild(this);
            }
        } else if( settings.type === "potion") {
            if(game.potion.potions[this.scrollIndex + 1].available){
                settings.image = "blank";

                me.game.world.removeChild(this);
            }
        }

        this._super(me.CollectableEntity, 'init', [x, y , settings]);
    },


    // this function is called by the engine, when
    // an object is touched by something (here collected)
    onCollision : function () {
        console.log("Scroll collision" + this.scrollIndex);
        // sets the scroll to available, writes the scroll name down and decreases the scroll-limit
        if(this.type === "enchantment" && 0 < game.data.scollLimit - game.data.collectedScrolls && !game.scroll.enchantments[this.scrollIndex - 20].available){
            game.data.scrolls[game.data.collectedScrolls] = this.scrollIndex;
            game.data.collectedScrolls ++;
            if(game.data.sound){
                me.audio.play("enchantment",false, null, game.data.soundVolume);
            }
            console.log("collected enchantment: " + this.scrollIndex, "at: " +(game.data.collectedScrolls), "result: " + game.data.scrolls[game.data.collectedScrolls -1]);

        }else if(this.type === "potion" && 0 < game.data.scollLimit - game.data.collectedScrolls && !game.potion.potions[this.scrollIndex + 1].available) {
            game.data.scrolls[game.data.collectedScrolls] = this.scrollIndex;
            game.data.collectedScrolls ++;
            if(game.data.sound){
                me.audio.play("potion", false, null, game.data.soundVolume);
            }
            console.log("potion: " + this.scrollIndex, "at: " +(game.data.collectedScrolls), "result: " + game.data.scrolls[game.data.collectedScrolls -1]);
        }

        // remove it
        me.game.world.removeChild(this);
    }
});


/*----------------
 a killing entity
 ----------------- */
game.SpikeEntity = me.CollectableEntity.extend({
    // extending the init function is not mandatory
    // unless you need to add some extra initialization
    init: function(x, y, settings) {
        // call the parent constructor
        this._super(me.CollectableEntity, 'init', [x, y , settings]);

    },

    // this function is called by the engine, when
    // an object is touched by something (here collected)
    onCollision : function () {
        // do something when collected

        me.state.change(me.state.GAMEOVER);
        me.game.world.removeChild(this);
    }
});

/*----------------
 a damaging entity
 ----------------- */
game.FireEntity = me.CollectableEntity.extend({
    // extending the init function is not mandatory
    // unless you need to add some extra initialization
    init: function(x, y, settings) {
        // call the parent constructor
        this._super(me.CollectableEntity, 'init', [x, y , settings]);
        console.log("Init Fire: ", game.stats.hp);

    },

    // this function is called by the engine, when
    // an object is touched by something (here collected)
    onCollision : function () {
        // do something when collected

        // lose health
        game.stats.hp = game.stats.hp - 1 ;

        if (game.stats.hp < 1 ){
            setTimeout(function(){
                me.state.change(me.state.GAMEOVER);
            }, 100);
        }

        // play a "fire" sound
        if(game.data.sound){
            me.audio.play("fire", false, null, game.data.soundVolume);
        }

        // make sure it cannot be collected "again"
        this.body.setCollisionMask(me.collision.types.NO_OBJECT);

        // remove it
        me.game.world.removeChild(this);
    }
});

/*----------------
 a buffinging entity
 ----------------- */
game.BuffEntity = me.CollectableEntity.extend({
    // extending the init function is not mandatory
    // unless you need to add some extra initialization
    init: function(x, y, settings) {
        this.speed = settings.speed;
        this.jump = settings.jump;
        this.defense = settings.defense;
        this.health = settings.health;
        this.time = settings.time;

        // call the parent constructor
        this._super(me.CollectableEntity, 'init', [x, y , settings]);

    },

    // this function is called by the engine, when
    // an object is touched by something (here collected)
    onCollision : function () {
        // do something when collected

        game.stats.hp = game.stats.hp + this.health;
        game.stats.addspeed = this.speed;
        game.stats.addjump = this.jump;
        game.stats.adddefense = this.defense;

        setTimeout(function () {
            game.stats.addspeed = 0;
            game.stats.addjump = 0;
            game.stats.adddefense = 0;
        }, this.time);
        me.game.world.removeChild(this);
    }
});
