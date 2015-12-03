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

        $scope.textAreaDataComments = "";

        console.log(commentTaskObject.content);

        var addCommentToTextArea = function (comment) {
            $scope.textAreaDataComments += comment.profile.username + " wrote:\n" + comment.text + "\nat: " + comment.createdAt + "\n\n";
        };

        for (var i = 0; i < commentTaskObject.content.comments.length; i++) {
            addCommentToTextArea(commentTaskObject.content.comments[i]);
        }


        $scope.ok = function() {

            if (commentTaskObject.type === 'task') {
                TaskService.postTaskComment(commentTaskObject.content.id, $scope.textAreaDataInputComment).then(
                    function (data) {
                        addCommentToTextArea(data);
                        $scope.textAreaDataInputComment = "";

                    }, function (error) {
                        FlashService.Error(data.message);

                    }
                );
            }

            if (commentTaskObject.type === 'taskSet') {
                TaskService.postTaskSetComment(commentTaskObject.content.id, $scope.textAreaDataInputComment).then(
                    function (data) {
                        addCommentToTextArea(data);
                        $scope.textAreaDataInputComment = "";

                    }, function (error) {
                        FlashService.Error(data.message);

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

