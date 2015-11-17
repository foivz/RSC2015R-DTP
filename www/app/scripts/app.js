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
    
    $httpProvider.interceptors.push('loginInterceptor');

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