/**********************************************************************************************************************
*   Controller for templates/makeHomeWork.template.html
*
*   Builds a Homework out of the Selected TaskFiles in /tasks.
*   The TaskService is used for the actual creation.
**********************************************************************************************************************/

(function () {


angular
    .module('app')
    .controller('makeHWController', makeHWController);

    makeHWController.$inject = ['TaskService', '$scope', 'FlashService'];
    function makeHWController(TaskService, $scope, FlashService) {
        var vm = this;

        $scope.taskSets=[];
        $scope.selectedTaskSets = [];
        $scope.orderReverse = false;
        $scope.orderTaskSetsPredicate = "updatedAt";
        $scope.orderSelectedTaskSetsPredicate = "taskSetName";

        var getTaskSets = function () {
            TaskService.getAllHomeWorkTaskSets().then(
                function (result) {
                    $scope.taskSets = result;

                    console.log(vm.taskSets);
                },
                function (error) {
                    $modalInstance.dismiss(error);
                }
                );
        }

        getTaskSets();

        $scope.selectTaskSet = function (taskSet) {
            var index = findInArray($scope.taskSets, taskSet);
            $scope.selectedTaskSets.push(taskSet)
            $scope.taskSets.splice(index, 1);
        }

        $scope.deselectTaskSet = function (taskSet) {
            var index = findInArray($scope.selectedTaskSets, taskSet);
            $scope.taskSets.push(taskSet)
            $scope.selectedTaskSets.splice(index, 1);
        }

        // We need a non-primitive, else the accoridion is going do mess with the scopes
        $scope.dates = {
            from: new Date(),
            to: new Date()
        }

        $scope.ok = function() {
            $scope.dates.from.setSeconds(0);
            $scope.dates.from.setMinutes(0);
            $scope.dates.from.setHours(0);

            $scope.dates.to.setSeconds(59);
            $scope.dates.to.setMinutes(59);
            $scope.dates.to.setHours(23);

            var taskSetIds = [];
            for (var i = 0; i < $scope.selectedTaskSets.length; i++) {
                taskSetIds.push($scope.selectedTaskSets[i].id);
            }

            var hw = {
                homeWorkName: $scope.homeWorkName,
                taskSetIds: taskSetIds,
                start_at: $scope.dates.from.valueOf(),
                expire_at: $scope.dates.to.valueOf()
            };

            $scope.$close(hw);
        };

        $scope.close = function() {
            FlashService.Clear();
            $scope.$dismiss('Closed');
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

