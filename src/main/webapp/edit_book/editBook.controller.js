(function() {
    angular
        .module("BookApp")
        .controller("EditBookController", EditBookController);

    function EditBookController(editBookService, bookDetailsService, $routeParams, $location) {
        var vm = this;
        var operationCallback = null;

        vm.languages = [];
        vm.categories = [];
        vm.originalBook = {};
        vm.book = {};
        vm.date_popup_opened = false;

        vm.reset = reset;
        vm.toggleSelection = toggleSelection;
        vm.deleteAuthor = deleteAuthor;
        vm.addFormField = addFormField;
        vm.openDatePopup = openDatePopup;
        vm.updateBook = updateBook;

        activate();

        function activate() {
            vm.languages = ["English", "French", "German", "Romanian", "Spanish"];
            vm.categories = ["HISTORICAL", "ROMANCE", "FANTASY", "YOUNG_ADULT", "ACTION", "MYSTERY", "POETRY", "ART",
                             "SCIENCE", "PROGRAMMING", "ADVENTURE", "WAR", "CHILDREN"];

            if ($location.url().endsWith("edit")) {
                activateEdit();
            } else {
                activateAdd();
            }
        }

        function activateEdit() {
            bookDetailsService
                .getBookDetails($routeParams.bookId)
                .then(function(data){
                    vm.originalBook = data;
                    vm.book = angular.copy(vm.originalBook);
                    vm.book.date = moment(new Date(vm.book.date)).format("DD-MMMM-YYYY");
                });

            operationCallback = function() {
                editBookService
                    .updateBook($routeParams.bookId, vm.book)
                    .then(function(data){
                        $location.path("/books/" + data.id);
                    });
            };
        }

        function activateAdd() {
            vm.originalBook = {
                authors: [""],
                categories:[]
            };
            vm.book = angular.copy(vm.originalBook);

            operationCallback = function() {
                editBookService
                    .addBook(vm.book)
                    .then(function(data){
                        $location.path("/books/" + data.id);
                    });
            };
        }

        function reset () {
            vm.book = angular.copy(vm.originalBook);
        }

        //$scope.book = {
        //    date: new Date(),
        //    authors: [""],
        //    categories:[]
        //};

        function toggleSelection(book_category) {
            var idx = vm.book.categories.indexOf(book_category);

            if (idx > -1)
                vm.book.categories.splice(idx, 1);
            else
                vm.book.categories.push(book_category);
        }

        function deleteAuthor($index) {
            vm.book.authors.splice($index, 1);

            if (vm.book.authors.length == 0)
                vm.book.authors.push("");
        }

        function addFormField() {
            vm.book.authors.push("");
        }

        function openDatePopup() {
            vm.date_popup_opened = true;
        }

        function updateBook(bookForm) {
            if(bookForm.$invalid)
                return;

            vm.book.date = moment(new Date(vm.book.date)).format("YYYY-MM-DD");

            operationCallback();
        }
    }
})();