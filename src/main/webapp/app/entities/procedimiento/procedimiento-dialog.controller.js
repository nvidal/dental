(function() {
    'use strict';

    angular
        .module('dentalApp')
        .controller('ProcedimientoDialogController', ProcedimientoDialogController);

    ProcedimientoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Procedimiento', 'Pieza', 'Paciente'];

    function ProcedimientoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Procedimiento, Pieza, Paciente) {
        var vm = this;

        vm.procedimiento = entity;
        if(!vm.procedimiento.fecha)
            vm.procedimiento.fecha = new Date();
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
            if (vm.procedimiento.id !== null) {
                Procedimiento.update(vm.procedimiento, onSaveSuccess, onSaveError);
            } else {
                Procedimiento.save(vm.procedimiento, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('dentalApp:procedimientoUpdate', result);
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
