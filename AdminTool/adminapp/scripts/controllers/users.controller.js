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
                    if (result.error) {
                        FlashService.Error(result.message);
                    } else {
                        vm.users = result.data;
                    }
                }
            );
        }

        $scope.promote = function(user, role) {
            console.log(user);
            UserService.promoteUser(user.id, role).then(
                function (result) {
                    if (result.error) {
                        FlashService.Error(result.message);
                    } else {
                        initController();
                    }
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

