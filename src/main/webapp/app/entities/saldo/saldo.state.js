(function() {
    'use strict';

    angular
        .module('dentalApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('saldo', {
            parent: 'entity',
            url: '/saldo?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'dentalApp.saldo.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/saldo/saldos.html',
                    controller: 'SaldoController',
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
                    $translatePartialLoader.addPart('saldo');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('saldo-detail', {
            parent: 'saldo',
            url: '/saldo/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'dentalApp.saldo.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/saldo/saldo-detail.html',
                    controller: 'SaldoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('saldo');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Saldo', function($stateParams, Saldo) {
                    return Saldo.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'saldo',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('saldo-detail.edit', {
            parent: 'saldo-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/saldo/saldo-dialog.html',
                    controller: 'SaldoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Saldo', function(Saldo) {
                            return Saldo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('saldo.new', {
            parent: 'saldo',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/saldo/saldo-dialog.html',
                    controller: 'SaldoDialogController',
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
                    $state.go('saldo', null, { reload: 'saldo' });
                }, function() {
                    $state.go('saldo');
                });
            }]
        })
        .state('saldo.edit', {
            parent: 'saldo',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/saldo/saldo-dialog.html',
                    controller: 'SaldoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Saldo', function(Saldo) {
                            return Saldo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('saldo', null, { reload: 'saldo' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('saldo.delete', {
            parent: 'saldo',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/saldo/saldo-delete-dialog.html',
                    controller: 'SaldoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Saldo', function(Saldo) {
                            return Saldo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('saldo', null, { reload: 'saldo' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
