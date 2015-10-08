game.LegendScreen = me.ScreenObject.extend({

    onResetEvent: function() {
        me.game.world.addChild(
            new me.Sprite(
                0, 0,
                me.loader.getImage('ranking_screen')
            ),
            1
        );

        me.game.world.addChild(new backFromLegend(1080,20),2);

        var explanationHeader = new game.TextOutputElement('explanationHeaders', 58, 9, 13, 13, 1);
        var explanation       = new game.TextOutputElement('explanation', 55, 65, 13, 24, 15);

        me.game.world.addChild(explanationHeader);
        me.game.world.addChild(explanation);

        onProfileReply = function(xmlHttpRequest) {
            var profileObject = JSON.parse(xmlHttpRequest.responseText);
            if (profileObject.student == true) {

                homeworkClick = function () {
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

                var homeworks = new game.ClickableElement('homeworks','homework', homeworkClick, 17, 4, 80, 91, 1);
                me.game.world.addChild(homeworks);
            }

        };

        ajaxSendProfileRequest(onProfileReply);

        explanationHeader.writeHTML("Explanations:");
        explanation.writeHTML("Here you can find all the information about our game you need. Just click the buttons"+
            " on the right!");

        stopDouble = function() {
            game.data.playing = false;
        };
        fatLadyClick = function() {
            if (game.data.sound && !game.data.playing){
                game.data.playing = true;
                me.audio.play("page", false, stopDouble, game.data.soundVolume);
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

        scrollClick = function() {
            if (game.data.sound && !game.data.playing){
                game.data.playing = true;
                me.audio.play("page", false, stopDouble, game.data.soundVolume);
            }
            explanationHeader.clear();
            explanationHeader.writeHTML("Scroll Collection");
            explanation.clear();
            explanation.writeHTML("A white Scroll represents a recipe for an enchantment, one with a red ribbon is a recipe"+
                " for a potion. Unlike a potion, an enchantment increases the attributes of the"     +
                " player permanently." + "<br>" + "The Scroll Collection shows the progress you have made during the whole story and"+
                " all levels of the Dungeon. If you collected all scrolls, used the enchantments in the" +
                " Mirror and beaten the Boss, you will skip that level.");
        };

        doorClick = function() {
            if (game.data.sound && !game.data.playing){
                game.data.playing = true;
                me.audio.play("page", false, stopDouble, game.data.soundVolume);
            }
            explanationHeader.clear();
            explanationHeader.writeHTML("Door to Dungeon");
            explanation.clear();
            explanation.writeHTML("By clicking the door you are going to start your run in the Dungeon. Down there you"  +
                " can find new scrolls you need for brewing potions. The Dungeon is devided into multiple levels"        +
                " all consisting of 5 different maps. The last stage of each level is a very difficult Bossmap. For these"    +
                " maps you need your best potions." + "<br>" + "So don't forget: You have to brew a lot of potions to" +
                " get through the story!");
        };

        mirrorClick = function() {
            if (game.data.sound && !game.data.playing){
                game.data.playing = true;
                me.audio.play("page", false, stopDouble, game.data.soundVolume);
            }
            explanationHeader.clear();
            explanationHeader.writeHTML("Mirror");
            explanation.clear();
            explanation.writeHTML("The magic mirror, Ecnedifnoc, reflects your inner power. Here you can change your avatar and"+
                " use the enchantments you found in the dungeon." + "<br>" + "Enchantments increase the attributes health,"+
                " speed, jump and defense for ALL your avatars." + "<br>" + "New avatars can be bought in the shop in the main menu."+
                " Every avatar has special stats, but if you want to know which, you have to buy them.");
        };

        beltClick = function() {
            if (game.data.sound && !game.data.playing){
                game.data.playing = true;
                me.audio.play("page", false, stopDouble, game.data.soundVolume);
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

        tutorialClick = function() {
            if (game.data.sound && !game.data.playing){
                game.data.playing = true;
                me.audio.play("page", false, stopDouble, game.data.soundVolume);
            }
            explanationHeader.clear();
            explanationHeader.writeHTML("Tutorial");
            explanation.clear();
            explanation.writeHTML("The tutorial is supposed to give you a small tour around the game. If you accidentally" +
                " skipped the whole tutorial and would like to revise it, you can reset the story in the settings."
                + "<br>" +
                "Be aware, that you entire progress will be deleted!");
        };

        taskClick = function() {
            if (game.data.sound && !game.data.playing){
                game.data.playing = true;
                me.audio.play("page", false, stopDouble, game.data.soundVolume);
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

        ratingClick = function() {
            if (game.data.sound && !game.data.playing){
                game.data.playing = true;
                me.audio.play("page", false, stopDouble, game.data.soundVolume);
            }
            explanationHeader.clear();
            explanationHeader.writeHTML("Rating");
            explanation.clear();
            explanation.writeHTML("In the Qiuz Mode you can also rate the tasks! If you think a tasks is " +
                " either to difficult or maybe even to easy, you can rate them with a thumbs-up (the green one), or a" +
                " thumbs-down (the red one). If you think that a tasks is complete nonsense, click on the magnifying glass" +
                " and some HiWi from IfIs will review and correct them.");
        };

        var fatLady   = new game.ClickableElement('fatLady', 'Fat Lady', fatLadyClick, 14, 4, 81.5, 25, 1);
        var scrollCol = new game.ClickableElement('scrollCol', "scroll collection", scrollClick, 16, 8, 80.7, 31, 2);
        var door      = new game.ClickableElement('door', 'door to dungeon', doorClick, 16, 8, 80.8, 40.8, 2);
        var mirror    = new game.ClickableElement('mirror', 'mirror', mirrorClick, 12, 4, 82.5, 52, 1);
        var belt      = new game.ClickableElement('belt', 'belt', beltClick, 8, 4, 84.5, 60, 1);
        var tutorial  = new game.ClickableElement('tutorial', 'tutorial', tutorialClick, 15, 4, 81, 68, 1);
        var task      = new game.ClickableElement('triviamode','quiz mode', taskClick, 18, 4, 79.5, 76, 1);
        var rating    = new game.ClickableElement('rating','rating', ratingClick, 12, 4, 82.5, 84, 1);

        me.game.world.addChild(fatLady);
        me.game.world.addChild(scrollCol);
        me.game.world.addChild(door);
        me.game.world.addChild(mirror);
        me.game.world.addChild(belt);
        me.game.world.addChild(tutorial);
        me.game.world.addChild(task);
        me.game.world.addChild(rating);
        $("#fatLady").fadeIn(100);
        $("#scrollCol").fadeIn(100);
        $("#door").fadeIn(100);
        $("#mirror").fadeIn(100);
        $("#belt").fadeIn(100);
        $("#tutorial").fadeIn(100);
        $("#triviamode").fadeIn(100);
        $("#rating").fadeIn(100);



    }
});
