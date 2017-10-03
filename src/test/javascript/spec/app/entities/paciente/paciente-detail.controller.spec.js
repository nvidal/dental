'use strict';

describe('Controller Tests', function() {

    describe('Paciente Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockPaciente, MockProcedimiento, MockSaldo;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockPaciente = jasmine.createSpy('MockPaciente');
            MockProcedimiento = jasmine.createSpy('MockProcedimiento');
            MockSaldo = jasmine.createSpy('MockSaldo');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Paciente': MockPaciente,
                'Procedimiento': MockProcedimiento,
                'Saldo': MockSaldo
            };
            createController = function() {
                $injector.get('$controller')("PacienteDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'dentalApp:pacienteUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
