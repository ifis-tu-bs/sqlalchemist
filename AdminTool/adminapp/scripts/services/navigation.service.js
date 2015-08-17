(function () {

    angular
        .module('app')
        .factory('routeNavigation', routeNavigationFkt);

    routeNavigationFkt.$inject = ['$route', '$location'];
    function routeNavigationFkt($route, $location) {
        var routes = [];
        angular.forEach($route.routes, function (route, path) {
            if (route.name && route.see) {
                routes.push({
                    path: path,
                    name: route.name
                });
            }
        });

        return {
            routes: routes,
            activeRoute: function (route) {
                return route.path === $location.path();
            }
        };
    }

})();

