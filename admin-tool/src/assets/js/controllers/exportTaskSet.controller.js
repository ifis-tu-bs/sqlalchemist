/**********************************************************************************************************************
*   Controller for templates/makeHomeWork.template.html
*
*   Builds a Homework out of the Selected TaskFiles in /tasks.
*   The TaskService is used for the actual creation.
**********************************************************************************************************************/

(function () {


angular
    .module('app')
    .controller('exportTaskSetController', exportTaskSetController);

    exportTaskSetController.$inject = ['TaskService', '$scope', 'FlashService'];
    function exportTaskSetController(TaskService, $scope, FlashService) {
        var vm = this;

        $scope.taskSets=[];
        $scope.selectedTaskSets = [];
        $scope.orderReverse = false;
        $scope.orderTaskSetsPredicate = "updatedAt";
        $scope.orderSelectedTaskSetsPredicate = "taskSetName";

        var getTaskSets = function () {
            TaskService.getAllTaskSets().then(
                function (result) {
                    $scope.taskSets = result;

                    console.log(vm.taskSets);
                },
                function (error) {
                    $modalInstance.dismiss(error);
                }
                );
        };

        getTaskSets();

        $scope.selectTaskSet = function (taskSet) {
            var index = findInArray($scope.taskSets, taskSet);
            $scope.selectedTaskSets.push(taskSet);
            $scope.taskSets.splice(index, 1);
        };

        $scope.deselectTaskSet = function (taskSet) {
            var index = findInArray($scope.selectedTaskSets, taskSet);
            $scope.taskSets.push(taskSet);
            $scope.selectedTaskSets.splice(index, 1);
        };

        $scope.ok = function() {

            var taskSetIds = [];
            for (var i = 0; i < $scope.selectedTaskSets.length; i++) {
                taskSetIds.push($scope.selectedTaskSets[i].id);
            }

            $scope.$close(taskSetIds);
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
