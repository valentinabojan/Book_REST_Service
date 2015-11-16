(function() {
    angular
        .module("BookApp")
        .controller("BookDetailsController", BookDetailsController);

    function BookDetailsController(bookDetailsService, editBookService, $routeParams, $location, $uibModal) {
        var vm = this;
        var cleanReview = {};

        vm.book = {};
        vm.review = {};

        vm.editBook = editBook;
        vm.deleteBook = deleteBook;
        vm.addReview = addReview;
        vm.reset = reset;

        activate();

        function activate() {
            cleanReview = {
                rating: 0,
                collapsed: true
            };

            vm.review = angular.copy(cleanReview);

            bookDetailsService
                .getBookDetails($routeParams.bookId)
                .then(function(data){
                    data.coverUrl = "api" + $location.url();
                    vm.book = data;
                });
        }

        function editBook() {
            $location.path($location.url() + "/edit");
        }

        function deleteBook(){
           $uibModal.open({
                templateUrl: 'delete_book/deleteBook.html',
                controller: 'BookDeleteController',
                controllerAs: 'vm',
                resolve: {
                   book: function () {
                       return vm.book;
                   }
               }
            });
        }

        function addReview(reviewForm) {
            if(reviewForm.$invalid)
                return;

            vm.review.date = moment().format("YYYY-MM-DD");

            if (!vm.book.stars)
                vm.book.stars = 0;
            vm.book.stars = (vm.book.stars * vm.book.reviews.length + vm.review.rating) / (vm.book.reviews.length + 1);

            editBookService
                .updateBook($routeParams.bookId, vm.book);

            bookDetailsService
                .addReview($routeParams.bookId, vm.review)
                .then(function(data){
                    vm.book.reviews.unshift(data);
                    vm.review.collapsed = true;
                });

            reset(reviewForm);
        }

        function reset(reviewForm) {
            vm.review = angular.copy(cleanReview);
            reviewForm.$setPristine();
        }
    }
})();