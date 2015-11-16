(function(){
    angular
        .module("BookApp")
        .directive('errorSrc', function(){
            return {
                restrict: 'A',
                link: function(scope, element, attrs) {
                    element.bind('error', function() {
                        attrs.$set('src', attrs.errorSrc);
                    });
                }
            }
        })
})();