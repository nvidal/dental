(function() {
    'use strict';

    angular
        .module('dentalApp')
        .controller('PacienteDeleteController',PacienteDeleteController);

    PacienteDeleteController.$inject = ['$uibModalInstance', 'entity', 'Paciente'];

    function PacienteDeleteController($uibModalInstance, entity, Paciente) {
        var vm = this;

        vm.paciente = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Paciente.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
