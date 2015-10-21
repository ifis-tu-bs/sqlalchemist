game.TextScreen = me.ScreenObject.extend({

    /**
     *  action to perform on state change
     */
    onResetEvent : function() {
        me.audio.stopTrack();
        if(game.persistent.boss){
            game.persistent.boss = false;
            game.data.text = 0;
        }
        var image = "lab_screen";
        var state = STATE_TEXT;
        var change = true;
        var skip = false;
        var y    = 280;
        var x    = 240;

        switch(game.data.text) {
            case 4 :
            {
                console.log("we entered: " + game.data.text);
                me.game.world.addChild(new tutorialButton(220, 66, "lab_mirror", 153, 210, 0));
                change = false;
                y = 290;
                break;
            }
            case 5 :
            {
                console.log("we entered: " + game.data.text);
                image = "sheet_screen";
                state = STATE_SHEET;
                x = 385;
                break;
            }
            case 6 :
            {
                console.log("we entered: " + game.data.text);
                me.game.world.addChild(new tutorialButton(422, 111, "lab_dungen_door", 223, 382, 1));
                change = false;
                y = 505;
                break;
            }
            case 7 :
            {
                console.log("we entered: " + game.data.text);
                console.log(game.potion.potions[16],game.data.scrolls);
                if(game.data.scrolls.length === 0 ){
                    console.log("AGAIN THE DUNGEON");
                    game.data.text =6;
                    me.state.change(state);
                    skip = true;
                }else{
                    image = "game_over_screen";
                    state = me.state.GAMEOVER;
                }
                break;
            }
            case 8 :
            {
                console.log("we entered: " + game.data.text);
                me.game.world.addChild(new tutorialButton(98, 590, "lab_belt", 507, 430, 0));
                y = 200;
                change = false;
                break;
            }
            case 9 :
            {
                console.log("we entered: " + game.data.text);
                image = "belt_screen";
                state = STATE_BELT;
                break;
            }
            case 10 :
            {

                console.log("we entered: " + game.data.text);
                console.log(game.potion.potions[16]);
                console.log(game.belt.beltSlots[0] + "give me null");

                if(game.potion.potions[16].amount === 0 && game.belt.beltSlots[0] === null  ){
                    console.log("Potion nicht gebraut");
                    game.data.text = 9;
                    me.state.change(state);
                    skip = true;
                }else if(game.belt.beltSlots[0] !== null) {
                }else{
                        image = "belt_screen";
                        state = STATE_BELT;
                }
                break;
            }
            case 11 :
            {
                console.log("we entered: !!!!!!!!!" + game.data.text);
                console.log(game.belt.beltSlots[0].id);
                if(game.belt.beltSlots[0] === null){
                    console.log("Potion nicht attached");
                    game.data.text = 10;
                    me.state.change(state);
                    skip = true;
                }else {
                    console.log("drin!!!!!!!!");
                    me.game.world.addChild(new tutorialButton(421, 111, "lab_dungen_door", 223, 382, 1));
                    y = 505;
                    change = false;
                }

                break;
            }
            case 12 :
            {

                console.log("we entered: " + game.data.text);
                if(game.belt.beltSlots[0] === null){
                    console.log("AGAIN THE 10 POTION");
                    game.data.text =10;
                    me.state.change(STATE_BELT);
                    skip = true;
                }else{
                    me.game.world.addChild(new tutorialButton(421, 111, "lab_dungen_door", 223, 382, 1));
                    y = 505;
                    change = false;
                }
                break;
            }
            case 13 :
            {
                console.log("we entered: " + game.data.text);
                me.game.world.addChild(new tutorialButton(1103, 76, "lab_fat_lady", 193, 332, 2));
                me.game.world.addChild(new tutorialButton(781, 528, "lab_scrolls", 268, 114, 3));
                x = 140;
                change = false;
            }
        }

        if(!skip) {
            console.log("leave Switch");
            // lab_screen
            me.game.world.addChild(
                new me.Sprite(
                    0, 0,
                    me.loader.getImage(image)
                ),
                1
            );


            me.game.world.addChild(
                new me.Sprite(
                    0, 0,
                    me.loader.getImage('faded_lab_screen')
                ),
                2
            );

            me.game.world.addChild(new game.HUD.Text(x, y, game.data.playerStat.texts[game.data.text].text), 5);
            if (game.data.playerStat.isTutorial) {
                me.game.world.addChild(new skipTutorial(1050, 20));
                me.game.world.addChild(new nextTutorial(1050, 680, state, change));
            }


            function onend() {
                if (game.data.text !== game.data.playerStat.texts.length - 1) {
                    game.data.text++;
                    console.log("next: " + game.data.text);

                    if (change) {
                        me.state.change(state);
                    } else {
                        game.data.wait = true;
                    }
                } else {
                    me.state.change(me.state.READY);
                }
            }

            if (game.data.playerStat.isTutorial) {
                var audio = "take".concat(game.data.text + 1);
            } else {
                var audio = "bosstext".concat(Math.floor((game.persistent.depth - 1) / 5));
            }

            game.data.audio = audio;
            console.log(audio);
            me.audio.play(audio, false, onend, 1);
        }


    }
});

