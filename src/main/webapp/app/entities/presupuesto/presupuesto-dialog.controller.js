(function() {
    'use strict';

    angular
        .module('dentalApp')
        .controller('PresupuestoDialogController', PresupuestoDialogController);

    PresupuestoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Presupuesto', 'Paciente'];

    function PresupuestoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Presupuesto, Paciente) {
        var vm = this;

        vm.presupuesto = entity;
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
            if (vm.presupuesto.id !== null) {
                Presupuesto.update(vm.presupuesto, onSaveSuccess, onSaveError);
            } else {
                Presupuesto.save(vm.presupuesto, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('dentalApp:presupuestoUpdate', result);
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
