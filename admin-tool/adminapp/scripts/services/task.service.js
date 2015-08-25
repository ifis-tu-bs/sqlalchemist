(function() {
    angular
        .module('app')
        .factory('TaskService', TaskService);

    TaskService.$inject = ['$http'];
    function TaskService($http) {
        var service = {};

        service.createTaskFile = createTaskFile;
        service.editTaskFile = editTaskFile;
        service.getAllTasks = getAllTasks;
        service.getTaskFile = getTaskFile;
        service.getSubTask = getSubTask
        service.rateTaskFile = rateTaskFile;
        service.rateSubTask = rateSubTask;
        service.postSubTaskComment = postSubTaskComment;
        service.postTaskFileComment = postTaskFileComment;
        service.createHomeWork = createHomeWork;
        service.getAllHomeworks = getAllHomeworks;
        service.deleteHomework = deleteHomework;
        service.getAllSubmits = getAllSubmits;
        service.getSubmitsForSubTaskAndHomeWork = getSubmitsForSubTaskAndHomeWork;

        return service;

        function createTaskFile(xmlContent, isHomeWork) {
            return $http.post('/taskFile', {text: xmlContent, isHomeWork: isHomeWork}).then(handleSuccess, handleError);
        }

        function editTaskFile(xmlContent, isHomeWork, taskFileId) {
            return $http.post('/taskFile/' + taskFileId, {text: xmlContent, isHomeWork: isHomeWork}).then(handleSuccess, handleError);
        }

        function rateSubTask(id, ratingJson) {
            return $http.post("/task/" + id + "/rate", ratingJson).then(handleSuccess, handleError);
        }

        function createHomeWork(dateFromUTCString, dateToUTCString, taskFiles, name) {
            return $http.post("/homework",{from: dateFromUTCString, to: dateToUTCString, tasks: taskFiles, name: name}).then(handleSuccess, handleError);
        }

        function rateTaskFile(id, ratingJson) {
            return $http.post("/taskFile/" + id + "/rate", ratingJson).then(handleSuccess, handleError);
        }

        function getAllHomeworks() {
            return $http.get('/homework').then(handleSuccess, handleError);
        }

        function deleteHomework(id) {
            return $http.get('/homework/delete/' + id).then(handleSuccess, handleError);
        }

        function getAllTasks() {
            return $http.get('/taskFile').then(handleSuccess, handleError);
        }

        function getTaskFile(id) {
            return $http.get('/taskFile/' + id).then(handleSuccess, handleError);
        }

        function getSubTask(id) {
            return $http.get('/task/' + id).then(handleSuccess, handleError);
        }

        function postSubTaskComment(id, comment) {
            return $http.post('/task/' + id + '/comment', {text: comment}).then(handleSuccess, handleError);
        }

        function postTaskFileComment(id, comment) {
            return $http.post('/taskFile/' + id + '/comment', {text: comment}).then(handleSuccess, handleError);
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

        function handleError(error) {
            return {error: true, message: error.data};
        }

    }



})();

