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
		console.log("ping ping");
		controller.firstPing = setInterval(function(){ 
			//var path = 'http://46.101.173.23:8080/game/' + $rootScope.Match.idGame + '/team/' + controller.team1.idTeam;
			var path = 'http://46.101.173.23:8080/game/2/team/3';
			$http.get(path)
				.success(function(data){
					console.log("Tim 1");
					console.log(data);
					controller.team1People = data;
				}).error(function(data){
					console.log(data);
				})
			path = 'http://46.101.173.23:8080/game/2/team/2';
			$http.get(path)
				.success(function(data){
					console.log("Tim 2");
					console.log(data);
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
	//gameID/team/teamID

	// match.FetchMatchByID($rootScope.Match.idGame)
	// 	.then(function(data){
	// 		console.log(data);
	// 	}, function(){
	// 		console.log("Error");
	// 	})


  }]);
