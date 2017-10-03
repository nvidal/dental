(function() {
    'use strict';

    angular
        .module('dentalApp')
        .controller('ProcedimientoDetailController', ProcedimientoDetailController);

    ProcedimientoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Procedimiento', 'Pieza', 'Paciente'];

    function ProcedimientoDetailController($scope, $rootScope, $stateParams, previousState, entity, Procedimiento, Pieza, Paciente) {
        var vm = this;

        vm.procedimiento = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('dentalApp:procedimientoUpdate', function(event, result) {
            vm.procedimiento = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
