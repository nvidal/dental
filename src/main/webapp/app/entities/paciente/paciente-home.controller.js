(function() {
    'use strict';

    angular
        .module('dentalApp')
        .controller('PacienteHomeController', PacienteHomeController);

    PacienteHomeController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Paciente', 'Tratamiento', 'Pago', 'Diagnostico'];

    function PacienteHomeController($scope, $rootScope, $stateParams, previousState, entity, Paciente, Tratamiento, Pago, Diagnostico) {
        var vm = this;

        vm.paciente = entity;
        console.log(entity);
        //vm.trats = Tratamiento.query();

        vm.saldo = 2000;
        vm.previousState = previousState.name;

        vm.ocultarAMedicos = true;
        vm.ocultarDatos = true;
        vm.ocultarTratamientos = true;
        vm.ocultarDiagnosticos = true;
        vm.ocultarPagos = true;

        var unsubscribe = $rootScope.$on('dentalApp:pacienteUpdate', function(event, result) {
            vm.paciente = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
