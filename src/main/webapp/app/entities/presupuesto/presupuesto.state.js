(function() {
    'use strict';

    angular
        .module('dentalApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('presupuesto', {
            parent: 'entity',
            url: '/presupuesto?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'dentalApp.presupuesto.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/presupuesto/presupuestos.html',
                    controller: 'PresupuestoController',
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
                    $translatePartialLoader.addPart('presupuesto');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('presupuesto-detail', {
            parent: 'presupuesto',
            url: '/presupuesto/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'dentalApp.presupuesto.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/presupuesto/presupuesto-detail.html',
                    controller: 'PresupuestoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('presupuesto');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Presupuesto', function($stateParams, Presupuesto) {
                    return Presupuesto.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'presupuesto',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('presupuesto-detail.edit', {
            parent: 'presupuesto-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/presupuesto/presupuesto-dialog.html',
                    controller: 'PresupuestoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Presupuesto', function(Presupuesto) {
                            return Presupuesto.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('presupuesto.new', {
            parent: 'presupuesto',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/presupuesto/presupuesto-dialog.html',
                    controller: 'PresupuestoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                fecha: null,
                                descripcion: null,
                                precio: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('presupuesto', null, { reload: 'presupuesto' });
                }, function() {
                    $state.go('presupuesto');
                });
            }]
        })
        .state('presupuesto.edit', {
            parent: 'presupuesto',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/presupuesto/presupuesto-dialog.html',
                    controller: 'PresupuestoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Presupuesto', function(Presupuesto) {
                            return Presupuesto.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('presupuesto', null, { reload: 'presupuesto' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('presupuesto.delete', {
            parent: 'presupuesto',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/presupuesto/presupuesto-delete-dialog.html',
                    controller: 'PresupuestoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Presupuesto', function(Presupuesto) {
                            return Presupuesto.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('presupuesto', null, { reload: 'presupuesto' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
