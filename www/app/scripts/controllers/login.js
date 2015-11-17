'use strict';

/**
 * @ngdoc function
 * @name webAngularTemplateApp.controller:LoginCtrl
 * @description
 * # LoginCtrl
 * Controller of the webAngularTemplateApp
 */
angular.module('webAngularTemplateApp')
  .controller('LoginCtrl', ['auth', '$location', '$window', function (auth, $location, $window) {
    
  	var controller = this;
		
	controller.CheckUser = function()
	{
		var credentials = {
			username:controller.username, 
			password:controller.password
		};
		auth.login(credentials)
			.then(function(data){
				$location.path('/main');
			},
			function(data){
				console.log("Error");
				controller.errorMessage = "Invalid credentials";
			})
	};

  }]);
