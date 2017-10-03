(function() {
    'use strict';

    angular
        .module('dentalApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('procedimiento', {
            parent: 'entity',
            url: '/procedimiento?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'dentalApp.procedimiento.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/procedimiento/procedimientos.html',
                    controller: 'ProcedimientoController',
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
                    $translatePartialLoader.addPart('procedimiento');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('procedimiento-detail', {
            parent: 'procedimiento',
            url: '/procedimiento/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'dentalApp.procedimiento.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/procedimiento/procedimiento-detail.html',
                    controller: 'ProcedimientoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('procedimiento');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Procedimiento', function($stateParams, Procedimiento) {
                    return Procedimiento.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'procedimiento',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('procedimiento-detail.edit', {
            parent: 'procedimiento-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/procedimiento/procedimiento-dialog.html',
                    controller: 'ProcedimientoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Procedimiento', function(Procedimiento) {
                            return Procedimiento.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('procedimiento.new', {
            parent: 'procedimiento',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/procedimiento/procedimiento-dialog.html',
                    controller: 'ProcedimientoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                fecha: null,
                                procedimiento: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('procedimiento', null, { reload: 'procedimiento' });
                }, function() {
                    $state.go('procedimiento');
                });
            }]
        })
        .state('procedimiento.edit', {
            parent: 'procedimiento',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/procedimiento/procedimiento-dialog.html',
                    controller: 'ProcedimientoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Procedimiento', function(Procedimiento) {
                            return Procedimiento.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('procedimiento', null, { reload: 'procedimiento' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('procedimiento.delete', {
            parent: 'procedimiento',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/procedimiento/procedimiento-delete-dialog.html',
                    controller: 'ProcedimientoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Procedimiento', function(Procedimiento) {
                            return Procedimiento.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('procedimiento', null, { reload: 'procedimiento' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
