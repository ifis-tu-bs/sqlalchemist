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
        service.getAllHomeWorkTaskSets = getAllHomeWorkTaskSets;
        service.editTaskSet = editTaskSet;
        service.viewTaskSet = viewTaskSet;
        service.rateTaskSet = rateTaskSet;
        service.postTaskSetComment = postTaskSetComment;
        service.deleteTaskSet = deleteTaskSet;
        service.exportTaskSets = exportTaskSets;

        /* TaskControlls */
        service.createTask = createTask;
        service.editTask = editTask;
        service.rateTask = rateTask;
        service.deleteTask = deleteTask;
        service.postTaskComment = postTaskComment

        /* HomeWorkControlls*/
        service.getAllHomeworks = getAllHomeworks;
        service.createHomeWork = createHomeWork;
        service.deleteHomework = deleteHomework;
        service.getSubmitsForTaskInHomeWork = getSubmitsForTaskInHomeWork;

        /* Data */
        service.getColumnDefinitionDataTypes = getColumnDefinitionDataTypes;

        service.getAllSubmits = getAllSubmits;

        return service;


        /* TaskSet Controlls */

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

        function deleteTaskSet(taskId) {
            return $http.delete('/TaskSet/' + taskId + '/').then(handleSuccess, handleError);
        }

        function getAllHomeWorkTaskSets() {
            return $http.get('/TaskSet/homework').then(handleSuccess, handleError);
        }

        function exportTaskSets(taskSetIds) {
            return $http.post('/TaskSet/download', taskSetIds).then(function (result) {location.href = '/download/' + result.data}, handleError);
        }

        /* Task Controlls */

        function createTask(taskSetId, taskJson) {
            return $http.post('/TaskSet/' + taskSetId + '/Task', taskJson).then(handleSuccess, handleError);
        }

        function editTask(taskId, taskJson) {
            return $http.patch('/Task/' + taskId + '/', taskJson).then(handleSuccess, handleError);
        }

        function rateTask(taskId, ratingJson) {
            return $http.post("/Task/" + taskId + "/rate", ratingJson).then(handleSuccess, handleError);
        }

        function postTaskComment(taskId, comment) {
            return $http.post('/Task/' + taskId + '/comment', {text: comment}).then(handleSuccess, handleError);
        }

        function deleteTask(taskId) {
            $http.delete('/Task/'+ taskId + '/').then(handleSuccess, handleError);
        }


        /* HomeWork Controlls */

        function getAllHomeworks() {
            return $http.get('/Homework/').then(handleSuccess, handleError);
        }

        function createHomeWork(homeWorkJson) {
            return $http.post("/Homework/", homeWorkJson).then(handleSuccess, handleError);
        }

        function deleteHomework(id) {
            return $http.delete("/Homework/" + id + "/").then(handleSuccess, handleError);
        }

        function getSubmitsForTaskInHomeWork(taskId, homeworkId) {
            return $http.get('/Homework/result/' + taskId + '/' + homeworkId + '/').then(handleSuccess, handleError);
        }

        /* DataTypes */

        function getColumnDefinitionDataTypes() {
            return $http.get("/ColumnDefinition/DataTypes/").then(handleSuccess, handleError);
        }


        /* Left Overs*/


        function getAllTasks() {
            return $http.get('/taskFile').then(handleSuccess, handleError);
        }

        function getAllSubmits(homeworkId, taskFileName) {
            return $http.post('/homework/result', {homework: homeworkId, taskFile: taskFileName}).then(handleSuccess, handleError);
        }


        function handleSuccess(data) {
            return data.data;
        }

        function handleError(data) {
            throw data.data;
        }

    }



})();

