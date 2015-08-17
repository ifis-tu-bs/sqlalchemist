(function () {


angular
    .module('app')
    .controller('viewSubTaskController', viewSubTaskController);

    viewSubTaskController.$inject = ['TaskService', '$scope', '$modalInstance', 'subTaskData'];
    function viewSubTaskController(TaskService, $scope, $modalInstance, subTaskData) {
        var vm = this;

        $scope.textAreaTask = subTaskData.task;
        $scope.textAreaSolve = subTaskData.solve;

        $scope.close = function() {
            $modalInstance.close(null);
        };
    }

})();

