(function () {
    'use strict';

    angular
        .module('app', ['ngRoute', 'ngCookies', 'ngAnimate', 'ngSanitize', 'ui.bootstrap', 'ui.ace'])
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

    run.$inject = ['$rootScope', '$location', '$cookies', '$http'];
    function run($rootScope, $location, $cookies, $http) {
        // See if we got a Session stored...
        $rootScope.session = $cookies.getObject('session') || {};
        $rootScope.Homework = $rootScope.Homework || {};
        $rootScope.Tasks = $rootScope.Tasks || {};

        $rootScope.$on('$locationChangeStart', function (event, next, current) {
            // redirect to login if not having a session stored
            var restrictedPage = $.inArray($location.path(), ['/login']) === -1 && !$location.path().startsWith('/passwordreset/');;
            var loggedIn = $rootScope.session.currentUser;
            if (restrictedPage && !loggedIn) {
                $location.path('/login');
            }
        });
    }

})();