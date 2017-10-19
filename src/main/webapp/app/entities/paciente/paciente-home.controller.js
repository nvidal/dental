(function() {
    'use strict';

    angular
        .module('dentalApp')
        .controller('PacienteHomeController', PacienteHomeController);

    PacienteHomeController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Paciente', 'Tratamiento', 'Pago', 'Diagnostico', 'Nota'];

    function PacienteHomeController($scope, $rootScope, $stateParams, previousState, entity, Paciente, Tratamiento, Pago, Diagnostico, Nota) {
        var vm = this;

        vm.paciente = entity;
        console.log(entity);
        vm.trats = Tratamiento.query();
        vm.diags = Diagnostico.query();
        vm.pagos = Pago.query();
        vm.notas = Nota.query();
        console.log(vm.notas);

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
