(function() {
    angular
        .module("BookApp")
        .factory("bookDetailsService", BookDetailsService);

    function BookDetailsService($http) {
        return {
            getBookDetails: function(bookId) {
                return $http
                    .get("/api/books/" + bookId)
                    .then(function(response){
                        return response.data;
                    });
            }
        };
    }
})();