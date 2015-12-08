(function () {
    'use strict';

    angular
        .module('app')
        .factory('UserService', UserService);

    UserService.$inject = ['$http'];
    function UserService($http) {
        var service = {};

        service.getAllUsers = getAllUsers;
        service.resetPassword = resetPassword;
        service.promoteUser = promoteUser;

        return service;

        function resetPassword(resetCode, password) {
            return $http.post('/API/User/dopasswordreset/' + resetCode, {newpassword: password}).then(handleSuccess, handleError);
        }

        function getAllUsers() {
            return $http.get("/API/User/").then(handleSuccess, handleError);
        }

        function promoteUser(userId, roleId) {
            return $http.post("/API/User/"+ userId, {role: roleId}).then(handleSuccess, handleError);
        }

        // private Funktionen

        function handleSuccess(data) {
            return data.data;
        }

        function handleError(error) {
            throw error.data;
        }
    }

})();
