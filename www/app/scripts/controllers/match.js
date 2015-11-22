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
