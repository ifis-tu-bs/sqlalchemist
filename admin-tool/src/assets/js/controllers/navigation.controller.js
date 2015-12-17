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
        navigationBarController.$inject = ['$scope'];
        function navigationBarController($scope) {
            initController();
            console.log($scope);

            function initController() {
                routeNavigation.update();
                $scope.routes = routeNavigation.getRoutes();
                $scope.activeRoute = routeNavigation.getActiveRoute();
            }

            $scope.$watch(function(){return routeNavigation.getRoutes();}, function() {
                initController();
            }, $scope.routes);
        }

        return {
            replace: true,
            templateUrl: "assets/templates/navigationBar.template.html",
            controller: navigationBarController
        };
    }




})();
