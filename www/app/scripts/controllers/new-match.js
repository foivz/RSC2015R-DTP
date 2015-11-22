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

    map.FetchMaps()
    	.then(function(data){
    		console.log(data);
    		controller.mapList = data.data;
    		console.log(controller.mapList);
    	}, function(){
    		console.log("Error map fetch");
    	});

    controller.AddMatch = function(){
    	var obj = {
    		timer: controller.timer,
    		code: "qjaguarKod",
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
