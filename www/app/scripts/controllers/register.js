'use strict';

/**
 * @ngdoc function
 * @name webAngularTemplateApp.controller:RegisterCtrl
 * @description
 * # RegisterCtrl
 * Controller of the webAngularTemplateApp
 */
angular.module('webAngularTemplateApp')
	.controller('RegisterCtrl', ['$location', '$window', '$http', 'registerUser', function($location, $window, $htpp, registerUser){
		var controller = this;

		controller.passwordType = 'password';

		controller.ChangePassVisibility = function(){
			controller.passwordType = controller.passwordType == 'password' ? 'text' : 'password';
		}

		controller.Register = function(){

			var obj = {
				name : controller.name,
				surname : controller.surname,
				email : controller.email,
				username: controller.username,
				password : controller.password,
				image : "app/img/default.jpeg",
				type : {
					id : '2', // 2 - user
					type : 'user'
				}
			}
			console.log(obj);
			registerUser.register(obj)
				.then(function(data){
					console.log(data);
					$location.path('/main');
				},
				function(data){
					console.log("Registration Error");
				});
		}
	}]);
