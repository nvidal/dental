(function() {
    'use strict';

    angular
        .module('dentalApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('tratamiento', {
            parent: 'entity',
            url: '/tratamiento?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'dentalApp.tratamiento.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tratamiento/tratamientos.html',
                    controller: 'TratamientoController',
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
                    $translatePartialLoader.addPart('tratamiento');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('tratamiento-detail', {
            parent: 'tratamiento',
            url: '/tratamiento/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'dentalApp.tratamiento.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tratamiento/tratamiento-detail.html',
                    controller: 'TratamientoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('tratamiento');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Tratamiento', function($stateParams, Tratamiento) {
                    return Tratamiento.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'tratamiento',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('tratamiento-detail.edit', {
            parent: 'tratamiento-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tratamiento/tratamiento-dialog.html',
                    controller: 'TratamientoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Tratamiento', function(Tratamiento) {
                            return Tratamiento.get({id : $stateParams.id}).$promise;
                        }],
                        paciente: null
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tratamiento.new', {
            parent: 'tratamiento',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tratamiento/tratamiento-dialog.html',
                    controller: 'TratamientoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                fecha: null,
                                procedimiento: null,
                                precio: null,
                                id: null
                            };
                        },
                        paciente: null
                    }
                }).result.then(function() {
                    $state.go('tratamiento', null, { reload: 'tratamiento' });
                }, function() {
                    $state.go('tratamiento');
                });
            }]
        })
        .state('tratamiento.edit', {
            parent: 'tratamiento',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tratamiento/tratamiento-dialog.html',
                    controller: 'TratamientoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Tratamiento', function(Tratamiento) {
                            return Tratamiento.get({id : $stateParams.id}).$promise;
                        }],
                        paciente : null
                    }
                }).result.then(function() {
                    $state.go('tratamiento', null, { reload: 'tratamiento' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tratamiento.delete', {
            parent: 'tratamiento',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tratamiento/tratamiento-delete-dialog.html',
                    controller: 'TratamientoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Tratamiento', function(Tratamiento) {
                            return Tratamiento.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tratamiento', null, { reload: 'tratamiento' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
