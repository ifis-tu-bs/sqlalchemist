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

        var chooseHomeworkTitle = new game.TextOutputElement('chooseHomeworkTitle', 100, 5, 0, 25, 1);
        me.game.world.addChild(chooseHomeworkTitle);
        chooseHomeworkTitle.writeHTML("choose a homework:");


        currentHomeworkReply = function(xmlHttpRequest){
            var currentHomework = JSON.parse(xmlHttpRequest.responseText);
            console.log(currentHomework);
            var expireDate = currentHomework.expire_at.split(".");

            var expireDateObject = new Date();

            var day = new Date();
            console.log(day.toDateString());



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


            this.homeworkButtonClick = function (homeworkId) {
                return function(){
                    game.homework.currentHomeworkId = homeworkId;
                    me.state.change(STATE_HOMEWORKTASKSET);
                }
            };

            for (var i = 0; i < currentHomework.length; i++) {
                var homeworkId = currentHomework[i].id;
                var homeworkButtons = new game.ClickableElement('homeworkButtonId' + i, "•  " + currentHomework[i].name + "  •",
                                                                this.homeworkButtonClick(homeworkId), 70, 5, 15, 35 + 6 * i, 1);
                me.game.world.addChild(homeworkButtons);
                $('#homeworkButtonId' + i).fadeIn(100);

            }






            /**


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
            expireDate.writeHTML("due to: " + currentHomework.expire_at);*/

        };

        ajaxSendCurrentHomeworkRequest(currentHomeworkReply);



    }
});