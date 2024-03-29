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
          var encoded = window.btoa('angular:davinci2015');
          
          var serializedData = $.param(credentials);
          serializedData = serializedData.replace('+', ' ');
          console.log(serializedData);
          $http({
            method: 'POST',
            url: 'http://46.101.173.23:8080/oauth/token', 
            data: serializedData,
            headers: {
              'Content-Type' : 'application/x-www-form-urlencoded',
              'Authorization' : 'Basic ' + encoded
            }
          }).then(function(result){
            
            console.log("Auth success");
            $window.sessionStorage['access_token'] = result.data.access_token;
            $window.sessionStorage['refresh_token'] = result.data.access_token;
            
            deferred.resolve(result);
          }, function(data){
            
            console.log("Auth error");

            deferred.reject(data);
          });
          return deferred.promise;
    };

    return {
      login: login,
    };

  }]);
