(function() {
    'use strict';

    angular
        .module('dentalApp')
        .controller('CuentaDetailController', CuentaDetailController);

    CuentaDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Cuenta', 'Paciente'];

    function CuentaDetailController($scope, $rootScope, $stateParams, previousState, entity, Cuenta, Paciente) {
        var vm = this;

        vm.cuenta = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('dentalApp:cuentaUpdate', function(event, result) {
            vm.cuenta = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
