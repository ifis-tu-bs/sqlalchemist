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

    HomeworkController.$inject = ['$scope', '$uibModal', 'TaskService', 'FlashService', '$rootScope', '$location', '$filter', '$timeout', '$q'];
    function HomeworkController($scope, $uibModal, TaskService, FlashService, $rootScope, $location, $filter, $timeout, $q) {
        var vm = this;

        $scope.orderReverse = false;
        $scope.orderHomeworkPredicate = 'name';
        $scope.orderTaskSetPredicate = 'taskSetName';
        $scope.orderTaskPredicate  = 'id';
        $scope.orderSubmitPredicate   = 'student.matno';
        $scope.search = '';

        $scope.clearStates = function() {
            $scope.state.homeworkSelection = false;
            $scope.state.studentSelection = false;
            $scope.state.taskSetSelection = false;
            $scope.state.taskSelection = false;
            $scope.state.inspectSubmit = false;
        };
        $scope.state = {
            homeworkSelection : true,
            studentSelection : false,
            taskSetSelection : false,
            taskSelection : false,
            inspectSubmit : false
        };


        $scope.animationsEnabled = true;

        vm.homeworks = [];
        vm.students = [];
        vm.taskSets = [];
        vm.tasks  = [];
        vm.submits   = [];
        $scope.selectedHomework = {};
        $scope.selectedStudent = {};
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
                    vm.homeworks = result;
                    console.log($scope.state);
                },
                function (error) {
                    FlashService.Error(result.message);
                }
            );
        }

        /* Landing in HomeworkSelection */

        $scope.selectHomework = function(homework) {
            $scope.selectedHomework = vm.homeworks[findInArray(vm.homeworks, homework)];
            vm.students = $scope.selectedHomework.students;
            $scope.clearStates();
            $scope.state.studentSelection = true;
        };

        $scope.deselectToHomeworkSelection = function() {
            $scope.selectedHomework = undefined;
            $scope.clearStates();
            $scope.state.homeworkSelection = true;
        };

        /* Landing in StudentSelection */

        $scope.selectStudent = function(student) {
            $scope.selectedStudent = vm.students[findInArray(vm.students, student)];
            vm.taskSets = $scope.selectedStudent.taskSets;
            $scope.clearStates();
            $scope.state.taskSetSelection = true;
        };

        $scope.deselectToStudentSelection = function() {
            $scope.selectedStudent = undefined;
            $scope.clearStates();
            $scope.state.studentSelection = true;
        };

        /* Landing in TaskSetSelection*/

        $scope.selectTaskSet = function(taskSet) {
            $scope.selectedTaskSet = vm.taskSets[findInArray(vm.taskSets, taskSet)];
            vm.tasks = $scope.selectedTaskSet.tasks.tasks;
            $scope.clearStates();
            $scope.state.taskSelection = true;
        };

        $scope.deselectToTaskSetSelection = function() {
            $scope.selectedTaskSet = undefined;
            $scope.clearStates();
            $scope.state.taskSetSelection = true;
        };

        /* Landing in SubmitInspection */

        $scope.selectTask = function(task) {
            $scope.selectedTask = vm.tasks[findInArray(vm.tasks, task)];
            vm.submits = null;
            $scope.clearStates();
            $scope.state.inspectSubmit = true;
        };

        $scope.deselectToTaskSelection = function() {
            $scope.selectedTask = undefined;
            $scope.clearStates();
            $scope.state.taskSelection = true;
        };

        //////////////////////////////777
        //  Path Functions (Changing STATES)
        //////////////////////////////777

        $scope.newHomeWork = function () {
            var modalInstance = $uibModal.open({
                    animation: $scope.animationsEnabled,
                    templateUrl: 'assets/templates/makeHomeWork.template.html',
                    controller: 'makeHWController',
                    size: "lg"
            });

            modalInstance.result.then(
                    function (homework) {
                        return TaskService.createHomeWork(homework);
                    },
                    function (error) {
                        return $q.reject({noerror: true});
                    }
            ).then(
                    function (result) {
                        initController();
                        FlashService.Success("Homework created.");
                    }, function (error) {
                        if (!error.noerror)
                            FlashService.Error(error);
                    }
            );
        };

        //////////////////////////////777
        //  Path Finding and Display organization
        //////////////////////////////777

        $scope.shouldIBeDisplayed = function(me) {
            switch (me) {
                case (1) : return true;
                /* falls through */
                case (2) : if ($scope.state.studentSelection) return true;
                /* falls through */
                case (3) : if ($scope.state.taskSetSelection) return true;
                /* falls through */
                case (4) : if ($scope.state.taskSelection) return true;
                /* falls through */
                case (5) : if ($scope.state.inspectSubmit) return true;
            }
            return false;
        };

        $scope.prevTask = function() {
            var currentSpot = findInArray(vm.tasks, $scope.selectedTask);
            currentSpot = (currentSpot + vm.tasks.length - 1) % vm.tasks.length;
            $scope.selectTask(vm.tasks[currentSpot]);
        };

        $scope.nextTask = function() {
            var currentSpot = findInArray(vm.tasks, $scope.selectedTask);
            currentSpot = (currentSpot + 1) % vm.tasks.length;
            $scope.selectTask(vm.tasks[currentSpot]);
        };

        //////////////////////////////777
        //  HomeWork Deletion
        //////////////////////////////777


        $scope.deleteHomework = function (homework) {
            var homeworkIndex = findInArray(vm.homeworks, homework);
            var modalInstance = $uibModal.open({
                    animation: $scope.animationsEnabled,
                    templateUrl: 'assets/templates/sure.template.html',
                    controller: 'sureTemplateController',
                    resolve: {
                        sureTemplateMessage: function () {
                                return "Are you sure you want to delete Homework: " + vm.homeworks[homeworkIndex].name + "?\nRemember, that only Homeworks with no submits until now can be deleted.";
                            }
                    }
            });

            modalInstance.result.then(
                function (result) {
                    return TaskService.deleteHomework(vm.homeworks[homeworkIndex].id);
                }, function () {
                    return $q.reject({});
                }
            ).then(
                initController,
                null
            );
        };

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
                    initController();
                }, function (error) {
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
                    initController();
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

        $scope.setAceReadOnly = function (_editor) {
            _editor.setReadOnly(true);
        };
    }


})();
