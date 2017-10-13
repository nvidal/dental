(function() {
    'use strict';

    angular
        .module('dentalApp')
        .controller('TratamientoDetailController', TratamientoDetailController);

    TratamientoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Tratamiento', 'Pieza', 'Paciente'];

    function TratamientoDetailController($scope, $rootScope, $stateParams, previousState, entity, Tratamiento, Pieza, Paciente) {
        var vm = this;

        vm.tratamiento = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('dentalApp:tratamientoUpdate', function(event, result) {
            vm.tratamiento = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
