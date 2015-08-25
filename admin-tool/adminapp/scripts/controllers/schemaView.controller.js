(function () {


angular
    .module('app')
    .controller('schemaViewController', schemaViewController);

    schemaViewController.$inject = ['TaskService', '$scope', '$modalInstance', 'schemaObject'];
    function schemaViewController(TaskService, $scope, $modalInstance, schemaObject) {
        var vm = this;

        $scope.textAreaData = schemaObject;


        $scope.close = function() {
            $modalInstance.close(null);
        };
    }

})();

