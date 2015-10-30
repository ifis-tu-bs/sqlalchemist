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

// ~ POST /login ~ jsonData: JSON.login
function ajaxSendLoginRequest(jsonData, onload) {
    var xmlHttpRequest = createRequest("POST", "/login", onload);

    xmlHttpRequest.send(jsonData);

    return xmlHttpRequest;
}

// ~ GET /logout
function ajaxSendLogoutRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/logout", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

/* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ User ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

// ~ POST /signup ~ jsonData: JSON.signup
function ajaxSendSignupRequest(jsonData, onload) {
    var xmlHttpRequest = createRequest("POST", "/signup", onload);

    xmlHttpRequest.send(jsonData);

    return xmlHttpRequest;
}

// ~ POST /users ~ jsonData: JSON.user
function ajaxSendUsersRequest(jsonData, onload) {
    var xmlHttpRequest = createRequest("POST", "/users", onload);

    xmlHttpRequest.send(jsonData);

    return xmlHttpRequest;
}

// ~ DELETE /users
function ajaxSendUsersDeleteRequest(onload) {
    var xmlHttpRequest = createRequest("DELETE", "/users", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ POST /users/password
function ajaxSendUsersResetPasswordRequest(jsonData, onload) {
    var xmlHttpRequest = createRequest("POST", "/users/password", onload);

    xmlHttpRequest.send(jsonData);

    return xmlHttpRequest;
}

// ~ GET /users/student
function ajaxSendUserStudentRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/users/student", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}


/* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Profile ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

// ~ GET /profile/homework
function ajaxSendProfileHomeworkRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/profile/homework", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ GET /profile
function ajaxSendProfileRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/profile", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ GET /profile/:id ~ id: the ProfileID
function ajaxSendProfileIdRequest(id, onload) {
    var xmlHttpRequest = createRequest("GET", "/profile/" + id, onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ GET /profile/character
function ajaxSendProfileCharacterRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/profile/character", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ GET /profile/inventory
function ajaxSendProfileInventoryRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/profile/character", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

/* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Item ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

// ~ GET /profile/character
function ajaxSendProfileBeltRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/profile/belt", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ POST /profile/belt ~ jsonData: JSON.belt
function ajaxSendProfileBeltSetRequest(jsonData, onload) {
    var xmlHttpRequest = createRequest("POST", "/profile/belt", onload);

    xmlHttpRequest.send(jsonData);

    return xmlHttpRequest;
}

// ~ GET /profile/used/:potion,slot ~ potion: PotionID slot: SlotID ~ jsonData: JSON.potionused
function ajaxSendProfileUsedRequest( slot, onload) {
    var xmlHttpRequest = createRequest("GET", "/profile/used/" + slot, onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ POST /profile/collected ~ jsonData: JSON.collected
function ajaxSendProfileCollectedRequest(jsonData, onload) {
    var xmlHttpRequest = createRequest("POST", "/profile/collected", onload);

    xmlHttpRequest.send(jsonData);

    return xmlHttpRequest;
}

/* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Settings ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

// ~ GET /profile/settings
function ajaxSendProfileSettingsRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/profile/settings", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ POST /profile/settings ~ jsonData: JSON.settings
function ajaxSendProfileSettingsSetRequest(jsonData, onload) {
    var xmlHttpRequest = createRequest("POST", "/profile/settings", onload);

    xmlHttpRequest.send(jsonData);

    return xmlHttpRequest;
}

/* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Challenge ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

// ~ GET /challenge/reset
function ajaxSendChallengeResetRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/challenge/reset", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ GET /challenge
function ajaxSendChallengeRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/challenge", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ GET /challenge/skip
function ajaxNextChallengeRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/challenge/skip", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ GET /challenge ~ id: ChallengeID
function ajaxSendChallengeIDRequest(id, onload) {
    var xmlHttpRequest = createRequest("GET", "/challenge/" + id, onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ POST /challenge ~ jsonData: JSON.challenge
function ajaxSendChallengeCreateRequest(jsonData, onload) {
    var xmlHttpRequest = createRequest("POST", "/challenge", onload);

    xmlHttpRequest.send(jsonData);

    return xmlHttpRequest;
}

// ~ GET /challenge/story
function ajaxSendChallengeStoryRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/challenge/story", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ GET  /profile/avatar/:id
function ajaxSendProfileAvatarIdRequest(id, onload) {
    var xmlHttpRequest = createRequest("GET", "/profile/avatar/"+ id, onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ GET /challenge/trivia
function ajaxSendChallengeTriviaRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/challenge/trivia", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ GET /challenge/homework
function ajaxSendChallengeHomeworkRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/challenge/homework", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

/* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ SQLController ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

// ~ GET /SQL/Story/:id ~ id: StoryTaskID
function ajaxSendTaskStoryIDRequest(id, onload) {
    var xmlHttpRequest = createRequest("GET", "/SQL/Story/" + id + "/", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ POST /SQL/Story/:id ~ id: StoryTaskID, jsonData: JSON.task.solve
function ajaxSendTaskStorySolveRequest(id, jsonData, onload) {
    var xmlHttpRequest = createRequest("POST", "/SQL/Story/" + id + "/", onload);

    xmlHttpRequest.send(jsonData);

    return xmlHttpRequest;
}

// ~ GET /SQL/Trivia/:id: StoryTaskID | difficulty: DifficultyLevel
function ajaxSendTaskTriviaRequest(difficulty, onload) {
    var xmlHttpRequest = createRequest("GET", "/SQL/Trivia/" + difficulty + "/", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ GET /SQL/Trivia/:id/:stay StoryTaskID | difficulty: DifficultyLevel
function ajaxSendTaskTriviaStayRequest(difficulty, onload) {
    var xmlHttpRequest = createRequest("GET", "/SQL/Trivia/" + difficulty + "/stay", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ POST /SQL/Trivia/:id ~ id: TriviaTaskID, jsonData: JSON.task.solve
function ajaxSendTaskTriviaSolveRequest(id, jsonData, onload) {
    var xmlHttpRequest = createRequest("POST", "/SQL/Trivia/" + id + "/", onload);

    xmlHttpRequest.send(jsonData);

    return xmlHttpRequest;
}

// ~ GET /SQL/Homework
function ajaxSendTaskHomeworkRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/SQL/Homework/", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ POST /SQL/Homework/:id ~ id: HomeworkID, jsonData: JSON.task.solve
function ajaxSendTaskHomeworkSolveRequest(id, jsonData, onload) {
    var xmlHttpRequest = createRequest("POST", "/SQL/Homework/" + id + "/", onload);

    xmlHttpRequest.send(jsonData);

    return xmlHttpRequest;
}

/* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ TaskController ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

// ~ POST /TaskSet/:id/Task
function ajaxSendTaskOverTaskSetRequest(id, onload) {
    var xmlHttpRequest = createRequest("Post", "/TaskSet/" + id + "/Task", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ GET /Task
function ajaxSendTaskRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/Task/", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ GET /Task/:id/ ~ id: TaskID
function ajaxSendTaskIdRequest(id, onload) {
    var xmlHttpRequest = createRequest("GET", "/Task/" + id + "/", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ PATCH /Task/:id ~ id: TaskID, jsonData: JSON.task
function ajaxSendTaskIdPatchRequest(id, jsonData, onload) {
    var xmlHttpRequest = createRequest("PATCH", "/Task/" + id + "/", onload);

    xmlHttpRequest.send(jsonData);

    return xmlHttpRequest;
}

// ~ DELETE /Task/:id ~ id: TaskID, jsonData: JSON.task
function ajaxSendTaskIdDeleteRequest(id, jsonData, onload) {
    var xmlHttpRequest = createRequest("DELETE", "/Task/" + id + "/", onload);

    xmlHttpRequest.send(jsonData);

    return xmlHttpRequest;
}

// ~ POST /Task/:id/rate ~ id: TaskID, jsonData: JSON.rating
function ajaxSendTaskIdRatingRequest(id, jsonData, onload) {
    var xmlHttpRequest = createRequest("POST", "/Task/" + id + "/rate", onload);

    xmlHttpRequest.send(jsonData);

    return xmlHttpRequest;
}

// ~ POST /task/:id/comment ~ id: TaskID, jsonData: JSON.comment
function ajaxSendTaskIdCommentSetRequest(id, jsonData, onload) {
    var xmlHttpRequest = createRequest("POST", "/task/" + id + "/comment", onload);

    xmlHttpRequest.send(jsonData);

    return xmlHttpRequest;
}

/* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ TaskSetController ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

// ~ POST /TaskSet
function ajaxSendTaskSetResolveRequest(id, onload) {
    var xmlHttpRequest = createRequest("Post", "/TaskSet", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ GET /TaskSet
function ajaxSendTaskSetRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/TaskSet", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ GET /TaskSet/:id ~ id: TaskSetID
function ajaxSendTaskSetIdRequest(id, onload) {
    var xmlHttpRequest = createRequest("GET", "/TaskSet/" + id, onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ PATCH /TaskSet/:id ~ id: TaskSetID, jsonData: JSON.task
function ajaxSendTaskSetIdPatchRequest(id, jsonData, onload) {
    var xmlHttpRequest = createRequest("PATCH", "/TaskSet/" + id, onload);

    xmlHttpRequest.send(jsonData);

    return xmlHttpRequest;
}

// ~ DELETE /TaskSet/:id ~ id: TaskSetID, jsonData: JSON.task
function ajaxSendTaskSetIdDeleteRequest(id, jsonData, onload) {
    var xmlHttpRequest = createRequest("DELETE", "/TaskSet/" + id, onload);

    xmlHttpRequest.send(jsonData);

    return xmlHttpRequest;
}

// ~ POST /TaskSet/:id/rate ~ id: TaskSetID, jsonData: JSON.rating
function ajaxSendTaskSetIdRatingRequest(id, jsonData, onload) {
    var xmlHttpRequest = createRequest("POST", "/TaskSet/" + id + "/rate", onload);

    xmlHttpRequest.send(jsonData);

    return xmlHttpRequest;
}

// ~ POST /task/:id/comment ~ id: TaskSetID, jsonData: JSON.comment
function ajaxSendTaskSetIdCommentSetRequest(id, jsonData, onload) {
    var xmlHttpRequest = createRequest("POST", "/task/" + id + "/comment", onload);

    xmlHttpRequest.send(jsonData);

    return xmlHttpRequest;
}


/* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Shop ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

// ~ GET /shop
function ajaxSendShopRequest(onload) {
  var xmlHttpRequest = createRequest("GET", "/shop/avatar", onload);

  xmlHttpRequest.send();

  return xmlHttpRequest;
}

// ~ GET /shop/belt
function ajaxSendShopBeltRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/shop/belt", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ GET /shop/:id ~ id: ItemID, [jsonData: JSON.shopitem]
function ajaxSendShopIdRequest(id, onload) {
    var xmlHttpRequest = createRequest("GET", "/shop/" + id, onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ GET /shop/belt/:id ~ id: ItemID, [jsonData: JSON.shopitem]
function ajaxSendShopBeltIdRequest(id, onload) {
    var xmlHttpRequest = createRequest("GET", "/shop/belt/" + id, onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

/* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Highscore ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

// ~ GET /highscore/points
function ajaxSendHighscorePointsRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/highscore/points", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ GET /highscore/time
function ajaxSendHighscoreTimeRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/highscore/time", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ GET /highscore/runs
function ajaxSendHighscoreRunsRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/highscore/runs", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ GET /highscore/sql
function ajaxSendHighscoreSQLRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/highscore/sql", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ GET /highscore/rate
function ajaxSendHighscoreRateRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/highscore/rate", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}

// ~ GET /highscore/coins
function ajaxSendHighscoreCoinsRequest(onload) {
    var xmlHttpRequest = createRequest("GET", "/highscore/coins", onload);

    xmlHttpRequest.send();

    return xmlHttpRequest;
}