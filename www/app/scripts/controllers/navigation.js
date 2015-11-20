'use strict';

/**
 * @ngdoc function
 * @name webAngularTemplateApp.controller:NavigationCtrl
 * @description
 * # NavigationCtrl
 * Controller of the webAngularTemplateApp
 */
angular.module('webAngularTemplateApp')
  .controller('NavigationCtrl', ['$window', '$location', function ($window, $location) {
  	var controller = this;

  	controller.toggle = 0;
  	controller.CheckLogin = function(){
  		if($window.sessionStorage['access_token'] == undefined)
  			return false;
  		else 
  			return true;
  	};

  	controller.CheckPage = function(){
  		if($location.path() == '/')
  			return true;
  		else 
  			return false;
  	};

  	controller.ToggleNavigation = function(){
  		controller.toggle = (!controller.toggle) ? 1 : 0; 
  	};

  }]);
