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
	controller.DrawMap(46.311390, 16.334515);

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
