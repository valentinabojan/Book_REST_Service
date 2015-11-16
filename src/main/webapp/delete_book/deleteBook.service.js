(function() {
    angular
        .module("BookApp")
        .factory("deleteBookService", deleteBookService);

    function deleteBookService($http) {
        var service = {
            deleteBook: deleteBook
        };

        return service;

        function deleteBook(bookId){
            return $http.delete("/api/books/" + bookId);
        }
    }
})();