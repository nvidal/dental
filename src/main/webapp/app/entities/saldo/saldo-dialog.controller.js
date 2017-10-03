(function() {
    'use strict';

    angular
        .module('dentalApp')
        .controller('SaldoDialogController', SaldoDialogController);

    SaldoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Saldo', 'Paciente'];

    function SaldoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Saldo, Paciente) {
        var vm = this;

        vm.saldo = entity;
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
            if (vm.saldo.id !== null) {
                Saldo.update(vm.saldo, onSaveSuccess, onSaveError);
            } else {
                Saldo.save(vm.saldo, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('dentalApp:saldoUpdate', result);
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
