(function() {
    angular
        .module("BookApp")
        .factory("editBookService", editBookService);

    function editBookService($http) {
        var service = {
            updateBook: updateBook,
            addBook: addBook
        };

        return service;

        function updateBook(bookId, book) {
            return $http.put("/api/books/" + bookId, book)
                        .then(function(response){
                            return response.data;
                        });
        }

        function addBook(book) {
            return $http.post("/api/books", book)
                        .then(function(response){
                            return response.data;
                        });
        }
    }
})();