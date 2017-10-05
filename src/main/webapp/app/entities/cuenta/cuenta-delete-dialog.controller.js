(function() {
    'use strict';

    angular
        .module('dentalApp')
        .controller('CuentaDeleteController',CuentaDeleteController);

    CuentaDeleteController.$inject = ['$uibModalInstance', 'entity', 'Cuenta'];

    function CuentaDeleteController($uibModalInstance, entity, Cuenta) {
        var vm = this;

        vm.cuenta = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Cuenta.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
