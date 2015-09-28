/**********************************************************************************************************************
*   Controller for templates/comment.template.html
*
*   Manages Comment displacement and entering of new Comments.
**********************************************************************************************************************/

(function () {


angular
    .module('app')
    .controller('CommentController', CommentController);

    CommentController.$inject = ['TaskService', '$scope', '$modalInstance', 'commentTaskObject', 'FlashService'];
    function CommentController(TaskService, $scope, $modalInstance, commentTaskObject, FlashService) {
        var vm = this;
        vm.dataLoading = false;

        $scope.textAreaDataComments = "";

        if (commentTaskObject.type == 'subtask') {
            TaskService.getSubTask(commentTaskObject.content.id).then(
                function(data) {
                    commentTaskObject.content = data;
                    console.log(data);
                    for (var i = 0; i < commentTaskObject.content.commentList.length; i++) {
                        comment = commentTaskObject.content.commentList[i];
                        $scope.textAreaDataComments += comment.profile.username + " wrote:\n" + comment.text + "\nat: " + comment.written + "\n\n";
                    }
                });
        }

        if (commentTaskObject.type == 'taskfile') {
            TaskService.getTaskFile(commentTaskObject.content.id).then(
                function(data) {
                    commentTaskObject.content = data;
                    for (var i = 0; i < commentTaskObject.content.commentList.length; i++) {
                        comment = commentTaskObject.content.commentList[i];
                        $scope.textAreaDataComments += comment.profile.username + " wrote:\n" + comment.text + "\nat: " + comment.written + "\n\n";
                    }
                });
        }

        $scope.ok = function() {
            vm.dataLoading = true;

            if (commentTaskObject.type == 'subtask') {
                TaskService.postSubTaskComment(commentTaskObject.content.id, $scope.textAreaDataInputComment).then(
                    function (data) {
                        if (data.error) {
                            FlashService.Error(data.message);
                        } else {
                            $modalInstance.close(null);
                        }
                        vm.dataLoading = false;
                    }
                );
            }

            if (commentTaskObject.type == 'taskfile') {
                TaskService.postTaskFileComment(commentTaskObject.content.id, $scope.textAreaDataInputComment).then(
                    function (data) {
                        if (data.error) {
                            FlashService.Error(data.message);
                        } else {
                            FlashService.Clear();
                            $modalInstance.close(null);
                        }
                        vm.dataLoading = false;
                    }
                );
            }

        };

        $scope.close = function() {
            FlashService.Clear();
            $modalInstance.dismiss('Closed');
        };

    }

})();

