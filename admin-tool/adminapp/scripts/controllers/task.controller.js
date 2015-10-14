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

    TasksController.$inject = ['$scope', '$modal', 'TaskService', 'FlashService', '$rootScope', '$location', '$filter', '$timeout'];
    function TasksController($scope, $modal, TaskService, FlashService, $rootScope, $location, $filter, $timeout) {
        var vm = this;

        $scope.orderReverse = false;
        $scope.orderTaskSetPredicate = 'taskSetName';
        $scope.orderTaskPredicate = 'taskName';
        $scope.orderTablePredicate = 'tableName';
        $scope.search = '';

        $scope.items=["bigint", "Varchar(255)", "boolean"];

        $scope.taskTabActive = false;
        $scope.animationsEnabled = true;

        vm.taskSets = [];
        vm.tasks = [];
        vm.tables = [];
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
                console.log(vm.taskSets);

            } else {
                TaskService.getAllTaskSets().then(
                        function (result) {
                                if (result.error) {
                                    FlashService.Error(result.message);
                                } else {
                                    vm.taskSets = result;
                                    $scope.getCurrentPath();
                                    console.log(result);
                                }
                        }
                );

            }
        }

        //////////////////////////////777
        //  Saving current vm.taskSets when leaving
        //  Restoring saved Data and Displaying the correct screen
        //////////////////////////////777

        $scope.keepTaskSets = function () {
            $rootScope.Tasks.taskSets = vm.taskSets;
        }
        
        $scope.getCurrentPath = function () {
            var path = $rootScope.Tasks;

            //Find out if a TaskSet is currently selected
            if (path.selectedTaskSet) {
                $scope.selectTaskSet(path.selectedTaskSet);
            } else {
                return;
            }

            //Find out if a Task/Table is currently selected
            if (path.selectedTask) {
                $scope.selectTask(path.selectedTask);
            } else if (path.selectedTable){
                $scope.selectTable(path.selectedTable);
            } else {
                $scope.state = path.state;
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
            this.foreignKeys = [];
            this.tasks=[];
            this.isHomework = false;
        };


        /* Methods */
        $scope.pushNewTaskSet = function () {
            vm.taskSets.push(new DefaultTaskSet());
        }

        $scope.selectTaskSet = function(taskSet, destination) {
            console.log(taskSet);
            console.log(vm.taskSets);
            var index = findInArray(vm.taskSets, taskSet);
            selectTaskSet(index);
            $scope.taskSetSelectionStatus.taskSetSelected = true;
        }

        function selectTaskSet (taskSetIndex) {
            $scope.selectedTaskSet = vm.taskSets[taskSetIndex];
            vm.tasks = $scope.selectedTaskSet.tasks;
            vm.tables = $scope.selectedTaskSet.tableDefinitions;
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
        //////////////////////////////777
        //  Tasks: Control
        //////////////////////////////777

        /* Data */
        var DefaultTask = function () {
            this.taskSet = $scope.selectedTaskSet.id;
            this.taskName = "";
            this.refStatement = "";
            this.evaluationstrategy = 0;
            this.points = 1;
            this.requiredTerm = 0;
            this.isCollapsed = true;
        }

        /* Methods */

        $scope.pushNewTask = function () {
            vm.tasks.push(new DefaultTask());
        }


        $scope.enterTaskTab = function () {
            $scope.taskTabActive = true;
        }

        $scope.leaveTaskTab = function () {
            $scope.taskTabActive = false;
        }

        /* Server Side Methods */

        $scope.saveTask = function (task) {
            console.log(task);

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
                                FlashService.Success("Created new Task");
                            }
                        }
                );
            }
        }

        //////////////////////////////777
        //  HomeWork Deletion
        //////////////////////////////777

/*
        $scope.deleteHomework = function (taskSet) {
            var taskSetIndex = findInArray(vm.taskSets, taskSet);
            var modalInstance = $modal.open({
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
        //  Rating stuff
        //////////////////////////////777




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
                        FlashService.Error(result.message)
                    } else {
                        initController();
                    }
            });
        }

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



        initController();
    }
})();

