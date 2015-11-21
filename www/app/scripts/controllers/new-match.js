'use strict';

/**
 * @ngdoc function
 * @name webAngularTemplateApp.controller:AboutCtrl
 * @description
 * # AboutCtrl
 * Controller of the webAngularTemplateApp
 */
angular.module('webAngularTemplateApp')
  .controller('NewMatchCtrl', ['map', function (map) {
    var controller = this;
    controller.mapList = [];

    map.FetchMaps()
    	.then(function(data){
    		console.log(data);
    	}, function(){
    		console.log("Error map fetch");
    	})
    controller.AddMatch(){
    	var obj = {
    		team1: controller.team1,
    		team2: controller.team2,
    		map: controller.map
    	}
    	console.log(obj);
    }

  }]);
