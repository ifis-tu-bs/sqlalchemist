game.HomeworkScreen = me.ScreenObject.extend({

    onResetEvent: function () {

        /**
         * Create background-div and add image to it.
         */
        var backgroundHomework = new game.BackgroundElement('backgroundHomeworkId', 100, 100, 0, 0, 'none');
        backgroundHomework.setImage("assets/data/img/gui/homework_screen.png", "backgroundhomework");
        me.game.world.addChild(backgroundHomework);
        $("#backgroundHomeworkId").fadeIn(100);

        this.backToLab = function(){
            me.state.change(me.state.MENU);
            if (game.data.sound) {
                me.audio.play("switch", false, null, game.data.soundVolume);
            }
        };
        var returnButton = new game.ClickableElement('returnButton', 'Back', this.backToLab, 15, 5, 42.5, 85, 1);
        $('#returnButton').fadeIn(100);
        me.game.world.addChild(returnButton);

        var homeworkHeader = new game.TextOutputElement('homeworkHeader', 100, 7, 0, 5, 1);
        me.game.world.addChild(homeworkHeader);
        homeworkHeader.write("Homework");

        this.showCurrentHomework = function () {
            console.log("current");
        };

        var currentHomeworkButton = new game.ClickableElement('currentButton', 'current', this.showCurrentHomework, 7, 3, 42, 14, 1);
        $('#currentButton').fadeIn(100);
        me.game.world.addChild(currentHomeworkButton);

        this.showPreviousHomework = function () {
            console.log("previous");
        };

        var previousHomeworkButton = new game.ClickableElement('previousButton', 'previous', this.showPreviousHomework, 7, 3, 50, 14, 1);
        $('#previousButton').fadeIn(100);
        me.game.world.addChild(previousHomeworkButton);


        currentHomeworkReply = function(xmlHttpRequest){
            var currentHomework = JSON.parse(xmlHttpRequest.responseText);
            console.log(currentHomework);

            var homeworkTitle = new game.TextOutputElement('homeworkTitle', 50, 5, 10, 25, 1);
            me.game.world.addChild(homeworkTitle);
            homeworkTitle.writeHTML(currentHomework.taskSets[0].taskSetName);

            this.taskButtonClick = function (taskId) {
                return function () {
                    console.log(taskId);
                }

            };

            var donetask = 0;

            for (var i = 0; i < currentHomework.taskSets[0].tasks.length; i++ ) {
                console.log(currentHomework.taskSets[0].tasks.id);
                var taskId = currentHomework.taskSets[0].tasks.id;
                var taskButtons = new game.ClickableElement('taskButtonId' + i, "•" + currentHomework.taskSets[0].tasks[i].name,
                                                            this.taskButtonClick(taskId), 35, 5, 15, 35 + 6 * i, 1);
                me.game.world.addChild(taskButtons);
                $('#taskButtonId' + i).fadeIn(100);

                if (!currentHomework.taskSets[0].tasks[i].done) {
                    var checkbox = new game.BackgroundElement('checkboxId' + i, 3.5, 5, 70, 35 + 6 * i, 'none');
                    checkbox.setImage("assets/data/img/stuff/check_symbol.png", "checksymbolImage");
                    me.game.world.addChild(checkbox);
                    $('#checkboxId' + i).fadeIn(100);
                    donetask++;
                }

            }

            var doneCounter = new game.TextOutputElement('doneCounter', 25, 5, 38, 25, 1);
            me.game.world.addChild(doneCounter);
            doneCounter.writeHTML(donetask + "/" + currentHomework.taskSets[0].tasks.length);

            var expireDate = new game.TextOutputElement('expireDate', 35, 5, 60, 25, 1);
            me.game.world.addChild(expireDate);
            expireDate.writeHTML("due to: " + currentHomework.expire_at);

            //27,31

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


        };

        var id = game.data.profile.id;
        ajaxSendCurrentHomeworkRequest(id, currentHomeworkReply);



    }
});