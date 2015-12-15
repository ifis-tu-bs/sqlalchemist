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
                templateUrl: 'assets/views/home.view.html',
                controllerAs: 'vm'
            })

            .when('/home', {
                controller: 'HomeController',
                templateUrl: 'assets/views/home.view.html',
                controllerAs: 'vm'
            })

            .when('/login', {
                controller: 'LoginController',
                templateUrl: 'assets/views/login.view.html',
                controllerAs: 'vm'
            })

            .when('/task', {
                controller: 'TasksController',
                templateUrl: 'assets/views/task.view.html',
                controllerAs: 'vm',
                name : 'Tasks',
                see: ['/home', '/task', '/homework', '/users']
            })

            .when('/homework', {
                controller: 'HomeworkController',
                templateUrl: 'assets/views/homework.view.html',
                controllerAs: 'vm',
                name : 'Homework',
                see: ['/home', '/task', '/homework', '/users']
            })

            .when('/users', {
                controller: 'UsersController',
                templateUrl: 'assets/views/users.view.html',
                controllerAs: 'vm',
                name : 'Users',
                see: ['/home', '/task', '/homework', '/users']
            })

            .when('/passwordreset/:code', {
                controller: 'PasswordResetController',
                templateUrl: 'assets/views/passwordReset.view.html',
                controllerAs: 'vm'
            })

            .when('/roles', {
                controller: 'RoleController',
                templateUrl: 'assets/views/role.view.html',
                controllerAs: 'vm',
                name: 'Roles',
                see: true
            })

            .otherwise({ redirectTo: '/' });
    }

    run.$inject = ['$rootScope', '$location', '$cookies', '$http', 'AuthenticationService'];
    function run($rootScope, $location, $cookies, $http, AuthService) {
        // See if we got a Session stored...
        $rootScope.session = $cookies.getObject('session') || {};
        $rootScope.Homework = $rootScope.Homework || {};
        $rootScope.Tasks = $rootScope.Tasks || {};

        AuthService.Session();

        $rootScope.$on('$locationChangeStart', function (event, next, current) {
            // redirect to login if not having a session stored
            var restrictedPage = $.inArray($location.path(), ['/login']) === -1 && !$location.path().startsWith('/passwordreset/');
            var loggedIn = $rootScope.session.loggedIn;
            if (restrictedPage && !loggedIn) {
                $location.path('/login');
            }
        });
    }

})();
