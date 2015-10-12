(function () {
    'use strict';

    angular
        .module('app', ['ngRoute', 'ngCookies', 'ui.bootstrap', 'ui.codemirror'])
        .config(config)
        .run(run)
        .value('ui.codemirror.config', {
                   codemirror: {
                       mode: 'text/x-php',
                       lineNumbers: true,
                       matchBrackets: true
                   }
               });

    config.$inject = ['$routeProvider', '$locationProvider'];
    function config($routeProvider, $locationProvider) {
        $routeProvider
            .when('/', {
                controller: 'HomeController',
                templateUrl: 'adminapp/views/home.view.html',
                controllerAs: 'vm'
            })

            .when('/home', {
                controller: 'HomeController',
                templateUrl: 'adminapp/views/home.view.html',
                controllerAs: 'vm'
            })

            .when('/login', {
                controller: 'LoginController',
                templateUrl: 'adminapp/views/login.view.html',
                controllerAs: 'vm'
            })

            .when('/task', {
                controller: 'TasksController',
                templateUrl: 'adminapp/views/task.view.html',
                controllerAs: 'vm',
                name : 'Tasks',
                see: ['/home', '/task', '/homework', '/users']
            })

            .when('/homework', {
                controller: 'HomeworkController',
                templateUrl: 'adminapp/views/homework.view.html',
                controllerAs: 'vm',
                name : 'Homework',
                see: ['/home', '/task', '/homework', '/users']
            })

            .when('/users', {
                controller: 'UsersController',
                templateUrl: 'adminapp/views/users.view.html',
                controllerAs: 'vm',
                name : 'Users',
                see: ['/home', '/task', '/homework', '/users']
            })

            .when('/passwordreset/:code', {
                controller: 'PasswordResetController',
                templateUrl: 'adminapp/views/passwordReset.view.html',
                controllerAs: 'vm'
            })

            .otherwise({ redirectTo: '/' });
    }

    run.$inject = ['$rootScope', '$location', '$cookieStore', '$http'];
    function run($rootScope, $location, $cookieStore, $http) {
        // bleibt der Benutzer nach der Aktualisierung der Seite angemeldet
        $rootScope.globals = $cookieStore.get('globals') || {};
        $rootScope.Homework = $rootScope.Homework || {};
        $rootScope.Tasks = $rootScope.Tasks || {};

        if ($rootScope.globals.currentUser) {
            $http.defaults.headers.common['Authorization'] = 'Basic ' + $rootScope.globals.currentUser.authdata;
        }

        $rootScope.$on('$locationChangeStart', function (event, next, current) {
            // Umleitung zur Login-Seite in, wenn der Benutzer nicht angemeldet und versucht, einen eingeschränkten Seite zuzugreifen
             var restrictedPage = $.inArray($location.path(), ['/login', '/register']) === -1 && !$location.path().startsWith('/passwordreset/');;
            var loggedIn = $rootScope.globals.currentUser;
            if (restrictedPage && !loggedIn) {
                $location.path('/login');
            }
        });
    }

})();