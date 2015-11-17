'use strict';

/**
 * @ngdoc service
 * @name webAngularTemplateApp.auth
 * @description
 * # auth
 * Factory in the webAngularTemplateApp.
 */
angular.module('webAngularTemplateApp')
  .factory('auth', ['$http', '$q', '$window', function ($http, $q, $window) {
    
    var login = function (credentials){ 
          var deferred = $q.defer();
          var serializedData = $.param(credentials);
          $http({
            method: 'POST',
            url: 'UNESI URL ZA ZAHTJEV', 
            data: serializedData,
            headers: {
              'Content-Type' : 'application/x-www-form-urlencoded'
            }
          }).then(function(result){
            $window.sessionStorage['token'] = result.data.token;
            deferred.resolve(result);
          }, function(data){
            deferred.reject(data);
          });
          return deferred.promise;
    };

    return {
      login: login,
    };

  }]);
