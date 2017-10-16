(function() {
    'use strict';

    angular
        .module('dentalApp')
        .controller('PacienteHomeController', PacienteHomeController);

    PacienteHomeController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Paciente', 'Tratamiento', 'Cuenta', 'Diagnostico'];

    function PacienteHomeController($scope, $rootScope, $stateParams, previousState, entity, Paciente, Tratamiento, Cuenta, Diagnostico) {
        var vm = this;

        vm.paciente = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('dentalApp:pacienteUpdate', function(event, result) {
            vm.paciente = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
