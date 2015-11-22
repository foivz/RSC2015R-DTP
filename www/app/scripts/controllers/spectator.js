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
