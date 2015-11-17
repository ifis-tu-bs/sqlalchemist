/**********************************************************************************************************************
*   Controller for views/users.view.html
*
*   Manages Comment displacement and entering of new Comments.
**********************************************************************************************************************/

(function () {


angular
    .module('app')
    .controller('UsersController', UsersController);

    UsersController.$inject = ['$scope', '$modal', 'UserService', 'FlashService', '$filter'];
    function UsersController($scope, $modal, UserService, FlashService, $filter) {
        var vm = this;
        vm.users = [];

        $scope.search = '';
        $scope.orderReverse = false;
        $scope.orderPredicate = 'email';

        initController();

        function initController() {
            getAllUsers();
        }

        function getAllUsers() {
            UserService.getAllUsers().then(
                function (result) {
                    vm.users = result;
                }, function (error) {
                    FlashService.Error(error);
                }
            );
        }

        $scope.promote = function(user, role) {
            console.log(user);
            UserService.promoteUser(user.id, role).then(
                function (result) {
                    initController();
                }, function (error) {
                    FlashService.Error(error);
                }
            );
        }

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

