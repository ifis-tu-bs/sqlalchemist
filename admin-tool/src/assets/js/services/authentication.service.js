(function () {
    'use strict';

    angular
        .module('app')
        .factory('AuthenticationService', AuthenticationService);

    AuthenticationService.$inject = ['$http', '$cookies', '$rootScope', '$timeout'];
    function AuthenticationService($http, $cookies, $rootScope, $timeout) {
        var service = {};

        service.Session = Session;

        service.Login = Login;
        service.SetCredentials = SetCredentials;
        service.ClearCredentials = ClearCredentials;

        return service;

        function Login(username, password) {
            return $http.post('/API/Login', { email: username, password: password, adminTool: true});
        }

        function Logout() {
            $http.delete('/API/Logout').then(
                    ClearCredentials
            );
        }

        function SetCredentials() {
            $rootScope.session = {
                loggedIn : true
            };
            $cookies.putObject('session', $rootScope.session);
        }

        function ClearCredentials() {
            $rootScope.session = {};
            $cookies.remove('session');
        }

        function Session() {
            $http.get('/API/Session/').then(function(response) {
                    if (response.data.owner !== "")
                        service.SetCredentials();
                },
                ClearCredentials
            );
        }
    }

})();
