'use strict';

describe('Controller Tests', function() {

    describe('Diagnostico Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockDiagnostico, MockPieza, MockPaciente;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockDiagnostico = jasmine.createSpy('MockDiagnostico');
            MockPieza = jasmine.createSpy('MockPieza');
            MockPaciente = jasmine.createSpy('MockPaciente');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Diagnostico': MockDiagnostico,
                'Pieza': MockPieza,
                'Paciente': MockPaciente
            };
            createController = function() {
                $injector.get('$controller')("DiagnosticoDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'dentalApp:diagnosticoUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
