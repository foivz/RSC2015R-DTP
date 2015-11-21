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
    
    $httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';

    $routeProvider
      .when('/main', {
        templateUrl: 'views/main.html',
        controller: 'MainCtrl',
        controllerAs: 'main',
        //resolve: {auth: routeAuth}
      })
      .when('/new-match', {
        templateUrl: 'views/new-match.html',
        controller: 'NewMatchCtrl',
        controllerAs: 'newMatch',
        //resolve: {auth: routeAuth}
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
      .when('/map', {
        templateUrl: 'views/map.html',
        controller: 'MapCtrl',
        controllerAs: 'map'
      })
      .otherwise({
        redirectTo: '/'
      });
  }])
  .run(['$rootScope', '$location', '$route', function($rootScope, $location, $route){
    $route.reload();
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