/**********************************************************************************************************************
*   Controller for templates/sure.template.html
*
*   Are you really sure you want to read this?
**********************************************************************************************************************/

(function () {


angular
    .module('app')
    .controller('sureTemplateController', sureTemplateController);

    sureTemplateController.$inject = ['$scope', '$modalInstance', 'sureTemplateMessage'];
    function sureTemplateController($scope, $modalInstance, sureTemplateMessage) {
        var vm = this;

        $scope.message = sureTemplateMessage;

        $scope.ok = function() {
            $modalInstance.close(true);
        };

        $scope.close = function() {
            $modalInstance.dismiss("closed");
        };
    }


})();
