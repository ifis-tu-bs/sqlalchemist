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

    makeHWController.$inject = ['TaskService', '$scope', '$modalInstance', 'FlashService'];
    function makeHWController(TaskService, $scope, $modalInstance, FlashService) {
        var vm = this;
        vm.selectedTaskSets = [];



        $scope.datePickerFromData = new Date();
        $scope.datePickerToData = new Date();


        $scope.ok = function() {
            $scope.datePickerFromData.setSeconds(0);
            $scope.datePickerFromData.setMinutes(0);
            $scope.datePickerFromData.setHours(0);

            $scope.datePickerToData.setSeconds(59);
            $scope.datePickerToData.setMinutes(59);
            $scope.datePickerToData.setHours(23);
        };

        $scope.close = function() {
            FlashService.Clear();
            $modalInstance.dismiss('Closed');
        };

    }

})();

