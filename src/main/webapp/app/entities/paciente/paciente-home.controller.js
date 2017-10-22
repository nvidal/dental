(function() {
    'use strict';

    angular
        .module('dentalApp')
        .controller('PacienteHomeController', PacienteHomeController);

    PacienteHomeController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Paciente', 'Tratamiento', 'Pago', 'Diagnostico', 'Nota'];

    function PacienteHomeController($scope, $rootScope, $stateParams, previousState, entity, Paciente, Tratamiento, Pago, Diagnostico, Nota) {
        var vm = this;

        vm.paciente = entity
        vm.notas = Nota.query({paciente : vm.paciente.id});
        vm.previousState = previousState.name;
        vm.setEstado = setEstado;

        vm.ocultarAMedicos = true;
        vm.ocultarDatos = true;
        vm.ocultarTratamientos = true;
        vm.ocultarTratamiento = function(){
            vm.ocultarTratamientos = !vm.ocultarTratamientos;
            if(!vm.ocultarTratamientos)
                vm.trats = Tratamiento.query({paciente : vm.paciente.id});
        };
        vm.ocultarDiagnosticos = true;
        vm.ocultarDiagnostico = function(){
            vm.ocultarDiagnosticos = !vm.ocultarDiagnosticos;
            if (!vm.ocultarDiagnosticos)
                vm.diags = Diagnostico.query({paciente : vm.paciente.id});
        };
        vm.ocultarPagos = true;
        vm.ocultarPago = function(){
            vm.ocultarPagos = !vm.ocultarPagos;
            if (!vm.ocultarPagos)
                vm.pagos = Pago.query({paciente : vm.paciente.id});
        };

        function setEstado (diagnostico, val) {
            if (val)
                diagnostico.estado = 'REALIZADO';
            else
                diagnostico.estado = 'CANCELADO';
            Diagnostico.update(diagnostico, function () {
                //vm.loadAll();
                //vm.clear();
                console.log(diagnostico);
            });
        }

        var unsubscribe = $rootScope.$on('dentalApp:pacienteUpdate', function(event, result) {
            vm.paciente = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
