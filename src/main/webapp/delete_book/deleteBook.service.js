(function() {
    angular
        .module("BookApp")
        .factory("deleteBookService", DeleteBookService);

    function DeleteBookService($http) {
        return {
            deleteBook: function(bookId) {
                return $http
                    .delete("/api/books/" + bookId)
            }
        };
    }
})();