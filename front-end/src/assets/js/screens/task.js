/**
 * Der Task Screen
 * author: Christian Reineke
 */

/**
 * local functions
 */

function toMainMenu() {
	me.state.change(me.state.MENU);
}

/**
 * Define the class game.Taskscreen
 */
game.TaskScreen = me.ScreenObject.extend({
    /**
     *  action to perform on state change
     */
    onResetEvent: function() {

		if(game.data.music){
			me.audio.stopTrack();
			me.audio.playTrack("task",game.data.musicVolume);
		}

        var backgroundTask = new game.BackgroundElement('backgroundTaskId', 100, 100, 0, 0, 'none');
        backgroundTask.setImage("assets/data/img/gui/task_screen_new.png", "backgroundscreen");
        me.game.world.addChild(backgroundTask);
        $("#backgroundTaskId").fadeIn(100);

        var schemaCheck = new game.BackgroundElement('schemaCheckId', 4, 6, 32, 88, "none");
        schemaCheck.setImage("assets/data/img/stuff/check_symbol.png", "check_symbol");
        me.game.world.addChild(schemaCheck);
        schemaCheck.hide();


        game.data.count = 0;
        var solved = false;
    	// workaround fuer android bug CB-4404
    	if (me.device.android || me.device.android2) {
	        if (me.device.isFullscreen) {
		        me.device.exitFullscreen();
				document.body.style.minHeight = document.body.clientHeight + 'px';
			}
	    }

        //(id, width, height, left, top, rows)
        textOut          = new game.TextOutputElement('task', 55, 30, 3, 10, 10);
        textOutHead      = new game.TextOutputElement('head', 73, 4, 10, 5, 1);
        textOutSchema    = new game.TextOutputElement('schemaa', 39, 80, 61, 5, 32);

        checksLeft          = new game.TextOutputElement('checksleft', 8, 4.6, 33, 90.7, 2);
        submitsLeft         = new game.TextOutputElement('submitsleft', 8, 4.6, 47, 90.7, 2);

        submitButton     = new game.ClickableElement('submit', 'Submit', submitAnswer, 10, 6, 38, 90, 2);
        backButton       = new game.ClickableElement('mainmenu', 'Back', backTo, 10, 6, 3, 90, 2);
        tryButton        = new game.ClickableElement('trybutton', 'Retry', getTaskFromServer, 10, 6, 38, 90, 2);
        sameTaskButton   = new game.ClickableElement('sametaskbutton', 'Show Task Again', showSameTask, 12, 6, 12, 88, 2);

        nextTaskButton   = new game.ClickableElement('nexttaskbutton', 'New Task', getTaskFromServer, 11, 6, 50, 90, 2);
        nextExerciseButton= new game.ClickableElement('nextexercisebutton', 'Next Exercise', getNextExerciseFromServer, 10, 6, 50, 88, 2);

        checkButton      = new game.ClickableElement('checkbutton', 'Check', checkAnswer, 10, 6, 24, 90, 2);
        stayButton      = new game.ClickableElement('submit', 'stay in Schema', stayInSchema, 10, 6, 24, 88, 2);

        likeButton       = new game.ClickableElement('likebutton', '', likeIt, 2, 3, 50, 40, 2);
        dislikeButton    = new game.ClickableElement('dislikebutton', '', dislikeIt, 2, 3, 53, 40, 2);
        reviewButton     = new game.ClickableElement('reviewbutton', '', needReview, 2, 3, 56, 40, 2);

        likeButtonSet    = new game.ClickableElement('likebuttonSet', '', likeItSet, 2, 3, 88, 90, 2);
        dislikeButtonSet = new game.ClickableElement('dislikebuttonSet', '', dislikeItSet, 2, 3, 91, 90, 2);
        reviewButtonSet  = new game.ClickableElement('reviewbuttonSet', '', needReviewSet, 2, 3, 94, 90, 2);


        setDifficultyButton  = new game.ClickableElement('setDifficultyButton', '', showDifficulties, 4, 6, 62, 88, 2);


        var chooseDifficulty1  = new game.ClickableElement('chooseDifficulty1', '', setDifficulty(1), 4, 6, 67, 89, 2);
        chooseDifficulty1.setImage("assets/data/img/buttons/difficulty_coins/coin_1.png", "one");
        me.game.world.addChild(chooseDifficulty1);
        var chooseDifficulty2  = new game.ClickableElement('chooseDifficulty2', '', setDifficulty(2), 4, 6, 70, 88, 2);
        chooseDifficulty2.setImage("assets/data/img/buttons/difficulty_coins/coin_2.png", "two");
        me.game.world.addChild(chooseDifficulty2);
        var chooseDifficulty3  = new game.ClickableElement('chooseDifficulty3', '', setDifficulty(3), 4, 6, 73, 87, 2);
        chooseDifficulty3.setImage("assets/data/img/buttons/difficulty_coins/coin_3.png", "three");
        me.game.world.addChild(chooseDifficulty3);
        var chooseDifficulty4  = new game.ClickableElement('chooseDifficulty4', '', setDifficulty(4), 4, 6, 76, 88, 2);
        chooseDifficulty4.setImage("assets/data/img/buttons/difficulty_coins/coin_4.png", "four");
        me.game.world.addChild(chooseDifficulty4);
        var chooseDifficulty5  = new game.ClickableElement('chooseDifficulty5', '', setDifficulty(5), 4, 6, 79, 89, 2);
        chooseDifficulty5.setImage("assets/data/img/buttons/difficulty_coins/coin_5.png", "five");
        me.game.world.addChild(chooseDifficulty5);
        var chooseDifficulty6  = new game.ClickableElement('chooseDifficulty6', '', setDifficulty(6), 4, 6, 82, 88, 2);
        chooseDifficulty6.setImage("assets/data/img/buttons/difficulty_coins/coin_6.png", "six");
        me.game.world.addChild(chooseDifficulty6);

        chooseDifficulty1.hide();
        chooseDifficulty2.hide();
        chooseDifficulty3.hide();
        chooseDifficulty4.hide();
        chooseDifficulty5.hide();
        chooseDifficulty6.hide();


        textIn = {};

        checksLeft.hide();
        submitsLeft.hide();

        submitButton.hide();
        tryButton.hide();
        sameTaskButton.hide();
        nextTaskButton.hide();
        schemaCheck.hide();

        //hide ratings
        likeButton.hide();
        dislikeButton.hide();
        reviewButton.hide();

        likeButtonSet.hide();
        dislikeButtonSet.hide();
        reviewButtonSet.hide();

        setDifficultyButton.hide();


        if(game.task.kind === 2){
            switch(game.task.difficulty){
                case 1:
                    setDifficultyButton.setImage("assets/data/img/buttons/difficulty_coins/coin_1.png", "Difficulty");
                    break;
                case 2:
                    setDifficultyButton.setImage("assets/data/img/buttons/difficulty_coins/coin_2.png", "Difficulty");
                    break;
                case 3:
                    setDifficultyButton.setImage("assets/data/img/buttons/difficulty_coins/coin_3.png", "Difficulty");
                    break;
                case 4:
                    setDifficultyButton.setImage("assets/data/img/buttons/difficulty_coins/coin_4.png", "Difficulty");
                    break;
                case 5:
                    setDifficultyButton.setImage("assets/data/img/buttons/difficulty_coins/coin_5.png", "Difficulty");
                    break;
                case 6:
                    setDifficultyButton.setImage("assets/data/img/buttons/difficulty_coins/coin_6.png", "Difficulty");

            }
            setDifficultyButton.display();
            setDifficultyButton.setTitle("change next tasks difficulty");
            stayButton.display();
            stayButton.setTitle("toggle to keep the schema");
        }


    	if (game.task.kind != 3) {
            nextExerciseButton.hide();
            checkButton.hide();
            checkButton.setTitle("syntax check, before submitting");
    	}


        me.game.world.addChild(textOut);
        me.game.world.addChild(textOutHead);
        me.game.world.addChild(textOutSchema);
        me.game.world.addChild(checksLeft);
        me.game.world.addChild(submitsLeft);
        me.game.world.addChild(submitButton);
        me.game.world.addChild(backButton);
        $("#backButton").fadeIn(100);
        me.game.world.addChild(tryButton);
        me.game.world.addChild(sameTaskButton);
        me.game.world.addChild(nextTaskButton);
        me.game.world.addChild(nextExerciseButton);
        me.game.world.addChild(checkButton);
        me.game.world.addChild(stayButton);
        me.game.world.addChild(likeButton);
        me.game.world.addChild(dislikeButton);
        me.game.world.addChild(reviewButton);
        me.game.world.addChild(likeButtonSet);
        me.game.world.addChild(dislikeButtonSet);
        me.game.world.addChild(reviewButtonSet);
        me.game.world.addChild(setDifficultyButton);



        likeButton.setImage("assets/data/img/stuff/thumbs_up.png", "thumbup");
        likeButton.setTitle("good task");
        dislikeButton.setImage("assets/data/img/stuff/thumbs_down.png", "thumbdown");
        dislikeButton.setTitle("bad task");
        reviewButton.setImage("assets/data/img/buttons/magnifier.png", "review");
        reviewButton.setTitle("task needs review");

        likeButtonSet.setImage("assets/data/img/stuff/thumbs_up.png", "thumbup");
        likeButtonSet.setTitle("good schema");
        dislikeButtonSet.setImage("assets/data/img/stuff/thumbs_down.png", "thumbdown");
        dislikeButtonSet.setTitle("bad schema");
        reviewButtonSet.setImage("assets/data/img/buttons/magnifier.png", "review");
        reviewButtonSet.setTitle("schema needs review");

        tryButton.setTitle("back to the last Task");
        sameTaskButton.setTitle("show Task again");

        nextTaskButton.setTitle("another Task");
        submitButton.setTitle("submit your Query");




        dataTask = {

            id         : 0,
            schema     : '\nrel1((Integer)!key1!,(String)attr1,(String)attr2)\nrel2((String)!key2! -> attr1, (Integer)!key3! -> attr3)',
            exercise   : 'Aufgabe',
            taskSet    : 0,
            points     : 0,
            timelimit  : 0,
            difficulty : 0,
            rating     : {
                negative     : 0,
                positive     : 0,
                needReview   : 0
            }

        };

        dataTaskSolve = {

        	statement : 'SQL Statement',
        	time      : 0

        };

        dataTaskResult = {

        	terry : '',
        	time  : 0

        };

        dataTaskRating = {

        	negative     : 0,
        	positive     : 0,
        	needReview   : 0

        };

        //
        // Button related functions
        //

        /**
         * backButton
         */
        function backTo() {
	        if (game.task.kind === 0) {
	            me.state.change(STATE_BELT);
	        }
	        if (game.task.kind == 1) {
	            me.state.change(STATE_SHEET);
	        }
	        if (game.task.kind == 2) {
	            me.state.change(STATE_TRIVIA);
	        }
	        if (game.task.kind == 3) {
	            me.state.change(STATE_HOMEWORKTASKSET);
	        }
        }

        /**
         *
         * @param difficulty
         * @returns {Function}
         */
        function setDifficulty(difficulty){
            return function() {
                game.task.difficulty = difficulty;


                setTimeout(function(){
                    chooseDifficulty6.hide();
                    setTimeout(function(){
                        chooseDifficulty5.hide();
                        setTimeout(function(){
                            chooseDifficulty4.hide();
                            setTimeout(function(){
                                chooseDifficulty3.hide();
                                setTimeout(function(){
                                    chooseDifficulty2.hide();
                                    setTimeout(function(){
                                        chooseDifficulty1.hide();
                                        setDifficultyButton  = new game.ClickableElement('setDifficultyButton', '', showDifficulties, 4, 6, 62, 88, 2);
                                        setDifficultyButton.setTitle("change next tasks difficulty");

                                        setDifficultyButton.display();
                                        me.game.world.addChild(setDifficultyButton);

                                        switch(difficulty){
                                            case 1:
                                                setDifficultyButton.setImage("assets/data/img/buttons/difficulty_coins/coin_1.png", "Difficulty");
                                                break;
                                            case 2:
                                                setDifficultyButton.setImage("assets/data/img/buttons/difficulty_coins/coin_2.png", "Difficulty");
                                                break;
                                            case 3:
                                                setDifficultyButton.setImage("assets/data/img/buttons/difficulty_coins/coin_3.png", "Difficulty");
                                                break;
                                            case 4:
                                                setDifficultyButton.setImage("assets/data/img/buttons/difficulty_coins/coin_4.png", "Difficulty");
                                                break;
                                            case 5:
                                                setDifficultyButton.setImage("assets/data/img/buttons/difficulty_coins/coin_5.png", "Difficulty");
                                                break;
                                            case 6:
                                                setDifficultyButton.setImage("assets/data/img/buttons/difficulty_coins/coin_6.png", "Difficulty");

                                        }
                                    }, 100);
                                }, 100);
                            }, 100);
                        }, 100);
                    }, 100);
                }, 100);
            };
        }

        function showDifficulties(){
            setTimeout(function(){
                chooseDifficulty1.display();
                setTimeout(function(){
                    chooseDifficulty2.display();
                    setTimeout(function(){
                        chooseDifficulty3.display();
                        setTimeout(function(){
                            chooseDifficulty4.display();
                            setTimeout(function(){
                                chooseDifficulty5.display();
                                setTimeout(function(){
                                    chooseDifficulty6.display();
                                }, 100);
                            }, 100);
                        }, 100);
                    }, 100);
                }, 100);
            }, 100);
        }

		/**
		 * Init Ace editor
		 */
		function getAceEditor() {
			var aceEditor = new game.TextInputElement('pre', 'ace', 'wrapperInputAce', 'fieldInputAce', 55, 41, 3, 43.2, 12);
			me.game.world.addChild(aceEditor);
			textIn = aceEditor;
            aceEditor.hide();
			textIn.display();
			if (!tryButton.visibility) {
				submitButton.display();
			}
		}
        /**
         * likeButtonSet
         */
        function likeItSet() {
            //
            dataTaskRating.negative = 0;
            dataTaskRating.positive = 1;
            dataTaskRating.needReview = 0;
            var jsonData = JSON.stringify(dataTaskRating);
            ajaxSendTaskSetIdRatingRequest(dataTask.taskSet, jsonData, handleRatingSet);
        }

        /**
         * dislikeButtonSet
         */
        function dislikeItSet() {
            //
            dataTaskRating.negative = 1;
            dataTaskRating.positive = 0;
            dataTaskRating.needReview = 0;
            var jsonData = JSON.stringify(dataTaskRating);
            ajaxSendTaskSetIdRatingRequest(dataTask.taskSet, jsonData, handleRatingSet);
        }

        /**
         * reviewButton
         */
        function needReviewSet() {
            //
            dataTaskRating.negative = 0;
            dataTaskRating.positive = 0;
            dataTaskRating.needReview = 1;
            var jsonData = JSON.stringify(dataTaskRating);
            ajaxSendTaskSetIdRatingRequest(dataTask.taskSet, jsonData, handleRatingSet);
        }

        /**
         * likeButton
         */
        function likeIt() {
        	//
        	dataTaskRating.negative = 0;
        	dataTaskRating.positive = 1;
        	dataTaskRating.needReview = 0;
        	var jsonData = JSON.stringify(dataTaskRating);
        	ajaxSendTaskIdRatingRequest(dataTask.id, jsonData, handleRating);
        }

        /**
         * dislikeButton
         */
        function dislikeIt() {
        	//
        	dataTaskRating.negative = 1;
        	dataTaskRating.positive = 0;
        	dataTaskRating.needReview = 0;
        	var jsonData = JSON.stringify(dataTaskRating);
        	ajaxSendTaskIdRatingRequest(dataTask.id, jsonData, handleRating);
        }

        /**
         * reviewButton
         */
        function needReview() {
        	//
        	dataTaskRating.negative = 0;
        	dataTaskRating.positive = 0;
        	dataTaskRating.needReview = 1;
        	var jsonData = JSON.stringify(dataTaskRating);
            ajaxSendTaskIdRatingRequest(dataTask.id, jsonData, handleRating);
        }

        /**
	     * sameTaskButton
	     */
	    function showSameTask() {
            textOut.clear();
	    	textOut.writeHTML(dataTask.taskText, 'taskbody');
	    	sameTaskButton.hide();
	    	nextTaskButton.hide();

            handleRatingSet();
            handleRating();

            if(!solved){
                submitButton.display();
                nextTaskButton.display();
            }
            if(game.task.kind === 3){
                submitButton.display();
                checkButton.display();
                checksLeft.display();
                submitsLeft.display();
            }
        }


	    //
	    // textoutput related functions
	    //

	    /**
	     *
	     */
	    function renderSchema(schema) {

            res = schema;
            //# wird entfernt, ^ startet einfach so.
	    	//res = res.replace(/#(^\w+\b)/gmi, '<div class="relation" style="display: inline">$1</div>');
            res = res.replace(/#(\w+\b)/gmi, '<div class="relation" style="display: inline;">$1</div>');
	    	res = res.replace(/!(.+?)!/g, '<div class="keyattribute" style="display: inline">$1</div>');
            res = res.replace(/%/gmi, '<br />' + '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;');
            res = res.replace(/}/gmi, '<br />' + ')' + '<br /><br />');
            res = res.replace(/{/gmi, '(');

	        return res;

	    }

	    /**
	     *
	     */
        function writeHeadline() {

            textOutHead.clear();

        	if ((game.task.kind === 0) || (game.task.kind == 1)) {
        	    // Story Collector
        	    textOutHead.writeHTML(game.task.name, 'tasktitle');
            }

            if (game.task.kind == 2) {
        	    // Trivia
        	    textOutHead.writeHTML('Difficulty: ' + game.task.difficulty, 'tasktitle');
            }

            if (game.task.kind == 3) {
        	    // Homework
        	    textOutHead.writeHTML('Exercise', 'tasktitle');
            }

        }

        //
        // callback functions for ajax requests
        //

        /**
         *
         */
        function handleRatingSet(xhr) {
            likeButtonSet.hide();
            dislikeButtonSet.hide();
            reviewButtonSet.hide();
        }

        /**
         *
         */
        function handleRating(xhr) {
            likeButton.hide();
            dislikeButton.hide();
            reviewButton.hide();
        }



		/**
         *
         */
        function handleGetTask(xhr) {
            if (xhr.status == 200 || xhr.status ==  400) {
                dataTask = JSON.parse(xhr.responseText);
                console.log("Task: ",dataTask);

                if(game.task.difficulty !== dataTask.points){
                    game.task.difficulty = dataTask.points;
                    if(game.task.kind === 2) {
                        console.log("Change");
                        $("#setDifficultyButton #Difficulty").attr("src","assets/data/img/buttons/difficulty_coins/coin_" + dataTask.points +".png");
                        //setDifficulty(dataTask.points);
                    }
                }

                // write data
                writeHeadline();
                textOut.clear();
                textIn.clear();
                textOut.writeHTML(dataTask.taskText, 'taskbody');

                //write Schema
                textOutSchema.clear();
                textOutSchema.writeHTML('Schema', 'schematitle');
                var schematext = renderSchema(dataTask.relationsFormatted);
                //console.log("SchemaText: ",schematext);
                textOutSchema.writeHTML(schematext, 'taskbody');


                // buttons to hide
                tryButton.hide();
                nextTaskButton.hide();
                sameTaskButton.hide();

                game.homework.checks = dataTask.availableSyntaxChecks - dataTask.syntaxChecksDone;
                game.homework.submits = dataTask.availableSemanticChecks - dataTask.semanticChecksDone;


                handleRatingSet();
                handleRating();

                // buttons to display
                submitButton.display();
                backButton.display();


                if (game.task.kind == 3) {
                    // Homework !!!!!!!!!
                    displayTries();
                    checksLeft.display();
                    submitsLeft.display();
                    checkButton.display();
                }

                //get the starting time of the task
                game.task.startTime = me.timer.getTime();
            }
            else {
                // ajax request failed

                // buttons to hide
                checkButton.hide();
                submitButton.hide();

                // buttons to display
                tryButton.display();
                backButton.display();

                // write message
                textOut.clear();
                textOut.writeHTML(xhr.responseText, 'errorString');

            }


        }

        /*
         *
         */
        function handlePostTask(xhr) {

            dataResult = JSON.parse(xhr.responseText);
            console.log("RESULT: ",dataResult,"Thats it?",dataResult.score );
            game.data.gainScore = dataResult.score;
            // write data
            textOut.clear();

            if(dataResult.SQLError) {
                textOut.writeHTML(dataResult.terry + "<br>" + dataResult.SQLError, 'taskbody');
            } else {
                console.log( dataResult.score, (game.task.kind == 1 || game.task.kind === 0) && dataResult.score);
                if ((game.task.kind == 1 || game.task.kind === 0) && dataResult.score ) {
                    game.task.gainScore = dataResult.score;
                    game.task.gainCoins = dataResult.coins;
                    me.state.change(STATE_RESULT);
                }

                textOut.writeHTML(dataResult.terry + "<br>" + "Time: " + convertTime(dataResult.time), 'taskbody');

                if(game.task.kind == 2){
                    submitButton.hide();
                    solved = true;
                }
                //textOut.writeHTML("Time: " + dataResult.time, 'taskbody');
            }



            sameTaskButton.display();
            //submitButton.hide();
            //checkButton.hide();

            // buttons to display and hide
            if(game.task.kind !== 3){

                nextTaskButton.display();

                likeButton.display();
                dislikeButton.display();
                reviewButton.display();

                likeButtonSet.display();
                dislikeButtonSet.display();
                reviewButtonSet.display();
            }else{
                displayTries();
            }

        }

        /**
         *
         */
        function handlePostCheckTask(xhr) {

            // parse data
            dataResult = JSON.parse(xhr.responseText);
            console.log(dataResult);

            // write data
            textOut.clear();
            if (dataResult.SQLError) {
                textOut.writeHTML(dataResult.terry + ":\n" + dataResult.SQLError, 'taskbody');
            } else {
                textOut.writeHTML(dataResult.terry + "\nTime: " + dataResult.time, 'taskbody');
            }
        }

        //
        // ajax requests
        //

        /**
         *
         */
        function submitAnswer() {
            game.data.count ++;

        	// get answer
        	var answer = textIn.getText();

        	if ((!answer) || (answer == '\u00a0') || (answer === '')) {

        		//leere Eingabe
        		textIn.insertText('Your query is empty. I do not accept empty queries! Do you think I am a fool?');

        	}
        	else {
        		//get timestap;
		        game.task.finishTime = me.timer.getTime();
		        var ellapsedTime = game.task.finishTime - game.task.startTime;

		        // create JSON Object
        	    dataTaskSolve.statement = answer;
        	    dataTaskSolve.time = ellapsedTime;
        	    var jsonData = JSON.stringify(dataTaskSolve);

        	    // ajax requests
        	    if ((game.task.kind === 0) || (game.task.kind == 1)) {
        	    	// Story
        	    	ajaxSendTaskStorySolveRequest(dataTask.id, jsonData, handlePostTask);
        	    }
        	    if (game.task.kind == 2) {
        	    	// Trivia
        	    	ajaxSendTaskTriviaSolveRequest(dataTask.id, jsonData, handlePostTask);
        	    }
        	    if (game.task.kind == 3) {
        	    	// Homework
                    game.homework.submits--;
                    ajaxSendTaskHomeworkSolveRequest(dataTask.id, jsonData, handlePostTask);
        	    }

        	}
        }

        /**
         * Needs to be
         */
        function checkAnswer() {

            // get answer
            var answer = textIn.getText();

            if ((!answer) || (answer == '\u00a0') || (answer === '')) {

                //leere Eingabe
                textIn.insertText('Your query is empty. I do not accept empty queries! Do you think I am a fool?');

            }
            else {
                game.homework.checks--;
                //get timestap;
                game.task.finishTime = me.timer.getTime();
                var ellapsedTime = game.task.finishTime - game.task.startTime;

                // create JSON Object
                dataTaskSolve.statement = answer;
                dataTaskSolve.time = ellapsedTime;
                var jsonData = JSON.stringify(dataTaskSolve);

                // ajax request
                ajaxSendTaskHomeworkSyntaxSolveRequest(dataTask.id, jsonData, handlePostTask);

            }

        }

        /**
         *Checkbox Funktion
         */
        function stayInSchema() {

            if(schemaCheck.isVisibile()){
                schemaCheck.hide();
            }else{
                schemaCheck.display();
            }

        }

        function displayTries(){
            submitsLeft.clear();
            checksLeft.clear();
            submitsLeft.writeHTML(game.homework.submits + " left");
            checksLeft.writeHTML(game.homework.checks + " left");
        }

        /**
         *
         */
        function getTaskFromServer() {

        	// buttons to hide
        	sameTaskButton.hide();
        	nextTaskButton.hide();
        	submitButton.hide();
            solved = false;

        	// clear output
        	textOut.clear();

	        // ajax requests
            if ((game.task.kind === 0) || (game.task.kind == 1)){
        	    // Story
        	    ajaxSendTaskStoryIDRequest(game.task.potionId, handleGetTask);
            }
            if (game.task.kind == 2) {
        	    // Trivia
                if(schemaCheck.isVisibile()){
                    ajaxSendTaskTriviaStayRequest(game.task.difficulty, handleGetTask);
                }else{
                    ajaxSendTaskTriviaRequest(game.task.difficulty, handleGetTask);
                }


            }
            if (game.task.kind == 3) {
        	    // Homework
                console.log("Homework/",game.task.homeworkId+ "/"+ game.task.exercise);
                ajaxSendTaskHomeworkRequest(game.task.homeworkId, game.task.exercise ,handleGetTask);
            }

        }

        var stopp = 0;
        function getNextExerciseFromServer() {
            console.log("Stopp: ",stopp);
            // buttons to hide
            sameTaskButton.hide();
            nextTaskButton.hide();
            submitButton.hide();

            // clear output
            textOut.clear();

            // Homework
            game.homework.currentExercise++;
            if(game.homework.currentExercise > 5*game.homework.page){
                game.homework.page++;
                if(game.homework.page > game.homework.pages){
                    game.homework.page = 0;
                }

            }
            if(game.homework.currentExercise === game.homework.currentHomework.length){
                game.homework.currentExercise = 0;
                console.log("Switch");
            }
            console.log("INFO:",game.homework.currentHomework[game.homework.currentExercise].length,game.homework.currentExercise );
            console.log("Homework/",game.task.homeworkId+ "/"+ game.homework.currentHomework[game.homework.currentExercise].id);
            if(!game.homework.currentHomework[game.homework.currentExercise].done){
                ajaxSendTaskHomeworkRequest(game.task.homeworkId, game.homework.currentHomework[game.homework.currentExercise].id ,handleGetTask);
            }else{
                stopp++;
                //badly done
                if(stopp < 30){
                    getNextExerciseFromServer();
                }else{
                    me.state.change(STATE_HOMEWORKTASKSET);
                }
            }


        }

        getTaskFromServer();

		getAceEditor();


    },

	onDestroyEvent: function(){

        //setDifficultyButton.destroy();

		if(game.data.music ){
			me.audio.stopTrack();
			me.audio.playTrack("menu",game.data.musicVolume);
		}
	}

});
