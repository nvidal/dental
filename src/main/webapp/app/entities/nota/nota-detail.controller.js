(function() {
    'use strict';

    angular
        .module('dentalApp')
        .controller('NotaDetailController', NotaDetailController);

    NotaDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Nota', 'Paciente'];

    function NotaDetailController($scope, $rootScope, $stateParams, previousState, entity, Nota, Paciente) {
        var vm = this;

        vm.nota = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('dentalApp:notaUpdate', function(event, result) {
            vm.nota = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
