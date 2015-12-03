game.LegendScreen = me.ScreenObject.extend({

    onResetEvent: function() {
        /**
         * Create background-div and add image to it.
         */
        var backgroundLegend = new game.BackgroundElement('backgroundLegendId', 100, 100, 0, 0, 'none');
        backgroundLegend.setImage("assets/data/img/gui/ranking_screen.png", "backgroundlegend");
        me.game.world.addChild(backgroundLegend);
        $("#backgroundLegendId").fadeIn(100);

        /**
         * Create back button und make every HTML-element fade out.
         */
        this.backToLab = function () {
            $("#backgroundLegendId").fadeOut(100);
            $("#backFromLegend").fadeOut(100);
            $("#homeworks").fadeOut(100);
            $("#rating").fadeOut(100);
            $("#triviamode").fadeOut(100);
            $("#tutorial").fadeOut(100);
            $("#belt").fadeOut(100);
            $("#mirror").fadeOut(100);
            $("#door").fadeOut(100);
            $("#scrollCol").fadeOut(100);
            $("#fatLady").fadeOut(100);
            setTimeout( function() {
                me.state.change(me.state.READY);
            }, 100);
        };

        var backFromLegend = new game.ClickableElement('backFromLegend','', this.backToLab, 14.01515, 19.53125, 82, 0, 1);
        backFromLegend.setImage("assets/data/img/buttons/back_button_ink.png", "back");
        me.game.world.addChild(backFromLegend);
        $("#backFromLegend").fadeIn(100);

        /**
         * Set header and textbox for explanations
         */
        var explanationHeader = new game.TextOutputElement('explanationHeaders', 58, 9, 13, 13, 1);
        var explanation       = new game.TextOutputElement('explanation', 55, 65, 13, 24, 15);
        me.game.world.addChild(explanationHeader);
        me.game.world.addChild(explanation);
        explanationHeader.writeHTML("Explanations:");
        explanation.writeHTML("Here you can find all the information about our game you need. Just click the buttons"+
            " on the right!");


        /**
         * Ajax-call to check if user is a student. If thats true, there will be button for the explanation for the
         * homework mode.
         * @param xmlHttpRequest contains the the profile of the user.
         */
        onProfileReply = function(xmlHttpRequest) {
            var profileObject = JSON.parse(xmlHttpRequest.responseText);
            if (profileObject.student === true) {

                this.homeworkClick = function () {
                    if (game.data.sound) {
                        me.audio.play("page", false, null, game.data.soundVolume);
                    }
                    explanationHeader.clear();
                    explanationHeader.writeHTML("Homework");
                    explanation.clear();
                    explanation.writeHTML("Every week there will be a homework challenge for students. You can start it" +
                        " by clicking START. Before you finally submit your homework, you can check your statement" +
                        " first. Remember you can submit wrong answers too!");
                };

                var homeworks = new game.ClickableElement('homeworks','Homework', this.homeworkClick, 17, 4, 80, 91, 1);
                me.game.world.addChild(homeworks);
                $("#homeworks").fadeIn(100);
            }

        };
        ajaxSendProfileRequest(onProfileReply);


        this.stopDouble = function() {
            game.data.playing = false;
        };

        /**
         * Create buttons for every element from the laboratory that has to be explained and set explanation text to the
         * according box.
         */
        this.fatLadyClick = function() {
            if (game.data.sound && !game.data.playing){
                game.data.playing = true;
                me.audio.play("page", false, this.stopDouble, game.data.soundVolume);
            }
            explanationHeader.clear();
            explanationHeader.writeHTML("Fat Lady");
            explanation.clear();
            explanation.writeHTML("Perenelle Flamel was the wife of the famous alchemist, Nicolas Flamel, who created" +
                " the Philosopher's Stone. Perenelle drank the Elixir of Life which was produced by the stone, and was immortal," +
                " until the stone was destroyed. She died the following year at the"+
                " age of six hundred and fifty-eight." + "<br>" + "After her death, she became a permanent" +
                " visitor in a portrait, and the " +
                "guard of the Laboratory.");
        };
        var fatLady = new game.ClickableElement('fatLady', 'Fat Lady', this.fatLadyClick, 14, 4, 81.5, 25, 1);
        me.game.world.addChild(fatLady);
        $("#fatLady").fadeIn(100);


        this.scrollClick = function() {
            if (game.data.sound && !game.data.playing){
                game.data.playing = true;
                me.audio.play("page", false, this.stopDouble, game.data.soundVolume);
            }
            explanationHeader.clear();
            explanationHeader.writeHTML("Scroll Collection");
            explanation.clear();
            explanation.writeHTML("A white Scroll represents a recipe for an enchantment, one with a red ribbon is a recipe"+
                " for a potion. Unlike a potion, an enchantment increases the attributes of the" +
                " player permanently." + "<br>" + "The Scroll Collection shows the progress you have made during the whole story and"+
                " all levels of the Dungeon. If you collected all scrolls, used the enchantments in the" +
                " Mirror and beaten the Boss, you will skip that level.");
        };
        var scrollCol = new game.ClickableElement('scrollCol', "Scroll Collection", this.scrollClick, 16, 8, 80.7, 31, 2);
        me.game.world.addChild(scrollCol);
        $("#scrollCol").fadeIn(100);


        this.doorClick = function() {
            if (game.data.sound && !game.data.playing){
                game.data.playing = true;
                me.audio.play("page", false, this.stopDouble, game.data.soundVolume);
            }
            explanationHeader.clear();
            explanationHeader.writeHTML("Door to Dungeon");
            explanation.clear();
            explanation.writeHTML("By clicking the door you are going to start your run in the Dungeon. Down there you"  +
                " can find new scrolls you need for brewing potions. The Dungeon is devided into multiple levels"        +
                " all consisting of 5 different maps. The last stage of each level is a very difficult Bossmap. For these" +
                " maps you need your best potions." + "<br>" + "So don't forget: You have to brew a lot of potions to" +
                " get through the story!");
        };
        var door = new game.ClickableElement('door', 'Door to Dungeon', this.doorClick, 16, 8, 80.8, 40.8, 2);
        me.game.world.addChild(door);
        $("#door").fadeIn(100);


        this.mirrorClick = function() {
            if (game.data.sound && !game.data.playing){
                game.data.playing = true;
                me.audio.play("page", false, this.stopDouble, game.data.soundVolume);
            }
            explanationHeader.clear();
            explanationHeader.writeHTML("Mirror");
            explanation.clear();
            explanation.writeHTML("The magic mirror, Ecnedifnoc, reflects your inner power. Here you can change your avatar and"+
                " use the enchantments you found in the dungeon." + "<br>" + "Enchantments increase the attributes health,"+
                " speed, jump and defense for ALL your avatars." + "<br>" + "New avatars can be bought in the shop in the main menu."+
                " Every avatar has special stats, but if you want to know which, you have to buy them.");
        };
        var mirror = new game.ClickableElement('mirror', 'Mirror', this.mirrorClick, 12, 4, 82.5, 52, 1);
        me.game.world.addChild(mirror);
        $("#mirror").fadeIn(100);


        this.beltClick = function() {
            if (game.data.sound && !game.data.playing){
                game.data.playing = true;
                me.audio.play("page", false, this.stopDouble, game.data.soundVolume);
            }
            explanationHeader.clear();
            explanationHeader.writeHTML("Belt");
            explanation.clear();
            explanation.writeHTML("The belt shows all the potion recipes you have obtained so far and the number of" +
                " potions you have brewed yet." +
                " You will also see your belt which contains the potions you chose to take with you into the dungeon. By clicking on a"  +
                " potion bottle, the potion will be inserted into the first free belt slot. By clicking on the scroll" +
                " above it, you can keep more supplies coming." + "<br>" + "New belt slots can also be bought in the shop in the main menu.");
        };
        var belt = new game.ClickableElement('belt', 'Belt', this.beltClick, 8, 4, 84.5, 60, 1);
        me.game.world.addChild(belt);
        $("#belt").fadeIn(100);


        this.tutorialClick = function() {
            if (game.data.sound && !game.data.playing){
                game.data.playing = true;
                me.audio.play("page", false, this.stopDouble, game.data.soundVolume);
            }
            explanationHeader.clear();
            explanationHeader.writeHTML("Tutorial");
            explanation.clear();
            explanation.writeHTML("The tutorial is supposed to give you a small tour around the game. If you accidentally" +
                " skipped the whole tutorial and would like to revise it, you can reset the story in the settings." + 
                "<br>" +
                "Be aware, that you entire progress will be deleted!");
        };
        var tutorial = new game.ClickableElement('tutorial', 'Tutorial', this.tutorialClick, 15, 4, 81, 68, 1);
        me.game.world.addChild(tutorial);
        $("#tutorial").fadeIn(100);


        this.taskClick = function() {
            if (game.data.sound && !game.data.playing){
                game.data.playing = true;
                me.audio.play("page", false, this.stopDouble, game.data.soundVolume);
            }
            explanationHeader.clear();
            explanationHeader.writeHTML("Quiz Mode");
            explanation.clear();
            explanation.writeHTML("The Quiz-Mode represents the SQL-Trainer of the game. There you can solve" +
                " as many SQL-Statements as you like. You just have to write your answer in the existing editor and click" +
                " the submit button." + "<br>" + "If your statement is correct, you can choose between answering this statement " +
                " again or you can choose the next task. If your statement is wrong, you have to do it until " +
                " solve it, or you can simply give up by clicking the back button." + "<br>" +
                " For each solved statement you get a certain amount of coins and score. You can exchange your coins " +
                " in the shop for several characters or additional belt slots.");
        };
        var task = new game.ClickableElement('triviamode','Quiz Mode', this.taskClick, 18, 4, 79.5, 76, 1);
        me.game.world.addChild(task);
        $("#triviamode").fadeIn(100);


        this.ratingClick = function() {
            if (game.data.sound && !game.data.playing){
                game.data.playing = true;
                me.audio.play("page", false, this.stopDouble, game.data.soundVolume);
            }
            explanationHeader.clear();
            explanationHeader.writeHTML("Rating");
            explanation.clear();
            explanation.writeHTML("In the Qiuz Mode you can also rate the tasks! If you think a tasks is " +
                " either to difficult or maybe even to easy, you can rate them with a thumbs-up (the green one), or a" +
                " thumbs-down (the red one). If you think that a tasks is complete nonsense, click on the magnifying glass" +
                " and some HiWi from IfIs will review and correct them.");
        };
        var rating    = new game.ClickableElement('rating','Rating', this.ratingClick, 12, 4, 82.5, 84, 1);
        me.game.world.addChild(rating);
        $("#rating").fadeIn(100);
    }
});
