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
            // reseten des login status 
            AuthenticationService.ClearCredentials();
        })();

        function login() {
            vm.dataLoading = true;
            AuthenticationService.Login(vm.username, vm.password, function (response) {
                console.log(response.username);
                AuthenticationService.SetCredentials(vm.username, vm.password);
                $location.path('/');
            },
            function (response) {
                FlashService.Error(response);
                vm.dataLoading = false;
            });
        };
    }

})();

