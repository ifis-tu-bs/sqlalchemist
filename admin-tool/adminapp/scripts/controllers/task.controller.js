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

    TasksController.$inject = ['$scope', '$uibModal', 'TaskService', 'FlashService', '$rootScope', '$location', '$filter', '$timeout'];
    function TasksController($scope, $uibModal, TaskService, FlashService, $rootScope, $location, $filter, $timeout) {
        var vm = this;

        $scope.orderReverse = false;
        $scope.orderTaskSetPredicate = 'taskSetName';
        $scope.orderTaskPredicate = 'taskName';
        $scope.orderTablePredicate = 'tableName';
        $scope.search = '';

        $scope.items=["bigint", "Varchar(255)", "boolean"];
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
                            if (result.error) {
                                FlashService.Error(result.message);
                            } else {
                                vm.taskSets = result;
                                $scope.getCurrentPath();
                            }
                        }
                );

            }
        }

        //////////////////////////////777
        //  Saving data when leaving View
        //  Restoring saved Data and Displaying the correct screen
        //////////////////////////////777

        $scope.keepTabActive = function () {
            $rootScope.Tasks.tabActive = $scope.tabActive;
        }

        $scope.keepTaskSets = function () {
            $rootScope.Tasks.taskSets = vm.taskSets;
        }

        $scope.$on('$locationChangeStart', function(event) {
            $scope.keepTabActive();
            $scope.keepTaskSets();
        });
        
        $scope.getCurrentPath = function () {
            var path = $rootScope.Tasks;

            console.log(path);
            //Find out if a TaskSet is currently selected
            if (path.selectedTaskSet) {
                $scope.tabActive = path.tabActive;
                $scope.selectTaskSet(path.selectedTaskSet);
            } else {
                return;
            }


        }

        //////////////////////////////777
        //  TaskSet - Control
        //////////////////////////////777


        /* Data */
        $scope.taskSetSelectionStatus = {
            taskSetSelected : false
        }

        var DefaultTaskSet = function() {
            this.taskSetName = "";
            this.tableDefinitions = [];
            this.foreignKeyRelations = [];
            this.tasks=[];
            this.isHomework = false;
        };


        /* Methods */
        $scope.pushNewTaskSet = function () {
            vm.taskSets.push(new DefaultTaskSet());
        }

        $scope.selectTaskSet = function(taskSet, destination) {
            var index = findInArray(vm.taskSets, taskSet);
            selectTaskSet(index);
            $scope.tabActive.intensionTables = true;
        }

        function selectTaskSet (taskSetIndex) {

            $scope.selectedTaskSet = vm.taskSets[taskSetIndex];
            vm.tasks = $scope.selectedTaskSet.tasks;
            vm.tables = $scope.selectedTaskSet.tableDefinitions;
            vm.foreignKeyRelations = $scope.selectedTaskSet.foreignKeyRelations;
            $rootScope.Tasks.selectedTaskSet = $scope.selectedTaskSet;
        }

        /* Server Side Methods */
        $scope.saveSelectedTaskSet = function () {
            console.log($scope.selectedTaskSet);
            FlashService.Clear();

            if ($scope.selectedTaskSet.id != undefined) {

                TaskService.editTaskSet($scope.selectedTaskSet, $scope.selectedTaskSet.id).then(
                        function (result) {
                            if (result.error) {
                                FlashService.Error(result.message);
                            } else {
                                FlashService.Success("Updated TaskSet");
                            }
                        }
                );

            } else {

                TaskService.createTaskSet($scope.selectedTaskSet).then(
                        function (result) {
                            if (result.error) {
                                FlashService.Error(result.message);
                            } else {
                                FlashService.Success("Created new TaskSet");
                            }
                        }
                );

            }
        }

        $scope.deleteTaskSet = function (taskSet) {
            var modalInstance = $uibModal.open({
                    animation: $scope.animationsEnabled,
                    templateUrl: 'adminapp/templates/sure.template.html',
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
                null
            ).then(
                 function () {
                     vm.taskSets.splice(findInArray(vm.taskSets, taskSet), 1)
                 },
                 function (error) {
                         if (error.message) {
                             FlashService.Error(error.message);
                         }
                     }
            );
        }

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
                    if (result.error) {
                        FlashService.Error(result.message);
                    } else {
                        vm.taskSets[findInArray(vm.taskSets, taskSet)] = result;
                    }
            });
        }


        //////////////////////////////777
        //  Intension: TableDefinition - Control
        //////////////////////////////777

        /* Data */
        $scope.tableSelectionStatus = {
            tableSelected : false,
            tableNotSelected : true
        }

        var DefaultTable = function () {
            this.tableName = "";
            this.columns = [];
            this.extension = "";
        }

        /* Methods */
        $scope.pushNewTable = function () {
            vm.tables.push(new DefaultTable());
        }

        $scope.selectTable = function (table) {
            var index = findInArray(vm.tables, table);
            selectTable(index);
            $scope.tableSelectionStatus.tableNotSelected = false;
            $scope.tableSelectionStatus.tableSelected = true;
        }

        function selectTable(tableIndex) {
            $scope.selectedTable = vm.tables[tableIndex];
            $rootScope.Tasks.selectedTable = $scope.selectedTable;
            vm.columns = $scope.selectedTable.columns;
        }

        /* Changes are only changed on the Server via TaskSet Actions */
        $scope.deleteTable = function (table) {
            vm.tables.splice(findInArray(vm.tables, table), 1);
        }

        //////////////////////////////777
        //  Intension: TableDefinition: Column - Control
        //////////////////////////////777

        /* Data */
        var DefaultColumn = function () {
            this.columnName = "";
            this.dataType = "bigint"
            this.notNull = true;
            this.primaryKey = false;
        }

        /* Methods */
        $scope.pushNewColumn = function () {
            vm.columns.push(new DefaultColumn());
        }

        /* Changes are only changed on the Server via TaskSet Actions */
        $scope.deleteColumn = function (column) {
            vm.columns.splice(findInArray(vm.columns, column), 1);
        }

        //////////////////////////////777
        //  Tasks: Control
        //////////////////////////////777

        /* Data */
        var DefaultForeignKey = function () {
            this.sourceTable = "";
            this.sourceColumn = "";
            this.destinationTable = "";
            this.destinationColumn = "";
        }

        /* Methods */
        $scope.pushNewForeignKey = function () {
            vm.foreignKeyRelations.push(new DefaultForeignKey());
        }

        /* Changes are only changed on the Server via TaskSet Actions */
        $scope.deleteForeignKey = function (foreignKey) {
            vm.foreignKeyRelations.splice(findInArray(vm.foreignKeyRelations, foreignKey), 1);
        }

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
        }

        /* Methods */

        $scope.pushNewTask = function () {
            vm.tasks.push(new DefaultTask());
        }

        /* Server Side Methods */
        $scope.saveTask = function (task) {
            if (task.id != undefined) {
                TaskService.editTask(task.id, task).then(
                        function (result) {
                            if (result.error) {
                                FlashService.Error(result.message);
                            } else {
                                FlashService.Success("Updated Task");
                            }
                        }
                );
            } else {
                TaskService.createTask(task.taskSet, task).then(
                        function (result) {
                            if (result.error) {
                                FlashService.Error(result.message);
                            } else {

                                /* Let's put in the new values we just created in BackEnd */
                                vm.tasks[findInArray(vm.tasks, task)] = result;

                                FlashService.Success("Created new Task");
                            }
                        }
                );
            }
        }

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
                            if (result.error) {
                                FlashService.Error(result.message);
                            } else {
                                console.log($scope.tabActive);
                                vm.tasks[findInArray(vm.tasks, task)] = result;
                            }
                    });
                }

        //////////////////////////////777
        //  HomeWork Deletion
        //////////////////////////////777

/*
        $scope.deleteHomework = function (taskSet) {
            var taskSetIndex = findInArray(vm.taskSets, taskSet);
            var modalInstance = $uibModal.open({
                    animation: $scope.animationsEnabled,
                    templateUrl: 'adminapp/templates/sure.template.html',
                    controller: 'sureTemplateController',
                    resolve: {
                        sureTemplateMessage: function () {
                                return "Are you sure you want to delete Homework: " + vm.taskSets[taskSetIndex].name + "?\nRemember, that only Homeworks with no submits until now can be deleted.";
                            }
                    }
            });

            modalInstance.result.then(function (result) {
                if (result == true) {
                    TaskService.deleteHomework(vm.taskSets[taskSetIndex].id).then(
                        initController
                    );
                }
            });
        }
*/

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
                    for (var j = 0; j < vm.tables[i].columns.length; j++) {
                        columns.push(vm.tables[i].columns[j].columnName);
                    }
                }
            }
            return columns;
        }



        initController();
    }
})();

