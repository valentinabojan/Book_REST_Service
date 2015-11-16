(function() {
    angular
        .module("BookApp")
        .controller("BookListingController", BookListingController);

    function BookListingController($scope, bookListingService, $location) {
        var vm = this;
        var filterByTitle = false;
        var filterByAuthor = false;
        var filter = [];

        vm.books = {};
        vm.pageLimits = [5, 10, 15, 20, 50];
        vm.booksPerPage = 10;
        vm.maxPagesInFooter = 5;
        vm.sortCriteria = 'title';
        vm.searchedTitle = "";
        vm.searchedAuthor = "";

        vm.showBook = showBook;
        vm.pageChanged = pageChanged;
        vm.sortBy = sortBy;
        vm.filterBooks = filterBooks;

        function showBook(bookId) {
            $location.path($location.url() + "/" + bookId);
        }

        function pageChanged() {
            start = vm.booksPerPage * (vm.currentPage - 1);
            end = vm.booksPerPage * vm.currentPage - 1;

            getBooks();
        }

        function getBooks() {
            bookListingService
                .getAllBooks(start, end, filter, vm.sortCriteria)
                .then(success, error);

            function success(data) {
                data.books.forEach(function(book){
                    book.coverUrl = "api" + $location.url() + "/" + book.id;
                });
                vm.books = data.books;
                vm.totalBooks = data.booksCount;
            }

            function error() {
                vm.books = {};
                vm.totalBooks = 0;
            }
        }

        function sortBy(criteria){
            vm.sortCriteria = criteria;
            vm.currentPage = 1;
            vm.pageChanged();
        }

        function filterBooks(){
            filter = [];

            if (filterByTitle)
                filter.push({
                    name: 'title',
                    value: vm.searchedTitle
                });
            if (filterByAuthor)
                filter.push({
                    name: 'author',
                    value: vm.searchedAuthor
                });

            vm.pageChanged();
        }

        $scope.$watch('vm.booksPerPage', function() {
            vm.currentPage = 1;
            vm.pageChanged();
        });

        $scope.$watch('vm.searchedTitle', function(newValue) {
            if (!newValue)
                filterByTitle = false;
            else
                filterByTitle = true;
        });

        $scope.$watch('vm.searchedAuthor', function(newValue) {
            if (!newValue)
                filterByAuthor = false;
            else
                filterByAuthor = true;
        });
    }
})();