(function() {
    'use strict';

    angular
        .module('dentalApp')
        .controller('PiezaDialogController', PiezaDialogController);

    PiezaDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Pieza'];

    function PiezaDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Pieza) {
        var vm = this;

        vm.pieza = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.pieza.id !== null) {
                Pieza.update(vm.pieza, onSaveSuccess, onSaveError);
            } else {
                Pieza.save(vm.pieza, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('dentalApp:piezaUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
