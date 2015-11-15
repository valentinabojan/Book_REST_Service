(function() {
    angular
        .module("BookApp")
        .factory("bookListingService", BookListingService);

    function BookListingService($http) {
        return {
            getAllBooks: function(start, end, sortCriteria) {
                console.log("/api/books" + "?" + "start=" + start + "&" + "end=" + end + "&" + "sortBy=" + sortCriteria);
                return $http
                    .get("/api/books" + "?" + "start=" + start + "&" + "end=" + end + "&" + "sortBy=" + sortCriteria)
                    .then(function(response){
                        return response.data;
                    });
            }

        };
    }
})();