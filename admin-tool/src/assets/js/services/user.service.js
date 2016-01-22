(function () {
    'use strict';

    angular
        .module('app')
        .factory('UserService', UserService);

    UserService.$inject = ['$http', '$q', '$timeout'];
    function UserService($http, $q, $timeout) {
        var service = {};

        service.getAllUsers = getAllUsers;
        service.resetPassword = resetPassword;
        service.promoteUser = promoteUser;
        service.getUserByUsername = getUserByUsername;

        service.getAllRoles = getAllRoles;
        service.updateRole = updateRole;
        service.deleteRole = deleteRole;
        service.createRole = createRole;
        service.getRoleById = getRoleById;

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

        function getUserByUsername(username) {
            return $http.get("/API/User/" + username + "/").then(handleSuccess, handleError);
        }

        /* Role Calls */

        function getAllRoles() {
            return $http.get("/API/Role/").then(handleSuccess, handleError);
        }

        function createRole(roleObject) {
            return $http.post("/API/Role/", roleObject).then(handleSuccess, handleError);
        }

        function updateRole(role) {
            return $http.post("/API/Role/" + role.id + "/", role).then(handleSuccess, handleError);
        }

        function deleteRole(role) {
            return $http.delete("/API/Role/" + role.id + "/").then(handleSuccess, handleError);
        }

        function getRoleById(roleID) {
            return $http.get("/API/Role/" + roleID + "/").then(handleSuccess, handleError);
        }

        /* decapsulate XHR - Methods */

        function handleSuccess(data) {
            return data.data;
        }

        function handleError(error) {
            if (error.status === 500) {
                throw "500 Internal Server Error";
            }
            throw error.data;
        }
    }

})();
