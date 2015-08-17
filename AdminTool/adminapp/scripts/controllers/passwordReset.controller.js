(function () {
    'use strict';

    angular
        .module('app')
        .controller('PasswordResetController', PasswordResetController);

    PasswordResetController.$inject = ['$routeParams', 'UserService', '$location', 'FlashService', '$scope'];
    function PasswordResetController($routeParams, UserService, $location, FlashService, $scope) {
        var vm = this;

        vm.done = false;
        vm.submit = submit;
        vm.passwordsMatch = passwordsMatch;

        (function initController() {

        })();

        function passwordsMatch() {
            return vm.password == vm.passwordRepeat;
        }

        function submit() {
            vm.dataLoading = true;
            UserService.resetPassword($routeParams.code, vm.password).then(
                function(data) {
                    console.log(data);
                    if(data.error) {
                        FlashService.Error(data.message);
                        vm.dataLoading = false;
                    } else {
                        FlashService.Success(data.data);
                        vm.dataLoading = false;
                        vm.done = true;
                    }
                });
        };
    }

})();

