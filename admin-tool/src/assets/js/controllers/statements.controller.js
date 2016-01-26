/**********************************************************************************************************************
*   Controller for templates/comment.template.html
*
*   Manages Comment displacement and entering of new Comments.
**********************************************************************************************************************/

(function () {


angular
    .module('app')
    .controller('StatementsController', StatementsController);

    StatementsController.$inject = ['TaskService', '$scope', '$modalInstance', 'sqlStatementsObject', 'FlashService'];
    function StatementsController(TaskService, $scope, $modalInstance, sqlStatementsObject, FlashService) {
        var vm = this;
        $scope.sqlStatements = "";

        $scope.aceLoaded = function(_editor) {
            // Options
            console.log("setreadonly");
            _editor.setReadOnly(true);
          };


        for (var i = 0; i< sqlStatementsObject.length; i++) {
            $scope.sqlStatements += "\n" + sqlStatementsObject[i];
        }

        $scope.ok = function() {
            $modalInstance.close();
        };

        $scope.close = function() {
            FlashService.Clear();
            $modalInstance.dismiss('Closed');
        };

    }

})();

