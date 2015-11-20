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
          var path = 'http://46.101.173.23:8080/person/signup';
          var obj = {
            idPerson: '0',
            name: data.name,
            surname: data.surname,
            credentials: {
              username: data.username,
              password: data.password
            }
          };

          console.log(obj);
          $http({
            method: 'POST',
            url: path,
            data: obj,
          }).then(function(result){
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