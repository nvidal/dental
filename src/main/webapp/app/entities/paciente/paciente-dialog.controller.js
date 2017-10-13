(function() {
    'use strict';

    angular
        .module('dentalApp')
        .controller('PacienteDialogController', PacienteDialogController);

    PacienteDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Paciente', 'Tratamiento', 'Cuenta', 'Diagnostico'];

    function PacienteDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Paciente, Tratamiento, Cuenta, Diagnostico) {
        var vm = this;

        vm.paciente = entity;
        if (vm.paciente.fecha === null || vm.paciente.fecha == undefined)
            vm.paciente.fecha = new Date();
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.tratamientos = Tratamiento.query();
        vm.cuentas = Cuenta.query();
        vm.diagnosticos = Diagnostico.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.paciente.id !== null) {
                Paciente.update(vm.paciente, onSaveSuccess, onSaveError);
            } else {
                Paciente.save(vm.paciente, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('dentalApp:pacienteUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.fecha = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
