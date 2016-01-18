/**********************************************************************************************************************
*   Directive for templates/navigation.template.html
*
*   Automatically builds the navbar with all registered views in the app.
*       See app.js
**********************************************************************************************************************/

(function() {

    angular
        .module('app')
        .directive('navigation', directNavigationBar);

    directNavigationBar.$inject = ['routeNavigation'];
    function directNavigationBar(routeNavigation) {
        navigationBarController.$inject = ['$scope', 'AuthenticationService'];
        function navigationBarController($scope, AuthenticationService) {
            initController();


            function initController() {
                $scope.isLoggedIn = AuthenticationService.IsLoggedIn();
                routeNavigation.update();
                $scope.routes = routeNavigation.getRoutes();
                $scope.activeRoute = routeNavigation.getActiveRoute();
            }

            $scope.logout = function () {
                AuthenticationService.Logout();
            };

            $scope.$watch(function(){return routeNavigation.getRoutes();}, function() {
                initController();
            }, $scope.routes);

            $scope.$watch(function(){
                return AuthenticationService.IsLoggedIn();
            }, function() {
                initController();
            }, $scope.isLoggedIn);
        }

        return {
            replace: true,
            templateUrl: "assets/templates/navigationBar.template.html",
            controller: navigationBarController
        };
    }




})();
