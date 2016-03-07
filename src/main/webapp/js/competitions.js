/**
 * Angular JS for competitions.html
 */

var app = angular.module('cyclingResultsApp', ['cyclingResultsServices'])
.controller('competitionsListCtrl', ['$scope', '$filter', '$http', 'contracts', 'restServices',
   function($scope, $filter, $http, contracts,restServices) {
	$scope.types = contracts.competitionClassesCmb();
	$scope.genders =contracts.genderCmb();
	$scope.sports = contracts.sportCmb();
	$scope.competitionName = "-"
	$scope.results = [ {
		rank : "-",
		name : "empty"
	} ]

	var d = new Date();
	var finishDate = $filter('date')(d, "yyyyMMdd");
	d.setMonth(d.getMonth() - 1);
	var initDate = $filter('date')(d, "yyyyMMdd");

	$scope.filter = {
		sportID : "102",
		genderID : "1",
		initDate : initDate,
		finishDate : finishDate,
		type : "UWT"
	}
	var filter = $scope.filter;
	$http.get(
			"rest/competitions/query/" + filter.initDate + ","
					+ filter.finishDate + "," + filter.sportID + ","
					+ filter.genderID + ",1," + filter.type).then(
			function(response) {
				$scope.competitions = response.data;
			});
	$scope.getData = function() {
		$http.get(
				"rest/competitions/query/" + filter.initDate + ","
						+ filter.finishDate + "," + filter.sportID + ","
						+ filter.genderID + ",1," + filter.type).then(
				function(response) {
					$scope.competitions = response.data;
				});
	}

	$scope.getStages = function(competitionName, competitionID, eventID,
			editionID, genderID, classID) {
		$scope.competitionName = competitionName;
		$http.get(
				"rest/competitions/stageRaceCompetitions/" + competitionID
						+ "," + eventID + "," + editionID + "," + genderID
						+ "," + classID).then(function(response) {
			$scope.competitions = response.data;
		});
	}

	$scope.getOneDayResults = function(competitionName, competitionID, eventID,
			editionID, genderID, classID) {
		$scope.competitionName = competitionName;
		$scope.results = [ {
			rank : "...",
			name : "loading..."
		} ]
		$scope.results = restServices.getOneDayResults(competitionID, eventID, editionID, genderID, classID)
			.then(
				function(response) {
					$scope.results = response.data;
				});
	}

	$scope.getStageResults = function(competitionName, competitionID, eventID,
			editionID, genderID, classID, phase1ID) {
		$scope.competitionName = competitionName;
		$scope.results = [ {
			rank : "...",
			name : "loading..."
		} ]
		$http.get(
				"rest/results/stage/" + competitionID + "," + eventID + ","
						+ editionID + "," + genderID + "," + classID + ","
						+ phase1ID).then(function(response) {
			$scope.results = response.data;
		});
	}

	$scope.getClassification = function(competitionName, competitionID,
			eventID, editionID, genderID, classID, phase1ID,
			phaseClassificationID) {
		$scope.competitionName = competitionName;
		$scope.results = [ {
			rank : "...",
			name : "loading..."
		} ]
		$http.get(
				"rest/results/classification/" + competitionID + "," + eventID
						+ "," + editionID + "," + genderID + "," + classID
						+ "," + phase1ID + "," + phaseClassificationID).then(
				function(response) {
					$scope.results = response.data;
				});
	}
}]);
