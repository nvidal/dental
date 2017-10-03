(function() {
    'use strict';

    angular
        .module('dentalApp')
        .controller('PacienteDetailController', PacienteDetailController);

    PacienteDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Paciente', 'Procedimiento', 'Saldo'];

    function PacienteDetailController($scope, $rootScope, $stateParams, previousState, entity, Paciente, Procedimiento, Saldo) {
        var vm = this;

        vm.paciente = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('dentalApp:pacienteUpdate', function(event, result) {
            vm.paciente = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
