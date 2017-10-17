(function() {
    'use strict';

    angular
        .module('dentalApp')
        .controller('PagoDialogController', PagoDialogController);

    PagoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'paciente', 'Pago', 'Paciente'];

    function PagoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, paciente, Pago, Paciente) {
        var vm = this;

        vm.pago = entity;
        vm.mostrarPaciente = true;
        if (vm.pago.fecha === null || vm.pago.fecha == undefined)
            vm.pago.fecha = new Date();

        if (paciente != null) {
            vm.pago.paciente = paciente;
            vm.mostrarPaciente = false;
        }
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
            if (vm.pago.id !== null) {
                Pago.update(vm.pago, onSaveSuccess, onSaveError);
            } else {
                Pago.save(vm.pago, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('dentalApp:pagoUpdate', result);
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
