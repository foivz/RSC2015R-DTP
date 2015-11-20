'use strict';

/**
 * @ngdoc function
 * @name webAngularTemplateApp.controller:RegisterCtrl
 * @description
 * # RegisterCtrl
 * Controller of the webAngularTemplateApp
 */
angular.module('webAngularTemplateApp')
	.controller('RegisterCtrl', ['$location', '$window', '$http', 'registerUser', 'auth', function($location, $window, $htpp, registerUser, auth){
		var controller = this;

		controller.passwordType = 'password';

		controller.ChangePassVisibility = function(){
			controller.passwordType = controller.passwordType == 'password' ? 'text' : 'password';
		}

		controller.Register = function(){

			var credentials = {
				name : controller.name,
				surname : controller.surname,
				//email : controller.email,
				password:controller.password,
				username:controller.username,
				grant_type:'password',
				scope:'read write',
				client_secret:'davinci2015',
				client_id:'angular' 
				//image : "app/img/default.jpeg",
				// type : {
				// 	id : '2', // 2 - user
				// 	type : 'user'
				// }
			};

			registerUser.register(credentials)
			.then(function(data){
				console.log("User reg success");
				//After reg succes fetch token
				delete credentials.name;
				delete credentials.surname;
				console.log(credentials);
				auth.login(credentials)
				.then(function(){
					console.log("Fetch token success");
					$location.path('/main');
				}, function(data){
					console.log("Fetch token error")
				})

			}, function(data){
				console.log("User reg error");
			})
		}
	}]);
