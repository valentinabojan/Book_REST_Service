(function() {
    angular
        .module("BookApp")
        .factory("editBookService", EditBookService);

    function EditBookService($http) {
        return {
            updateBook: function(bookId, book) {
                return $http
                    .put("/api/books/" + bookId, book)
                    .then(function(response){
                        return response.data;
                    });
            },

            addBook: function(book) {
                return $http
                    .post("/api/books", book)
                    .then(function(response){
                        return response.data;
                    });
            }
        };
    }
})();