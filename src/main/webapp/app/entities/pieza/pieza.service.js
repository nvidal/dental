(function() {
    'use strict';
    angular
        .module('dentalApp')
        .factory('Pieza', Pieza);

    Pieza.$inject = ['$resource'];

    function Pieza ($resource) {
        var resourceUrl =  'api/piezas/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
