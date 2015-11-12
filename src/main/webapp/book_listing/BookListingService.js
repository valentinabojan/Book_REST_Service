(function() {
    angular
        .module("BookApp")
        .factory("bookListingService", BookListingService);

    function BookListingService($http) {
        return {
            getAllBooks: function() {
                return $http
                    .get("/api/books")
                    .then(function(response){
                        return response.data;
                    });
            }
        };
    }
})();