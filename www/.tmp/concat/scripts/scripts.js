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
      .when('/match-action', {
        templateUrl: 'views/match-action.html',
        controller: 'MatchActionCtrl',
        controllerAs: 'matchAction'
      })
      .when('/statistic', {
        templateUrl: 'views/statistic.html',
        controller: 'StatisticCtrl',
        controllerAs: 'statistic'
      })
      .when('/spectator', {
        templateUrl: 'views/spectator.html',
        controller: 'SpectatorCtrl',
        controllerAs: 'spectator'
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
'use strict';

/**
 * @ngdoc function
 * @name webAngularTemplateApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the webAngularTemplateApp
 */
angular.module('webAngularTemplateApp')
  .controller('MainCtrl', ['$rootScope', 'match', '$http', '$location', function ($rootScope, match, $http, $location) {
  	var controller = this;
  	controller.team1People = [];
  	controller.team2People = [];
  	controller.notifications = [];

  	controller.icons = {
    	house: {
    		icon: 'images/marker_house.png'
    	},
    	flag: {
    		icon: 'images/marker_flag.png'
    	},
    	fence: {
    		icon: 'images/marker_fence.png'
    	},
    	woman: {
    		icon: 'images/marker_woman.png'
    	}
    };

  	controller.DrawMap = function(data){
  		console.log(data.map.startLat + " " + data.map.startLng);
  			var lat = data.map.startLat;
  			var lng = data.map.startLng;
			var latlng = new google.maps.LatLng(lat, lng);
			var settings = {
		        zoom: 10,
		        center: latlng,
		        mapTypeId: google.maps.MapTypeId.SATELLITE
		    }
		    console.log(lat + " " + lng);
		    var map = new google.maps.Map(document.getElementById('matchMap'), settings);


		    var rectangle = new google.maps.Rectangle({
			    strokeColor: '#81c784',
			    strokeOpacity: 0.8,
			    strokeWeight: 2,
			    fillColor: '#a5d6a7',
			    fillOpacity: 0.35,
			    map: map,
			    clickable: true,
			    zIndex: 1,
			    bounds: {
			      north: data.map.startLat,
			      south: data.map.endLat,
			      east: data.map.endLng,
			      west: data.map.startLng
			    }
			});

		    var lat = data.map.flagLat;
    		var lng = data.map.flagLng;
    		var coord = {lat: lat, lng:lng};
			var marker = new google.maps.Marker({
				position: coord,
				map: map,
				icon: controller.icons['flag'].icon
			});

		    data.map.mapObstacles.forEach(function(obstacle){
		    	var lat = obstacle.lat;
    			var lng = obstacle.lng;
    			var coord = {lat: lat, lng:lng};
				var marker = new google.maps.Marker({
						position: coord,
						map: map,
						icon: controller.icons[obstacle.name].icon
				});
		    });

	};

	$http.get('http://46.101.173.23:8080/game/' + $rootScope.Match.idGame)
		.success(function(data){
			controller.data = data;
			controller.DrawMap(data);
			controller.team1 = data.team[0];
			controller.team2 = data.team[1];
			console.log(data);
			controller.ping();
		})
	controller.firstPing;
	controller.ping = function(){
		controller.firstPing = setInterval(function(){ 
			var path = 'http://46.101.173.23:8080/game/' + $rootScope.Match.idGame + '/team/' + controller.team1.idTeam;
			$http.get(path)
				.success(function(data){
					controller.team1People = data;
				}).error(function(data){
					console.log(data);
				})
			path = 'http://46.101.173.23:8080/game/' + $rootScope.Match.idGame + '/team/' + controller.team2.idTeam;
			$http.get(path)
				.success(function(data){
					controller.team2People = data;
				}).error(function(data){
					console.log(data);
				})
		}, 3000);
	}

	controller.StartMatch = function(){
		var path = 'http://46.101.173.23:8080/game/' + $rootScope.Match.idGame + '/start';
			$http.post(path)
				.success(function(data){
					console.log(data);
					clearInterval(controller.firstPing);
					$location.url('/match-action');
				}).error(function(data){
					console.log(data);
				})
	}

	// match.FetchMatchByID($rootScope.Match.idGame)
	// 	.then(function(data){
	// 		console.log(data);
	// 	}, function(){
	// 		console.log("Error");
	// 	})


  }]);

'use strict';

/**
 * @ngdoc function
 * @name webAngularTemplateApp.controller:AboutCtrl
 * @description
 * # AboutCtrl
 * Controller of the webAngularTemplateApp
 */

angular.module('webAngularTemplateApp')
  .controller('NewMatchCtrl', ['map', 'match', '$rootScope', '$location', function (map, match, $rootScope, $location) {
    var controller = this;
    controller.mapList = [];

    controller.GenerateQRCode = function(){
         var text = "";
        var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        for( var i=0; i < 6; i++ )
            text += possible.charAt(Math.floor(Math.random() * possible.length));

        return text;
    }

    map.FetchMaps()
    	.then(function(data){
    		console.log(data);
    		controller.mapList = data.data;
    		console.log(controller.mapList);
    	}, function(){
    		console.log("Error map fetch");
    	});

    controller.AddMatch = function(){
        var code = controller.GenerateQRCode();
        console.log(controller.mapList);
    	var obj = {
    		timer: controller.timer,
    		code: code,
    		team: 
	    		[
    				{ 
    					idTeam: '0',
    					name: controller.team1

    				},
    				{ 
    					idTeam: '0',
    					name: controller.team2 
    				}
	    		],
    		map: {
    			name: controller.mapList[controller.map].name,
				idMap: controller.mapList[controller.map].idMap,
				startLat: controller.mapList[controller.map].startLat,
				endLat: controller.mapList[controller.map].endLat,
				startLng: controller.mapList[controller.map].startLng,
				endLng: controller.mapList[controller.map].endLng,
				flagLat: controller.mapList[controller.map].flagLat,
				flagLng: controller.mapList[controller.map].flagLng 
    		}
    	};
    	console.log(obj);
    	match.AddMatch(obj, 6)
		.then(function(data){
			controller.alert = 1;
			$rootScope.Match = data.data;
			$location.url('/main');
		}, function(data){
			console.log(data);
		});
    };

  }]);

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
            
            console.log("Auth success");
            $window.sessionStorage['access_token'] = result.data.access_token;
            $window.sessionStorage['refresh_token'] = result.data.access_token;
            
            deferred.resolve(result);
          }, function(data){
            
            console.log("Auth error");

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
            config.headers['Authorization'] = "Bearer " + $window.sessionStorage['access_token'];
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
          var path = 'http://46.101.173.23:8080/person/signup';
          var obj = {
            idPerson: '0',
            name: data.name,
            surname: data.surname,
            credentials: {
              username: data.username,
              password: data.password
            }
          };

          console.log(obj);
          $http({
            method: 'POST',
            url: path,
            data: obj,
          }).then(function(result){
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
'use strict';

/**
 * @ngdoc directive
 * @name webAngularTemplateApp.directive:logout
 * @description
 * # logout
 */
angular.module('webAngularTemplateApp')
  .directive('logout',['$window', function ($window) {
    return {
      restrict: 'A',
      link: function postLink(scope, element, attrs) {
        element.on('click', function(){
        	$window.sessionStorage.removeItem('access_token');
        });
      }
    };
  }]);

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

'use strict';

/**
 * @ngdoc function
 * @name webAngularTemplateApp.controller:MatchCtrl
 * @description
 * # MatchCtrl
 * Controller of the webAngularTemplateApp
 */
angular.module('webAngularTemplateApp')
  .controller('MatchCtrl', ['match', function (match) {
    var controller = this;
    controller.Matches = [];
    match.FetchMatches()
	    .then(function(result){
	    	controller.Matches = result.data;
	    	console.log(controller.Matches);
	    }, function(data){
	    	console.log(data);
	    })
  }]);

'use strict';

/**
 * @ngdoc service
 * @name webAngularTemplateApp.match
 * @description
 * # match
 * Service in the webAngularTemplateApp.
 */
angular.module('webAngularTemplateApp')
  .service('match', ['$window', '$q', '$http', function ($window, $q, $http) {
    var deferred = $q.defer();

    var FetchMatches = function (){
          var path = 'http://46.101.173.23:8080/game/';
          $http({
            method: 'GET',
            url: path
          }).then(function(result){
            deferred.resolve(result);
          }, function(data){
            deferred.reject(data);
          });
          return deferred.promise;
    };

    var FetchMatchByID = function (id){
          var path = 'http://46.101.173.23:8080/game/' + id;
          $http({
            method: 'GET',
            url: path
          }).then(function(result){
            deferred.resolve(result);
          }, function(data){
            deferred.reject(data);
          });
          return deferred.promise;
    };

    var AddMatch = function(data, id){
          var path = 'http://46.101.173.23:8080/game/create/' + id;
            $http({
              method: 'POST',
              url: path,
              data: data
            }).then(function(result){
              deferred.resolve(result);
            }, function(data){
              deferred.reject(data);
            });
            return deferred.promise;
    };


    var FetchMatchTeamsPosition = function(){
          var path = 'http://46.101.173.23:8080/game/create/' + id;
              $http({
                method: 'POST',
                url: path,
                data: data
              }).then(function(result){
                deferred.resolve(result);
              }, function(data){
                deferred.reject(data);
              });
              return deferred.promise;
    }

    return {
      FetchMatches: FetchMatches,
      FetchMatchByID: FetchMatchByID,
      AddMatch: AddMatch
    };
  }]);

'use strict';

/**
 * @ngdoc function
 * @name webAngularTemplateApp.controller:MapCtrl
 * @description
 * # MapCtrl
 * Controller of the webAngularTemplateApp
 */
angular.module('webAngularTemplateApp')
  .controller('MapCtrl', ['map', function (map) {
    var controller = this;

    controller.marker = 0;
    controller.coordArray = [];
    controller.obstacles = [];

    controller.icons = {
    	house: {
    		icon: 'images/marker_house.png'
    	},
    	flag: {
    		icon: 'images/marker_flag.png'
    	},
    	fence: {
    		icon: 'images/marker_fence.png'
    	},
    	woman: {
    		icon: 'images/marker_woman.png'
    	}
    };

    controller.DrawMap = function(lat, lng){
			var latlng = new google.maps.LatLng(lat, lng);
			var settings = {
		        zoom: 10,
		        center: latlng,
		        mapTypeId: google.maps.MapTypeId.SATELLITE
		    }

		    var map = new google.maps.Map(document.getElementById('matchMap'), settings);

		    google.maps.event.addDomListener(map, 'click', function(event) {
		    		var clickEvent = this;
					controller.marker++;
					var lat = event.latLng.lat();
    				var lng = event.latLng.lng();
    				var coord = {lat: lat, lng:lng};
    				
					if(controller.marker <= 2) {
    					controller.coordArray.push([lat,lng]);
						controller.drawMarker(map, coord);
					}
					if(controller.marker == 2)
					{
						var rectangle = new google.maps.Rectangle({
						    strokeColor: '#81c784',
						    strokeOpacity: 0.8,
						    strokeWeight: 2,
						    fillColor: '#a5d6a7',
						    fillOpacity: 0.35,
						    map: map,
						    clickable: true,
						    zIndex: 1,
						    bounds: {
						      north: controller.coordArray[0][0], //startLat
						      south: controller.coordArray[1][0], //endLat
						      east: controller.coordArray[1][1], //endLng
						      west: controller.coordArray[0][1] //startLng
						    }
						  });
						google.maps.event.addListener(rectangle, 'click', function(event){
							controller.marker++;
							var lat = event.latLng.lat();
    						var lng = event.latLng.lng();
    						var coord = {lat: lat, lng:lng};
							var marker = new google.maps.Marker({
							    position: coord,
							    map: map,
							    icon: controller.icons[controller.icon].icon
							});
							if(controller.marker == 3)
								controller.coordArray.push([lat, lng]);
							else
								controller.obstacles.push({name: controller.icon, lat: lat,lng: lng});
						});
					}    						
			  	});
	};
	controller.DrawMap(46.307978, 16.338117);

	controller.drawMarker = function(map, coord){
		var marker = new google.maps.Marker({
			position: coord,
			map: map,
		});
	}

	controller.setIcon = function(icon){
		if(controller.icon == icon)
			controller.icon = "";
		else 
			controller.icon = icon;
	};

	controller.AddMap = function(){
		var obj = {
			name: controller.mapName,
			startLat: controller.coordArray[0][0],
			startLng: controller.coordArray[0][1],
			endLat: controller.coordArray[1][0],
			endLng: controller.coordArray[1][1],
			flagLat: controller.coordArray[2][0],
			flagLng: controller.coordArray[2][1],
			mapObstacles: controller.obstacles
		};
		console.log(obj);
		map.AddMap(obj)
		.then(function(data){
			controller.alert = 1;
		}, function(data){
			console.log(data);
		});
	};
	
  }]);

'use strict';

/**
 * @ngdoc service
 * @name webAngularTemplateApp.map
 * @description
 * # map
 * Service in the webAngularTemplateApp.
 */
angular.module('webAngularTemplateApp')
  .service('map', ['$window', '$q', '$http', function ($window, $q, $http) {
  	var deferred = $q.defer();

    var AddMap = function (data){
          var path = 'http://46.101.173.23:8080/maps/create';
          $http({
            method: 'POST',
            url: path,
            data: data
          }).then(function(result){
            deferred.resolve(result);
          }, function(data){
            deferred.reject(data);
          });
          return deferred.promise;
    };

    var FetchMaps = function (){
          var path = 'http://46.101.173.23:8080/maps/';
          $http({
            method: 'GET',
            url: path,
          }).then(function(result){
            deferred.resolve(result);
          }, function(data){
            deferred.reject(data);
          });
          return deferred.promise;
    };

    return {
      AddMap: AddMap,
      FetchMaps: FetchMaps
    };
  
  }]);


'use strict';

/**
 * @ngdoc directive
 * @name webAngularTemplateApp.directive:matchIdTraversal
 * @description
 * # matchIdTraversal
 */
angular.module('webAngularTemplateApp')
  .directive('matchIdTraversal', ['$rootScope', function ($rootScope) {
    return {
      restrict: 'A',
      link: function postLink(scope, element, attrs) {
        element.on('click', function(){
        	var id = element.attr('data-matchID');
        	$rootScope.Match.idGame = id;
        });
      }
    };
  }]);

'use strict';

/**
 * @ngdoc function
 * @name webAngularTemplateApp.controller:MatchActionCtrl
 * @description
 * # MatchActionCtrl
 * Controller of the webAngularTemplateApp
 */
angular.module('webAngularTemplateApp')
  .controller('MatchActionCtrl', ['$rootScope', 'match', '$http', '$location', function ($rootScope, match, $http, $location) {
    
  	var controller = this;
  	controller.team1People = [];
  	controller.team2People = [];
  	var firstPing, secPing;

  	controller.icons = {
    	house: {
    		icon: 'images/marker_house.png'
    	},
    	flag: {
    		icon: 'images/marker_flag.png'
    	},
    	fence: {
    		icon: 'images/marker_fence.png'
    	},
    	woman: {
    		icon: 'images/marker_woman.png'
    	},
    	blue: {
    		icon: 'images/marker_blue.png'
    	},
    	red: {
    		icon: 'images/marker_red.png'
    	}
    };
    var map;
    var markersBlue = [];
    var markersRed = [];
    controller.notifications = [];
    controller.data = [];

  	controller.DrawMap = function(data){
  		console.log(data.map.startLat + " " + data.map.startLng);
  			var lat = data.map.startLat;
  			var lng = data.map.startLng;
			var latlng = new google.maps.LatLng(lat, lng);
			var settings = {
		        zoom: 10,
		        center: latlng,
		        mapTypeId: google.maps.MapTypeId.SATELLITE
		    }
		    map = new google.maps.Map(document.getElementById('matchMap'), settings);

		    var rectangle = new google.maps.Rectangle({
			    strokeColor: '#81c784',
			    strokeOpacity: 0.8,
			    strokeWeight: 2,
			    fillColor: '#a5d6a7',
			    fillOpacity: 0.35,
			    map: map,
			    clickable: true,
			    zIndex: 1,
			    bounds: {
			      north: data.map.startLat,
			      south: data.map.endLat,
			      east: data.map.endLng,
			      west: data.map.startLng
			    }
			});

		    var lat = data.map.flagLat;
    		var lng = data.map.flagLng;
    		var coord = {lat: lat, lng:lng};
			var marker = new google.maps.Marker({
				position: coord,
				map: map,
				icon: controller.icons['flag'].icon
			});

		    data.map.mapObstacles.forEach(function(obstacle){
		    	var lat = obstacle.lat;
    			var lng = obstacle.lng;
    			var coord = {lat: lat, lng:lng};
				var marker = new google.maps.Marker({
						position: coord,
						map: map,
						icon: controller.icons[obstacle.name].icon
				});
		    });

	};

	controller.GetMarkers = function(team, teamNumber){
		controller.clear(null);
		markersBlue = [];
		markersRed = [];
		console.log(team);
		for(var i = 0; i < team.length; i++){
			var lat = team[i].lat;
  			var lng = team[i].lng;
			var coord = {lat:lat,lng:lng};
			var marker = new google.maps.Marker({
				position: coord,
				map: map,
				icon: controller.icons[teamNumber].icon
			})
			if(teamNumber == 'blue')
				markersBlue.push(marker);
			else
				markersRed.push(marker);
		};
		if(teamNumber == 'blue')
			controller.DrawTeams('blue');
		else
			controller.DrawTeams('red');
	}

	controller.clear = function(map){
		for (var i = 0; i < markersBlue.length; i++) {
		    markersBlue[i].setMap(map);
		}
		for (var i = 0; i < markersRed.length; i++) {
		   markersRed[i].setMap(map);
		}
	}

	controller.DrawTeams = function(teamNumber){
		if(teamNumber == 'blue'){
			for (var i = 0; i < markersBlue.length; i++) {
				console.log("crtam plavi");	
			    markersBlue[i].setMap(map);
			}
		}
		else {
			for (var i = 0; i < markersRed.length; i++) {
				console.log("crtam crveni");	
			    markersRed[i].setMap(map);
			}
		}
	}

	$http.get('http://46.101.173.23:8080/game/' + $rootScope.Match.idGame)
		.success(function(data){
			controller.data = data;
			controller.DrawMap(data);
			controller.team1 = data.team[0];
			controller.team2 = data.team[1];
			controller.ping();
			controller.Timer(60 * data.timer, $('#time'));
		})

	controller.ping = function(){
		console.log("ping ping");
		var firstPing = setInterval(function(){ 
			var path = 'http://46.101.173.23:8080/game/' + $rootScope.Match.idGame + '/team/' + controller.team1.idTeam;
			$http.get(path)
				.success(function(data){
					controller.team1People = data;
					controller.GetMarkers(data, 'blue');
				}).error(function(data){
					console.log(data);
				})
			path = 'http://46.101.173.23:8080/game/' + $rootScope.Match.idGame + '/team/' + controller.team2.idTeam;
			$http.get(path)
				.success(function(data){
					controller.team2People = data;
					controller.GetMarkers(data, 'red');
				}).error(function(data){
					console.log(data);
				})

			path = 'http://46.101.173.23:8080/notification/' + controller.team1.idTeam + '/teamnotifications';
			$http.get(path)
				.success(function(data){
					console.log("Notifikacije 1");
					console.log(data);
					controller.data = data;
					controller.data.forEach(function(entry){
						path = 'http://46.101.173.23:8080/game/person/' + entry.idPerson;
						$http.get(path)
							.success(function(person){
								console.log(entry.name + " " + person.name);
								controller.notifications.push({notif: entry.name, person: person.name});
							}).error(function(data){
								console.log(data);
							})
					})
				}).error(function(data){
					console.log(data);
				})

			path = 'http://46.101.173.23:8080/notification/' + controller.team2.idTeam + '/teamnotifications';
			$http.get(path)
				.success(function(data){
					console.log("Notifikacije 2");
					console.log(data);

					controller.data = data;
					controller.data.forEach(function(entry){
						path = 'http://46.101.173.23:8080/game/person/' + entry.idPerson;
						$http.get(path)
							.success(function(person){
								console.log(entry.name + " " + person.name);
								controller.notifications.push({notif: entry.name, person: person.name});
							}).error(function(data){
								console.log(data);
							})
					})

				}).error(function(data){
					console.log(data);
				})

		}, 3000);
	}

	controller.Timer = function(duration, display){
	    var timer = duration, minutes, seconds;
	    var secToSend;
	    var secPing = setInterval(function () {
	        minutes = parseInt(timer / 60, 10)
	        seconds = parseInt(timer % 60, 10);

	        secToSend = minutes*60 + seconds;
	        var path = 'http://46.101.173.23:8080/game/' + $rootScope.Match.idGame + '/timer/' + secToSend;
			$http.post(path)
				.success(function(data){
					console.log(data);
				}).error(function(data){
					path = 'http://46.101.173.23:8080/game/' + $rootScope.Match.idGame + '/end';
			        console.log(path);
					$http.get(path)
						.success(function(data){
							$rootScope.statistics = data;
							console.log(data);
							clearInterval(firstPing);
							clearInterval(secPing);
							$location.url('/statistic');
						}).error(function(data){
							console.log("Error end");
					})
			})

	        minutes = minutes < 10 ? "0" + minutes : minutes;
	        seconds = seconds < 10 ? "0" + seconds : seconds;

	        display.text(minutes + ":" + seconds);

	        if (--timer < 0) {
	            timer = duration;
	        }
	    }, 1000);
	}

	controller.EndGame = function(){
		var path = 'http://46.101.173.23:8080/game/' + $rootScope.Match.idGame + '/end';
		console.log(path);
		$http.get(path)
			.success(function(data){
				$rootScope.statistics = data;
				console.log(data);
				clearInterval(firstPing);
				clearInterval(secPing);
				$location.url('/statistic');
				}).error(function(data){
				console.log("Error end");
		})
	}
  }]);

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

'use strict';

/**
 * @ngdoc function
 * @name webAngularTemplateApp.controller:MatchActionCtrl
 * @description
 * # MatchActionCtrl
 * Controller of the webAngularTemplateApp
 */
angular.module('webAngularTemplateApp')
  .controller('SpectatorCtrl', ['$rootScope', 'match', '$http', '$location', function ($rootScope, match, $http, $location) {
    
  	var controller = this;
  	controller.team1People = [];
  	controller.team2People = [];
  	var firstPing, secPing;

  	controller.icons = {
    	house: {
    		icon: 'images/marker_house.png'
    	},
    	flag: {
    		icon: 'images/marker_flag.png'
    	},
    	fence: {
    		icon: 'images/marker_fence.png'
    	},
    	woman: {
    		icon: 'images/marker_woman.png'
    	},
    	blue: {
    		icon: 'images/marker_blue.png'
    	},
    	red: {
    		icon: 'images/marker_red.png'
    	}
    };
    var map;
    var markersBlue = [];
    var markersRed = [];

  	controller.DrawMap = function(data){
  		console.log(data.map.startLat + " " + data.map.startLng);
  			var lat = data.map.startLat;
  			var lng = data.map.startLng;
			var latlng = new google.maps.LatLng(lat, lng);
			var settings = {
		        zoom: 10,
		        center: latlng,
		        mapTypeId: google.maps.MapTypeId.SATELLITE
		    }
		    map = new google.maps.Map(document.getElementById('matchMap'), settings);

		    var rectangle = new google.maps.Rectangle({
			    strokeColor: '#81c784',
			    strokeOpacity: 0.8,
			    strokeWeight: 2,
			    fillColor: '#a5d6a7',
			    fillOpacity: 0.35,
			    map: map,
			    clickable: true,
			    zIndex: 1,
			    bounds: {
			      north: data.map.startLat,
			      south: data.map.endLat,
			      east: data.map.endLng,
			      west: data.map.startLng
			    }
			});

		    var lat = data.map.flagLat;
    		var lng = data.map.flagLng;
    		var coord = {lat: lat, lng:lng};
			var marker = new google.maps.Marker({
				position: coord,
				map: map,
				icon: controller.icons['flag'].icon
			});

		    data.map.mapObstacles.forEach(function(obstacle){
		    	var lat = obstacle.lat;
    			var lng = obstacle.lng;
    			var coord = {lat: lat, lng:lng};
				var marker = new google.maps.Marker({
						position: coord,
						map: map,
						icon: controller.icons[obstacle.name].icon
				});
		    });

	};

	controller.GetMarkers = function(team, teamNumber){
		controller.clear(null);
		markersBlue = [];
		markersRed = [];
		console.log(team);
		for(var i = 0; i < team.length; i++){
			var lat = team[i].lat;
  			var lng = team[i].lng;
			var coord = {lat:lat,lng:lng};
			var marker = new google.maps.Marker({
				position: coord,
				map: map,
				icon: controller.icons[teamNumber].icon
			})
			if(teamNumber == 'blue')
				markersBlue.push(marker);
			else
				markersRed.push(marker);
		};
		if(teamNumber == 'blue')
			controller.DrawTeams('blue');
		else
			controller.DrawTeams('red');
	}

	controller.clear = function(map){
		for (var i = 0; i < markersBlue.length; i++) {
		    markersBlue[i].setMap(map);
		}
		for (var i = 0; i < markersRed.length; i++) {
		   markersRed[i].setMap(map);
		}
	}

	controller.DrawTeams = function(teamNumber){
		if(teamNumber == 'blue'){
			for (var i = 0; i < markersBlue.length; i++) {
				console.log("crtam plavi");	
			    markersBlue[i].setMap(map);
			}
		}
		else {
			for (var i = 0; i < markersRed.length; i++) {
				console.log("crtam crveni");	
			    markersRed[i].setMap(map);
			}
		}
	}

	$http.get('http://46.101.173.23:8080/game/' + $rootScope.Match.idGame)
		.success(function(data){
			controller.data = data;
			controller.DrawMap(data);
			controller.team1 = data.team[0];
			controller.team2 = data.team[1];
			controller.ping();
			controller.Timer(60 * data.timer, $('#time'));
		})

	controller.ping = function(){
		console.log("ping ping");
		var firstPing = setInterval(function(){ 
			var path = 'http://46.101.173.23:8080/game/' + $rootScope.Match.idGame + '/team/' + controller.team1.idTeam;
			$http.get(path)
				.success(function(data){
					controller.team1People = data;
					controller.GetMarkers(data, 'blue');
				}).error(function(data){
					console.log(data);
				})
			path = 'http://46.101.173.23:8080/game/' + $rootScope.Match.idGame + '/team/' + controller.team2.idTeam;
			$http.get(path)
				.success(function(data){
					controller.team2People = data;
					controller.GetMarkers(data, 'red');
				}).error(function(data){
					console.log(data);
				})
		}, 500);
	}

	controller.Timer = function(duration, display){
	    var timer = duration, minutes, seconds;
	    var secToSend;
	    var secPing = setInterval(function () {
	        minutes = parseInt(timer / 60, 10)
	        seconds = parseInt(timer % 60, 10);

	        secToSend = minutes*60 + seconds;
	        var path = 'http://46.101.173.23:8080/game/' + $rootScope.Match.idGame + '/timer/' + secToSend;
			$http.post(path)
				.success(function(data){
					console.log(data);
				}).error(function(data){
					path = 'http://46.101.173.23:8080/game/' + $rootScope.Match.idGame + '/end';
			        console.log(path);
					$http.get(path)
						.success(function(data){
							$rootScope.statistics = data;
							console.log(data);
							clearInterval(firstPing);
							clearInterval(secPing);
							$location.url('/statistic');
						}).error(function(data){
							console.log("Error end");
					})
			})

	        minutes = minutes < 10 ? "0" + minutes : minutes;
	        seconds = seconds < 10 ? "0" + seconds : seconds;

	        display.text(minutes + ":" + seconds);

	        if (--timer < 0) {
	            timer = duration;
	        }
	    }, 500);
	}

	controller.EndGame = function(){
		var path = 'http://46.101.173.23:8080/game/' + $rootScope.Match.idGame + '/end';
		console.log(path);
		$http.get(path)
			.success(function(data){
				$rootScope.statistics = data;
				console.log(data);
				clearInterval(firstPing);
				clearInterval(secPing);
				$location.url('/statistic');
				}).error(function(data){
				console.log("Error end");
		})
	}
  }]);

angular.module('webAngularTemplateApp').run(['$templateCache', function($templateCache) {
  'use strict';

  $templateCache.put('views/introduction.html',
    "<div class=\"text-center\" ng-controller=\"MatchCtrl as match\"> <div id=\"fullpage\"> <div class=\"section first-section\"> <div class=\"img-container\"> <img src=\"images/logo.png\" alt=\"\" width=\"400px\"> </div> </div> <div class=\"section second-section\"> <div class=\"row\"> <div class=\"col-md-4\"> <img src=\"images/aim.png\" alt=\"\" width=\"200px\"> <h2>AIM FOR VICTORY</h2> </div> <div class=\"col-md-4\"> <img src=\"images/paintbal.png\" alt=\"\" width=\"330px\"> <h2>BATTLE FOR FLAG</h2> </div> <div class=\"col-md-4\"> <img src=\"images/multiplayer.png\" alt=\"\" width=\"200px\"> <h2>PLAY WITH FRIENDS</h2> </div> </div> </div> <div class=\"section third-section\"> <h1>WATCH GAME LIVE</h1> <div class=\"container\"> <table class=\"table\"> <tr class=\"text-center\"> <th>No.</th> <th>Team 1</th> <th>Team 2</th> <th>Map</th> <th>WATCH GAME</th> </tr> <tr class=\"text-left\" ng-repeat=\"match in match.Matches\" ng-show=\"match.start\"> <td>{{$index+1}}.</td> <td>{{match.team[0].name}}</td> <td>{{match.team[1].name}}</td> <td>{{match.map.name}}</td> <td> <a href=\"#/match-action\"> <span class=\"glyphicon glyphicon-play\" match-id-traversal data-matchid=\"{{match.idGame}}\"> </span> </a> </td> </tr> </table> <a href=\"#/login\"> <h4>JUDGE LOGIN</h4> </a> </div> </div> </div> </div> <script>jQuery(document).ready(function($) {\n" +
    "\t\t$('#fullpage').fullpage();\n" +
    "\t});</script>"
  );


  $templateCache.put('views/login.html',
    "<div class=\"container login\"> <div class=\"row\"> <div class=\"text-center\" style=\"margin-top:10px\"> <img src=\"images/logo.png\" alt=\"\" width=\"300px\"> </div> <div class=\"form-wrapper col-md-8 col-md-offset-2\"> <hr> <form ng-submit=\"login.CheckUser()\"> <div class=\"form-group\"> <div class=\"input-group\"> <div class=\"input-group-addon\"><span class=\"glyphicon glyphicon-user\" aria-hidden=\"true\"></span></div> <input ng-model=\"login.username\" type=\"text\" name=\"username\" class=\"form-control\" placeholder=\"Username\" required> </div> </div> <div class=\"form-group\"> <div class=\"input-group\"> <div class=\"input-group-addon\"><span class=\"glyphicon glyphicon-lock\" aria-hidden=\"true\"></span></div> <input ng-model=\"login.password\" type=\"password\" name=\"password\" class=\"form-control\" placeholder=\"Password\" required> </div> </div> <div ng-show=\"login.errorMessage\" class=\"alert alert-danger\" role=\"alert\">{{login.errorMessage}}</div> <button class=\"btn btn-primary btn-block\">LOGIN</button> <div class=\"text-center\"> <a ng-href=\"#/register\">Don't have an account? <strong>Sign up</strong></a> </div> </form> </div> </div> </div>"
  );


  $templateCache.put('views/main.html',
    "<div class=\"container-fluid\"> <div id=\"matchMap\" style=\"width: 100%; height: 400px\"></div> </div> <div class=\"mainMatch\"> <div class=\"col-md-6\"> <h3>Code: {{main.data.code}}</h3> <h2>{{main.team1.idTeam + \" \" + main.team1.name}}</h2> <table class=\"table\"> <tr> <th>No.</th> <th>Name</th> <th>Kill</th> <th>Death</th> <th>Ready to rumble</th> </tr> <tr ng-repeat=\"people in main.team1People\"> <td>{{$index+1}}</td> <td>{{people.name + \" \" + people.surname}}</td> <td>{{people.kill}}</td> <td>{{people.death}}</td> <td> <span class=\"glyphicon glyphicon-ok\" ng-show=\"people.ready\"></span> <span class=\"glyphicon glyphicon-time\" ng-hide=\"people.ready\"></span> </td> </tr> </table> </div> <div class=\"col-md-6\"> <br><br> <h2>{{main.team2.idTeam + \" \" + main.team2.name}}</h2> <table class=\"table\"> <tr> <th>No.</th> <th>Name</th> <th>Kill</th> <th>Death</th> <th>Ready to rumble</th> </tr> <tr ng-repeat=\"people in main.team2People\"> <td>{{$index+1}}</td> <td>{{people.name + \" \" + people.surname}}</td> <td>{{people.kill}}</td> <td>{{people.death}}</td> <td> <span class=\"glyphicon glyphicon-ok\" ng-show=\"people.ready\"></span> <span class=\"glyphicon glyphicon-time\" ng-hide=\"people.ready\"></span> </td> </tr> </table> </div> <div class=\"row\"> <div class=\"col-md-12 text-center\"> <div class=\"container text-center\"> <button ng-click=\"main.StartMatch()\" class=\"btn btn-info\">START</button> </div> </div> </div> <!-- <button class=\"btn btn-default\"><img src=\"images/chip.png\" width=\"30px\" alt=\"\"> Place a bet</button> --> </div>"
  );


  $templateCache.put('views/map.html',
    "<div class=\"container-fluid matchMap\"> <div id=\"matchMap\" style=\"width: 100%; height: 400px\"></div> <div class=\"container\"> <div class=\"obstacles\"> <img ng-click=\"map.setIcon('flag')\" ng-class=\"{'bordered' : map.icon == 'flag'}\" src=\"images/icon_flag.png\" width=\"70px\" alt=\"\"> <img ng-click=\"map.setIcon('house')\" ng-class=\"{'bordered' : map.icon == 'house'}\" src=\"images/icon_house.png\" width=\"70px\" alt=\"\"> <img ng-click=\"map.setIcon('fence')\" ng-class=\"{'bordered' : map.icon == 'fence'}\" src=\"images/icon_fence.png\" width=\"70px\" alt=\"\"> <img ng-click=\"map.setIcon('woman')\" ng-class=\"{'bordered' : map.icon == 'woman'}\" src=\"images/icon_woman.png\" width=\"70px\" alt=\"\"> </div> <form> <br> <input type=\"text\" class=\"text-center\" ng-model=\"map.mapName\" name=\"username\" placeholder=\"Map name\" required> <br> <br> <br> <br> <div class=\"circle-container\" ng-click=\"map.AddMap()\"> <div class=\"outer-ring\"></div> <div class=\"circle\"> <div class=\"circle-front circle-front-map\"> <p>CREATE MAP</p> </div> <div class=\"circle-back\"> <img class=\"back-logo\" src=\"images/logo2.png\" alt=\"The Elevation Group Logo\"> </div> </div> </div> <div class=\"alert alert-success\" ng-show=\"map.alert\" role=\"alert\"><span class=\"glyphicon glyphicon-ok\"></span> Map added succesfully</div> </form></div> <br>  </div> "
  );


  $templateCache.put('views/match-action.html',
    "<span id=\"time\">05:00</span> <div class=\"container-fluid\"> <div id=\"matchMap\" style=\"width: 100%; height: 400px\"></div> </div> <div class=\"mainMatch\"> <div class=\"col-md-4\"> <h2>{{matchAction.team1.idTeam + \" \" + matchAction.team1.name}}</h2> <table class=\"table\"> <tr> <th>No.</th> <th>Name</th> <th>Kill</th> <th>Death</th> </tr> <tr ng-repeat=\"people in matchAction.team1People\"> <td>{{$index+1}}</td> <td>{{people.name + \" \" + people.surname}}</td> <td>{{people.kill}}</td> <td>{{people.death}}</td> </tr> </table> </div> <div class=\"col-md-4\"> <h2>{{matchAction.team2.idTeam + \" \" + matchAction.team2.name}}</h2> <table class=\"table\"> <tr> <th>No.</th> <th>Name</th> <th>Kill</th> <th>Death</th> </tr> <tr ng-repeat=\"people in matchAction.team2People\"> <td>{{$index+1}}</td> <td>{{people.name + \" \" + people.surname}}</td> <td>{{people.kill}}</td> <td>{{people.death}}</td> </tr> </table> </div> <div class=\"col-md-4\"> <h2>Live Action</h2> <table class=\"table\"> <tr> <th>Name</th> <th>Description</th> </tr> <tr ng-repeat=\"not in matchAction.notifications\"> <td>{{not.person}}</td> <td>{{not.notif}}</td> </tr> </table> </div> <div class=\"row\"> <div class=\"col-md-12\"> <div class=\"container text-center\"> <button ng-click=\"matchAction.EndGame()\" class=\"btn btn-info\">END</button> </div> </div> </div> <!-- <button class=\"btn btn-default\"><img src=\"images/chip.png\" width=\"30px\" alt=\"\"> Place a bet</button> --> </div>"
  );


  $templateCache.put('views/new-match.html',
    "<div class=\"container-fluid\"> <form> <div class=\"row\"> <div class=\"col-md-12 alphaTeam\"> <div class=\"col-md-4\"> <div class=\"form-group\"> <input type=\"text\" name=\"team1\" ng-model=\"newMatch.team1\" placeholder=\"Team 1 Name\" required> </div> </div> </div> <div class=\"col-md-12 betaTeam\"> <div class=\"col-md-4 col-md-offset-5\"> <div class=\"form-group\"> <input type=\"text\" name=\"team2\" ng-model=\"newMatch.team2\" placeholder=\"Team 2 Name\" required> </div> </div> </div> </div> <div class=\"container\"> <div class=\"row MapChoice\"> <h2>Choose map:</h2> <table class=\"table\"> <tr ng-repeat=\"map in newMatch.mapList\"> <td> <div class=\"takeForm\"> <input type=\"radio\" id=\"{{map.name}}\" ng-model=\"newMatch.map\" name=\"name\" value=\"{{$index}}\" required> <label for=\"{{map.name}}\"> {{map.name}} </label> </div> </td> </tr> </table> <!-- <select class=\"form-control\" ng-model=\"newMatch.map\">\n" +
    "\t\t\t\t\t<option value=\"\" disabled selected>Select Map</option>\n" +
    "\t\t\t\t\t<option ng-repeat=\"map in newMatch.mapList\" value=\"{{$index}}\">{{map.name}}</option>\n" +
    "\t\t\t\t</select>\n" +
    "\t\t\t\t<br>\n" +
    " --> <div class=\"form-group timer\" class=\"text-center\"> <input type=\"text\" class=\"text-center\" name=\"timer\" ng-model=\"newMatch.timer\" placeholder=\"Game time (min)\" required> </div> <br> <br> <div class=\"circle-container\" ng-click=\"newMatch.AddMatch()\"> <div class=\"outer-ring\"></div> <div class=\"circle\"> <div class=\"circle-front\"> <p>START</p> </div> <div class=\"circle-back\"> <img class=\"back-logo\" src=\"images/logo2.png\" alt=\"The Elevation Group Logo\"> </div> </div> </div> <div class=\"alert alert-success\" ng-show=\"newMatch.alert\" role=\"alert\"> <span class=\"glyphicon glyphicon-ok\"></span> Match created successfully </div> <br><br> </div> </div> </form> </div>"
  );


  $templateCache.put('views/register.html',
    "<div class=\"container login register\"> <div class=\"row\"> <div class=\"text-center\"> <h2>LOGO</h2> </div> <form ng-submit=\"register.Register()\" class=\"form-wrapper col-md-10 col-md-offset-1\" enctype=\"multipart/form-data\"> <hr> <div class=\"form-group\"> <input ng-model=\"register.name\" type=\"text\" name=\"name\" class=\"form-control\" placeholder=\"Name\" required> </div> <div class=\"form-group\"> <input ng-model=\"register.surname\" type=\"text\" name=\"surname\" class=\"form-control\" placeholder=\"Surname\" required> </div> <div class=\"form-group\"> <input ng-model=\"register.email\" type=\"email\" name=\"email\" class=\"form-control\" placeholder=\"Email\" required> </div> <div class=\"form-group\"> <input ng-model=\"register.username\" type=\"text\" name=\"username\" class=\"form-control\" placeholder=\"Username\" required> </div> <div class=\"form-group\"> <div class=\"input-group\"> <input ng-model=\"register.password\" type=\"{{register.passwordType}}\" name=\"password\" class=\"form-control\" placeholder=\"Password\" required> <div ng-click=\"register.ChangePassVisibility()\" class=\"input-group-addon pass-opacity\"> <span ng-class=\"register.passwordType == 'password' ? 'glyphicon-eye-open':'glyphicon-eye-close'\" class=\"glyphicon\"></span> </div> </div> </div> <button class=\"btn btn-primary btn-block\">SUBMIT</button> </form> </div> </div>"
  );


  $templateCache.put('views/spectator.html',
    "<span id=\"time\">05:00</span> <div class=\"container-fluid\"> <div id=\"matchMap\" style=\"width: 100%; height: 400px\"></div> </div> <div class=\"mainMatch\"> <div class=\"col-md-4\"> <h2>{{matchAction.team1.idTeam + \" \" + matchAction.team1.name}}</h2> <table class=\"table\"> <tr> <th>No.</th> <th>Name</th> <th>Kill</th> <th>Death</th> </tr> <tr ng-repeat=\"people in matchAction.team1People\"> <td>{{$index+1}}</td> <td>{{people.name + \" \" + people.surname}}</td> <td>{{people.kill}}</td> <td>{{people.death}}</td> </tr> </table> </div> <div class=\"col-md-4\"> <h2>{{matchAction.team2.idTeam + \" \" + matchAction.team2.name}}</h2> <table class=\"table\"> <tr> <th>No.</th> <th>Name</th> <th>Kill</th> <th>Death</th> </tr> <tr ng-repeat=\"people in matchAction.team2People\"> <td>{{$index+1}}</td> <td>{{people.name + \" \" + people.surname}}</td> <td>{{people.kill}}</td> <td>{{people.death}}</td> </tr> </table> </div> <div class=\"col-md-4\"> <h2>Notifications</h2> <table class=\"table\"> <tr> <th>Name</th> <th>Description</th> </tr> <tr> <td></td> </tr> </table> </div> <!-- <button class=\"btn btn-default\"><img src=\"images/chip.png\" width=\"30px\" alt=\"\"> Place a bet</button> --> </div>"
  );


  $templateCache.put('views/statistic.html',
    "<div class=\"container statistic\"> <br> <br> <div class=\"text-center head\"> <img src=\"images/statistic.png\" alt=\"\" width=\"500px\"> <h1>WINNER IS <span>ALPHA</span> TEAM</h1> </div> <br> <br> <h1>Killers and Score</h1> <table class=\"table\"> <tr> <th>Name</th> <th>Kill</th> <th>Death</th> </tr> <tr ng-repeat=\"stat in statistic.stat\"> <td>{{stat.name}}</td> <td>{{stat.kill}}</td> <td>{{stat.death}}</td> </tr> </table> </div>"
  );


  $templateCache.put('views/wrapper.html',
    "<div class=\"menu-container\"> <div class=\"menu-container__icon\" ng-click=\"nav.ToggleNavigation();\" ng-class=\"{'menu-rotate': nav.toggle}\" ng-show=\"nav.CheckLogin()\"> <div class=\"menu-icon menu-icon--top\" ng-class=\"{'menu-top-action': nav.toggle}\"></div> <div class=\"menu-icon menu-icon--middle\" ng-class=\"{'menu-middle-action': nav.toggle}\"></div> <div class=\"menu-icon menu-icon--bottom\" ng-class=\"{'menu-bottom-action': nav.toggle}\"></div> </div> <div class=\"menu-container__app\"> <div class=\"menu-container__app-login\"> <!-- <a href=\"#register\" ng-hide=\"nav.CheckLogin()\">\n" +
    "        <span class=\"glyphicon glyphicon-user\"></span> Register\n" +
    "      </a> --> <a href=\"#login\" ng-hide=\"nav.CheckLogin()\"> <span class=\"glyphicon glyphicon-log-in\"></span> Judge </a> <a href=\"#login\" logout ng-show=\"nav.CheckLogin()\"> <span class=\"glyphicon glyphicon-log-out\"></span> Log out </a> </div> </div> </div> <div class=\"flip-container\"> <div class=\"flip-inner\" ng-class=\"{'flip-inner-action': nav.toggle}\"> <div class=\"front\"> <div class=\"content\"> <div ng-view=\"\"></div> </div> </div> <div class=\"back\"> <nav> <ul> <li><a href=\"#new-match\" ng-click=\"nav.ToggleNavigation();\">NEW MATCH</a></li> <li><a href=\"#map\" ng-click=\"nav.ToggleNavigation();\">MAP</a></li> <li ng-show=\"nav.CheckLogin()\"> <a href=\"#login\" logout> Log out </a> </li> </ul> </nav> </div> <!-- back --> </div> <!-- flip-inner --> </div> <!-- flip-container -->"
  );

}]);
