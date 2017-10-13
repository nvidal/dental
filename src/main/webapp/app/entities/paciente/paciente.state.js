(function() {
    'use strict';

    angular
        .module('dentalApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('paciente', {
            parent: 'entity',
            url: '/paciente?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'dentalApp.paciente.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/paciente/pacientes.html',
                    controller: 'PacienteController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('paciente');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('paciente-detail', {
            parent: 'paciente',
            url: '/paciente/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'dentalApp.paciente.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/paciente/paciente-detail.html',
                    controller: 'PacienteDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('paciente');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Paciente', function($stateParams, Paciente) {
                    return Paciente.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'paciente',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('paciente-detail.edit', {
            parent: 'paciente-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/paciente/paciente-dialog.html',
                    controller: 'PacienteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Paciente', function(Paciente) {
                            return Paciente.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('paciente.new', {
            parent: 'paciente',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/paciente/paciente-dialog.html',
                    controller: 'PacienteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                fecha: null,
                                nombres: null,
                                apellidos: null,
                                cedula: null,
                                telefono: null,
                                celular: null,
                                direccion: null,
                                mail: null,
                                alergico: null,
                                diabetes: null,
                                presionAlta: null,
                                tiroides: null,
                                cicatrizacion: null,
                                cardiaca: null,
                                farmacos: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('paciente', null, { reload: 'paciente' });
                }, function() {
                    $state.go('paciente');
                });
            }]
        })
        .state('paciente.edit', {
            parent: 'paciente',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/paciente/paciente-dialog.html',
                    controller: 'PacienteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Paciente', function(Paciente) {
                            return Paciente.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('paciente', null, { reload: 'paciente' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('paciente.delete', {
            parent: 'paciente',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/paciente/paciente-delete-dialog.html',
                    controller: 'PacienteDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Paciente', function(Paciente) {
                            return Paciente.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('paciente', null, { reload: 'paciente' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
