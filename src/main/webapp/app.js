(function() {
    angular
        .module("BookApp", ["ngRoute", "ui.bootstrap", "ngMessages"])
        .config(function($routeProvider){
            $routeProvider
                .when("/books", {
                    templateUrl: "book_listing/bookListing.html",
                    controller: "BookListingController",
                    controllerAs: "vm"
                })
                .when("/books/:bookId", {
                    templateUrl: "book_details/bookDetails.html",
                    controller: "BookDetailsController",
                    controllerAs: "vm"
                })
                .when("/books/:bookId/edit", {
                    templateUrl: "edit_book/editBook.html",
                    controller: "EditBookController",
                    controllerAs: "vm"
                })
                .when("/new_book", {
                    templateUrl: "edit_book/editBook.html",
                    controller: "EditBookController",
                    controllerAs: "vm"
                })
                .otherwise({redirectTo:"/books"});
        });
})();