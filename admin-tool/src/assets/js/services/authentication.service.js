(function () {
    'use strict';

    angular
        .module('app')
        .factory('AuthenticationService', AuthenticationService);

    AuthenticationService.$inject = ['$http', '$cookies', '$rootScope', '$timeout'];
    function AuthenticationService($http, $cookies, $rootScope, $timeout) {
        var service = {};

        service.Login = Login;
        service.SetCredentials = SetCredentials;
        service.ClearCredentials = ClearCredentials;

        return service;

        function Login(username, password, callbackSuccess, callbackError) {
            $http.post('/API/Login', { email: username, password: password, adminTool: true })
                .success(function (response) {
                    callbackSuccess(response);
                })
                .error(function (response) {
                    callbackError(response);
                });
        }

        function SetCredentials(username, password) {
             $rootScope.session = {
                currentUser: {
                    sessionValid: true
                }
            };

            $cookies.putObject('session', $rootScope.session);
        }

        function ClearCredentials() {
            $rootScope.session = {};
            $cookies.remove('session');
        }
    }

})();
