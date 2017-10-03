(function() {
    'use strict';

    angular
        .module('dentalApp')
        .controller('ProcedimientoDeleteController',ProcedimientoDeleteController);

    ProcedimientoDeleteController.$inject = ['$uibModalInstance', 'entity', 'Procedimiento'];

    function ProcedimientoDeleteController($uibModalInstance, entity, Procedimiento) {
        var vm = this;

        vm.procedimiento = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Procedimiento.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
