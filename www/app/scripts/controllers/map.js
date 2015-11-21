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

    controller.DrawMap = function(){
			var latlng = new google.maps.LatLng(46.311390, 16.334515);
			var settings = {
		        zoom: 10,
		        center: latlng,
		        mapTypeId: google.maps.MapTypeId.SATELLITE
		    }

		    var map = new google.maps.Map(document.getElementById('matchMap'), settings);

		    google.maps.event.addDomListener(map, 'click', function(event) {
					controller.marker++;
					
					var lat = event.latLng.lat();
    				var lng = event.latLng.lng();
    				controller.coordArray.push([lat,lng]);
    				var coord = {lat: lat, lng:lng};
    				console.log(lat + " " + lng);
    				var marker = new google.maps.Marker({
					    position: coord,
					    map: map,
					});
					
					if(controller.marker == 3)
					{
						console.log(controller.coordArray);
						var rectangle = new google.maps.Rectangle({
						    strokeColor: '#81c784',
						    strokeOpacity: 0.8,
						    strokeWeight: 2,
						    fillColor: '#a5d6a7',
						    fillOpacity: 0.35,
						    map: map,
						    bounds: {
						      north: controller.coordArray[0][0],
						      south: controller.coordArray[1][0],
						      east: controller.coordArray[1][1],
						      west: controller.coordArray[0][1]
						    }
						  });
					}
			  	});
	}();


	controller.AddMap = function(){
		var obj = {
			name: controller.mapName,
			startLat: controller.coordArray[0][0],
			startLng: controller.coordArray[0][1],
			endLat: controller.coordArray[1][0],
			endLng: controller.coordArray[1][1],
			flagLat: controller.coordArray[2][0],
			flagLng: controller.coordArray[2][1]
		};
		map.AddMap(obj)
		.then(function(data){
			controller.alert = 1;
		}, function(){
			console.log("Map add error");
		});
	};
	
  }]);
