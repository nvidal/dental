(function() {
    'use strict';
    angular
        .module('dentalApp')
        .factory('Paciente', Paciente).factory('PacienteFilter', PacienteFilter);

    Paciente.$inject = ['$resource', 'DateUtils'];
    PacienteFilter.$inject = ['$resource'];

    function Paciente ($resource, DateUtils) {
        var resourceUrl =  'api/pacientes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.fecha = DateUtils.convertLocalDateFromServer(data.fecha);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.fecha = DateUtils.convertLocalDateToServer(copy.fecha);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.fecha = DateUtils.convertLocalDateToServer(copy.fecha);
                    return angular.toJson(copy);
                }
            }
        });
    }

    function PacienteFilter ($resource) {
        var resourceUrl =  'api/pacientes/query';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
