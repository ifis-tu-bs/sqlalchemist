/**********************************************************************************************************************
*   Controller for views/home.view.html
*
*   Has no real task.
**********************************************************************************************************************/

(function () {
    'use strict';

    angular
        .module('app')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'AuthenticationService'];
    function HomeController($scope, AuthenticationService) {

    }

})();

