(function() {
    angular
        .module("BookApp")
        .controller("BookDeleteController", BookDeleteController);

    function BookDeleteController($scope, deleteBookService, $uibModalInstance, book, $location) {
        $scope.book = book;

        $scope.ok = function () {
            deleteBookService.deleteBook(book.id);

            $uibModalInstance.close($scope.book);

            $location.path("/books");
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();