/**
 * Servicios angular JS
 */

angular.module('cyclingResultsServices', [])
.factory('contracts', function() {
	 var competitionClassesCmb = function () {
	     return [ "ALL", "UWT", "1.WWT", "2.WWT", "1.HC", "2.HC", "1.1", "2.1", "1.2", "2.2", "CN", "CDM" ];
	 }
	 var genderCmb = function(){
		 return [{genderID:"1", name:"MALE"}, {genderID:"2", name:"FEMALE"}];
	 } 
	 var sportCmb = function(){
		 return [{sportID:"102", name:"ROAD"}, {genderID:"306", name:"CYCLOCROSS"}];
	 } 
	 
	 return {
		 competitionClassesCmb: competitionClassesCmb,
		 genderCmb: genderCmb,
		 sportCmb: sportCmb
	  };
})
.factory('restServices', ['$http', function($http) {

	var getOneDayResults = function(competitionID, eventID, editionID, genderID, classID) {
		return $http.get(
				"rest/results/oneDay/" + competitionID + "," + eventID + ","
						+ editionID + "," + genderID + "," + classID);
	}
	
	return {
		getOneDayResults:getOneDayResults
	}
}]);
