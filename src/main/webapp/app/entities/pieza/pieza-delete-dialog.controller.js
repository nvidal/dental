(function() {
    'use strict';

    angular
        .module('dentalApp')
        .controller('PiezaDeleteController',PiezaDeleteController);

    PiezaDeleteController.$inject = ['$uibModalInstance', 'entity', 'Pieza'];

    function PiezaDeleteController($uibModalInstance, entity, Pieza) {
        var vm = this;

        vm.pieza = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Pieza.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
