(function() {
    'use strict';

    angular
        .module('dentalApp')
        .controller('PresupuestoDetailController', PresupuestoDetailController);

    PresupuestoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Presupuesto', 'Paciente'];

    function PresupuestoDetailController($scope, $rootScope, $stateParams, previousState, entity, Presupuesto, Paciente) {
        var vm = this;

        vm.presupuesto = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('dentalApp:presupuestoUpdate', function(event, result) {
            vm.presupuesto = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
