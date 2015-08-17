(function() {

    angular
        .module('app')
        .directive('navigation', directNavigationBar);

    directNavigationBar.$inject = ['routeNavigation'];
    function directNavigationBar(routeNavigation) {

        return {
            restrict: "E",
            replace: true,
            templateUrl: "adminapp/templates/navigationBar.template.html",
            controller: navigationBarController
        };

        navigationBarController.$inject = ['$scope'];
        function navigationBarController($scope) {
            $scope.routes = routeNavigation.routes;
            $scope.activeRoute = routeNavigation.activeRoute;
        };

    }



})();

