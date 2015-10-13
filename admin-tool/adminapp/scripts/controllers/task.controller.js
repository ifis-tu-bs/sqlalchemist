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


        //$scope.codemirrorOptions = { mode: 'text/x-mysql', readOnly: true};
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
                getCurrentPath();

            } else {
                TaskService.getAllTaskSets().then(
                        function (result) {
                                if (result.error) {
                                    FlashService.Error(result.message);
                                } else {
                                    vm.taskSets = result;
                                    getCurrentPath();
                                    console.log(result);
                                }
                        }
                );

            }
        }

        //////////////////////////////777
        //  Saving current vm.taskSets when leaving
        //////////////////////////////777

        $scope.keepTaskSets = function () {
            $rootScope.Tasks.taskSets = vm.taskSets;
            console.log(vm.taskSets);
        }

        //////////////////////////////777
        //  TaskSet Operations
        //////////////////////////////777

        $scope.taskSetSelectionStatus = {
            taskSetSelected : false
        }

        $scope.selectTaskSet = function(taskSet, destination) {
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


        //////////////////////////////777
        //  Intension: TableHandling
        //////////////////////////////777

        $scope.tableSelectionStatus = {
            tableSelected : false,
            tableNotSelected : true
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
        //  Path Finding and Display organization
        //////////////////////////////777

        function getCurrentPath () {
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

        $scope.shouldIBeDisplayed = function(stateButton) {
            var answer = false;
            for (var i = states.length; i >= 0; i--) {
                answer |= states[i] === $scope.state;
                if (states[i] === stateButton) {
                    break;
                }
            }

            return answer;
        }

        /*
        $scope.prevSubmit = function() {
            var currentSpot = findInArray(vm.submits, $scope.selectedSubmit);
            currentSpot = (currentSpot + vm.submits.length - 1) % vm.submits.length;
            selectSubmit(currentSpot);
        }

        $scope.nextSubmit = function() {
            var currentSpot = findInArray(vm.submits, $scope.selectedSubmit);
            currentSpot = (currentSpot + 1) % vm.submits.length;
            selectSubmit(currentSpot);
        }*/

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


        $scope.rateTaskFile = function (taskSet, decision) {
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
                        initController();
                    }
            });
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
                if (array[i].id === item.id) {
                    return i;
                }
            }
            return -1;
        }



        initController();
    }
})();

