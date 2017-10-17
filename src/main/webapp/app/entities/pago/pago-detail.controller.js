(function() {
    'use strict';

    angular
        .module('dentalApp')
        .controller('PagoDetailController', PagoDetailController);

    PagoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Pago', 'Paciente'];

    function PagoDetailController($scope, $rootScope, $stateParams, previousState, entity, Pago, Paciente) {
        var vm = this;

        vm.pago = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('dentalApp:pagoUpdate', function(event, result) {
            vm.pago = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
