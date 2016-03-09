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
	 var classCmb = function(){
		 return [{classID:"1", name:"ELITE"}, {classID:"101", name:"SUB-23"}, {classID:"2", name:"JUNIOR"}];
	 } 
	 
	 return {
		 competitionClassesCmb: competitionClassesCmb,
		 genderCmb: genderCmb,
		 sportCmb: sportCmb,
		 classCmb: classCmb
	  };
})
.factory('restServices', ['$http', function($http) {

	var getOneDayResults = function(competitionID, eventID, editionID, genderID, classID) {
		return $http.get("rest/results/oneDay/" + competitionID + "," + eventID + ","
						+ editionID + "," + genderID + "," + classID);
	}
	
	
	var getCompetitions = function(initDate, finishDate, sportID, genderID, classID, competitionClass) {
		return $http.get("rest/competitions/query/" + initDate + ","
						+ finishDate + "," + sportID + ","
						+ genderID + ","+classID+"," + competitionClass);
	}

	var getStages = function(competitionID, eventID, editionID, genderID, classID) {
		return $http.get("rest/competitions/stageRaceCompetitions/" + competitionID
						+ "," + eventID + "," + editionID + "," + genderID
						+ "," + classID);
	}

	var getStageResults = function(competitionID, eventID,editionID, genderID, classID, phase1ID) {
		return $http.get("rest/results/stage/" + competitionID + "," + eventID + ","
						+ editionID + "," + genderID + "," + classID + ","
						+ phase1ID);
	}

	var getClassification = function(competitionID,eventID, editionID, genderID, classID, phase1ID,phaseClassificationID) {
		return $http.get("rest/results/classification/" + competitionID + "," + eventID
						+ "," + editionID + "," + genderID + "," + classID
						+ "," + phase1ID + "," + phaseClassificationID);
	}
	
	var loadCompetitions = function(sportID, genderID, classID, initDate, finishDate) {
		return $http.get("rest/competitions/loadCompetitions/"+sportID+","+genderID+","+classID+","+initDate+","+finishDate);
	}
	
	
	
	
	return {
		getOneDayResults:getOneDayResults,
		getCompetitions:getCompetitions,
		getStages:getStages,
		getStageResults:getStageResults,
		getClassification: getClassification, 
		loadCompetitions: loadCompetitions
	}
}]);
