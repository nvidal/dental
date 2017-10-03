(function() {
    'use strict';

    angular
        .module('dentalApp')
        .controller('SaldoDeleteController',SaldoDeleteController);

    SaldoDeleteController.$inject = ['$uibModalInstance', 'entity', 'Saldo'];

    function SaldoDeleteController($uibModalInstance, entity, Saldo) {
        var vm = this;

        vm.saldo = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Saldo.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
