(function() {
    angular
        .module("BookApp", ["ngRoute", "ui.bootstrap"])
        .config(function($routeProvider, $locationProvider){
            $routeProvider
                .when("/", {
                    templateUrl: "book_details/bookDetails.html",
                    controller: "BookDetailsController"
                })
                .when("/books/:bookId", {
                    templateUrl: "book_details/bookDetails.html",
                    controller: "BookDetailsController"
                })
                .otherwise({redirectTo:"/"});

            //$locationProvider.html5Mode(true);
        });

})();