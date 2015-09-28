/**********************************************************************************************************************
*   Controller for views/task.view.html
*
*   Shows all tasks and subtasks. Also allows to Rate/Comment/Edit.
**********************************************************************************************************************/

(function () {


angular
    .module('app')
    .controller('TaskController', TaskController);

    TaskController.$inject = ['TaskService', '$scope', '$modal', 'FlashService'];
    function TaskController(TaskService, $scope, $modal, FlashService) {
        var vm = this;

        vm.tasks = [{"subTasks": [{}]}];
            //[{"a": "a", "b": "b", "c": "c", "d": "d", "e": "e", "f": "f", "subTasks": [{'a':'a', 'b':'b', 'c': 'c', 'd': 'd'}] },
            //        {"a": "a", "b": "b", "c": "c", "d": "d", "e": "e", "f": "g", "subTasks": [{'a':'a', 'b':'b', 'c': 'c', 'd': 'd'},
            //                                                                                  {'a':'a', 'b':'b', 'c': 'c', 'd': 'e'}] }];
        vm.selectedTask = vm.tasks[0];

        $scope.orderTaskFilePredicate = "fileName";
        $scope.orderSubTaskPredicate = "id";
        $scope.orderReverseTaskFile = false;
        $scope.orderReverseSubTask = false;



        function initController() {
            var activeTask = vm.selectedTask;

            FlashService.Clear();
            getAllTasks(activeTask);

        }

        function getAllTasks(oldSelectedTask) {
            TaskService.getAllTasks().then(function(data) {
                var index = 0;

                vm.tasks = data;

                if ((index = findInArray(vm.tasks, oldSelectedTask)) != -1) {
                    $scope.selectTask(vm.tasks[index]);
                } else {
                    $scope.selectTask(vm.tasks[0]);
                }

            });
        }

        $scope.selectTask = function(selectTask) {
            vm.selectedTask = selectTask;
        }

        initController();

        /* ~~~~~~~~~~~~~~~~~~~~ Rating  ~~~~~~~~~~~~~~~~~~~~ */

        $scope.rateTaskFile = function (taskFile, decision) {
            var ratingJson = {};
            switch (decision) {
                case 'positive': {
                    ratingJson = {'positive': 1, 'needReview': 0, 'negative': 0};
                    break;
                }
                case 'needReview': {
                    ratingJson = {'positive': 0, 'needReview': 1, 'negative': 0};
                    break;
                }
                case 'negative': {
                    ratingJson = {'positive': 0, 'needReview': 0, 'negative': 1};
                    break;
                }
            }

            TaskService.rateTaskFile(taskFile.id, ratingJson).then(
                function (result) {
                    if (result.error) {
                        FlashService.Error(result.message);
                    } else {
                        initController();
                    }
            });

        }

        $scope.rateSubTask = function (subTask, decision) {

            var ratingJson = {};
            switch (decision) {
                case 'positive': {
                    ratingJson = {'positive': 1, 'needReview': 0, 'negative': 0};
                    break;
                }
                case 'needReview': {
                    ratingJson = {'positive': 0, 'needReview': 1, 'negative': 0};
                    break;
                }
                case 'negative': {
                    ratingJson = {'positive': 0, 'needReview': 0, 'negative': 1};
                    break;
                }
            }
            TaskService.rateSubTask(subTask.id, ratingJson).then(
                function (result) {
                    if (result.error) {
                        FlashService.Error(result.message)
                    } else {
                        initController();
                    }
            });
        }

        /* ~~~~~~~~~~~~~~~~~~~~ PopUps  ~~~~~~~~~~~~~~~~~~~~ */

        $scope.animationsEnabled = true;

        $scope.createTaskOpen = function (size) {
            var modalInstance = $modal.open({
                animation: $scope.animationsEnabled,
                templateUrl: 'adminapp/templates/editTask.template.html',
                controller: 'taskEditController',
                resolve: {
                     editTaskObject: function() {
                         return null;
                     }
                },
                windowClass: 'app-modal-window'
            });

            modalInstance.result.then(
                initController,
                FlashService.Clear
            );
        }

        $scope.editTask = function(editTask) {
            var modalInstance = $modal.open({
                animation: $scope.animationsEnabled,
                templateUrl: 'adminapp/templates/editTask.template.html',
                controller: 'taskEditController',
                resolve: {
                    editTaskObject: function() {
                        return editTask;
                    }
                },
                windowClass: 'app-modal-window'
            });

            modalInstance.result.then(
                initController,
                FlashService.Clear
            );
        }


        $scope.viewSubTask = function(viewSubTask) {
            var modalInstance = $modal.open({
                animation: $scope.animationsEnabled,
                templateUrl: 'adminapp/templates/viewSubTask.template.html',
                controller: 'viewSubTaskController',
                resolve: {
                    subTaskData: function() {
                        return { task: viewSubTask.exercise,
                                solve: viewSubTask.refStatement
                            };
                    }
                }
            });

            modalInstance.result.then(
                initController,
                FlashService.Clear
            );
        }

        $scope.viewSchema = function(viewSchemaTask) {
            var modalInstance = $modal.open({
                animation: $scope.animationsEnabled,
                templateUrl: 'adminapp/templates/schemaView.template.html',
                controller: 'schemaViewController',
                resolve: {
                    schemaObject: function() {
                        return viewSchemaTask.schema;
                    }
                }
            });
        }

        $scope.viewSubTaskComments = function(subTask) {
            var modalInstance = $modal.open({
                animation: $scope.animationsEnabled,
                templateUrl: 'adminapp/templates/comment.template.html',
                controller: 'CommentController',
                resolve: {
                    commentTaskObject: function() {
                        return {content: subTask, type: "subtask"};
                    }
                }
            });

            modalInstance.result.then(
                initController,
                FlashService.Clear
            );
        }

        $scope.viewTaskFileComments = function(taskFile) {
            var modalInstance = $modal.open({
                animation: $scope.animationsEnabled,
                templateUrl: 'adminapp/templates/comment.template.html',
                controller: 'CommentController',
                resolve: {
                    commentTaskObject: function() {
                        return {content: taskFile, type: "taskfile"};
                    }
                }
            });

            modalInstance.result.then(
                initController,
                FlashService.Clear
            );
        }

        $scope.makeHomeWork = function() {
            var taskFilesForHomework = [];

            FlashService.Clear();

            for (i = 0; i < vm.tasks.length; i++) {
                task = vm.tasks[i];
                if (task.checked) {
                    taskFilesForHomework.push(task.fileName);
                }

            }

            if (taskFilesForHomework.length != 0) {
                var modalInstance = $modal.open({
                    animation: $scope.animationsEnabled,
                    templateUrl: 'adminapp/templates/makeHomeWork.template.html',
                    controller: 'makeHWController',
                    resolve: {
                        TaskFilesForHomeWork: function() {
                            return taskFilesForHomework;
                        }
                    }
                });
            } else {
                FlashService.Error("Pick at least one TaskFile");
            }

        }

        function findInArray(array, item) {
            for (var i = 0; i < array.length; i++) {
                if (array[i].id === item.id) {
                    return i;
                }
            }
            return -1;
        }

    }



})();

