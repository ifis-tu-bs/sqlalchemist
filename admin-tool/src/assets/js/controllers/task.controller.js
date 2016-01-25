/**********************************************************************************************************************
*   Controller for views/task.view.html
*
*   Manages the displacement of all Homeworks.
*
*   Page is orderd via:
*   Homeworks -> TaskFiles(in the selected HW) -> SubTasks(in the selected TF) -> Submits(of the selected ST)
*
*   Selected Items are stored in $scope, the recieved data from the server in vm
*
*   The Controller is handled in states to fit the current Selection
**********************************************************************************************************************/

(function () {


angular
    .module('app')
    .controller('TasksController', TasksController);

    TasksController.$inject = ['$scope', '$uibModal', 'TaskService', 'FlashService', '$rootScope', '$location', '$filter', '$timeout', '$q'];
    function TasksController($scope, $uibModal, TaskService, FlashService, $rootScope, $location, $filter, $timeout, $q) {
        var vm = this;

        $scope.orderReverse = false;
        $scope.orderTaskSetPredicate = 'taskSetName';
        $scope.orderTaskPredicate = 'taskName';
        $scope.orderTablePredicate = 'tableName';
        $scope.search = '';

        $scope.dataTypes = [];
        $scope.tabActive = {
            intensionTables : false,
            intensionForeignKeys : false,
            extension : false,
            tasks : false
        };

        $scope.animationsEnabled = true;

        vm.taskSets = [];
        vm.tasks = [];
        vm.tables = [];
        vm.foreignKeys = [];
        $scope.selectedTaskSet = {};
        $scope.selectedTask = {};
        $scope.selectedTable = {};


        function initController() {
            getAllTaskSets();
            getColumnDefinitionDataTypes();
        }

        //////////////////////////////777
        //  Fetch initial Data
        //////////////////////////////777

        function getAllTaskSets() {
            if ($rootScope.Tasks.taskSets) {
                vm.taskSets = $rootScope.Tasks.taskSets;
                $scope.getCurrentPath();
            } else {
                TaskService.getAllTaskSets().then(
                        function (result) {
                            vm.taskSets = result;
                        }, function (error) {
                            FlashService.Error(error);
                        }
                );
            }
        }

        function getColumnDefinitionDataTypes() {
            TaskService.getColumnDefinitionDataTypes().then(
                function (result) {
                    vm.dataTypes = result;
                },
                null
            );
        }

        function getTasksForTaskSet(taskSet) {
            TaskService.getAllTasksForTaskSet(taskSet.id).then(
                function(result) {
                    if (result) {
                        vm.tasks = result;
                    } else {
                        vm.tasks = [];
                    }
                },
                function(error) {
                    FlashService.Error(error);
                }
            );
        }
        //////////////////////////////777
        //  Saving data when leaving View
        //  Restoring saved Data and Displaying the correct screen
        //////////////////////////////777

        $scope.keepTabActive = function () {
            $rootScope.Tasks.tabActive = $scope.tabActive;
        };

        $scope.keepTaskSets = function () {
            $rootScope.Tasks.taskSets = vm.taskSets;
        };

        $scope.$on('$locationChangeStart', function(event) {
            $scope.keepTabActive();
            $scope.keepTaskSets();
        });

        $scope.getCurrentPath = function () {
            var path = $rootScope.Tasks;

            //Find out if a TaskSet is currently selected
            if (path.selectedTaskSet) {
                $scope.tabActive = path.tabActive;
                $scope.selectTaskSet(path.selectedTaskSet);
            } else {
                return;
            }


        };

        //////////////////////////////777
        //  TaskSet - Control
        //////////////////////////////777


        /* Data */
        $scope.taskSetSelectionStatus = {
            taskSetSelected : false
        };

        var DefaultTaskSet = function() {
            this.taskSetName = "";
            this.tableDefinitions = [];
            this.foreignKeyRelations = [];
            this.tasks=[];
            this.isHomeWork = false;
        };


        /* Methods */
        $scope.pushNewTaskSet = function () {
            vm.taskSets.push(new DefaultTaskSet());
        };

        $scope.selectTaskSet = function(taskSet, destination) {
            var index = findInArray(vm.taskSets, taskSet);
            selectTaskSet(index);
            $scope.tabActive.intensionTables = true;
            getTasksForTaskSet(taskSet);
        };

        function selectTaskSet (taskSetIndex) {
            $scope.selectedTable = undefined;
            $scope.selectedTask = undefined;
            $scope.selectedTaskSet = vm.taskSets[taskSetIndex];
            vm.tables = $scope.selectedTaskSet.tableDefinitions;
            vm.foreignKeyRelations = $scope.selectedTaskSet.foreignKeyRelations;
            $rootScope.Tasks.selectedTaskSet = $scope.selectedTaskSet;
            $scope.tableSelectionStatus.tableNotSelected = true;
            $scope.tableSelectionStatus.tableSelected = false;
        }

        $scope.viewTaskSetComments = function(taskSet) {
            var modalInstance = $uibModal.open({
                    animation: $scope.animationsEnabled,
                    templateUrl: 'assets/templates/comment.template.html',
                    controller: 'CommentController',
                    resolve: {
                        commentTaskObject: function() {
                            return {content: taskSet, type: 'taskSet'};
                        }
                    }
            });

            modalInstance.result.then(
                FlashService.Clear
            );
        };


        /* Server Side Methods */
        $scope.saveTaskSet = function(taskSet) {
            taskSet.sqlstatements = undefined;

            console.log(taskSet);

            if (taskSet.id !== undefined) {
                return TaskService.editTaskSet(taskSet, taskSet.id).then(
                        function (result) {
                            FlashService.Success("Updated TaskSet");

                        }, function (error) {
                            FlashService.Error(error);
                        }
                );

            } else {
                return TaskService.createTaskSet(taskSet).then(
                        function (result) {
                            FlashService.Success("Created new TaskSet");
                            vm.taskSets[findInArray(vm.taskSets, taskSet)] = result;

                        }, function (error) {
                            FlashService.Error(error);

                        }
                );

            }
        };

        $scope.exportTaskSets = function() {
            var modalInstance = $uibModal.open({
                    animation: $scope.animationsEnabled,
                    templateUrl: 'assets/templates/exportTaskSet.template.html',
                    controller: 'exportTaskSetController'
            });

            modalInstance.result.then(
                function (taskSetIds) {
                    return TaskService.exportTaskSets({taskSetIds: taskSetIds});
                }, null
            );
        };

        $scope.importTaskSets = function() {
            var modalInstance = $uibModal.open({
                    animation: $scope.animationsEnabled,
                    templateUrl: 'assets/templates/importTaskSet.template.html',
                    controller: 'importTaskSetController'
            });
        };

        $scope.saveSelectedTaskSet = function () {
             $scope.saveTaskSet($scope.selectedTaskSet);
        };

        $scope.deleteTaskSet = function (taskSet) {
            if (taskSet.id === undefined) {
                vm.taskSets.splice(findInArray(vm.taskSets, taskSet), 1);
                return;
            }

            var modalInstance = $uibModal.open({
                    animation: $scope.animationsEnabled,
                    templateUrl: 'assets/templates/sure.template.html',
                    controller: 'sureTemplateController',
                    resolve: {
                        sureTemplateMessage: function () {
                                return "<span style='font-size:15pt'> Are you sure you want to delete TaskSet: <b>" + taskSet.taskSetName + "</b>? <br> It will be deleted forever!</span>";
                            }
                    }
            });

            modalInstance.result
            .then(
                function () {
                    return TaskService.deleteTaskSet(taskSet.id);
                },
                function () {
                    return $q.reject({noerror: true});
                }
            ).then(
                function () {
                    vm.taskSets.splice(findInArray(vm.taskSets, taskSet), 1);
                },
                function (error) {
                    if (!error.noerror)
                        FlashService.Error(error);
                }
            );
        };

        $scope.rateTaskSet = function (taskSet, decision) {
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

            TaskService.rateTaskSet(taskSet.id, ratingJson).then(
                function (result) {
                    vm.taskSets[findInArray(vm.taskSets, taskSet)] = result;

                }, function (error) {
                    FlashService.Error(error);

                });
        };


        //////////////////////////////777
        //  Intension: TableDefinition - Control
        //////////////////////////////777

        /* Data */
        $scope.tableSelectionStatus = {
            tableSelected : false,
            tableNotSelected : true
        };

        var DefaultTable = function () {
            this.tableName = "";
            this.columnDefinitions = [];
            this.extension = "";
        };

        /* Methods */
        $scope.pushNewTable = function () {
            vm.tables.push(new DefaultTable());
        };

        $scope.selectTable = function (table) {
            var index = findInArray(vm.tables, table);
            selectTable(index);
            $scope.tableSelectionStatus.tableNotSelected = false;
            $scope.tableSelectionStatus.tableSelected = true;
        };

        function selectTable(tableIndex) {
            $scope.selectedTable = vm.tables[tableIndex];
            $rootScope.Tasks.selectedTable = $scope.selectedTable;
            vm.columnDefinitions = $scope.selectedTable.columnDefinitions;
        }

        /* Changes are only changed on the Server via TaskSet Actions */
        $scope.deleteTable = function (table) {
            vm.tables.splice(findInArray(vm.tables, table), 1);
        };

        //////////////////////////////777
        //  Intension: TableDefinition: Column - Control
        //////////////////////////////777

        /* Data */
        var DefaultColumn = function () {
            this.columnName = "";
            this.dataType = "BIGINT";
            this.notNull = true;
            this.primaryKey = false;
        };

        /* Methods */
        $scope.pushNewColumn = function () {
            vm.columnDefinitions.push(new DefaultColumn());
        };

        /* Changes are only changed on the Server via TaskSet Actions */
        $scope.deleteColumn = function (column) {
            vm.columnDefinitions.splice(findInArray(vm.columnDefinitions, column), 1);
        };

        //////////////////////////////777
        //  Intension: Foreign Keys - Control
        //////////////////////////////777

        /* Data */
        var DefaultForeignKey = function () {
            this.sourceTable = "";
            this.sourceColumn = "";
            this.destinationTable = "";
            this.destinationColumn = "";
            this.isCombined = false;
            this.combinedKeyId = null;
        };

        /* Methods */
        $scope.pushNewForeignKey = function () {
            vm.foreignKeyRelations.push(new DefaultForeignKey());
        };

        $scope.switchCombined = function(foreignKey) {
            if (foreignKey.isCombined) {
                if (foreignKey.combinedKeyId === null) {
                    foreignKey.combinedKeyId = 0;
                }
            } else {
                foreignKey.combinedKeyId = null;
            }
        };

        /* Changes are only changed on the Server via TaskSet Actions */
        $scope.deleteForeignKey = function (foreignKey) {
            vm.foreignKeyRelations.splice(findInArray(vm.foreignKeyRelations, foreignKey), 1);
        };

        //////////////////////////////777
        //  Tasks: Control
        //////////////////////////////777

        /* Data */
        var DefaultTask = function () {
            this.taskSet = $scope.selectedTaskSet.id;
            this.taskName = "";
            this.refStatement = "";
            this.evaluationStrategy = 1;
            this.points = 1;
            this.requiredTerm = 0;
            this.isCollapsed = true;
            this.availableSyntaxChecks = 0;
            this.availableSemanticChecks = 0;
        };

        /* Methods */

        $scope.pushNewTask = function () {
            vm.tasks.push(new DefaultTask());
        };

        $scope.viewTaskComments = function(task) {
            var modalInstance = $uibModal.open({
                    animation: $scope.animationsEnabled,
                    templateUrl: 'assets/templates/comment.template.html',
                    controller: 'CommentController',
                    resolve: {
                        commentTaskObject: function() {
                            return {content: task, type: 'task'};
                        }
                    }
            });

            modalInstance.result.then(
                FlashService.Clear
            );
        };

        /* Server Side Methods */
        $scope.saveTask = function (task) {
            if (task.id !== undefined) {
                TaskService.editTask(task.id, task).then(
                        function (result) {
                            FlashService.Success("Updated Task");
                        }, function (error) {
                            FlashService.Error(error);
                        }
                );
            } else {
                TaskService.createTask(task.taskSet, task).then(
                        function (result) {
                            /* Let's put in the new values we just created in BackEnd */
                            vm.tasks[findInArray(vm.tasks, task)] = result;

                            FlashService.Success("Created new Task");

                        }, function (error) {
                            FlashService.Error(error);

                        }
                );
            }
        };


        $scope.deleteTask = function (task) {
            /* If not in Database, we don't need to do much */
            if (task.id === undefined) {
                vm.tasks.splice(findInArray(vm.tasks, task), 1);
                return;
            }


            var modalInstance = $uibModal.open({
                    animation: $scope.animationsEnabled,
                    templateUrl: 'assets/templates/sure.template.html',
                    controller: 'sureTemplateController',
                    resolve: {
                        sureTemplateMessage: function () {
                                return "<span style='font-size:15pt'> Are you sure you want to delete Task: <b>" + task.taskName + "</b>? <br> It will be deleted forever!</span>";
                            }
                    }
            });

            modalInstance.result
            .then(
                function () {
                    return TaskService.deleteTask(task.id);
                }, function () {
                    return $q.reject({noerror: true});
                }
            ).then(
                function (result) {
                    FlashService.Success("Deleted Task.");
                    vm.tasks.splice(findInArray(vm.tasks, task), 1);
                },
                function (error) {
                    if (!error.noerror)
                        FlashService.Error(error);
                }
            );
        };

        $scope.rateTask = function (task, decision) {
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
            TaskService.rateTask(task.id, ratingJson).then(
                function (result) {
                    vm.tasks[findInArray(vm.tasks, task)] = result;

                }, function (error) {
                    FlashService.Error(error);
                }
            );
        };

        //////////////////////////////777
        //  Array functions
        //////////////////////////////777

        function findInArray(array, item) {
            for (var i = 0; i < array.length; i++) {
                if (array[i].$$hashKey === item.$$hashKey) {
                    return i;
                }
            }
            return -1;
        }

        $scope.findColumnsForTableName = function (tableName) {
            var columns = [];
            for (var i = 0; i < vm.tables.length; i++) {
                if (vm.tables[i].tableName === tableName) {
                    for (var j = 0; j < vm.tables[i].columnDefinitions.length; j++) {
                        columns.push(vm.tables[i].columnDefinitions[j].columnName);
                    }
                }
            }
            return columns;
        };



        initController();
    }
})();
