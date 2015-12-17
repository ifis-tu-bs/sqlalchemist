(function () {
    'use strict';

    angular
        .module('app')
        .factory('AuthenticationService', AuthenticationService);

    AuthenticationService.$inject = ['$http', '$cookies', '$rootScope', '$timeout', '$location', 'UserService', '$q'];
    function AuthenticationService($http, $cookies, $rootScope, $timeout, $location, UserService, $q) {
        var service = {};

        service.Session = Session;

        service.Login = Login;
        service.Logout = Logout;
        service.SetCredentials = SetCredentials;
        service.ClearCredentials = ClearCredentials;

        return service;

        function Login(username, password) {
            return $http.post('/API/Login', { email: username, password: password});
        }

        function Logout() {
            $http.delete('/API/Logout').then( function (response) {
                service.ClearCredentials();
                service.Session();
            },
            function (error) {
                console.log(error);
                service.Session();
            }
            );
        }

        function SetCredentials(role) {
            $rootScope.session = {
                loggedIn : role
            };
            $cookies.putObject('session', $rootScope.session);
        }

        function ClearCredentials() {
            $rootScope.session = {};
            $cookies.remove('session');
        }

        function Session() {
            return $http.get('/API/Session/').then(function(response) {
                    if (response.data.owner === "") {
                        service.ClearCredentials();
                        $location.path('/login');
                        return $q.reject("Not Logged in.");
                    } else {
                        return $q.resolve(response.data);
                    }
                }, function (error) {
                    return $q.reject(error.data);
                }
            ).then(
                function(data) {
                    return UserService.getUserByUsername(data.owner);
                },
                function(error) {
                    return $q.reject(error);
                }
            ).then(
                function (data) {
                    return UserService.getRoleById(data.roleID);
                },
                function(error) {
                    return $q.reject(error);
                }
            ).then(
                function (data) {
                    service.SetCredentials(data);
                    if ($location.url() === "/login"){
                        $location.path("/home");
                    }
                },
                function(error) {
                    service.ClearCredentials();
                    if (error !== "Not Logged in.") {
                        console.log("Error: ");
                        console.log(error);
                    }
                }
            );

        }
    }

})();
