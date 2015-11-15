(function() {
    angular
        .module("BookApp")
        .controller("BookListingController", BookListingController);

    function BookListingController($scope, bookListingService, $location) {
        $scope.books = {};
        $scope.pageLimits = [5, 10, 15, 20, 50];
        $scope.booksPerPage = 10;
        $scope.maxPagesInFooter = 5;
        $scope.sortCriteria = 'title';


        $scope.showBook = function(bookId) {
            $location.path($location.url() + "/" + bookId);
        };


        $scope.$watch('booksPerPage', function(newValue, oldValue) {
            $scope.currentPage = 1;

            $scope.pageChanged();
        });




        $scope.pageChanged = function() {
            start = $scope.booksPerPage * ($scope.currentPage - 1);
            end = $scope.booksPerPage * $scope.currentPage - 1;

            getBooks();
        };

        function getBooks() {
            bookListingService
                .getAllBooks(start, end, $scope.sortCriteria)
                .then(function(data){
                    data.books.forEach(function(book){
                        book.coverUrl = "api" + $location.url() + "/" + book.id;
                    });
                    $scope.books = data.books;
                    $scope.totalBooks = data.booksCount;
                });
        }




        $scope.sortBy = function (criteria){
            $scope.sortCriteria = criteria;

            $scope.currentPage = 1;

            $scope.pageChanged();
        }


        //
        //$scope.sortOrder = function() {
        //    console.log($scope.booksPerPage);
        //
        //    return $scope.sortReverse ? '-' + $scope.sortType : $scope.sortType;
        //};
        //
        //$scope.sortReverse = false;
        //$scope.sortType = 'title';
    }
})();