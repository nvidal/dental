(function() {
    'use strict';

    angular
        .module('dentalApp')
        .controller('NotaDialogController', NotaDialogController);

    NotaDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'paciente', 'Nota', 'Paciente', 'Principal'];

    function NotaDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, paciente, Nota, Paciente, Principal) {
        var vm = this;

        vm.nota = entity;
        vm.mostrarPaciente = true;
        if (vm.nota.fecha === null || vm.nota.fecha == undefined)
            vm.nota.fecha = new Date();

        if (paciente != null) {
            vm.nota.paciente = paciente;
            vm.mostrarPaciente = false;
        }
        Principal.identity().then(function(account) {
            vm.nota.usuario = account.firstName +' '+account.lastName;
        });
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
            if (vm.nota.id !== null) {
                Nota.update(vm.nota, onSaveSuccess, onSaveError);
            } else {
                Nota.save(vm.nota, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('dentalApp:notaUpdate', result);
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
