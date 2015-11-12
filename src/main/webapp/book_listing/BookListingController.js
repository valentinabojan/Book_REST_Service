(function() {
    angular
        .module("BookApp")
        .controller("BookListingController", BookListingController);

    function BookListingController($scope, bookListingService, $location) {
        $scope.books = {};

        bookListingService
            .getAllBooks()
            .then(function(data){

                data.forEach(function(book){
                    book.coverUrl = "api" + $location.url() + "/" + book.id;
                    console.log(book.coverUrl);
                });
                console.log(data);
                $scope.books = data;
            });

        $scope.showBook = function(bookId) {
            $location.path($location.url() + "/" + bookId);
        };

        $scope.sortOrder = function() {
            return $scope.sortReverse ? '-' + $scope.sortType : $scope.sortType;
        };

        $scope.sortReverse = false;
        $scope.sortType = 'title';
    }
})();