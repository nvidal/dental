(function() {
    'use strict';

    angular
        .module('dentalApp')
        .controller('PiezaDetailController', PiezaDetailController);

    PiezaDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Pieza'];

    function PiezaDetailController($scope, $rootScope, $stateParams, previousState, entity, Pieza) {
        var vm = this;

        vm.pieza = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('dentalApp:piezaUpdate', function(event, result) {
            vm.pieza = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
