/**********************************************************************************************************************
*   Controller for templates/viewStatement.template.html
*
*   Manages displacement of a Statement.
**********************************************************************************************************************/

(function () {


angular
    .module('app')
    .controller('viewStatementController', viewStatementController);

    viewStatementController.$inject = ['TaskService', '$scope', '$modalInstance', 'viewStatement'];
    function viewStatementController(TaskService, $scope, $modalInstance, viewStatement) {
        var vm = this;

        $scope.textAreaStatement = viewStatement;


        $scope.close = function() {
            $modalInstance.close(null);
        };


    }

})();

