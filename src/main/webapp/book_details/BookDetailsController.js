(function() {
    angular
        .module("BookApp")
        .controller("BookDetailsController", BookDetailsController);

    function BookDetailsController($scope, bookDetailsService, $routeParams, $location, $uibModal) {
        $scope.book = {};

        bookDetailsService
            .getBookDetails($routeParams.bookId)
            .then(function(data){
                data.coverUrl = "api" + $location.url();
                $scope.book = data;
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

    }
})();