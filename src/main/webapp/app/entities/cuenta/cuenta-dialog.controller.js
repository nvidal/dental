(function() {
    'use strict';

    angular
        .module('dentalApp')
        .controller('CuentaDialogController', CuentaDialogController);

    CuentaDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Cuenta', 'Paciente'];

    function CuentaDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Cuenta, Paciente) {
        var vm = this;

        vm.cuenta = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.pacientes = Paciente.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.cuenta.id !== null) {
                Cuenta.update(vm.cuenta, onSaveSuccess, onSaveError);
            } else {
                Cuenta.save(vm.cuenta, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('dentalApp:cuentaUpdate', result);
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
