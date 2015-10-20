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

        game.data.count = 0;
    	// workaround fuer android bug CB-4404
    	if (me.device.android || me.device.android2) {
	        if (me.device.isFullscreen) {
		        me.device.exitFullscreen();
				document.body.style.minHeight = document.body.clientHeight + 'px';
			}
	    }

        //(id, width, height, left, top, rows)
        textOut          = new game.TextOutputElement('task', 55, 42, 3, 8, 14);
        textOutHead      = new game.TextOutputElement('head', 73, 8, 10, 2, 2);
        textOutSchema    = new game.TextOutputElement('schemaa', 35, 81, 61, 8, 27);

        submitButton     = new game.ClickableElement('submit', 'Submit', submitAnswer, 10, 6, 38, 90, 2);
        backButton       = new game.ClickableElement('mainmenu', 'Back', backTo, 10, 6, 3, 90, 2);
        tryButton        = new game.ClickableElement('trybutton', 'Try again', getTaskFromServer, 10, 6, 38, 90, 2);
        sameTaskButton   = new game.ClickableElement('sametaskbutton', 'Try Again', showSameTask, 10, 6, 38, 90, 2);

        nextTaskButton   = new game.ClickableElement('nexttaskbutton', 'New Task', getTaskFromServer, 10, 6, 52, 90, 2);

        /*
        codemirrorButton = new game.ClickableElement('codemirrorbutton', 'CodeMirror Editor', getCodemirrorEditor, 73, 6, 12, 50, 2);
        */

        checkButton      = new game.ClickableElement('checkbutton', 'Check', checkAnswer, 10, 6, 24, 80, 2);

        likeButton       = new game.ClickableElement('likebutton', '', likeIt, 2, 3, 48, 40, 2);
        dislikeButton    = new game.ClickableElement('dislikebutton', '', dislikeIt, 2, 3, 52, 40, 2);
        reviewButton     = new game.ClickableElement('reviewbutton', '', needReview, 2, 3, 56, 40, 2);

        likeButtonSet       = new game.ClickableElement('likebuttonSet', '', likeItSet, 2, 3, 86, 90, 2);
        dislikeButtonSet    = new game.ClickableElement('dislikebuttonSet', '', dislikeItSet, 2, 3, 90, 90, 2);
        reviewButtonSet     = new game.ClickableElement('reviewbuttonSet', '', needReviewSet, 2, 3, 94, 90, 2);

        textIn = new Object();

        console.log(1);

        submitButton.hide();
        tryButton.hide();
        sameTaskButton.hide();
        nextTaskButton.hide();

    	if (game.task.kind != 3) {
            checkButton.hide();
    	}


        me.game.world.addChild(textOut);
        me.game.world.addChild(textOutHead);
        me.game.world.addChild(textOutSchema);
        me.game.world.addChild(submitButton);
        me.game.world.addChild(backButton);
        $("#backButton").fadeIn(100);
        me.game.world.addChild(tryButton);
        me.game.world.addChild(sameTaskButton);
        me.game.world.addChild(nextTaskButton);
        me.game.world.addChild(checkButton);
        me.game.world.addChild(likeButton);
        me.game.world.addChild(dislikeButton);
        me.game.world.addChild(reviewButton);


        likeButton.setImage("assets/data/img/stuff/thumbs_up.png", "thumbup");
        dislikeButton.setImage("assets/data/img/stuff/thumbs_down.png", "thumbdown");
        reviewButton.setImage("assets/data/img/buttons/magnifier.png", "review");

        likeButtonSet.setImage("assets/data/img/stuff/thumbs_up.png", "thumbup");
        dislikeButtonSet.setImage("assets/data/img/stuff/thumbs_down.png", "thumbdown");
        reviewButtonSet.setImage("assets/data/img/buttons/magnifier.png", "review");


        dataTask = {

            id         : 0,
            schema     : '\nrel1((Integer)!key1!,(String)attr1,(String)attr2)\nrel2((String)!key2! -> attr1, (Integer)!key3! -> attr3)',
            exercise   : 'Aufgabe',
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
	        if (game.task.kind == 0) {
	            me.state.change(STATE_BELT);
	        }
	        if (game.task.kind == 1) {
	            me.state.change(STATE_SHEET);
	        }
	        if (game.task.kind == 2) {
	            me.state.change(STATE_TRIVIA);
	        }
	        if (game.task.kind == 3) {
	            me.state.change(STATE_HOMEWORK);
	        }
        }

		/**
		 * Init Ace editor
		 */
		function getAceEditor() {
			var aceEditor = new game.TextInputElement('pre', 'ace', 'wrapperInputAce', 'fieldInputAce', 55, 35, 3, 45, 10);
			me.game.world.addChild(aceEditor);
			textIn = aceEditor;
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
            ajaxSendTaskSetIdRatingRequest(dataTask.id, jsonData, handleRatingSet);
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
            ajaxSendTaskSetIdRatingRequest(dataTask.id, jsonData, handleRatingSet);
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
            ajaxSendTaskSetIdRatingRequest(dataTask.id, jsonData, handleRatingSet);
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
	    	textOut.writePara(dataTask.exercise, 'taskbody');
	    	sameTaskButton.hide();
	    	nextTaskButton.hide();
            submitButton.display();
        }


	    //
	    // textoutput related functions
	    //

	    /**
	     *
	     */
	    function renderSchema(schema) {

	    	res = schema.replace(/(^\w+\b)/gmi, '<div class="relation" style="display: inline">$1</div>');
	    	res = res.replace(/!(.+?)!/g, '<div class="keyattribute" style="display: inline">$1</div>');
	    	res = res.replace(/\n/gmi, '<br>');

	        return res;

	    };

	    /**
	     *
	     */
        function writeHeadline() {

            console.log(4);
            textOutHead.clear();

        	if ((game.task.kind == 0) || (game.task.kind == 1)) {
        	    // Story Collector
        	    textOutHead.writePara(game.task.name, 'tasktitle');
            }

            if (game.task.kind == 2) {
        	    // Trivia
        	    textOutHead.writePara('Difficulty: ' + game.task.difficulty, 'tasktitle');
            }

            if (game.task.kind == 3) {
        	    // Homework
        	    textOutHead.writePara('Exercise', 'tasktitle');
            }

        };

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
        };

        /**
         *
         */
        function handleRating(xhr) {
            likeButton.hide();
            dislikeButton.hide();
            reviewButton.hide();
        };

		/**
         *
         */
        function handleGetTask(xhr) {
            if (xhr.status == 200 || xhr.status ==  400) {
                dataTask = JSON.parse(xhr.responseText);
                console.log(dataTask);
                //dataTask.schema = renderSchema(dataTask.schema);

                // write data
                writeHeadline();
                textOut.clear();
                textOut.writePara(dataTask.taskText, 'taskbody');

                //write Schema
                textOutSchema.clear();
                textOutSchema.writePara('Schema', 'schematitle');
                var schematext = renderSchema(dataTask.relationsFormatted);
                console.log("SchemaText: ",schematext);
                textOutSchema.writeHTML(schemaa, 'schematext');
                console.log(4,5);


                // buttons to hide
                tryButton.hide();
                nextTaskButton.hide();
                sameTaskButton.hide();
                console.log(5);

                // buttons to display
                submitButton.display();
                backButton.display();

                likeButton.display();
                dislikeButton.display();
                reviewButton.display();

                likeButtonSet.display();
                dislikeButtonSet.display();
                reviewButtonSet.display();

                if (game.task.kind == 3) {
                    // Homework
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


        };
        
        /**
         *
         */
        function handlePostTask(xhr) {

            dataResult = JSON.parse(xhr.responseText);
            console.log(dataResult);
            // write data
            textOut.clear();

            if(dataResult.DBMessage) {
                textOut.writePara(dataResult.terry, 'taskbody');
                textOut.writePara("Message: " + dataResult.DBMessage, 'taskbody');
            } else {
                console.log( dataResult.score, (game.task.kind == 1 || game.task.kind == 0) && dataResult.score);
                if ((game.task.kind == 1 || game.task.kind == 0) && dataResult.score ) {
                    game.task.gainScore = dataResult.score;
                    game.task.gainCoins = dataResult.coins;
                    me.state.change(STATE_RESULT);
                }

                textOut.writePara(dataResult.terry, 'taskbody');
                textOut.writePara("Time: " + dataResult.time, 'taskbody');
            }

            // buttons to hide
            submitButton.hide();

            // buttons to display
            sameTaskButton.display();
            nextTaskButton.display();

        };
        
        /**
         *
         */
        function handlePostCheckTask(xhr) {

            // parse data
            dataResult = JSON.parse(xhr.responseText);
            console.log(dataResult);

            // write data
            textOut.clear();
            if (dataResult.DBMessage) {
                textOut.writePara(dataResult.terry + ":\n" + dataResult.DBMessage, 'taskbody');
            } else {
                textOut.writePara(dataResult.terry + "\nTime: " + dataResult.time, 'taskbody');
            }
        };
        
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
        	
        	if ((!answer) || (answer == '\u00a0') || (answer == '')) {
        		
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
        	    if ((game.task.kind == 0) || (game.task.kind == 1)) {
        	    	// Story
        	    	ajaxSendTaskStorySolveRequest(dataTask.id, jsonData, handlePostTask);
        	    }
        	    if (game.task.kind == 2) {
        	    	// Trivia
        	    	ajaxSendTaskTriviaSolveRequest(dataTask.id, jsonData, handlePostTask);
        	    }
        	    if (game.task.kind == 3) {
        	    	// Homework
        	    	ajaxSendTaskHomeworkSolveRequest(dataTask.id, jsonData, handlePostTask);
        	    }
        	    
        	}
        };
        
        /**
         *
         */
        function checkAnswer() {
        	
        	// get answer
        	var answer = textIn.getText();
        	
        	if ((!answer) || (answer == '\u00a0') || (answer == '')) {
        		
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
        	    dataTaskSolve.check = true;
        	    var jsonData = JSON.stringify(dataTaskSolve);

                ajaxSendTaskHomeworkSolveRequest(dataTask.id, jsonData, handlePostCheckTask);
            }
        	
        };
        
        /**
         *
         */
        function getTaskFromServer() {
        	
        	// buttons to hide
        	sameTaskButton.hide();
        	nextTaskButton.hide();
        	submitButton.hide();
        	
        	// clear output
        	textOut.clear();
        	
	        // ajax requests
            if ((game.task.kind == 0) || (game.task.kind == 1)){
        	    // Story
        	    ajaxSendTaskStoryIDRequest(game.task.potionId, handleGetTask);
            }
            if (game.task.kind == 2) {
        	    // Trivia
                ajaxSendTaskTriviaRequest(game.task.difficulty, handleGetTask);

            }
            if (game.task.kind == 3) {
        	    // Homework
        	    ajaxSendTaskHomeworkRequest(handleGetTask);
            }
            
        };
        
        getTaskFromServer();

		getAceEditor();
        
    },

	onDestroyEvent: function(){

		if(game.data.music ){
			me.audio.stopTrack();
			me.audio.playTrack("menu",game.data.musicVolume);
		}
	}

});
