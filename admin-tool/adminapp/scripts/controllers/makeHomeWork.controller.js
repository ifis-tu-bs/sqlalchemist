(function () {


angular
    .module('app')
    .controller('makeHWController', makeHWController);

    makeHWController.$inject = ['TaskService', '$scope', '$modalInstance', 'TaskFilesForHomeWork', 'FlashService'];
    function makeHWController(TaskService, $scope, $modalInstance, TaskFilesForHomeWork, FlashService) {
        var vm = this;
        console.log(TaskFilesForHomeWork);
        $scope.datePickerFromData = new Date();
        $scope.datePickerToData = new Date();


        $scope.ok = function() {
            $scope.datePickerFromData.setSeconds(0);
            $scope.datePickerFromData.setMinutes(0);
            $scope.datePickerFromData.setHours(0);

            $scope.datePickerToData.setSeconds(59);
            $scope.datePickerToData.setMinutes(59);
            $scope.datePickerToData.setHours(23);

            TaskService.createHomeWork($scope.datePickerFromData.getTime(),
                    $scope.datePickerToData.getTime(),
                    TaskFilesForHomeWork,
                    $scope.name)
                .then(
                    function (result) {
                        console.log(result);
                        if (result.error) {
                            FlashService.Error(result.message);
                        } else {
                            FlashService.Clear();
                            $modalInstance.close();
                        }
                    }
                );
        };

        $scope.close = function() {
            FlashService.Clear();
            $modalInstance.dismiss('Closed');
        };

    }

})();

