(function() {
    angular
        .module("BookApp")
        .controller("EditBookController", EditBookController);

    function EditBookController($scope, editBookService, bookDetailsService, $routeParams, $location) {
        $scope.languages = ["English", "French", "German", "Romanian", "Spanish"];
        $scope.categories = ["HISTORICAL", "ROMANCE", "FANTASY", "YOUNG_ADULT", "ACTION", "MYSTERY", "POETRY", "ART",
                            "SCIENCE", "PROGRAMMING", "ADVENTURE", "WAR", "CHILDREN"];

        if ($location.url().endsWith("edit")) {
            bookDetailsService
                .getBookDetails($routeParams.bookId)
                .then(function(data){
                    $scope.originalBook = data;
                    $scope.book = angular.copy($scope.originalBook);
                    $scope.book.date = moment(new Date($scope.book.date)).format("DD-MMMM-YYYY");
                });

            $scope.updateBook = function (bookForm) {
                if(bookForm.$invalid)
                    return;

                $scope.book.date = moment(new Date($scope.book.date)).format("YYYY-MM-DD");

                editBookService
                    .updateBook($routeParams.bookId, $scope.book)
                    .then(function(data){
                        $location.path("/books/" + data.id);
                    });
            }
        } else {
            $scope.originalBook = {
                authors: [""],
                categories:[]
            };
            $scope.book = angular.copy($scope.originalBook);

            $scope.updateBook = function (bookForm) {
                if(bookForm.$invalid)
                    return;

                $scope.book.date = moment(new Date($scope.book.date)).format("YYYY-MM-DD");

                editBookService
                    .addBook($scope.book)
                    .then(function(data){
                        $location.path("/books/" + data.id);
                    });
            }
        }

        $scope.reset = function () {
            $scope.book = angular.copy($scope.originalBook);
        }

        $scope.book = {
            date: new Date(),
            authors: [""],
            categories:[]
        };

        $scope.toggleSelection = function toggleSelection(book_category) {
            var idx = $scope.book.categories.indexOf(book_category);

            if (idx > -1)
                $scope.book.categories.splice(idx, 1);
            else
                $scope.book.categories.push(book_category);
        };

        $scope.deleteAuthor = function ($index) {
            $scope.book.authors.splice($index, 1);

            if ($scope.book.authors.length == 0)
                $scope.book.authors.push("");
        };

        $scope.addFormField = function () {
            $scope.book.authors.push("");
        };

        $scope.open = function() {
            $scope.date_popup_opened = true;
        };

        $scope.date_popup_opened = false;
    }
})();