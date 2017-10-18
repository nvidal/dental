(function() {
    'use strict';

    angular
        .module('dentalApp')
        .controller('NotaDeleteController',NotaDeleteController);

    NotaDeleteController.$inject = ['$uibModalInstance', 'entity', 'Nota'];

    function NotaDeleteController($uibModalInstance, entity, Nota) {
        var vm = this;

        vm.nota = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Nota.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
