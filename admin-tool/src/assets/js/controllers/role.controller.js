/**********************************************************************************************************************
*   Controller for views/homework.view.html
*
*   Manages the displacement of all Homeworks.
*
*   Page is orderd via:
*   Homeworks -> taskSets(in the selected HW) -> tasks(in the selected TF) -> Submits(of the selected ST)
*
*   Selected Items are stored in $scope, the recieved data from the server in vm
*
*   The Controller is handled in states to fit the current Selection
**********************************************************************************************************************/

(function () {


angular
    .module('app')
    .controller('RoleController', RoleController);

    RoleController.$inject = ['$scope', 'FlashService', 'UserService'];
    function RoleController($scope, FlashService, UserService) {
        var vm = this;
        vm.roles = [];
        $scope.show = {
            task: false,
            homework: true,
            role: true,
            group: true,
            user: true
        };

        initController();

        function initController() {
            UserService.getAllRoles().then(
                function (data) {
                    vm.roles = data;

        console.log(vm.roles);

                }, function(error) {
                    FlashService.Error(error);
                }
            );
        }

        $scope.saveRole = function(role) {
            if (role.id) {
                UserService.updateRole(role).then(
                    function (data) {
                        FlashService.Success("Saved Role.");
                    }, function (error) {
                        FlashService.Error(error);
                    }
                );
            } else {
                UserService.createRole(role).then(
                    function (data) {
                        FlashService.Success("Saved Role.");
                    }, function (error) {
                        console.log("ERROR");
                        FlashService.Error(error);
                    }
                );
            }
        };

        $scope.deleteRole = function(role) {
            if (role.id) {
                UserService.deleteRole(role).then(
                    function (data) {
                        FlashService.Success("Deleted Role.");
                        vm.roles.splice(findInArray(vm.roles, role), 1);
                    }, function (error) {
                        FlashService.Error(error);
                    }
                );
            } else {
                vm.roles.splice(findInArray(vm.roles, role), 1);
            }
        };


        var DefaultRole = function() {
            this.priority=0;
            this.roleName="";
            this.ownTaskSetPermissions = {
                    create:false,
                    read:false,
                    update:false,
                    delete:false
                };
            this.foreignTaskSetPermissions = {
                    create:false,
                    read:false,
                    update:false,
                    delete:false
                };
            this.ownTaskPermissions = {
                    create:false,
                    read:false,
                    update:false,
                    delete:false
                };
            this.foreignTaskPermissions =
                {
                    create:false,
                    read:false,
                    update:false,
                    delete:false
                };
            this.homeworkPermissions = {
                    create:false,
                    read:false,
                    update:false,
                    delete:false
                };
            this.rolePermissions = {
                    create:false,
                    read:false,
                    update:false,
                    delete:false
                };
            this.groupPermissions = {
                    create:false,
                    read:false,
                    update:false,
                    delete:false
                };
            this.userPermissions = {
                    create:false,
                    read:false,
                    update:false,
                    delete:false
                };
            this.votes = 200;
            this.deletable = true;
        };

        $scope.pushNewRole = function() {
            vm.roles.push(new DefaultRole());
        };

        function findInArray(array, item) {
            for (var i = 0; i < array.length; i++) {
                if (array[i].$$hashKey === item.$$hashKey) {
                    return i;
                }
            }
            return -1;
        }

    }

})();
