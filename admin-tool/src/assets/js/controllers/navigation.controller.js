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
            $scope.routes = routeNavigation.routes;
            $scope.activeRoute = routeNavigation.activeRoute;
        }

        return {
            replace: true,
            templateUrl: "assets/templates/navigationBar.template.html",
            controller: navigationBarController
        };
    }




})();
