(function() {
    'use strict';

    angular
        .module('dentalApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('cuenta', {
            parent: 'entity',
            url: '/cuenta?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'dentalApp.cuenta.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cuenta/cuentas.html',
                    controller: 'CuentaController',
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
                    $translatePartialLoader.addPart('cuenta');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('cuenta-detail', {
            parent: 'cuenta',
            url: '/cuenta/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'dentalApp.cuenta.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cuenta/cuenta-detail.html',
                    controller: 'CuentaDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('cuenta');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Cuenta', function($stateParams, Cuenta) {
                    return Cuenta.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'cuenta',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('cuenta-detail.edit', {
            parent: 'cuenta-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cuenta/cuenta-dialog.html',
                    controller: 'CuentaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Cuenta', function(Cuenta) {
                            return Cuenta.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('cuenta.new', {
            parent: 'cuenta',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cuenta/cuenta-dialog.html',
                    controller: 'CuentaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                fecha: null,
                                descripcion: null,
                                debe: null,
                                haber: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('cuenta', null, { reload: 'cuenta' });
                }, function() {
                    $state.go('cuenta');
                });
            }]
        })
        .state('cuenta.edit', {
            parent: 'cuenta',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cuenta/cuenta-dialog.html',
                    controller: 'CuentaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Cuenta', function(Cuenta) {
                            return Cuenta.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('cuenta', null, { reload: 'cuenta' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('cuenta.delete', {
            parent: 'cuenta',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cuenta/cuenta-delete-dialog.html',
                    controller: 'CuentaDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Cuenta', function(Cuenta) {
                            return Cuenta.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('cuenta', null, { reload: 'cuenta' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
