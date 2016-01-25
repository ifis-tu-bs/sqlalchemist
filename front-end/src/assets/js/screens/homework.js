game.HomeworkScreen = me.ScreenObject.extend({

    onResetEvent: function () {

        game.homework.page = 1;
        game.homework.pages = 1;
        game.homework.taskSet = 1;
        game.homework.taskSets = 1;

        /**
         * Create background-div and add image to it.
         */
        var backgroundHomework = new game.BackgroundElement('backgroundHomeworkId', 100, 100, 0, 0, 'none');
        backgroundHomework.setImage("assets/data/img/gui/task_screen_new.png", "backgroundhomework");
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

        var chooseHomeworkTitle = new game.TextOutputElement('chooseHomeworkTitle', 100, 5, 0, 16, 1);
        me.game.world.addChild(chooseHomeworkTitle);
        chooseHomeworkTitle.writeHTML("choose a homework:");


        currentHomeworkReply = function(xmlHttpRequest){
            var homeworks = JSON.parse(xmlHttpRequest.responseText);
            console.log("Homework: ",homeworks);

            function homeworkButtonClick(homeworkIndex) {
                return function(){
                    game.homework.currentHomeworkIndex = homeworkIndex;
                    game.task.homeworkId = homeworks[homeworkIndex].id;
                    me.state.change(STATE_HOMEWORKTASKSET);
                };
            }

            for (var i = 0; i < homeworks.length && i < 8 ; i++) {
                if (!homeworks[i].expired) {
                    var currentHomeworkButtons = new game.ClickableElement('currentHomeworkButtonId' + i, "•  " + homeworks[i].name +"  due  "+ homeworks[i].expire_at + "  •",
                        homeworkButtonClick(i), 70, 5, 15, 35 + 6 * i, 1);
                    me.game.world.addChild(currentHomeworkButtons);
                }
                if (homeworks[i].expired) {
                    var previousHomeworkButtons = new game.ClickableElement('previousHomeworkButtonId' + i, "•  " + homeworks[i].name + "  •",
                        homeworkButtonClick(i), 70, 5, 15, 35 + 6 * i, 1);
                    me.game.world.addChild(previousHomeworkButtons);
                }
            }

            this.showCurrentHomework = function () {
                $("[id*='previousHomeworkButtonId']").hide();
                $("[id*='currentHomeworkButtonId']").fadeIn();
                console.log("current");
            };

            var currentHomeworkButton = new game.ClickableElement('currentButton', 'current', this.showCurrentHomework, 7, 5, 42, 25, 2);
            $('#currentButton').fadeIn(100);
            me.game.world.addChild(currentHomeworkButton);

            this.showPreviousHomework = function () {
                $("[id*='currentHomeworkButtonId']").hide();
                $("[id*='previousHomeworkButtonId']").fadeIn();
                console.log("previous");
            };

            var previousHomeworkButton = new game.ClickableElement('previousButton', 'previous', this.showPreviousHomework, 7, 5, 50, 25, 2);
            $('#previousButton').fadeIn(100);
            me.game.world.addChild(previousHomeworkButton);

            this.showCurrentHomework();

        };

        ajaxSendCurrentHomeworkRequest(currentHomeworkReply);

    }
});
