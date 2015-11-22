'use strict';

/**
 * @ngdoc function
 * @name webAngularTemplateApp.controller:LoginCtrl
 * @description
 * # LoginCtrl
 * Controller of the webAngularTemplateApp
 */
angular.module('webAngularTemplateApp')
  .controller('LoginCtrl', ['auth', '$location', '$window', '$http', function (auth, $location, $window, $http) {
    
  	var controller = this;
		
	controller.CheckUser = function()
	{
		var credentials = {
			password:controller.password,
			username:controller.username,
			grant_type:'password',
			scope:'read write',
			client_secret:'davinci2015',
			client_id:'angular' 
		};
		
		auth.login(credentials)
			.then(function(data){
				$location.path('/map');
			},
			function(data){
				console.log("Error");
				controller.errorMessage = "Invalid credentials";
			})
	};

  }]);
