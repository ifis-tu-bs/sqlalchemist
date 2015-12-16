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

        service.getAllRoles = getAllRoles;
        service.updateRole = updateRole;
        service.deleteRole = deleteRole;
        service.createRole = createRole;

        return service;

        function resetPassword(resetCode, password) {
            return $http.post('/API/User/dopasswordreset/' + resetCode, {newpassword: password}).then(handleSuccess, handleError);
        }

        function getAllUsers() {
            return $http.get("/API/User/").then(handleSuccess, handleError);
        }

        function promoteUser(user) {
            return $http.post("/API/User/"+ user.username + "/", {roleID: user.roleID}).then(handleSuccess, handleError);
        }

        function getAllRoles() {
            return $http.get("/API/Role/").then(handleSuccess, handleError);
        }

        function createRole(roleObject) {
            return $http.post("/API/Role/", roleObject).then(handleSuccess, handleError);
        }

        function updateRole(role) {
            return $http.post("/API/Role/" + role.id + "/").then(handleSuccess, handleError);
        }

        function deleteRole(role) {
            return $http.delete("/API/Role/" + role.id + "/").then(handleSuccess, handleError);
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
