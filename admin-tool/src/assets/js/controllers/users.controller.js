/**********************************************************************************************************************
*   Controller for views/users.view.html
*
*   Manages Comment displacement and entering of new Comments.
**********************************************************************************************************************/

(function () {


angular
    .module('app')
    .controller('UsersController', UsersController);

    UsersController.$inject = ['$scope', 'UserService', 'FlashService', '$filter', '$rootScope'];
    function UsersController($scope, UserService, FlashService, $filter, $rootScope) {
        var vm = this;
        vm.users = [];
        vm.roles = [];

        $scope.search = '';
        $scope.orderReverse = false;
        $scope.orderPredicate = 'email';
        $scope.mayChangeRoles = true;

        function initController() {
            getAllUsers();
            $scope.getAllRoles();
        }

        $scope.getAllRoles = function() {
            UserService.getAllRoles().then(
                function (data) {
                console.log(data);
                    if (data.id) {
                        console.log("Yep");
                        $scope.mayChangeRoles = false;
                    } else {
                        vm.roles = data;
                    }
                },
                function (error) {
                    FlashService.Error(error);
                }
            );
        };

        function getAllUsers() {
            UserService.getAllUsers().then(
                function (result) {
                    vm.users = result;
                }, function (error) {
                    FlashService.Error(error);
                }
            );
        }

        $scope.promote = function(user) {
            UserService.promoteUser(user).then(
                initController,
                function (error) {
                    FlashService.Error(error);
                }
            );
        };

        initController();

        //////////////////////////////777
        //  Array functions
        //////////////////////////////777

        function findInArray(array, item) {
            for (var i = 0; i < array.length; i++) {
                if (array[i].id === item.id) {
                    return i;
                }
            }
            return -1;
        }
    }
})();
