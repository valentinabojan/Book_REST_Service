(function() {
    angular
        .module("BookApp")
        .factory("bookListingService", bookListingService);

    function bookListingService($http) {
        var service = {
            getAllBooks: getAllBooks
        };

        return service;

        function getAllBooks(start, end, filter, sortCriteria) {
            var url = "/api/books" + "?" + "start=" + start + "&" + "end=" + end + "&" + "sortBy=" + sortCriteria;

            if (filter.length > 0)
                filter.forEach(function(filterCriteria) {
                    url += "&" + filterCriteria.name + "=" + filterCriteria.value;
                });

            return $http
                .get(url)
                .then(function(response){
                    return response.data;
                });
        }
    }
})();