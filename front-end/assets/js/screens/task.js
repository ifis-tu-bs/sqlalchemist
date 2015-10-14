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

        game.data.count = 0;
    	// workaround fuer android bug CB-4404
    	if (me.device.android || me.device.android2) {
	        if (me.device.isFullscreen) {
		        me.device.exitFullscreen();
				document.body.style.minHeight = document.body.clientHeight + 'px';
			}
	    }

        me.game.world.addChild(
            new me.Sprite (
                0,0,
                me.loader.getImage('task_screen')
            ),
            1
        );
     
        textOut          = new game.TextOutputElement('task', 73, 30, 12, 13, 10);
        
        submitButton     = new game.ClickableElement('submit', 'Submit', submitAnswer, 10, 6, 38, 80, 2);
        backButton       = new game.ClickableElement('mainmenu', 'Back', backTo, 10, 6, 10, 80, 2);
        schemaButton     = new game.ClickableElement('schema', 'Schema',toggleSchemaButtonWithTaskButton, 10, 6, 52, 80, 2);
        taskButton       = new game.ClickableElement('taskbutton', 'Task', toggleSchemaButtonWithTaskButton, 10, 6, 52, 80, 2);
        tryButton        = new game.ClickableElement('trybutton', 'Try again', getTaskFromServer, 10, 6, 38, 80, 2);
        sameTaskButton   = new game.ClickableElement('sametaskbutton', 'Try Again', showSameTask, 10, 6, 38, 80, 2);

        nextTaskButton   = new game.ClickableElement('nexttaskbutton', 'New Task', getTaskFromServer, 10, 6, 52, 80, 2);
        
        aceButton        = new game.ClickableElement('acebutton', 'Ace Editor', getAceEditor, 73, 6, 12, 50, 2);
        /*
        codemirrorButton = new game.ClickableElement('codemirrorbutton', 'CodeMirror Editor', getCodemirrorEditor, 73, 6, 12, 50, 2);
        */

        checkButton      = new game.ClickableElement('checkbutton', 'Check', checkAnswer, 10, 6, 24, 80, 2);
        
        likeButton       = new game.ClickableElement('likebutton', '', likeIt, 2, 3, 70, 80, 2);
        dislikeButton    = new game.ClickableElement('dislikebutton', '', dislikeIt, 2, 3, 76, 80, 2);
        reviewButton     = new game.ClickableElement('reviewbutton', '', needReview, 2, 3, 82, 80, 2); // needs image
        
        textIn = new Object();
        
        submitButton.hide();
        schemaButton.hide();
        taskButton.hide();
        tryButton.hide();
        sameTaskButton.hide();
        nextTaskButton.hide();

    	if (game.task.kind != 3) {
            checkButton.hide();
    	}


        me.game.world.addChild(textOut);
        me.game.world.addChild(submitButton);
        me.game.world.addChild(schemaButton);
        me.game.world.addChild(taskButton);
        me.game.world.addChild(backButton);
        $("#backButton").fadeIn(100);
        me.game.world.addChild(tryButton);
        me.game.world.addChild(sameTaskButton);
        me.game.world.addChild(nextTaskButton);
        me.game.world.addChild(checkButton);
        me.game.world.addChild(likeButton);
        me.game.world.addChild(dislikeButton);
        me.game.world.addChild(reviewButton);
        
        me.game.world.addChild(aceButton);

        aceButton.display();
        
        likeButton.setImage("assets/data/img/stuff/thumbs_up.png", "thumbup");
        dislikeButton.setImage("assets/data/img/stuff/thumbs_down.png", "thumbdown");
        reviewButton.setImage("assets/data/img/buttons/magnifier.png", "review");

        
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
                needReview   : 0,
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
        	needReview   : 0,
        	
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
         * codemirrorButton
         */
        /*
        function getCodemirrorEditor() {
        	var codeMirrorEditor = new game.TextInputElement('textarea', 'codemirror', 'wrapperInputCM', 'fieldInputCM', 73, 25, 12, 50, 7);
        	me.game.world.addChild(codeMirrorEditor);
        	codemirrorButton.hide();
        	textIn = codeMirrorEditor;
        	textIn.display();
        	if (!tryButton.visibility) {
        		submitButton.display();
        	}
        }
        */
        
		/**
		 * aceButton
		 */
		function getAceEditor() {
			var aceEditor = new game.TextInputElement('pre', 'ace', 'wrapperInputAce', 'fieldInputAce', 71, 25, 12, 44, 7);
			me.game.world.addChild(aceEditor);
			aceButton.hide();
			textIn = aceEditor;
			textIn.display();
			if (!tryButton.visibility) {
				submitButton.display();
			}
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
            schemaButton.display();
        }
        
        /**
         * schemaButton and taskButton
         */
        function toggleSchemaButtonWithTaskButton() {
        	if (schemaButton.visibility == true) {
        		schemaButton.hide();
        		taskButton.display();
        		textOut.clear();
        		textOut.writePara('Schema', 'schematitle');
        		var schema = renderSchema(dataTask.schema);
        		textOut.writeHTML(schema, 'schematext');
        	}
        	else {
        		taskButton.hide();
        		schemaButton.display();
        		textOut.clear();
        		writeHeadline();
        		textOut.writePara(dataTask.exercise, 'taskbody');
        	}	
        };
	    
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
        	
            textOut.clear();
            
        	if ((game.task.kind == 0) || (game.task.kind == 1)) {
        	    // Story Collector
        	    textOut.writePara(game.task.name, 'tasktitle');
            }
            
            if (game.task.kind == 2) {
        	    // Trivia
        	    textOut.writePara('Difficulty: ' + game.task.difficulty, 'tasktitle');
            }
            
            if (game.task.kind == 3) {
        	    // Homework
        	    textOut.writePara('Exercise', 'tasktitle');
            }
            
        };
        
        //
        // callback functions for ajax requests
        //
        
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
                //dataTask.schema = renderSchema(dataTask.schema);

                // write data
                writeHeadline();
                textOut.clear();
                textOut.writePara(dataTask.exercise, 'taskbody');

                // buttons to hide
                tryButton.hide();
                nextTaskButton.hide();
                sameTaskButton.hide();

                // buttons to display
                submitButton.display();
                schemaButton.display();

                likeButton.display();
                dislikeButton.display();
                reviewButton.display();

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
            schemaButton.hide();
            taskButton.hide();
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
