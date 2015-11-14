(function() {
    angular
        .module("BookApp", ["ngRoute", "ui.bootstrap", "ngMessages"])
        .config(function($routeProvider, $locationProvider){
            $routeProvider
                .when("/books", {
                    templateUrl: "book_listing/bookListing.html",
                    controller: "BookListingController"
                })
                .when("/books/:bookId", {
                    templateUrl: "book_details/bookDetails.html",
                    controller: "BookDetailsController"
                })
                .when("/books/:bookId/edit", {
                    templateUrl: "edit_book/editBook.html",
                    controller: "EditBookController"
                })
                .when("/new_book", {
                    templateUrl: "edit_book/editBook.html",
                    controller: "EditBookController"
                })
                .otherwise({redirectTo:"/books"});

            //$locationProvider.html5Mode(true);
        });

})();