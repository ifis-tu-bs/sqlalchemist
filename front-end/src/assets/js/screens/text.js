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
        var image = "new_lab";
        var state = STATE_TEXT;
        var nextState = 0;
        var buttonImage = 0;
        var change = true;
        var skip = false;
        var y    = 36.5;
        var x    = 18.1;
        var position = [0,0,0,0];


        switch(game.data.text) {
            case 4 :
            {
                console.log("we entered: " + game.data.text);
                nextState = 0;
                buttonImage = "mirror";
                position = [12, 27.2, 16.3, 8.3];
                //me.game.world.addChild(new tutorialButton(220, 66, "lab_mirror", 153, 210, 0));
                change = false;
                y = 37.7;
                break;
            }
            case 5 :
            {
                console.log("we entered: " + game.data.text);
                image = "sheet_screen";
                state = STATE_SHEET;
                x = 29.2;
                break;
            }
            case 6 :
            {
                console.log("we entered: " + game.data.text);
                nextState = 1;
                buttonImage = "dungeon_door";
                position = [17.2, 49.6, 31.7, 14.5];
                //me.game.world.addChild(new tutorialButton(422, 111, "lab_dungen_door", 223, 382, 1));
                change = false;
                y = 65.8;
                break;
            }
            case 7 :
            {
                console.log("we entered: " + game.data.text);
                console.log(game.potion.potions[16],game.data.scrolls,game.potion.potions[16].available,!game.potion.potions[16].available && game.data.scrolls.length  === 0);
                if(!game.potion.potions[16].available && game.data.scrolls.length  === 0){
                    console.log("AGAIN THE DUNGEON");
                    game.data.text = 6;
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
                nextState = 0;
                buttonImage = "potion_belt_cut";
                position = [ 20, 13, 60, 70];
                //me.game.world.addChild(new tutorialButton(98, 590, "lab_belt", 507, 430, 0));
                y = 26;
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
                    nextState = 1;
                    buttonImage = "dungeon_door";
                    position = [17.2, 49.6, 31.7, 14.5];
                    //me.game.world.addChild(new tutorialButton(421, 111, "lab_dungen_door", 223, 382, 1));
                    y = 65.8;
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
                } else {
                    nextState = 1;
                    buttonImage = "dungeon_door";
                    position = [17.2, 49.6, 31.7, 14.5];
                    //me.game.world.addChild(new tutorialButton(421, 111, "lab_dungen_door", 223, 382, 1));
                    y = 65.8;
                    change = false;
                }
                break;
            }
            case 13 :
            {
                console.log("we entered: " + game.data.text);
                nextState = 2;
                buttonImage = "fat_lady";
                position = [14.6, 43.2, 83.5, 9.8];
                me.game.world.addChild(new tutorialButton(1103, 76, "lab_fat_lady", 193, 332, 2));
                me.game.world.addChild(new tutorialButton(781, 528, "lab_scrolls", 268, 114, 3));
                x = 10.6;
                change = false;
            }
        }

        function skipOne() {

            me.audio.pause(game.data.audio);
            if(change){
                me.state.change(state);
                game.data.text++;
            }else{
                if(!game.data.wait){
                    game.data.wait = true;
                    game.data.text++;
                }
            }
        }

        function skipAll() {

            function skip(xmlHttpRequest) {

                console.log(xmlHttpRequest);
                //add sound name
                me.audio.pause(game.data.audio);
                game.data.text = game.data.playerStat.texts.length;
                me.state.change(me.state.READY);

            }
            ajaxNextChallengeRequest(skip);
        }

        function tutorialClick() {
            console.log("clicked!");
            if(game.data.wait) {
                game.data.wait = false;
                switch (nextState) {
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

        if(!skip) {
            console.log("leave Switch");

            var BackgroundText = new game.BackgroundElement('BackgroundTextId', 100, 100, 0, 0, 'none');
             BackgroundText.setImage("assets/data/img/gui/" + image + ".png", "backgroundscreen");
             me.game.world.addChild(BackgroundText);
             $("#BackgroundTextId").fadeIn(100);


//------------------>
            if (image ==="new_lab") { 
                var runButton        = new game.BackgroundElement('runtut', 17.2, 49.6, 31.7, 14.5);
                var colButton        = new game.BackgroundElement('coltut', 20, 13, 60, 70);
                var sheetButton      = new game.BackgroundElement('sheettut', 12, 27.2, 16.3, 8.3);
                var terryButton      = new game.BackgroundElement('terrytut', 19.32, 66.15, 53.3, 10.29);
                var scrollColButton  = new game.BackgroundElement('scrollColtut', 29.7, 23.7, 17.6, 76.3);
                var backButton       = new game.BackgroundElement('backtut', 14.6, 43.2, 83.5, 9.8);
                var bookButton       = new game.BackgroundElement('booktut', 18.6, 46.5, 81.74, 53.5);
                var catButton        = new game.BackgroundElement('cattut', 9.24, 20.96, 46.89, 52.99);
            
                runButton.setImage("assets/data/img/buttons/menubuttons/dungeon_door.png", "dungeonImage");
                colButton.setImage("assets/data/img/buttons/menubuttons/scroll_collection.png", "scrollsImage");
                sheetButton.setImage("assets/data/img/buttons/menubuttons/mirror.png", "sheetImage");
                terryButton.setImage("assets/data/img/buttons/menubuttons/schrank.png", "terryImage");
                scrollColButton.setImage("assets/data/img/buttons/menubuttons/potion_belt_cut.png", "beltImage");
                backButton.setImage("assets/data/img/buttons/menubuttons/fat_lady.png", "fatLadyImage");
                bookButton.setImage("assets/data/img/buttons/menubuttons/table.png", "tableImage");
                catButton.setImage("assets/data/img/buttons/menubuttons/cat.png", "catImage");
            
                me.game.world.addChild(runButton);
                me.game.world.addChild(sheetButton);
                me.game.world.addChild(colButton);
                me.game.world.addChild(terryButton);
                me.game.world.addChild(scrollColButton);
                me.game.world.addChild(backButton);
                me.game.world.addChild(bookButton);
                me.game.world.addChild(catButton);
            }
        

             var fadeBackgroundText = new game.BackgroundElement('fadeBackgroundTextId', 100, 100, 0, 0, 'none');
            fadeBackgroundText.setImage("assets/data/img/gui/faded_lab_screen.png", "backgroundscreen");
            me.game.world.addChild(fadeBackgroundText);
            $("#fadeBackgroundTextId").fadeIn(100);

            skipAllButton      = new game.ClickableElement('skipAll', 'skip All', skipAll, 10, 6, 85, 90, 2);
            skipOneButton      = new game.ClickableElement('skipOne', 'skip One', skipOne, 10, 6, 75, 90, 2);

            textOut          = new game.TextOutputElement('text', 80, 60, x, y, 18);
            me.game.world.addChild(textOut);
            textOut.clear();
            textOut.writeHTML(game.data.playerStat.texts[game.data.text].text, 'textbody');

            console.log(buttonImage);
            if(buttonImage !== 0){
                newTutorialButton       = new game.ClickableElement('newtutorialbutton', '', tutorialClick, position[0], position[1], position[2], position[3], 1);
                newTutorialButton.setImage("assets/data/img/buttons/menubuttons/"+ buttonImage +".png", "newTutorialButton");
                me.game.world.addChild(newTutorialButton);
                newTutorialButton.display();
            }

            if (game.data.playerStat.isTutorial) {
                me.game.world.addChild(skipAllButton);
                me.game.world.addChild(skipOneButton);
                skipAllButton.display();
                skipOneButton.display();
            }


            var audio;
            if (game.data.playerStat.isTutorial) {
                audio = "take".concat(game.data.text + 1);
            } else {
                skipAllButton.hide();
                skipOneButton.hide();
                audio = "bosstext".concat(Math.floor((game.persistent.depth - 1) / 5));
            }

            game.data.audio = audio;
            console.log(audio);
            me.audio.play(audio, false, onend, 1);
        }


    }
});
