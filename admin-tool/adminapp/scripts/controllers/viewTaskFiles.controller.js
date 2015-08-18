(function () {


angular
    .module('app')
    .controller('viewTaskFilesController', viewTaskFilesController);

    viewTaskFilesController.$inject = ['$scope', '$modalInstance', 'forHomeworkObject'];
    function viewTaskFilesController($scope, $modalInstance, forHomeworkObject) {
        var vm = this;

        console.log(forHomeworkObject);

        $scope.taskFiles = forHomeworkObject.taskFiles;
        $scope.title = forHomeworkObject.name;

        $scope.close = function() {
            $modalInstance.dismiss("closed");
        }

        $scope.viewStudents = function(taskFileIndex) {
            $modalInstance.close($scope.taskFiles[taskFileIndex]);
        }
    }


})();

