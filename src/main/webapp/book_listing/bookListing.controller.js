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
        var filterByTitle = false;
        var filterByAuthor = false;
        $scope.searchedTitle = "";
        $scope.searchedAuthor = "";
        var filter = [];


        $scope.showBook = function(bookId) {
            $location.path($location.url() + "/" + bookId);
        };


        $scope.$watch('booksPerPage', function(newValue, oldValue) {
            $scope.currentPage = 1;
            $scope.pageChanged();
        });

        $scope.$watch('searchedTitle', function(newValue, oldValue) {
            if (!newValue)
                filterByTitle = false;
            else
                filterByTitle = true;
        });

        $scope.$watch('searchedAuthor', function(newValue, oldValue) {
            if (!newValue)
                filterByAuthor = false;
            else
                filterByAuthor = true;
        });




        $scope.pageChanged = function() {
            start = $scope.booksPerPage * ($scope.currentPage - 1);
            end = $scope.booksPerPage * $scope.currentPage - 1;

            getBooks();
        };

        function getBooks() {
            bookListingService
                .getAllBooks(start, end, filter, $scope.sortCriteria)
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

        $scope.filterBooks = function (){
            //console.log(filterByTitle + " - " + filterByAuthor + " -> " + $scope.searchedTitle + " - " + $scope.searchedAuthor);
            filter = [];

            if (filterByTitle)
                filter.push({name: 'title', value:$scope.searchedTitle});
            if (filterByAuthor)
                filter.push({name: 'author', value:$scope.searchedAuthor});

            $scope.pageChanged();
        }



    }
})();