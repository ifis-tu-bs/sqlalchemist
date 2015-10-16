/**********************************************************************************************************************
*   Controller for views/homework.view.html
*
*   Manages the displacement of all Homeworks.
*
*   Page is orderd via:
*   Homeworks -> taskSets(in the selected HW) -> tasks(in the selected TF) -> Submits(of the selected ST)
*
*   Selected Items are stored in $scope, the recieved data from the server in vm
*
*   The Controller is handled in states to fit the current Selection
**********************************************************************************************************************/

(function () {


angular
    .module('app')
    .controller('HomeworkController', HomeworkController);

    HomeworkController.$inject = ['$scope', '$modal', 'TaskService', 'FlashService', '$rootScope', '$location', '$filter', '$timeout'];
    function HomeworkController($scope, $modal, TaskService, FlashService, $rootScope, $location, $filter, $timeout) {
        var vm = this;

        $scope.orderReverse = false;
        $scope.orderHomeWorkPredicate = 'name';
        $scope.ordertaskSetPredicate = 'filename';
        $scope.ordertaskPredicate  = 'id';
        $scope.orderSubmitPredicate   = 'student.matno';
        $scope.search = '';

        var states = ['HomeWorkSelection', 'taskSetSelection', 'taskSelection', 'SubmitSelection', 'InspectSubmit'];
        $scope.state = '';

        $scope.animationsEnabled = true;

        vm.homeworks = [];
        vm.taskSets = [];
        vm.tasks  = [];
        vm.submits   = [];
        $scope.selectedHomework = {};
        $scope.selectedTaskSet = {};
        $scope.selectedTask  = {};
        $scope.selectedSubmit   = {};

        initController();

        function initController() {
            getAllHomeworks();
        }

        //////////////////////////////777
        //  Select functions (STATES)
        //////////////////////////////777

        function getAllHomeworks() {
            TaskService.getAllHomeworks().then(
                function (result) {
                    if (result.error) {
                        FlashService.Error(result.message);
                    } else {
                        vm.homeworks = result;
                        console.log(result);
                        getCurrentPath();
                    }
                }
            );
        }


        $scope.selectHomework = function(homework) {
            var index = findInArray(vm.homeworks, homework);
            selectHomework(index);
            $scope.state = 'taskSetSelection';
        }

        $scope.selectTaskSet = function(taskSet) {
            var index = findInArray(vm.taskSets, taskSet);
            selectTaskSet(index);
            $scope.state = 'taskSelection';
        }

        $scope.selectTask = function(task){
            var taskIndex = findInArray(vm.tasks, task);
            selectTask(taskIndex).then(
                function (result) {
                    if (result.error) {
                        FlashService.Error(result.message);
                    } else {
                        $scope.selectedTask = vm.tasks[taskIndex];
                        vm.submits = result;
                        $rootScope.Homework.selectedTask = $scope.selectedTask;
                        $scope.state = 'SubmitSelection';
                    }
                });
        }

        $scope.selectSubmit = function (submit) {
            var index = findInArray(vm.submits, submit);
            selectSubmit(index);
        }

        function selectHomework (homeworkIndex) {
            $scope.selectedHomework = vm.homeworks[homeworkIndex];
            vm.taskSets = $scope.selectedHomework.taskSets;
            $rootScope.Homework.selectedHomework = $scope.selectedHomework;
            $scope.state = 'taskSetSelection';
        }

        function selectTaskSet (taskSetIndex) {
            $scope.selectedTaskSet = vm.taskSets[taskSetIndex];
            vm.tasks = $scope.selectedTaskSet.tasks;
            $rootScope.Homework.selectedTaskSet = $scope.selectedTaskSet;
            $scope.state = 'taskSelection';
        }


        function selectTask (taskIndex) {
            $scope.selectedTask = vm.tasks[taskIndex];

            return TaskService.getSubmitsForTaskInHomeWork(
                    $scope.selectedTask.id,
                    $scope.selectedHomework.id);

        }

        function selectSubmit(SubmitIndex) {
            $scope.state = 'InspectSubmit';

            //CodeMirror and its abstrusities...
            $timeout(
                function() {
                    $scope.$apply(
                        function() {
                            $scope.selectedSubmit = vm.submits[SubmitIndex];
                            $rootScope.Homework.selectedSubmit = $scope.selectedSubmit;
                            $scope.selectedTask.refStatement += " ";
                        }
                    )
                }, 100);
        }

        //////////////////////////////777
        //  Path Functions (Changing STATES)
        //////////////////////////////777

        $scope.goSelectHomework = function() {
            $scope.selectedHomework = undefined;
            $scope.selectedTaskSet = undefined;
            $scope.selectedTask = undefined;
            $scope.selectedSubmit = undefined;
            vm.taskSets = undefined;
            vm.tasks = undefined;
            vm.submits = undefined;
            $rootScope.Homework.selectedHomework = undefined;
            $rootScope.Homework.selectedTaskSet = undefined;
            $rootScope.Homework.selectedTask = undefined;
            $rootScope.Homework.selectedSubmit = undefined;
            getCurrentPath();
        }

        $scope.goSelectTaskSet = function() {
            $scope.selectedTaskSet = undefined;
            $scope.selectedTask = undefined;
            $scope.selectedSubmit = undefined;
            vm.tasks = undefined;
            vm.submits = undefined;
            $rootScope.Homework.selectedTaskSet = undefined;
            $rootScope.Homework.selectedTask = undefined;
            $rootScope.Homework.selectedSubmit = undefined;
            getCurrentPath();
        }

        $scope.goSelectTask = function() {
            $scope.selectedTask = undefined;
            $scope.selectedSubmit = undefined;
            vm.submits = undefined;
            $rootScope.Homework.selectedTask = undefined;
            $rootScope.Homework.selectedSubmit = undefined;
            getCurrentPath();
        }

        $scope.goSelectSubmit = function() {
            $scope.selectedSubmit = undefined;
            $rootScope.Homework.selectedSubmit = undefined;
            getCurrentPath();
        }

        //////////////////////////////777
        //  Path Finding and Display organization
        //////////////////////////////777

        function getCurrentPath () {
            var path = $rootScope.Homework;

            if (path.selectedHomework) {
                selectHomework(findInArray(vm.homeworks, path.selectedHomework));

                if (path.selectedTaskSet) {
                    selectTaskSet(findInArray(vm.taskSets, path.selectedTaskSet));

                    if (path.selectedTask) {
                        selectTaskIndex = findInArray(vm.tasks, path.selectedTask);
                        selectTask(selectTaskIndex).then(
                            function (result) {
                                if (result.error) {
                                    FlashService.Error(result.message);
                                    $scope.state = 'taskSelection';
                                } else {
                                    $scope.selectedTask = vm.tasks[selectTaskIndex];
                                    vm.submits = result;
                                    $rootScope.Homework.selectedTask = $scope.selectedTask;

                                    if (path.selectSubmit) {
                                        selectSubmit(findInArray(vm.submits, path.selectedSubmit));
                                        $scope.state = 'InspectSubmit'
                                    } else {
                                        $scope.state = 'SubmitSelection';
                                    }
                                }
                             });
                    } else {
                        $scope.state = 'taskSelection'
                    }
                } else {
                    $scope.state = 'taskSetSelection'
                }
            } else {
                $scope.state = 'HomeWorkSelection'
            }
        }

        $scope.shouldIBeDisplayed = function(me) {
            var answer = false;
            for (var i = states.length; i >= 0; i--) {
                answer |= states[i] === $scope.state;
                if (states[i] === me) {
                    break;
                }
            }
            return answer;
        }

        $scope.prevSubmit = function() {
            var currentSpot = findInArray(vm.submits, $scope.selectedSubmit);
            currentSpot = (currentSpot + vm.submits.length - 1) % vm.submits.length;
            selectSubmit(currentSpot);
        }

        $scope.nextSubmit = function() {
            var currentSpot = findInArray(vm.submits, $scope.selectedSubmit);
            currentSpot = (currentSpot + 1) % vm.submits.length;
            selectSubmit(currentSpot);
        }

        //////////////////////////////777
        //  HomeWork Deletion
        //////////////////////////////777


        $scope.deleteHomework = function (homework) {
            var homeworkIndex = findInArray(vm.homeworks, homework);
            var modalInstance = $modal.open({
                    animation: $scope.animationsEnabled,
                    templateUrl: 'adminapp/templates/sure.template.html',
                    controller: 'sureTemplateController',
                    resolve: {
                        sureTemplateMessage: function () {
                                return "Are you sure you want to delete Homework: " + vm.homeworks[homeworkIndex].name + "?\nRemember, that only Homeworks with no submits until now can be deleted.";
                            }
                    }
            });

            modalInstance.result.then(function (result) {
                if (result == true) {
                    TaskService.deleteHomework(vm.homeworks[homeworkIndex].id).then(
                        initController
                    );
                }
            });
        }

        //////////////////////////////777
        //  Rating stuff
        //////////////////////////////777


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

