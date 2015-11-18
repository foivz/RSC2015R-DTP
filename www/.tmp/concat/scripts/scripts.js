'use strict';

/**
 * @ngdoc overview
 * @name webAngularTemplateApp
 * @description
 * # webAngularTemplateApp
 *
 * Main module of the application.
 */
angular
  .module('webAngularTemplateApp', [
    'ngCookies',
    'ngRoute',
    'ngSanitize'
  ])
  .config(['$httpProvider', '$routeProvider', function ($httpProvider, $routeProvider) {
    
   //$httpProvider.interceptors.push('loginInterceptor');
    
     $httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';


    $routeProvider
      .when('/', {
        templateUrl: 'views/main.html',
        controller: 'MainCtrl',
        controllerAs: 'main',
        //resolve: {auth: routeAuth}
      })
      .when('/about', {
        templateUrl: 'views/about.html',
        controller: 'AboutCtrl',
        controllerAs: 'about'
      })
      .when('/login', {
        templateUrl: 'views/login.html',
        controller: 'LoginCtrl',
        controllerAs: 'login'
      })
      .when('/register', {
        templateUrl: 'views/register.html',
        controller: 'RegisterCtrl',
        controllerAs: 'register'
      })
      .otherwise({
        redirectTo: '/'
      });
  }])
  .run(['$rootScope', '$location', function($rootScope, $location){
    $rootScope.$on('$routeChangeError', function(eventObj){
      if(!eventObj.login)
        $location.path('/login');
    });
    $rootScope.$on('$routeChangeSuccess', function(){
      //do something
    })
  }]);


var routeAuth = ['$window', '$q', function($window, $q){
            var token = $window.sessionStorage['token'];
            if (token != undefined)
              return $q.resolve(token);
            else
              return $q.reject({ login:false });
          }]
'use strict';

/**
 * @ngdoc function
 * @name webAngularTemplateApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the webAngularTemplateApp
 */
angular.module('webAngularTemplateApp')
  .controller('MainCtrl', function () {
  });

'use strict';

/**
 * @ngdoc function
 * @name webAngularTemplateApp.controller:AboutCtrl
 * @description
 * # AboutCtrl
 * Controller of the webAngularTemplateApp
 */
angular.module('webAngularTemplateApp')
  .controller('AboutCtrl', function () {
    this.awesomeThings = [
      'HTML5 Boilerplate',
      'AngularJS',
      'Karma'
    ];
  });

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
		var encoded = window.btoa('angular:davinci2015');
          var serializedData = $.param(credentials);
          serializedData = serializedData.replace('+', ' ');
          console.log(serializedData);

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

'use strict';

/**
 * @ngdoc service
 * @name webAngularTemplateApp.auth
 * @description
 * # auth
 * Factory in the webAngularTemplateApp.
 */
angular.module('webAngularTemplateApp')
  .factory('auth', ['$http', '$q', '$window', function ($http, $q, $window) {
    
    var login = function (credentials){ 
          var deferred = $q.defer();
          var encoded = window.btoa('angular:davinci2015');
          
          var serializedData = $.param(credentials);
          serializedData = serializedData.replace('+', ' ');
          console.log(serializedData);
          $http({
            method: 'POST',
            url: 'http://46.101.173.23:8080/oauth/token', 
            data: serializedData,
            headers: {
              'Content-Type' : 'application/x-www-form-urlencoded',
              'Authorization' : 'Basic ' + encoded
            }
          }).then(function(result){
            
            console.log("Prošao auth");
            console.log(result);
            console.log(result.access_token);

            //$window.sessionStorage['token'] = result.data.token;
            
            deferred.resolve(result);
          }, function(data){
            
            console.log("Nije prošao auth");

            deferred.reject(data);
          });
          return deferred.promise;
    };

    return {
      login: login,
    };

  }]);

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
            config.headers['x-session-token'] = $window.sessionStorage['token'];
          }
          return config;
        }
      };
      return interceptor;
      
  }]);

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

'use strict';

/**
 * @ngdoc service
 * @name webAngularTemplateApp.register
 * @description
 * # register
 * Factory in the webAngularTemplateApp.
 */
angular.module('webAngularTemplateApp')
  .factory('registerUser', ['$http', '$q', '$window', function($http, $q, $window){
    var deferred = $q.defer();
    var register = function (data){
          var path = 'URL ZA REG';
          $http({
            method: 'POST',
            url: path,
            data: data,
          }).then(function(result){
            $window.sessionStorage['token'] = result.data.token;
            deferred.resolve(result);
          }, function(data){
            deferred.reject(data);
          });
          return deferred.promise;
    };
    return {
      register: register
    };
  }]);
jQuery(document).ready(function($) {
	var Menu =
	{
		el: 
		{
			$icon: $('.menu-container__icon'),
			$iconTop: $('.menu-icon--top'),
			$iconMiddle: $('.menu-icon--middle'),
			$iconBottom: $('.menu-icon--bottom'),
			$innerContent: $('.flip-inner'),
			$menuLinks: $('nav ul li a')
		},   

		init: function()
		{
			this.BindActions();
		},

		BindActions: function()
		{
			this.el.$icon.on('click', function(){
				Menu.actions();
			});
			this.el.$menuLinks.on('click', function(){
				Menu.actions();
			})
		},

		actions: function()
		{
			this.el.$iconTop.toggleClass('menu-top-action');
			this.el.$iconMiddle.toggleClass('menu-middle-action');
			this.el.$iconBottom.toggleClass('menu-bottom-action');
			this.el.$icon.toggleClass('menu-rotate');			
			this.el.$innerContent.toggleClass('flip-inner-action');	
		}
	}
	Menu.init();
});
angular.module('webAngularTemplateApp').run(['$templateCache', function($templateCache) {
  'use strict';

  $templateCache.put('views/about.html',
    "<div class=\"container\"> <h2>Kontakt lista</h2> <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Dolorum earum, corrupti voluptatem magni, vero suscipit, quos maiores repudiandae minus deleniti architecto fugiat excepturi provident pariatur quisquam odit. A quae, quia!</p> </div>"
  );


  $templateCache.put('views/login.html',
    "<div class=\"container login\"> <div class=\"row\"> <div class=\"text-center\"> <h2>LOGO</h2> </div> <div class=\"form-wrapper col-md-8 col-md-offset-2\"> <hr> <form ng-submit=\"login.CheckUser()\"> <div class=\"form-group\"> <div class=\"input-group\"> <div class=\"input-group-addon\"><span class=\"glyphicon glyphicon-user\" aria-hidden=\"true\"></span></div> <input ng-model=\"login.username\" type=\"text\" name=\"username\" class=\"form-control\" placeholder=\"Username\" required> </div> </div> <div class=\"form-group\"> <div class=\"input-group\"> <div class=\"input-group-addon\"><span class=\"glyphicon glyphicon-lock\" aria-hidden=\"true\"></span></div> <input ng-model=\"login.password\" type=\"password\" name=\"password\" class=\"form-control\" placeholder=\"Password\" required> </div> </div> <div ng-show=\"login.errorMessage\" class=\"alert alert-danger\" role=\"alert\">{{login.errorMessage}}</div> <button class=\"btn btn-primary btn-block\">Sign in</button> <div class=\"text-center\"> <a ng-href=\"#/register\">Don't have an account? <strong>Sign up</strong></a> </div> </form> </div> </div> </div>"
  );


  $templateCache.put('views/main.html',
    "<div class=\"container\"> <h1>Pocetna stranica</h1> <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ea eius obcaecati suscipit maxime ducimus! Qui illum adipisci accusamus consectetur magnam cumque quo voluptatem dolorem perferendis libero ullam possimus atque maxime.</p> </div>"
  );


  $templateCache.put('views/register.html',
    "<div class=\"container registration\"> <div class=\"row\"> <div class=\"text-center\" style=\"margin-top: 10%\"> <h2>LOGO</h2> </div> <form ng-submit=\"register.GetCoordinates()\" class=\"form-wrapper col-md-6 col-md-offset-3\" enctype=\"multipart/form-data\"> <hr> <div class=\"form-group\"> <input ng-model=\"register.name\" type=\"text\" name=\"name\" class=\"form-control\" placeholder=\"Name\" required> </div> <div class=\"form-group\"> <input ng-model=\"register.surname\" type=\"text\" name=\"surname\" class=\"form-control\" placeholder=\"Surname\" required> </div> <div class=\"form-group\"> <input ng-model=\"register.email\" type=\"email\" name=\"email\" class=\"form-control\" placeholder=\"Email\" required> </div> <div class=\"form-group\"> <input ng-model=\"register.username\" type=\"text\" name=\"username\" class=\"form-control\" placeholder=\"Username\" required> </div> <div class=\"form-group\"> <div class=\"input-group\"> <input ng-model=\"register.password\" type=\"{{register.passwordType}}\" name=\"password\" class=\"form-control\" placeholder=\"Password\" required> <div ng-click=\"register.ChangePassVisibility()\" class=\"input-group-addon pass-opacity\"> <span ng-class=\"register.passwordType == 'password' ? 'glyphicon-eye-open':'glyphicon-eye-close'\" class=\"glyphicon\"></span> </div> </div> </div> <button class=\"btn btn-primary btn-block\">Submit</button> <div class=\"text-center\"> <a ng-href=\"#/login\">Back to login</a> </div> </form> </div> </div>"
  );

}]);
