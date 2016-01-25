/* Handles all the Ajax Requests */

/** Create the XMLHttpRequest object.
 *
 *  @param method: "POST"/"GET"/etc
 *  @param url: the URL to send the Request
 *  @param callback: the callback function to be called on ServerResponse.
 *                   We serve the xmlHttpRequest for that function.
 *  @return xmlHttpRequest ready to be send()
 */
function createRequest(method, url, callback) {

    var xhr = new XMLHttpRequest();

    if ("withCredentials" in xhr) {
        // XHR for Chrome/Firefox/Opera/Safari.
        xhr.open(method, url, true);
    } else if (typeof XDomainRequest != "undefined") {
        // XDomainRequest for IE.
        xhr = new XDomainRequest();
        xhr.open(method, url);
    } else {
        // CORS not supported.
        xhr = null;
    }

    xhr.setRequestHeader('Content-type', 'application/json; charset=utf-8');
    xhr.withCredentials = true;
    xhr.onload = function () {
        var notificationElement;
        if (xhr.status == 400 || xhr.status == 200 || xhr.status == 401) {
            console.log(xhr);
        } else if (xhr.status == 404 || xhr.status == 408 || xhr.status == 444 ||
                   xhr.status == 503 || xhr.status == 504) {
            console.log(xhr);
            notificationElement = new game.fdom.NotificationElement(rootContainer, "Sorry!", "Something went wrong, please check your internet connection!");
            me.game.world.addChild(notificationElement);
            return;
        } else {
            if (xhr.status == 403){
                if (me.state.isCurrent(STATE_LOGIN)) {
                    notificationElement = new game.fdom.NotificationElement(rootContainer, "LogIn failed!", "wrong e-mail or password!");
                    me.game.world.addChild(notificationElement);
                }
                return;
            }
            console.log(xhr);
            notificationElement = new game.fdom.NotificationElement(rootContainer, "Sorry!", "Internal server error. Please try again, later!");
            me.game.world.addChild(notificationElement);
            return;
        }
        console.log(xhr);
        callback(xhr);
    };

    return xhr;
}



/* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */
/* ~~~~~~~~~~~~~~~~ Defining the Request-Send-Functions ~~~~~~~~~~~~~~~~~~~~ */
/* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

/* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Session ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

// ~ POST /API/Session
function ajaxGetSession(onload) {
    var xmlHttpRequest = createRequest("GET", "/API/Session/", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ POST /API/login ~ jsonData: JSON.login
function ajaxSendLoginRequest(jsonData, onload) {
    var xmlHttpRequest = createRequest("POST", "/API/Login", onload);

    xmlHttpRequest.send(jsonData);

    return xmlHttpRequest;
}

// ~ GET /API/Logout
function ajaxSendLogoutRequest(onload) {
    var xmlHttpRequest = createRequest("DELETE", "/API/Logout", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

/* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ User ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

// ~ POST /API/User/ ~ jsonData: JSON.signup
function ajaxCreateUser(jsonData, onload) {
    var xmlHttpRequest = createRequest("POST", "/API/User/", onload);

    xmlHttpRequest.send(jsonData);

    return xmlHttpRequest;
}

// ~ GET /API/User/ ~ jsonData: JSON.user
function ajaxGetUsers( onload) {
    var xmlHttpRequest = createRequest("GET", "/API/User/", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}


// ~ GET /API/User/:username/ ~ jsonData: JSON.user
function ajaxGetUser(username, onload) {
    var xmlHttpRequest = createRequest("GET", "/API/User/" + username + "/", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ POST /API/User/:username/ ~ jsonData: JSON.user
function ajaxUpdateUser(username, jsonData, onload) {
    var xmlHttpRequest = createRequest("POST", "/API/User/" + username + "/", onload);

    xmlHttpRequest.send(jsonData);

    return xmlHttpRequest;
}

// ~ DELETE /API/User/:username/
function ajaxDeleteUser(username, onload) {
    var xmlHttpRequest = createRequest("DELETE", "/API/User/" + username + "/", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}


// ~ GET /API/Users/student
function ajaxSendUserStudentRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/API/User/student", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}


/* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ PasswordController~~~~~~~~~~~~~~~~~~~~~~~~ */

// ~ POST /API/Users/ForgotPassword
function ajaxCreatePasswordResetRequest(jsonData, onload) {
    var xmlHttpRequest = createRequest("POST", "/API/Password/", onload);

    xmlHttpRequest.send(jsonData);

    return xmlHttpRequest;
}

// ~ POST /API/Users/ForgotPassword
function ajaxUpdatePassword(username, jsonData, onload) {
    var xmlHttpRequest = createRequest("POST", "/API/Password/" + username + "/", onload);

    xmlHttpRequest.send(jsonData);

    return xmlHttpRequest;
}

/* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ PasswordController~~~~~~~~~~~~~~~~~~~~~~~~ */

// ~ POST /API/Validate/Student/:username
function ajaxVerifyStudent(username, jsonData, onload) {
    var xmlHttpRequest = createRequest("POST", "/API/Validate/Student/" + username, onload);

    xmlHttpRequest.send(jsonData);

    return xmlHttpRequest;
}


/* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Profile ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

// ~ GET /API/Profile/homework
function ajaxSendProfileHomeworkRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/API/Profile/homework", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ GET /API/Profile
function ajaxSendProfileRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/API/Profile", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ GET /API/Profile/:id ~ id: the ProfileID
function ajaxSendProfileIdRequest(id, onload) {
    var xmlHttpRequest = createRequest("GET", "/API/Profile/" + id, onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ GET /API/Profile/character
function ajaxSendProfileCharacterRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/API/Profile/character", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ GET /API/Profile/inventory
function ajaxSendProfileInventoryRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/API/Profile/inventory", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

/* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Item ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

// ~ GET /API/Profile/belt
function ajaxSendProfileBeltRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/API/Profile/belt", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ POST /API/profile/belt ~ jsonData: JSON.belt
function ajaxSendProfileBeltSetRequest(jsonData, onload) {
    var xmlHttpRequest = createRequest("POST", "/API/Profile/belt", onload);

    xmlHttpRequest.send(jsonData);

    return xmlHttpRequest;
}

// ~ GET /API/Profile/used/:potion,slot ~ potion: PotionID slot: SlotID ~ jsonData: JSON.potionused
function ajaxSendProfileUsedRequest( slot, onload) {
    var xmlHttpRequest = createRequest("GET", "/API/Profile/used/" + slot, onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ POST /API/Profile/collected ~ jsonData: JSON.collected
function ajaxSendProfileCollectedRequest(jsonData, onload) {
    var xmlHttpRequest = createRequest("POST", "/API/Profile/collected", onload);

    xmlHttpRequest.send(jsonData);

    return xmlHttpRequest;
}

/* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Challenge ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

// ~ GET /API/Challenge/reset
function ajaxSendChallengeResetRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/API/Challenge/reset", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

/*/ ~ GET /API/Challenge
function ajaxSendChallengeRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/API/Challenge", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}*/

// ~ GET /API/Challenge/skip
function ajaxNextChallengeRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/API/Challenge/skip", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

/*/ ~ GET /API/Challenge ~ id: ChallengeID
function ajaxSendChallengeIDRequest(id, onload) {
    var xmlHttpRequest = createRequest("GET", "/API/Challenge/" + id, onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}*/

// ~ POST /API/Challenge ~ jsonData: JSON.challenge
function ajaxSendChallengeCreateRequest(jsonData, onload) {
    var xmlHttpRequest = createRequest("POST", "/API/Challenge", onload);

    xmlHttpRequest.send(jsonData);

    return xmlHttpRequest;
}

// ~ GET /API/Challenge/story
function ajaxSendChallengeStoryRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/API/Challenge/story", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ GET  /API/Profile/avatar/:id
function ajaxSendProfileAvatarIdRequest(id, onload) {
    var xmlHttpRequest = createRequest("GET", "/API/Profile/avatar/"+ id, onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

/*/ ~ GET /API/challenge/trivia
function ajaxSendChallengeTriviaRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/API/challenge/trivia", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}*/

/*/ ~ GET /API/challenge/homework
function ajaxSendChallengeHomeworkRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/API/challenge/homework", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}*/

/* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ SQLController ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

// ~ GET /API/SQL/Story/:id ~ id: StoryTaskID
function ajaxSendTaskStoryIDRequest(id, onload) {
    var xmlHttpRequest = createRequest("GET", "/API/SQL/Story/" + id + "/", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ POST /API/SQL/Story/:id ~ id: StoryTaskID, jsonData: JSON.task.solve
function ajaxSendTaskStorySolveRequest(id, jsonData, onload) {
    var xmlHttpRequest = createRequest("POST", "/API/SQL/Story/" + id + "/", onload);

    xmlHttpRequest.send(jsonData);

    return xmlHttpRequest;
}

// ~ GET /API/SQL/Trivia/:id: StoryTaskID | difficulty: DifficultyLevel
function ajaxSendTaskTriviaRequest(difficulty, onload) {
    var xmlHttpRequest = createRequest("GET", "/API/SQL/Trivia/" + difficulty + "/", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ GET /API/SQL/Trivia/:id/:stay StoryTaskID | difficulty: DifficultyLevel
function ajaxSendTaskTriviaStayRequest(difficulty, onload) {
    var xmlHttpRequest = createRequest("GET", "/API/SQL/Trivia/" + difficulty + "/stay", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ POST /API/SQL/Trivia/:id ~ id: TriviaTaskID, jsonData: JSON.task.solve
function ajaxSendTaskTriviaSolveRequest(id, jsonData, onload) {
    var xmlHttpRequest = createRequest("POST", "/API/SQL/Trivia/" + id + "/", onload);

    xmlHttpRequest.send(jsonData);

    return xmlHttpRequest;
}

// ~ GET /API/SQL/Homework/:HomeworId/:TaskId/ ~ id: HomeworkID, jsonData: JSON.task.solve
function ajaxSendTaskHomeworkRequest(HomeworkId, TaskId, onload) {
    var xmlHttpRequest = createRequest("GET", "/API/SQL/Homework/" + HomeworkId + "/" + TaskId + "/" , onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ POST /API/SQL/Homework/:HomeworId/:TaskId/ ~ id: HomeworkID, jsonData: JSON.task.solve
function ajaxSendTaskHomeworkSolveRequest( TaskId, jsonData, onload) {
    var xmlHttpRequest = createRequest("POST", "/API/SQL/Homework/" + TaskId + "/submit", onload);

    xmlHttpRequest.send(jsonData);

    return xmlHttpRequest;
}

// ~ POST /API/SQL/Homework/:HomeworId/:TaskId/ ~ id: HomeworkID, jsonData: JSON.task.solve
function ajaxSendTaskHomeworkSyntaxSolveRequest( TaskId, jsonData, onload) {
    var xmlHttpRequest = createRequest("POST", "/API/SQL/Homework/" + TaskId + "/" , onload);

    xmlHttpRequest.send(jsonData);

    return xmlHttpRequest;
}

/* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ TaskController ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

// ~ POST /API/TaskSet/:id/Task
function ajaxSendTaskOverTaskSetRequest(id, onload) {
    var xmlHttpRequest = createRequest("Post", "/API/TaskSet/" + id + "/Task", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ GET /API/Task
function ajaxSendTaskRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/API/Task/", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ GET /API/Task/:id/ ~ id: TaskID
function ajaxSendTaskIdRequest(id, onload) {
    var xmlHttpRequest = createRequest("GET", "/API/Task/" + id + "/", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ PATCH /API/Task/:id ~ id: TaskID, jsonData: JSON.task
function ajaxSendTaskIdPatchRequest(id, jsonData, onload) {
    var xmlHttpRequest = createRequest("PATCH", "/API/Task/" + id + "/", onload);

    xmlHttpRequest.send(jsonData);

    return xmlHttpRequest;
}

// ~ DELETE /API/Task/:id ~ id: TaskID, jsonData: JSON.task
function ajaxSendTaskIdDeleteRequest(id, jsonData, onload) {
    var xmlHttpRequest = createRequest("DELETE", "/API/Task/" + id + "/", onload);

    xmlHttpRequest.send(jsonData);

    return xmlHttpRequest;
}

// ~ POST /API/Task/:id/rate ~ id: TaskID, jsonData: JSON.rating
function ajaxSendTaskIdRatingRequest(id, jsonData, onload) {
    var xmlHttpRequest = createRequest("POST", "/API/Task/" + id + "/rate", onload);

    xmlHttpRequest.send(jsonData);

    return xmlHttpRequest;
}

// ~ POST /API/task/:id/comment ~ id: TaskID, jsonData: JSON.comment
function ajaxSendTaskIdCommentSetRequest(id, jsonData, onload) {
    var xmlHttpRequest = createRequest("POST", "/API/Task/" + id + "/comment", onload);

    xmlHttpRequest.send(jsonData);

    return xmlHttpRequest;
}

/* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ TaskSetController ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

// ~ POST /API/TaskSet
function ajaxSendTaskSetResolveRequest(id, onload) {
    var xmlHttpRequest = createRequest("Post", "/API/TaskSet", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ GET /API/TaskSet
function ajaxSendTaskSetRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/API/TaskSet", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ GET /API/TaskSet/:id ~ id: TaskSetID
function ajaxSendTaskSetIdRequest(id, onload) {
    var xmlHttpRequest = createRequest("GET", "/API/TaskSet/" + id, onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ PATCH /API/TaskSet/:id ~ id: TaskSetID, jsonData: JSON.task
function ajaxSendTaskSetIdPatchRequest(id, jsonData, onload) {
    var xmlHttpRequest = createRequest("PATCH", "/API/TaskSet/" + id, onload);

    xmlHttpRequest.send(jsonData);

    return xmlHttpRequest;
}

// ~ DELETE /API/TaskSet/:id ~ id: TaskSetID, jsonData: JSON.task
function ajaxSendTaskSetIdDeleteRequest(id, jsonData, onload) {
    var xmlHttpRequest = createRequest("DELETE", "/API/TaskSet/" + id, onload);

    xmlHttpRequest.send(jsonData);

    return xmlHttpRequest;
}

// ~ POST /API/TaskSet/:id/rate ~ id: TaskSetID, jsonData: JSON.rating
function ajaxSendTaskSetIdRatingRequest(id, jsonData, onload) {
    var xmlHttpRequest = createRequest("POST", "/API/TaskSet/" + id + "/rate", onload);

    xmlHttpRequest.send(jsonData);

    return xmlHttpRequest;
}

// ~ POST /API/TaskSet/:id/comment ~ id: TaskSetID, jsonData: JSON.comment
function ajaxSendTaskSetIdCommentSetRequest(id, jsonData, onload) {
    var xmlHttpRequest = createRequest("POST", "/API/TaskSet/" + id + "/comment", onload);

    xmlHttpRequest.send(jsonData);

    return xmlHttpRequest;
}


/* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Shop ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

// ~ GET /API/Shop
function ajaxSendShopRequest(onload) {
  var xmlHttpRequest = createRequest("GET", "/API/Shop/avatar", onload);

  xmlHttpRequest.send();

  return xmlHttpRequest;
}

// ~ GET /API/Shop/belt
function ajaxSendShopBeltRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/API/Shop/belt", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ GET /API/Shop/:id ~ id: ItemID, [jsonData: JSON.shopitem]
function ajaxSendShopIdRequest(id, onload) {
    var xmlHttpRequest = createRequest("GET", "/API/Shop/" + id, onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ GET /API/Shop/belt/:id ~ id: ItemID, [jsonData: JSON.shopitem]
function ajaxSendShopBeltIdRequest(id, onload) {
    var xmlHttpRequest = createRequest("GET", "/API/Shop/belt/" + id, onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

/* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Highscore ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

// ~ GET /API/Highscore/points
function ajaxSendHighscorePointsRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/API/Highscore/points", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ GET /API/Highscore/time
function ajaxSendHighscoreTimeRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/API/Highscore/time", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ GET /API/Highscore/runs
function ajaxSendHighscoreRunsRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/API/Highscore/runs", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ GET /API/Highscore/sql
function ajaxSendHighscoreSQLRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/API/Highscore/sql", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ GET /API/Highscore/rate
function ajaxSendHighscoreRateRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/API/Highscore/rate", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ GET /API/Highscore/coins
function ajaxSendHighscoreCoinsRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/API/Highscore/coins", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

/* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Homework ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

// ~ GET /API/homework/tasks
function ajaxSendCurrentHomeworkRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/API/Homework/student/", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}
