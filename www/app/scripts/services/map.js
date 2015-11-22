'use strict';

/**
 * @ngdoc service
 * @name webAngularTemplateApp.map
 * @description
 * # map
 * Service in the webAngularTemplateApp.
 */
angular.module('webAngularTemplateApp')
  .service('map', ['$window', '$q', '$http', function ($window, $q, $http) {
  	var deferred = $q.defer();

    var AddMap = function (data){
          var path = 'http://46.101.173.23:8080/maps/create';
          $http({
            method: 'POST',
            url: path,
            data: data
          }).then(function(result){
            deferred.resolve(result);
          }, function(data){
            deferred.reject(data);
          });
          return deferred.promise;
    };

    var FetchMaps = function (){
          var path = 'http://46.101.173.23:8080/maps/';
          $http({
            method: 'GET',
            url: path,
          }).then(function(result){
            deferred.resolve(result);
          }, function(data){
            deferred.reject(data);
          });
          return deferred.promise;
    };

    return {
      AddMap: AddMap,
      FetchMaps: FetchMaps
    };
  
  }]);

