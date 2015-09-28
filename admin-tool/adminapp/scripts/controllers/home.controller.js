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

    HomeController.$inject = ['UserService', '$rootScope'];
    function HomeController(UserService, $rootScope) {

        var vm = this;

        initController();

        function initController() {
        }


    }

})();

