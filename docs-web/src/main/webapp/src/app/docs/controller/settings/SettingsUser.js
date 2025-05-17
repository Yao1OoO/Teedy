'use strict';

/**
 * Settings user page controller.
 */
angular.module('docs').controller('SettingsUser', function($scope, $state, Restangular) {
  /**
   * Load users from server.
   */

  $scope.loadRegisterRequests = function() {
    Restangular.one('register/list/active').get().then(function(data) {
      console.log('API响应数据:', data);
      $scope.registerRequests = data.register;
    })
  }

  $scope.loadUsers = function() {
    Restangular.one('user/list').get({
      sort_column: 1,
      asc: true
    }).then(function(data) {
      $scope.users = data.users;
    });
  };

  $scope.loadRegisterRequests();
  $scope.loadUsers();
  
  /**
   * Edit a user.
   */
  $scope.editUser = function(user) {
    $state.go('settings.user.edit', { username: user.username });
  };

  $scope.accept = function(id) {
    Restangular.one('register').post('accept', {
      id: id
    }).then(function(data){
      $scope.loadRegisterRequests();
      $scope.loadUsers();
    })
  }

  $scope.reject = function(id) {
    Restangular.one('register').post('reject', {
      id: id
    }).then(function(data){
      $scope.loadRegisterRequests();
      $scope.loadUsers();
    })
  }
});