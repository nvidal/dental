(function() {
    'use strict';

    angular
        .module('dentalApp')
        .controller('DiagnosticoDeleteController',DiagnosticoDeleteController);

    DiagnosticoDeleteController.$inject = ['$uibModalInstance', 'entity', 'Diagnostico'];

    function DiagnosticoDeleteController($uibModalInstance, entity, Diagnostico) {
        var vm = this;

        vm.diagnostico = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Diagnostico.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
