game.HomeworkTaskSetScreen = me.ScreenObject.extend({

    onResetEvent: function () {

        /**
         * Create background-div and add image to it.
         */
        var backgroundHomework = new game.BackgroundElement('backgroundHomeworkId', 100, 100, 0, 0, 'none');
        backgroundHomework.setImage("assets/data/img/gui/homework_screen.png", "backgroundhomework");
        me.game.world.addChild(backgroundHomework);
        $("#backgroundHomeworkId").fadeIn(100);

        this.backToHomework = function(){
            me.state.change(STATE_HOMEWORK);
        };

        var returnFromHWTaskButton = new game.ClickableElement('returnFromHWTaskButton', 'Back', this.backToHomework, 15, 5, 42.5, 85, 1);
        $('#returnFromHWTaskButton').fadeIn(100);
        me.game.world.addChild(returnFromHWTaskButton);

        currentHomeworkTaskSetsReply = function(xmlHttpRequest) {
            var currentHomeworks = JSON.parse(xmlHttpRequest.responseText);
            var choosenHomework  = game.homework.currentHomeworkIndex;
            console.log(choosenHomework, currentHomeworks);

             function taskButtonClick(taskId) {
                return function () {
                    console.log(taskId);
                }

            }


            var donetask = 0;

            for (var i = 0; i < currentHomeworks[choosenHomework].taskSets[0].tasks.length; i++) {
                var taskId = currentHomeworks[choosenHomework].taskSets[0].tasks.id;
                var taskButtons = new game.ClickableElement('taskButtonId' + i, "• " + currentHomeworks[choosenHomework].taskSets[0].tasks[i].name,
                    taskButtonClick(taskId), 35, 5, 15, 35 + 6 * i, 1);
                me.game.world.addChild(taskButtons);
                $('#taskButtonId' + i).fadeIn(100);

                if (!currentHomeworks[choosenHomework].taskSets[0].tasks[i].done) {
                    var checkbox = new game.BackgroundElement('checkboxId' + i, 3.5, 5, 70, 35 + 6 * i, 'none');
                    checkbox.setImage("assets/data/img/stuff/check_symbol.png", "checksymbolImage");
                    me.game.world.addChild(checkbox);
                    $('#checkboxId' + i).fadeIn(100);
                    donetask++;

                }
            }

        };

        ajaxSendCurrentHomeworkRequest(currentHomeworkTaskSetsReply);


        this.showPreviousTaskSet = function () {
            console.log("left chalk button");
        };

        var previousTaskSetButton = new game.ClickableElement('previousTaskSetButton', '', this.showPreviousTaskSet, 3.5, 7, 7, 55, 1);
        $('#previousTaskSetButton').fadeIn(100);
        me.game.world.addChild(previousTaskSetButton);
        previousTaskSetButton.setImage("assets/data/img/buttons/chalk_arrow_left.png", "chalkLeftImage");


        this.showNextTaskSet = function () {
            console.log("right chalk button");
        };

        var nextTaskSetButton = new game.ClickableElement('nextTaskSetButton', '', this.showNextTaskSet, 3.5, 7, 89.5, 55, 1);
        $('#nextTaskSetButton').fadeIn(100);
        me.game.world.addChild(nextTaskSetButton);
        nextTaskSetButton.setImage("assets/data/img/buttons/chalk_arrow_right.png", "chalkRightImage");



        /**



         for (var i = 0; i < currentHomework.taskSets[0].tasks.length; i++ ) {
                console.log(currentHomework.taskSets[0].tasks.id);


                }

            }

         var doneCounter = new game.TextOutputElement('doneCounter', 25, 5, 38, 25, 1);
         me.game.world.addChild(doneCounter);
         doneCounter.writeHTML(donetask + "/" + currentHomework.taskSets[0].tasks.length);

         var expireDate = new game.TextOutputElement('expireDate', 35, 5, 60, 25, 1);
         me.game.world.addChild(expireDate);
         expireDate.writeHTML("due to: " + currentHomework.expire_at);*/


    }
});