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
        if (xhr.status == 400 || xhr.status == 200 || xhr.status == 401) {
            console.log(xhr);
            callback(xhr);
        } else if (xhr.status == 404 || xhr.status == 408 || xhr.status == 444 ||
                   xhr.status == 503 || xhr.status == 504) {
            console.log(xhr);
            alert("Something went wrong, please check your internet connection!");
            return;
        } else {
            if (xhr.status == 403){
                if (me.state.isCurrent(STATE_LOGIN)) {
                    alert("wrong e-mail or password!");
                }
                return;
            }
            console.log(xhr);
            alert("Internal server error. Please try again, later!");
            return;
        }
    };

    return xhr;
}



/* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */
/* ~~~~~~~~~~~~~~~~ Defining the Request-Send-Functions ~~~~~~~~~~~~~~~~~~~~ */
/* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

/* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Session ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

// ~ POST /API/login ~ jsonData: JSON.login
function ajaxSendLoginRequest(jsonData, onload) {
    var xmlHttpRequest = createRequest("POST", "/API/login", onload);

    xmlHttpRequest.send(jsonData);

    return xmlHttpRequest;
}

// ~ GET /API/logout
function ajaxSendLogoutRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/API/logout", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

/* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ User ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

// ~ POST /API/signup ~ jsonData: JSON.signup
function ajaxSendSignupRequest(jsonData, onload) {
    var xmlHttpRequest = createRequest("POST", "/API/signup", onload);

    xmlHttpRequest.send(jsonData);

    return xmlHttpRequest;
}

// ~ POST /API/users ~ jsonData: JSON.user
function ajaxSendUsersRequest(jsonData, onload) {
    var xmlHttpRequest = createRequest("POST", "/API/users", onload);

    xmlHttpRequest.send(jsonData);

    return xmlHttpRequest;
}

// ~ DELETE /API/users
function ajaxSendUsersDeleteRequest(onload) {
    var xmlHttpRequest = createRequest("DELETE", "/API/users", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ POST /API/users/password
function ajaxSendUsersResetPasswordRequest(jsonData, onload) {
    var xmlHttpRequest = createRequest("POST", "/API/users/password", onload);

    xmlHttpRequest.send(jsonData);

    return xmlHttpRequest;
}

// ~ GET /API/users/student
function ajaxSendUserStudentRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/API/users/student", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}


/* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Profile ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

// ~ GET /API/profile/homework
function ajaxSendProfileHomeworkRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/API/profile/homework", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ GET /API/profile
function ajaxSendProfileRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/API/profile", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ GET /API/profile/:id ~ id: the ProfileID
function ajaxSendProfileIdRequest(id, onload) {
    var xmlHttpRequest = createRequest("GET", "/API/profile/" + id, onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ GET /API/profile/character
function ajaxSendProfileCharacterRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/API/profile/character", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ GET /API/profile/inventory
function ajaxSendProfileInventoryRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/API/profile/character", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

/* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Item ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

// ~ GET /API/profile/character
function ajaxSendProfileBeltRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/API/profile/belt", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ POST /API/profile/belt ~ jsonData: JSON.belt
function ajaxSendProfileBeltSetRequest(jsonData, onload) {
    var xmlHttpRequest = createRequest("POST", "/API/profile/belt", onload);

    xmlHttpRequest.send(jsonData);

    return xmlHttpRequest;
}

// ~ GET /API/profile/used/:potion,slot ~ potion: PotionID slot: SlotID ~ jsonData: JSON.potionused
function ajaxSendProfileUsedRequest( slot, onload) {
    var xmlHttpRequest = createRequest("GET", "/API/profile/used/" + slot, onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ POST /API/profile/collected ~ jsonData: JSON.collected
function ajaxSendProfileCollectedRequest(jsonData, onload) {
    var xmlHttpRequest = createRequest("POST", "/API/profile/collected", onload);

    xmlHttpRequest.send(jsonData);

    return xmlHttpRequest;
}

/* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Settings ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

// ~ GET /API/profile/settings
function ajaxSendProfileSettingsRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/API/profile/settings", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ POST /API/profile/settings ~ jsonData: JSON.settings
function ajaxSendProfileSettingsSetRequest(jsonData, onload) {
    var xmlHttpRequest = createRequest("POST", "/API/profile/settings", onload);

    xmlHttpRequest.send(jsonData);

    return xmlHttpRequest;
}

/* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Challenge ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

// ~ GET /API/challenge/reset
function ajaxSendChallengeResetRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/API/challenge/reset", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ GET /API/challenge
function ajaxSendChallengeRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/API/challenge", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ GET /API/challenge/skip
function ajaxNextChallengeRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/API/challenge/skip", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ GET /API/challenge ~ id: ChallengeID
function ajaxSendChallengeIDRequest(id, onload) {
    var xmlHttpRequest = createRequest("GET", "/API/challenge/" + id, onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ POST /API/challenge ~ jsonData: JSON.challenge
function ajaxSendChallengeCreateRequest(jsonData, onload) {
    var xmlHttpRequest = createRequest("POST", "/API/challenge", onload);

    xmlHttpRequest.send(jsonData);

    return xmlHttpRequest;
}

// ~ GET /API/challenge/story
function ajaxSendChallengeStoryRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/API/challenge/story", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ GET  /API/profile/avatar/:id
function ajaxSendProfileAvatarIdRequest(id, onload) {
    var xmlHttpRequest = createRequest("GET", "/API/profile/avatar/"+ id, onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ GET /API/challenge/trivia
function ajaxSendChallengeTriviaRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/API/challenge/trivia", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ GET /API/challenge/homework
function ajaxSendChallengeHomeworkRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/API/challenge/homework", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

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
    var xmlHttpRequest = createRequest("POST", "/API/task/" + id + "/comment", onload);

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

// ~ POST /API/task/:id/comment ~ id: TaskSetID, jsonData: JSON.comment
function ajaxSendTaskSetIdCommentSetRequest(id, jsonData, onload) {
    var xmlHttpRequest = createRequest("POST", "/API/task/" + id + "/comment", onload);

    xmlHttpRequest.send(jsonData);

    return xmlHttpRequest;
}


/* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Shop ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

// ~ GET /API/shop
function ajaxSendShopRequest(onload) {
  var xmlHttpRequest = createRequest("GET", "/API/shop/avatar", onload);

  xmlHttpRequest.send();

  return xmlHttpRequest;
}

// ~ GET /API/shop/belt
function ajaxSendShopBeltRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/API/shop/belt", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ GET /API/shop/:id ~ id: ItemID, [jsonData: JSON.shopitem]
function ajaxSendShopIdRequest(id, onload) {
    var xmlHttpRequest = createRequest("GET", "/API/shop/" + id, onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ GET /API/shop/belt/:id ~ id: ItemID, [jsonData: JSON.shopitem]
function ajaxSendShopBeltIdRequest(id, onload) {
    var xmlHttpRequest = createRequest("GET", "/API/shop/belt/" + id, onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

/* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Highscore ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

// ~ GET /API/highscore/points
function ajaxSendHighscorePointsRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/API/highscore/points", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ GET /API/highscore/time
function ajaxSendHighscoreTimeRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/API/highscore/time", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ GET /API/highscore/runs
function ajaxSendHighscoreRunsRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/API/highscore/runs", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ GET /API/highscore/sql
function ajaxSendHighscoreSQLRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/API/highscore/sql", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ GET /API/highscore/rate
function ajaxSendHighscoreRateRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/API/highscore/rate", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ GET /API/highscore/coins
function ajaxSendHighscoreCoinsRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/API/highscore/coins", onload);

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