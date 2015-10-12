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
        $scope.search = '';

        var states = ['TaskSetSelection', 'TaskSelection', 'InspectTask'];
        $scope.state = '';

        //$scope.codemirrorOptions = { mode: 'text/x-mysql', readOnly: true};
        $scope.animationsEnabled = true;

        vm.taskSets = [];
        vm.tasks = [];
        $scope.selectedTaskSet = {};
        $scope.selectedTask = {};

        initController();

        function initController() {
            getAllTaskSets();
        }

        //////////////////////////////777
        //  Select Stuff
        //////////////////////////////777

        function getAllTaskSets() {
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


        $scope.selectTaskSet = function(taskSet) {
            var index = findInArray(vm.taskSets, taskSet);
            selectTaskSet(index);
            $scope.state = 'TaskSelection';
        }

        $scope.selectTask = function(task) {
            var index = findInArray(vm.tasks, task);
            selectTask(index);
            $scope.state = 'InspectTask';
        }

        //////////////////////////////777
        //  Path Functions upon Selection
        //////////////////////////////777

        $scope.goSelectTaskSet = function() {
            $scope.selectedTaskSet = undefined;
            $scope.selectedTask = undefined;
            vm.taskSets = undefined;
            vm.tasks = undefined;
            $rootScope.Tasks.selectedTaskSet = undefined;
            $rootScope.Tasks.selectedTask = undefined;
            getCurrentPath();
        }

        $scope.goSelectTask = function() {
            $scope.selectedSubTask = undefined;
            vm.tasks = undefined;
            $rootScope.Tasks.selectedTask = undefined;
            getCurrentPath();
        }

        function selectHomework (taskSetIndex) {
            $scope.selectedTaskSet = vm.taskSets[taskSetIndex];
            vm.tasks = $scope.selectedTaskSet.tasks;
            $rootScope.Tasks.selectedTaskSet = $scope.selectedTaskSet;
            $scope.state = 'TaskSelection';
        }

        function selectTaskFile (taskIndex) {
            $scope.selectedTask = vm.tasks[taskIndex];
            $rootScope.Tasks.selectedTask = $scope.selectedTask;
            $scope.state = 'InspectTask';
        }

        //////////////////////////////777
        //  Path Finding and Display organization
        //////////////////////////////777

        function getCurrentPath () {
            var path = $rootScope.Tasks;

            //Find out if a TaskSet is currently selected
            if (path.selectedTaskSet) {
                selectTaskSet(findInArray(vm.taskSets, path.selectedTaskSet));
            } else {
                $scope.state = 'TaskSetSelection';
                return;
            }

            //Find out if a Task is currently selected
            if (path.selectedTask) {
                selectTask(findInArray(vm.tasks, path.selectedTask));
            } else {
                $scope.state = 'TaskSelection';
                return;
            }

            $scope.state = 'InspectTask';
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
    }


})();

