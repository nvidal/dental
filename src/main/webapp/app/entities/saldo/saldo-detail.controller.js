(function() {
    'use strict';

    angular
        .module('dentalApp')
        .controller('SaldoDetailController', SaldoDetailController);

    SaldoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Saldo', 'Paciente'];

    function SaldoDetailController($scope, $rootScope, $stateParams, previousState, entity, Saldo, Paciente) {
        var vm = this;

        vm.saldo = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('dentalApp:saldoUpdate', function(event, result) {
            vm.saldo = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
