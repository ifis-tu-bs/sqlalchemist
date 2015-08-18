(function () {


angular
    .module('app')
    .controller('HomeworkController', HomeworkController);

    HomeworkController.$inject = ['$scope', '$modal', 'TaskService', 'FlashService', '$rootScope', '$location', '$filter', '$timeout'];
    function HomeworkController($scope, $modal, TaskService, FlashService, $rootScope, $location, $filter, $timeout) {
        var vm = this;

        $scope.orderReverse = false;
        $scope.orderHomeWorkPredicate = 'name';
        $scope.orderTaskFilePredicate = 'filename';
        $scope.orderSubTaskPredicate  = 'id';
        $scope.orderSubmitPredicate   = 'student.matno';
        $scope.search = '';

        var states = ['HomeWorkSelection', 'TaskFileSelection', 'SubTaskSelection', 'SubmitSelection', 'InspectSubmit'];
        $scope.state = '';

        $scope.codemirrorOptions = { mode: 'text/x-mysql', readOnly: true};
        $scope.animationsEnabled = true;

        vm.homeworks = [];
        vm.taskFiles = [];
        vm.subTasks  = [];
        vm.submits   = [];
        $scope.selectedHomework = {};
        $scope.selectedTaskFile = {};
        $scope.selectedSubTask  = {};
        $scope.selectedSubmit   = {};

        initController();

        function initController() {
            getAllHomeworks();
        }

        //////////////////////////////777
        //  Select Stuff
        //////////////////////////////777

        function getAllHomeworks() {
            TaskService.getAllHomeworks().then(
                function (result) {
                    if (result.error) {
                        FlashService.Error(result.message);
                    } else {
                        vm.homeworks = result;
                        getCurrentPath();
                    }
                }
            );
        }


        $scope.selectHomework = function(homework) {
            var index = findInArray(vm.homeworks, homework);
            selectHomework(index);
            $scope.state = 'TaskFileSelection';
        }

        $scope.selectTaskFile = function(taskFile) {
            var index = findInArray(vm.taskFiles, taskFile);
            selectTaskFile(index);
            $scope.state = 'SubTaskSelection';
        }

        $scope.selectSubTask = function(subTask){
            var subTaskIndex = findInArray(vm.subTasks, subTask);
            selectSubTask(subTaskIndex).then(
                function (result) {
                    if (result.error) {
                        FlashService.Error(result.message);
                    } else {
                        $scope.selectedSubTask = vm.subTasks[subTaskIndex];
                        vm.submits = result;
                        $rootScope.Homework.selectedSubTask = $scope.selectedSubTask;
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
            vm.taskFiles = $scope.selectedHomework.taskFiles;
            $rootScope.Homework.selectedHomework = $scope.selectedHomework;
            $scope.state = 'TaskFileSelection';
        }

        function selectTaskFile (taskFileIndex) {
            $scope.selectedTaskFile = vm.taskFiles[taskFileIndex];
            vm.subTasks = $scope.selectedTaskFile.subTasks;
            $rootScope.Homework.selectedTaskFile = $scope.selectedTaskFile;
            $scope.state = 'SubTaskSelection';
        }


        function selectSubTask (subTaskIndex) {
            $scope.selectedSubTask = vm.subTasks[subTaskIndex];

            return TaskService.getSubmitsForSubTaskAndHomeWork(
                    $scope.selectedSubTask.id,
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
                            $scope.selectedSubTask.refStatement += " ";
                        }
                    )
                }, 100);
        }

        //////////////////////////////777
        //  Path Functions goSelectStuff
        //////////////////////////////777

        $scope.goSelectHomework = function() {
            $scope.selectedHomework = undefined;
            $scope.selectedTaskFile = undefined;
            $scope.selectedSubTask = undefined;
            $scope.selectedSubmit = undefined;
            vm.taskFiles = undefined;
            vm.subTasks = undefined;
            vm.submits = undefined;
            $rootScope.Homework.selectedHomework = undefined;
            $rootScope.Homework.selectedTaskFile = undefined;
            $rootScope.Homework.selectedSubTask = undefined;
            $rootScope.Homework.selectedSubmit = undefined;
            getCurrentPath();
        }

        $scope.goSelectTaskFile = function() {
            $scope.selectedTaskFile = undefined;
            $scope.selectedSubTask = undefined;
            $scope.selectedSubmit = undefined;
            vm.subTasks = undefined;
            vm.submits = undefined;
            $rootScope.Homework.selectedTaskFile = undefined;
            $rootScope.Homework.selectedSubTask = undefined;
            $rootScope.Homework.selectedSubmit = undefined;
            getCurrentPath();
        }

        $scope.goSelectSubtask = function() {
            $scope.selectedSubTask = undefined;
            $scope.selectedSubmit = undefined;
            vm.submits = undefined;
            $rootScope.Homework.selectedSubTask = undefined;
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

                if (path.selectedTaskFile) {
                    selectTaskFile(findInArray(vm.taskFiles, path.selectedTaskFile));

                    if (path.selectedSubTask) {
                        selectSubTaskIndex = findInArray(vm.subTasks, path.selectedSubTask);
                        selectSubTask(selectSubTaskIndex).then(
                            function (result) {
                                if (result.error) {
                                    FlashService.Error(result.message);
                                    $scope.state = 'SubTaskSelection';
                                } else {
                                    $scope.selectedSubTask = vm.subTasks[selectSubTaskIndex];
                                    vm.submits = result;
                                    $rootScope.Homework.selectedSubTask = $scope.selectedSubTask;

                                    if (path.selectSubmit) {
                                        selectSubmit(findInArray(vm.submits, path.selectedSubmit));
                                        $scope.state = 'InspectSubmit'
                                    } else {
                                        $scope.state = 'SubmitSelection';
                                    }
                                }
                             });
                    } else {
                        $scope.state = 'SubTaskSelection'
                    }
                } else {
                    $scope.state = 'TaskFileSelection'
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

