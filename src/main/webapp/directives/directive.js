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
    //
    //
    //.directive('nationpicker', function ($parse) {
    //        return {
    //            restrict: 'A',
    //            replace: false,
    //            transclude: false,
    //            link: function (scope, element, attrs) {
    //                element.append('<select id="language" class="form-control bfh-languages" data-language="English"></select>');
    //                $('#language').bfhlanguages()
    //            }
    //        }
    //    })
})();