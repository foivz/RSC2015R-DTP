'use strict';

/**
 * @ngdoc directive
 * @name webAngularTemplateApp.directive:logout
 * @description
 * # logout
 */
angular.module('webAngularTemplateApp')
  .directive('logout',['$window', function ($window) {
    return {
      restrict: 'A',
      link: function postLink(scope, element, attrs) {
        element.on('click', function(){
        	$window.sessionStorage.removeItem('access_token');
        });
      }
    };
  }]);
