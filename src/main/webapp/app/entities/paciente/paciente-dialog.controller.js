(function() {
    'use strict';

    angular
        .module('dentalApp')
        .controller('PacienteDialogController', PacienteDialogController);

    PacienteDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Paciente', 'Procedimiento', 'Saldo'];

    function PacienteDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Paciente, Procedimiento, Saldo) {
        var vm = this;

        vm.paciente = entity;
        vm.paciente.fecha = new Date();
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.procedimientos = Procedimiento.query();
        vm.saldos = Saldo.query();

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
