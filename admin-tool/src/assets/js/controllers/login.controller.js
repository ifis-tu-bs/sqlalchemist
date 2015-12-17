/**********************************************************************************************************************
*   Controller for views/login.view.html
*
*   Handles the Login view. Not too special.
*   The Authentication service is used for the actual Login.
**********************************************************************************************************************/

(function () {
    'use strict';

    angular
        .module('app')
        .controller('LoginController', LoginController);

    LoginController.$inject = ['$location', 'AuthenticationService', 'FlashService'];
    function LoginController($location, AuthenticationService, FlashService) {
        var vm = this;

        vm.login = login;

        (function initController() {
            // reset login status
            AuthenticationService.ClearCredentials();
        })();

        function login() {
            vm.dataLoading = true;
            AuthenticationService.Login(vm.username, vm.password).then(
                AuthenticationService.Session,
                function (response) {
                    FlashService.Error(response.data);
                    vm.dataLoading = false;
                }
            );
        }
    }

})();
