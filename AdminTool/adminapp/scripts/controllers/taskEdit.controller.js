(function () {


angular
    .module('app')
    .controller('taskEditController', taskEditController);

    taskEditController.$inject = ['TaskService', '$scope', '$modalInstance', 'editTaskObject', 'FlashService', '$timeout'];
    function taskEditController(TaskService, $scope, $modalInstance, editTaskObject, FlashService, $timeout) {
        var vm = this;
        vm.dataLoading = false;
        $scope.isCreate = false;
        $scope.isHomeWork = false;

        if (editTaskObject) {
            $scope.title = "Edit Task";
            TaskService.getTaskFile(editTaskObject.id).then(function(data) {
                $scope.textAreaData = data.xmlContent;
            });
            if (editTaskObject.isHomeWork) {
                $scope.isHomeWork = true;
            }
        } else {
            $scope.title = "Create Task";
            $scope.isCreate = true;

            //Due to a Code-Mirror Bug this has to be time-outed
            $timeout(function(){},50) .then(
                function() {
                    $scope.textAreaData = ('<?xml version="1.0" encoding="UTF-8"?>\n\n' +
                        '<tasks schemaversion="1" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="recipe.xsd">\n\n' +
                        '  <task taskid="YourTaskName(PleaseChangeThis)" querylanguage="SQL">\n\n' +
                        '     <titles>\n' +
                        '       <title language="EN">YourTitle</title>\n' +
                        '     </titles>\n\n' +
                        '     <schema>\n' +
                        '       <!-- "<intension> And <Extension> in here"-->\n' +
                        '     </schema>\n\n' +
                        '     <subtasks>\n' +
                        '       <!-- "<subtask>s in here" -->\n' +
                        '     </subtasks>\n\n' +
                        '   </task>\n\n' +
                        '</tasks>');
                    }
            );
        }

        $scope.ok = function() {
            vm.dataLoading = true;

            if (editTaskObject) {
                TaskService.editTaskFile($scope.textAreaData, $scope.isHomeWork, editTaskObject.id).then(
                    function (data) {
                        if(data.error) {
                            FlashService.Error(data.message);
                        } else {
                            FlashService.Clear();
                            $modalInstance.close(null);
                        }
                    }
                );

            } else {
                TaskService.createTaskFile($scope.textAreaData, $scope.isHomeWork).then(
                    function (data) {
                        if(data.error) {
                            FlashService.Error(data.message);
                        } else {
                            FlashService.Clear();
                            $modalInstance.close(null);
                        }
                    }
                );
            }




            $modalInstance.close(null);
        };

        $scope.close = function() {
            FlashService.Clear();
            $modalInstance.dismiss('Closed');
        };

    }

})();

