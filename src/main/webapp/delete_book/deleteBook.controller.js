(function() {
    angular
        .module("BookApp")
        .controller("BookDeleteController", BookDeleteController);

    function BookDeleteController(deleteBookService, $uibModalInstance, book, $location) {
        var vm = this;

        vm.deleteBook = deleteBook;
        vm.cancel = cancel;

        function deleteBook() {
            deleteBookService.deleteBook(book.id);
            $uibModalInstance.close(book);
            $location.path("/books");
        }

        function cancel() {
            $uibModalInstance.dismiss('cancel');
        }
    }
})();