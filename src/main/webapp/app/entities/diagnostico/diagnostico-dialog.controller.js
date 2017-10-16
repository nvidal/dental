(function() {
    'use strict';

    angular
        .module('dentalApp')
        .controller('DiagnosticoDialogController', DiagnosticoDialogController);

    DiagnosticoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'paciente', 'Diagnostico', 'Pieza', 'Paciente'];

    function DiagnosticoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, paciente, Diagnostico, Pieza, Paciente) {
        var vm = this;

        vm.mostrarPaciente = true;
        vm.diagnostico = entity;
        if (vm.diagnostico.fecha === null || vm.diagnostico.fecha == undefined)
            vm.diagnostico.fecha = new Date();
        //Me fijo si viene el paciente cargado
        if (paciente != null) {
            vm.diagnostico.paciente = paciente;
            vm.mostrarPaciente = false;
        }
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
            if (vm.diagnostico.id !== null) {
                Diagnostico.update(vm.diagnostico, onSaveSuccess, onSaveError);
            } else {
                Diagnostico.save(vm.diagnostico, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('dentalApp:diagnosticoUpdate', result);
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
