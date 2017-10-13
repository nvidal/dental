(function() {
    'use strict';

    angular
        .module('dentalApp')
        .controller('TratamientoDeleteController',TratamientoDeleteController);

    TratamientoDeleteController.$inject = ['$uibModalInstance', 'entity', 'Tratamiento'];

    function TratamientoDeleteController($uibModalInstance, entity, Tratamiento) {
        var vm = this;

        vm.tratamiento = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Tratamiento.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
