(function() {
    angular
        .module("BookApp")
        .controller("BookDetailsController", BookDetailsController);

    function BookDetailsController($scope, bookDetailsService, editBookService, $routeParams, $location, $uibModal) {
        $scope.book = {};
        $scope.review = {
            rating:0,
            collapsed: true
        };

        bookDetailsService
            .getBookDetails($routeParams.bookId)
            .then(function(data){
                data.coverUrl = "api" + $location.url();
                $scope.book = data;
                console.log($scope.book);
            });

        $scope.edit = function () {
            $location.path($location.url() + "/edit");
        };

        $scope.delete = function () {
           $uibModal.open({
                templateUrl: 'delete_book/deleteBook.html',
                controller: 'BookDeleteController',
                resolve: {
                   book: function () {
                       return $scope.book;
                   }
               }
            });
        };




        $scope.addReview = function (reviewForm) {
            if(reviewForm.$invalid)
                return;

            $scope.review.date = moment().format("YYYY-MM-DD");

            $scope.book.stars = ($scope.book.stars * $scope.book.reviews.length + $scope.review.rating) / ($scope.book.reviews.length + 1);

            editBookService
                .updateBook($routeParams.bookId, $scope.book);

            bookDetailsService
                .addReview($routeParams.bookId, $scope.review)
                .then(function(data){
                    $scope.book.reviews.unshift(data);
                    $scope.review.collapsed = true;
                });
        }


        $scope.reset = function (reviewForm) {
            $scope.review = {collapsed: true};
            $scope.rating = 0;
            reviewForm.$setPristine();
        }

    }
})();