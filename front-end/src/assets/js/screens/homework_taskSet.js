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
            console.log(currentHomeworks);
            var choosenHomework  = game.homework.currentHomeworkIndex;
            var choosenTaskSet   = 0;
            var numberPages = Math.ceil(currentHomeworks[choosenHomework].taskSets[choosenTaskSet].tasks.length/5);
            game.homework.pages = Math.ceil(currentHomeworks[choosenHomework].taskSets[choosenTaskSet].tasks.length/5);
            var numberSets = currentHomeworks[choosenHomework].taskSets.length;
            game.homework.taskSets= currentHomeworks[choosenHomework].taskSets.length;
            console.log("Choosen & Current: ",choosenHomework, currentHomeworks);

            index          = new game.TextOutputElement('index', 40, 10, 30, 10, 2);
            pages          = new game.TextOutputElement('page', 20, 10, 40, 25, 2);



            if(currentHomeworks[choosenHomework].taskSets[choosenTaskSet].tasks.length > 5 ){
                me.game.world.addChild(pages);
                pages.clear();
                pages.writeHTML("Exercise: " + game.homework.page + "/" + numberPages , 'pagebody');
                if(game.homework.page < game.homework.pages){
                    nextPageButton.display();
                }
                if(game.homework.page !== 1 && numberPages > 1){
                    previousPageButton.display();
                }
            }else{
                me.game.world.addChild(pages);
                pages.clear();
                pages.writeHTML("Exercise", 'pagebody');
            }
            if(currentHomeworks[choosenHomework].taskSets.length > 1){

                me.game.world.addChild(index);
                index.clear();
                index.writeHTML(currentHomeworks[choosenHomework].taskSets[choosenTaskSet].taskSetName + " : " + (choosenTaskSet +1) + "/" + numberSets , 'indexbody');
                if(game.homework.taskSet !== game.homework.taskSets && numberSets > 1){
                    nextTaskSetButton.display();
                }
                if(game.homework.taskSets !== 1){
                    previousTaskSetButton.display();
                }
            }else{
                me.game.world.addChild(index);
                index.clear();
                index.writeHTML(currentHomeworks[choosenHomework].taskSets[choosenTaskSet].taskSetName, 'indexbody');
            }

             function taskButtonClick(taskId,exercise) {
                return function () {
                    game.task.kind = 3;
                    game.task.exercise = taskId;
                    game.homework.currentExercise = exercise;
                    me.state.change(STATE_TASK);

                };

            }

            game.homework.currentHomework = currentHomeworks[choosenHomework].taskSets[game.homework.currentHomeworkIndex].tasks;
            console.log("!!!",game.homework.currentHomework);

            //Schleife verbessern
            for (var i = 0; i < 5 && i < currentHomeworks[choosenHomework].taskSets[game.homework.currentHomeworkIndex].tasks.length ; i++) {

                var taskButtons = new game.ClickableElement('taskButtonId' + (i + 5 *(game.homework.page -1)), "• " + currentHomeworks[choosenHomework].taskSets[game.homework.currentHomeworkIndex].tasks[(i + 5 *(game.homework.page -1))].name,
                    taskButtonClick(currentHomeworks[choosenHomework].taskSets[game.homework.currentHomeworkIndex].tasks[(i + 5 *(game.homework.page -1))].id, (i + 5 *(game.homework.page -1)) ), 35, 5, 15, 35 + 6 * i, 1);
                me.game.world.addChild(taskButtons);
                $('#taskButtonId' + (i + 5 *(game.homework.page -1))).fadeIn(100);

                if (currentHomeworks[choosenHomework].taskSets[game.homework.currentHomeworkIndex].tasks[(i + 5 *(game.homework.page -1))].done) {
                    var checkbox = new game.BackgroundElement('checkboxId' + (i + 5 *(game.homework.page -1)), 3.5, 5, 70, 35 + 6 * i, 'none');
                    checkbox.setImage("assets/data/img/stuff/check_symbol.png", "checksymbolImage");
                    me.game.world.addChild(checkbox);
                    $('#checkboxId' + (i + 5 *(game.homework.page -1))).fadeIn(100);

                }
            }

        };

        ajaxSendCurrentHomeworkRequest(currentHomeworkTaskSetsReply);


        //Arrows
        this.showPreviousTaskSet = function () {
            console.log("showPreviousPage");
            game.homework.taskSet--;
            if(game.homework.taskSet === 1){
                previousTaskSetButton.display();
                nextTaskSetButton.hide();
            }
            ajaxSendCurrentHomeworkRequest(currentHomeworkTaskSetsReply);
            //me.state.change(STATE_HOMEWORKTASKSET);
        };

        var previousTaskSetButton = new game.ClickableElement('previousTaskSetButton', '', this.showPreviousTaskSet, 3.5, 7, 17, 9.7, 1);
        me.game.world.addChild(previousTaskSetButton);
        previousTaskSetButton.setImage("assets/data/img/buttons/chalk_arrow_left.png", "chalkLeftImage");
        previousTaskSetButton.hide();


        this.showNextTaskSet = function () {
            console.log("showNextTaskSet");
            game.homework.taskSet++;
            if(game.homework.taskSet === game.homework.taskSets){
                previousTaskSetButton.hide();
                nextTaskSetButton.display();
            }
            me.state.change(STATE_HOMEWORKTASKSET);
        };

        var nextTaskSetButton = new game.ClickableElement('nextTaskSetButton', '', this.showNextTaskSet, 3.5, 7, 79.5, 9.7, 1);
        me.game.world.addChild(nextTaskSetButton);
        nextTaskSetButton.setImage("assets/data/img/buttons/chalk_arrow_right.png", "chalkRightImage");
        nextTaskSetButton.hide();

        this.showPreviousPage = function () {
            console.log("showPreviousPage");
            game.homework.page--;
            nextPageButton.display();
            if(game.homework.page === 1){
                previousPageButton.hide();
            }
            me.state.change(STATE_HOMEWORKTASKSET);

        };

        var previousPageButton = new game.ClickableElement('previousPageButton', '', this.showPreviousPage, 3.5, 7, 17, 24.7, 1);
        me.game.world.addChild(previousPageButton);
        previousPageButton.setImage("assets/data/img/buttons/chalk_arrow_left.png", "chalkLeftImage");
        previousPageButton.hide();

        this.showNextPage = function () {
            console.log("showNextPage");
            game.homework.page++;
            previousPageButton.display();
            if(game.homework.page === game.homework.pages){
                nextPageButton.hide();
            }
            me.state.change(STATE_HOMEWORKTASKSET);

        };


        var nextPageButton = new game.ClickableElement('nextPageButton', '', this.showNextPage, 3.5, 7, 79.5, 24.7, 1);
        me.game.world.addChild(nextPageButton);
        nextPageButton.setImage("assets/data/img/buttons/chalk_arrow_right.png", "chalkRightImage");
        nextPageButton.hide();



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
