(function() {
    angular
        .module("BookApp")
        .factory("bookListingService", BookListingService);

    function BookListingService($http) {
        return {
            getAllBooks: function(start, end, filter, sortCriteria) {
                var url = "/api/books" + "?" + "start=" + start + "&" + "end=" + end + "&" + "sortBy=" + sortCriteria;

                if (filter.length > 0)
                    filter.forEach(function(filterCriteria) {
                       url += "&" + filterCriteria.name + "=" + filterCriteria.value;
                    });

                //console.log(url);
                return $http
                    .get(url)
                    .then(function(response){
                        return response.data;
                    });
            }

        };
    }
})();