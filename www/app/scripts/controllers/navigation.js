'use strict';

/**
 * @ngdoc function
 * @name webAngularTemplateApp.controller:NavigationCtrl
 * @description
 * # NavigationCtrl
 * Controller of the webAngularTemplateApp
 */
angular.module('webAngularTemplateApp')
  .controller('NavigationCtrl', ['$window', function ($window) {
  	this.CheckLogin = function(){
  		if($window.sessionStorage['access_token'] == undefined)
  			return false;
  		else 
  			return true;
  	}
  }]);
