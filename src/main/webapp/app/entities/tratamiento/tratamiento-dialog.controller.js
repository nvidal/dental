(function() {
    'use strict';

    angular
        .module('dentalApp')
        .controller('TratamientoDialogController', TratamientoDialogController);

    TratamientoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Tratamiento', 'Pieza', 'Paciente'];

    function TratamientoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Tratamiento, Pieza, Paciente) {
        var vm = this;

        vm.tratamiento = entity;
        if (vm.tratamiento.fecha === null || vm.tratamiento.fecha == undefined)
            vm.tratamiento.fecha = new Date();
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.piezas = Pieza.query();
        vm.pacientes = Paciente.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.tratamiento.id !== null) {
                Tratamiento.update(vm.tratamiento, onSaveSuccess, onSaveError);
            } else {
                Tratamiento.save(vm.tratamiento, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('dentalApp:tratamientoUpdate', result);
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
