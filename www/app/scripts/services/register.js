'use strict';

/**
 * @ngdoc service
 * @name webAngularTemplateApp.register
 * @description
 * # register
 * Factory in the webAngularTemplateApp.
 */
angular.module('webAngularTemplateApp')
  .factory('registerUser', ['$http', '$q', '$window', function($http, $q, $window){
    var deferred = $q.defer();
    var register = function (data){
          var path = 'URL ZA REG';
          $http({
            method: 'POST',
            url: path,
            data: data,
          }).then(function(result){
            $window.sessionStorage['token'] = result.data.token;
            deferred.resolve(result);
          }, function(data){
            deferred.reject(data);
          });
          return deferred.promise;
    };
    return {
      register: register
    };
  }]);