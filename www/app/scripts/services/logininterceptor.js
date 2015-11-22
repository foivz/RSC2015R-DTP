'use strict';

/**
 * @ngdoc service
 * @name webAngularTemplateApp.loginInterceptor
 * @description
 * # loginInterceptor
 * Factory in the webAngularTemplateApp.
 */
angular.module('webAngularTemplateApp')
  .factory('loginInterceptor', ['$window', function ($window) {
      
      var interceptor = {
        request: function(config){
          if($window.sessionStorage['token']){
            config.headers['Authorization'] = "Bearer " + $window.sessionStorage['access_token'];
          }
          return config;
        }
      };
      return interceptor;
      
  }]);
