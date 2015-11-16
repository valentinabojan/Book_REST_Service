(function() {
    angular
        .module("BookApp")
        .factory("bookDetailsService", bookDetailsService);

    function bookDetailsService($http) {
        var service = {
            getBookDetails: getBookDetails,
            addReview: addReview
        };

        return service;

        function getBookDetails(bookId) {
            return $http.get("/api/books/" + bookId)
                        .then(function(response){
                            return response.data;
                        });
        }

        function addReview(bookId, review) {
            return $http.post("/api/books/" + bookId + "/reviews", review)
                        .then(function(response){
                            return response.data;
                        });
        }
    }
})();