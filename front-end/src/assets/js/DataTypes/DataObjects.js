/**
 * Created by Soeren on 06.05.2015.
 */

/**
 * Skin Class: Holding a Skin
 * @param available Availability of that Skin
 * @param name Name of that Skin
 * @constructor
 */
function Skin(available, name, id) {
    this.available = available;
    this.name = name;
}

/*
 * Object to hold all information for the Skins
 */
game.skin = {};


//Skins Array (Contains Skin Objects)
game.skin.skins = [];

//Skin index to point to current Skin
game.skin.currentSkin = 0;

//Load Skins into game.skin.skins (INPUT)
for (var i = 0; i <= 60; i++) {
    game.skin.skins[i] = new Skin(0, "default");
}


/**
 * Potion Class: Holding a Potion
 * @param type Type of the Potion
 * @param power Power of the Potion
 * @param amount Amount of the Potion not used yet
 * @param available Availibility of the Potion (Scroll found yet?)
 * @constructor
 */
function Potion(type, power, amount, available,id,name) {
    this.type = type;
    this.power = power;
    this.amount = amount;
    this.available = available;
    this.id = id;
    this.name = name;
}

/*
 * Obect to hold all information for the Potions
 */
game.potion = {};

game.potion.usePotion = function(potion, slot) {
    switch(potion.type) {
        case 0 :
        {
            if (game.stats.health - game.stats.hp >= potion.power) {
                game.stats.hp += potion.power;
                if(game.data.sound){
                    me.audio.play("healthpotion", false, null, game.data.soundVolume);
                }
                ajaxSendProfileUsedRequest((slot + 1), deletePotion);
                return true;
            }
            break;
        }
        case 1 :
        {
            if(!game.persistent.speedOn) {
                game.persistent.speedOn = true;
                game.stats.addspeed += (potion.power) * 2;
                if(game.data.sound){
                    me.audio.play("speedpotion", false, null, game.data.soundVolume);
                }
                setTimeout(function () {
                    game.stats.addspeed = 0;
                    game.persistent.speedOn = false;
                }, 7500);
                ajaxSendProfileUsedRequest((slot + 1), deletePotion);
                return true;
            }
            break;
        }
        case 2 :
        {
            if(!game.persistent.jumpOn) {
                game.persistent.jumpOn = true;
                game.stats.addjump += (potion.power) * 3;
                if(game.data.sound){
                    me.audio.play("jumppotion", false, null, game.data.soundVolume);
                }
                setTimeout(function () {
                    game.stats.addjump = 0;
                    game.persistent.jumpOn = false;
                }, 7500);
                ajaxSendProfileUsedRequest((slot + 1), deletePotion);
                return true;
            }
            break;
        }
        case 3 : {
            if(!game.persistent.defenseOn) {
                game.persistent.defenseOn = true;
                game.stats.adddefense += (potion.power) * 2;
                if(game.data.sound){
                    me.audio.play("defensepotion", false, null, game.data.soundVolume);
                }
                setTimeout(function () {
                    game.stats.adddefense = 0;
                    game.persistent.defenseOn = false;

                }, 7500);
                ajaxSendProfileUsedRequest((slot + 1), deletePotion);
                return true;
            }
        }
    }


    function deletePotion(xmlHttpRequest){
    }

    return false;
};


//Potion Array (Contains Potion Obects)
game.potion.potions = [];

//Potion index to point to current Potion
game.potion.currentPotion = 0;

//Load Potions into game.potion.potions (INPUT)
for (var i = 0; i <= 20; i++) {
    game.potion.potions[i] = new Potion(
            "default",
            0,
            0,
            false,
            i,
            "Potionname"
    );
}

/*
 * Object to hold all information for the belt(Slots)
 */
game.belt = {};

//beltSlot Array (Contains Potion Objects)
game.belt.beltSlots = [];

//Load Potions into game.belt.beltSlots
for (var i = 0; i < 1; i++) {
    game.belt.beltSlots[i] = "default";
}

//beltSlotIndex to point to current beltSlot
game.belt.currentSlot = 0;

function Enchantment(type, difficulty, available, used, id, name) {
    this.type = type;
    this.difficulty = difficulty;
    this.available = available;
    this.used = used;
    this.id = id;
    this.name = name;

    this.setUsed = function() {
        this.used = true;
    };
}

/*
 * Object to hold all information for the Scrolls
 */
game.scroll = {};

//Scroll Array (Contains Scroll Objects)
game.scroll.enchantments = [];

//enchantmentIndex to point to current enchantment
game.scroll.currentEnchantment = 0;

//Load the scrolls into game.scroll.enchantments
for (var i = 0; i <= 61; i++) {
    game.scroll.enchantments[i] = new Enchantment(
            i,
            i,
            false,
            false,
            i,
            "default"
        );

}
