(function() {
    angular
        .module("BookApp")
        .controller("BookDetailsController", BookDetailsController);

    function BookDetailsController($scope, bookDetailsService, $routeParams, $location) {
        $scope.book = {};

        bookDetailsService
            .getBookDetails($routeParams.bookId)
            .then(function(data){
                console.log(data);
                data.coverUrl = "api" + $location.url();
                $scope.book = data;
            });
    }
})();