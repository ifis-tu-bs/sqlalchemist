(function() {
    angular
        .module('app')
        .factory('TaskService', TaskService);

    TaskService.$inject = ['$http'];
    function TaskService($http) {
        var service = {};

        /* TaskSetControlls */
        service.createTaskSet = createTaskSet;
        service.getAllTaskSets = getAllTaskSets;
        service.editTaskSet = editTaskSet;
        service.viewTaskSet = viewTaskSet;
        service.rateTaskSet = rateTaskSet;
        service.postTaskSetComment = postTaskSetComment;

        /* TaskControlls */
        service.createTask = createTask;
        service.editTask = editTask;


        service.getSubTask = getSubTask
        //service.rateSubTask = rateSubTask;
        service.postSubTaskComment = postSubTaskComment;
        service.createHomeWork = createHomeWork;
        //service.getAllHomeworks = getAllHomeworks;
        service.deleteHomework = deleteHomework;
        service.getAllSubmits = getAllSubmits;
        service.getSubmitsForSubTaskAndHomeWork = getSubmitsForSubTaskAndHomeWork;

        return service;


        /*
         * TaskSet Controlls
         */

        function createTaskSet(json) {
            return $http.post('/TaskSet/', json).then(handleSuccess, handleError);
        }

        function getAllTaskSets() {
            return $http.get('/TaskSet/').then(handleSuccess, handleError);
        }

        function editTaskSet(json, taskSetId) {
            return $http.patch('/TaskSet/' + taskSetId + "/", json).then(handleSuccess, handleError);
        }

        function viewTaskSet(json, taskSetId) {
            return $http.get('/TaskSet/' + taskSetId, json).then(handleSuccess, handleError);
        }

        function rateTaskSet(id, ratingJson) {
            return $http.post("/TaskSet/" + id + "/rate", ratingJson).then(handleSuccess, handleError);
        }

        function postTaskSetComment(id, comment) {
            return $http.post('/TaskSet/' + id + '/comment', {text: comment}).then(handleSuccess, handleError);
        }

        /*
         * Task Controlls
         */

        function createTask(taskSetId, taskJson) {
            return $http.post('/TaskSet/' + taskSetId + '/Task', taskJson).then(handleSuccess, handleError);
        }

        function editTask(taskId, taskJson) {
            return $http.patch('/Task/' + taskId + '/', taskJson).then(handleSuccess, handleError);
        }

        function rateTask(id, ratingJson) {
            return $http.post("/task/" + id + "/rate", ratingJson).then(handleSuccess, handleError);
        }




        function createHomeWork(dateFromUTCString, dateToUTCString, taskFiles, name) {
            return $http.post("/homework",{from: dateFromUTCString, to: dateToUTCString, tasks: taskFiles, name: name}).then(handleSuccess, handleError);
        }




        function deleteHomework(id) {
            return $http.get('/homework/delete/' + id).then(handleSuccess, handleError);
        }

        function getAllTasks() {
            return $http.get('/taskFile').then(handleSuccess, handleError);
        }

        function viewTaskSet(id) {
            return $http.get('/taskFile/' + id).then(handleSuccess, handleError);
        }

        function getSubTask(id) {
            return $http.get('/task/' + id).then(handleSuccess, handleError);
        }

        function postSubTaskComment(id, comment) {
            return $http.post('/task/' + id + '/comment', {text: comment}).then(handleSuccess, handleError);
        }


        function getAllSubmits(homeworkId, taskFileName) {
            return $http.post('/homework/result', {homework: homeworkId, taskFile: taskFileName}).then(handleSuccess, handleError);
        }

        function getSubmitsForSubTaskAndHomeWork(subtaskId, homeworkId) {
            return $http.get('/homework/result/' + subtaskId + '/' + homeworkId).then(handleSuccess, handleError);
        }
        function handleSuccess(data) {
            return data.data;
        }

        function handleError(data) {
            return {error: true, message: data.data};
        }

    }



})();

