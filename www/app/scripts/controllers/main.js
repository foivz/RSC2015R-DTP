'use strict';

/**
 * @ngdoc function
 * @name webAngularTemplateApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the webAngularTemplateApp
 */
angular.module('webAngularTemplateApp')
  .controller('MainCtrl', ['$rootScope', 'match', function ($rootScope, match) {
  	var controller = this;
	controller.Match = [];

  	controller.DrawMap = function(){
			var latlng = new google.maps.LatLng(45.5549624, 18.695514399999998);
			var settings = {
		        zoom: 10,
		        center: latlng,
		        mapTypeId: google.maps.MapTypeId.SATELLITE
		    }

		    var map = new google.maps.Map(document.getElementById('matchMap'), settings);
	}();

	// ovooo maknuti kasnije!
	var id = $rootScope.matchID == undefined ? 2 : $rootScope.matchID;

    match.FetchMatchByID(id)
	    .then(function(result){
	    	controller.Match = result.data;
	    	console.log(controller.Match);
	    }, function(data){
	    	console.log(data);
	    })

  }]);
