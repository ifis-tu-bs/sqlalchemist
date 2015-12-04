(function () {
    'use strict';

    angular
        .module('app')
        .factory('UserService', UserService);

    UserService.$inject = ['$http'];
    function UserService($http) {
        var service = {};

        service.GetByUsername = GetByUsername;
        service.getAllUsers = getAllUsers;
        service.resetPassword = resetPassword;
        service.promoteUser = promoteUser;

        return service;

        function resetPassword(resetCode, password) {
            return $http.post('/API/Users/dopasswordreset/' + resetCode, {newpassword: password}).then(handleSuccess, handleError);
        }

        function getAllUsers() {
            return $http.get("/API/Users").then(handleSuccess, handleError);
        }

        function promoteUser(userId, roleId) {
            return $http.post("/API/Users/"+ userId, {role: roleId}).then(handleSuccess, handleError);
        }



        function GetById(id) {
            return $http.get('/API/Users/' + id).then(handleSuccess, handleError('Fehler beim Abrufen vom Benutzername'));
        }

        function GetByUsername(username) {
            return $http.get('/API/Users/' + username).then(handleSuccess, handleError('Fehler beim Abrufen des Benutzernamens'));
        }

        function Create(user) {
            return $http.post('/API/Users', user).then(handleSuccess, handleError('Fehler beim Erstellen des Benutzers'));
        }

        function Update(user) {
            return $http.put('/API/Users/' + user.id, user).then(handleSuccess, handleError('Fehler beim Aktualisieren der Benutzer'));
        }

        function Delete(id) {
            return $http.delete('/API/Users/' + id).then(handleSuccess, handleError('Fehler beim löschen des Benutzers'));
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
