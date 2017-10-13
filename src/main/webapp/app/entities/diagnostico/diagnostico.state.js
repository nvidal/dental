(function() {
    'use strict';

    angular
        .module('dentalApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('diagnostico', {
            parent: 'entity',
            url: '/diagnostico?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'dentalApp.diagnostico.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/diagnostico/diagnosticos.html',
                    controller: 'DiagnosticoController',
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
                    $translatePartialLoader.addPart('diagnostico');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('diagnostico-detail', {
            parent: 'diagnostico',
            url: '/diagnostico/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'dentalApp.diagnostico.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/diagnostico/diagnostico-detail.html',
                    controller: 'DiagnosticoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('diagnostico');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Diagnostico', function($stateParams, Diagnostico) {
                    return Diagnostico.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'diagnostico',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('diagnostico-detail.edit', {
            parent: 'diagnostico-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/diagnostico/diagnostico-dialog.html',
                    controller: 'DiagnosticoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Diagnostico', function(Diagnostico) {
                            return Diagnostico.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('diagnostico.new', {
            parent: 'diagnostico',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/diagnostico/diagnostico-dialog.html',
                    controller: 'DiagnosticoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                fecha: null,
                                descripcion: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('diagnostico', null, { reload: 'diagnostico' });
                }, function() {
                    $state.go('diagnostico');
                });
            }]
        })
        .state('diagnostico.edit', {
            parent: 'diagnostico',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/diagnostico/diagnostico-dialog.html',
                    controller: 'DiagnosticoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Diagnostico', function(Diagnostico) {
                            return Diagnostico.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('diagnostico', null, { reload: 'diagnostico' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('diagnostico.delete', {
            parent: 'diagnostico',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/diagnostico/diagnostico-delete-dialog.html',
                    controller: 'DiagnosticoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Diagnostico', function(Diagnostico) {
                            return Diagnostico.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('diagnostico', null, { reload: 'diagnostico' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
