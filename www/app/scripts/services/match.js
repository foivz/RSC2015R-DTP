'use strict';

/**
 * @ngdoc service
 * @name webAngularTemplateApp.match
 * @description
 * # match
 * Service in the webAngularTemplateApp.
 */
angular.module('webAngularTemplateApp')
  .service('match', ['$window', '$q', '$http', function ($window, $q, $http) {
    var deferred = $q.defer();

    var FetchMatches = function (){
          var path = 'http://46.101.173.23:8080/game/';
          $http({
            method: 'GET',
            url: path
          }).then(function(result){
            deferred.resolve(result);
          }, function(data){
            deferred.reject(data);
          });
          return deferred.promise;
    };

    var FetchMatchByID = function (id){
          var path = 'http://46.101.173.23:8080/game/' + id;
          $http({
            method: 'GET',
            url: path
          }).then(function(result){
            deferred.resolve(result);
          }, function(data){
            deferred.reject(data);
          });
          return deferred.promise;
    };

    var AddMatch = function(data){
          var path = 'http://46.101.173.23:8080/game/' + id;
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
    }

    return {
      FetchMatches: FetchMatches,
      FetchMatchByID: FetchMatchByID,
      AddMatch: AddMatch
    };
  }]);
