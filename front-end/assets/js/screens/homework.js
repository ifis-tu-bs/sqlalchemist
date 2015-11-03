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
        var returnButton = new game.ClickableElement('returnButton', 'Back', this.backToLab, 15, 6, 23, 74, 1);
        $('#returnButton').fadeIn(100);
        me.game.world.addChild(returnButton);

        toTask = function(){
            game.task.kind = 3;
            me.state.change(STATE_TASK);
        };

        var homeworkHeader = new game.TextOutputElement('homeworkHeader', 45, 7, 28, 5, 1);
        me.game.world.addChild(homeworkHeader);
        homeworkHeader.write("Homework");

        this.showCurrentHomework = function () {
            console.log("current");
        };

        var currentHomeworkButton = new game.ClickableElement('currentButton', 'current', this.showCurrentHomework(), 15, 6, 23, 50, 1);
        $('#currentButton').fadeIn(100);
        me.game.world.addChild(currentHomeworkButton);

        this.showPreviousHomework = function () {
            console.log("previous");
        };

        var previousHomeworkButton = new game.ClickableElement('previousButton', 'previous', this.showPreviousHomework(), 15, 6, 23, 60, 1);
        $('#previousButton').fadeIn(100);
        me.game.world.addChild(previousHomeworkButton);


        currentHomeworkReply = function(xmlHttpRequest){
            var currentHomework = JSON.parse(xmlHttpRequest.responseText);
            console.log(currentHomework);

            var homeworkTitle = new game.TextOutputElement('homeworkTitle', 40, 5, 18, 25, 1);
            me.game.world.addChild(homeworkTitle);
            homeworkTitle.writeHTML(currentHomework.name);

        };

        var id = game.data.profile.id;
        ajaxSendCurrentHomeworkRequest(id, currentHomeworkReply);




    }
});



/*var startHomework = new game.ClickableElement('startHomework', 'Start', toTask, 15, 6, 62, 74, 1);
 me.game.world.addChild(startHomework);*/

/*homeworkReply = function(xmlHttpRequest){
 var homework_object = JSON.parse(xmlHttpRequest.responseText);

 var title = new game.TextOutputElement('title', 40, 45, 18, 25, 10);
 me.game.world.addChild(title);
 var expireDates = new game.TextOutputElement('expireDates', 15, 45, 60, 25, 10);
 me.game.world.addChild(expireDates);

 for (var i = 0; i < homework_object.length; i++) {
 var name = homework_object[i].name;
 var due_to = homework_object[i].expires_at;
 var submitted = homework_object[i].submitted;
 title.writeHTML(name + "<br>");
 expireDates.writeHTML(due_to + "<br>");

 me.game.world.addChild(
 new me.Sprite(
 1000, 192 + 35 * i,
 me.loader.getImage('checkbox')
 ),
 2
 );
 if (submitted != true) {
 me.game.world.addChild(new me.Sprite(995, 175 + 35 * i, me.loader.getImage('check_symbol')), 3);
 }
 }


 };

 ajaxSendProfileHomeworkRequest(homeworkReply);*/