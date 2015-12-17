(function () {

    angular
        .module('app')
        .factory('routeNavigation', routeNavigationFkt);

    routeNavigationFkt.$inject = ['$route', '$location','$rootScope', '$parse'];
    function routeNavigationFkt($route, $location, $rootScope, $parse) {
        var routes = [];
        var service = {};

        $rootScope.$watch(function(){return $rootScope.session;}, function () {
            service.update();
        });

        service.update = function () {
            routes = [];
            angular.forEach($route.routes, function (route, path) {
                if (route.name && route.see && $rootScope.session.loggedIn) {
                    var allowed = false;
                    if (route.needsPermissionOn) {
                        for(var i = 0; i < route.needsPermissionOn.length; i++) {
                            var permissionObject = $parse("$rootScope.session.loggedIn." + route.needsPermissionOn[i])({$rootScope: $rootScope},{});
                            allowed |= permissionObject.create || permissionObject.read || permissionObject.update ||permissionObject.delete;
                        }
                    } else {
                        allowed = true;
                    }

                    routes.push({
                        path: path,
                        name: route.name,
                        allowed: allowed
                    });
                }
            });
        };

        service.getRoutes = function () {
            return routes;
        };

        service.getActiveRoute = function () {
            return function (route) {
                return route.path === $location.path();
            };
        };

        return service;

    }

})();

