(function(){
    angular
        .module("BookApp")
        .directive('errorSrc', function(){
            console.log("ceva");
            return {
                restrict: 'A',
                link: function(scope, element, attrs) {
                    element.bind('error', function() {
                        if (attrs.src != attrs.errorSrc) {
                            attrs.$set('src', attrs.errorSrc);
                        }
                    });
                }
            }
        })

})();