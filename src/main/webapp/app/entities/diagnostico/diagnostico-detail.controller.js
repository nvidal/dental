(function() {
    'use strict';

    angular
        .module('dentalApp')
        .controller('DiagnosticoDetailController', DiagnosticoDetailController);

    DiagnosticoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Diagnostico', 'Pieza', 'Paciente'];

    function DiagnosticoDetailController($scope, $rootScope, $stateParams, previousState, entity, Diagnostico, Pieza, Paciente) {
        var vm = this;

        vm.diagnostico = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('dentalApp:diagnosticoUpdate', function(event, result) {
            vm.diagnostico = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
