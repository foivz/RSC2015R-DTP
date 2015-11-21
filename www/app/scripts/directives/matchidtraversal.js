'use strict';

/**
 * @ngdoc directive
 * @name webAngularTemplateApp.directive:matchIdTraversal
 * @description
 * # matchIdTraversal
 */
angular.module('webAngularTemplateApp')
  .directive('matchIdTraversal', ['$rootScope', function ($rootScope) {
    return {
      restrict: 'A',
      link: function postLink(scope, element, attrs) {
        element.on('click', function(){
        	var id = element.attr('data-matchID');
        	$rootScope.matchID = id;
        });
      }
    };
  }]);
