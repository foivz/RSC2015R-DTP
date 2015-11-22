'use strict';

/**
 * @ngdoc function
 * @name webAngularTemplateApp.controller:StatisticCtrl
 * @description
 * # StatisticCtrl
 * Controller of the webAngularTemplateApp
 */
angular.module('webAngularTemplateApp')
  .controller('StatisticCtrl', ['$rootScope', function ($rootScope) {
    	var controller = this;
    	controller.stat = $rootScope.statistics;
  }]);
