/**
 * Angular JS for competitions.html
 */

var app = angular.module('cyclingResultsApp', ['cyclingResultsServices'])
.controller('competitionsListCtrl', ['$scope', '$filter', '$http', '$window', 'contracts', 'restServices',
   function($scope, $filter, $http, $window, contracts,restServices) {
	$scope.verFiltro = true;
	$scope.types = contracts.competitionClassesCmb();
	$scope.genders =contracts.genderCmb();
	$scope.sports = contracts.sportCmb();
	$scope.classes = contracts.classCmb();
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
		classID: "1",
		initDate : initDate,
		finishDate : finishDate,
		type : "UWT",
		verKatapin: "true"
	}
	var filter = $scope.filter;
	
	$scope.loadCompetitions = function() { 
		restServices.loadCompetitions(filter.sportID, filter.genderID, filter.classID, filter.initDate, filter.finishDate)
		.then(function(response){
			$window.alert("resultado carga:"+response.data);
		});
	}
	
	$scope.getData = function() {
		restServices.getCompetitions(filter.initDate, filter.finishDate, 
				filter.sportID, filter.genderID, filter.classID, filter.type).then(
				function(response) {
					showCompetitions();
					$scope.competitions = response.data;
				});
	}

	$scope.getStages = function(competitionName, competitionID, eventID,
			editionID, genderID, classID) {
		$scope.competitionName = competitionName;
		restServices.getStages(competitionID, eventID ,editionID ,genderID, classID)
		.then(function(response) {
				showCompetitions();
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
		restServices.getOneDayResults(competitionID, eventID, editionID, genderID, classID)
			.then(
				function(response) {
					$scope.results = response.data;
					showResults();
				});
	}

	$scope.getStageResults = function(competitionName, competitionID, eventID,
			editionID, genderID, classID, phase1ID) {
		$scope.competitionName = competitionName;
		$scope.results = [ {
			rank : "...",
			name : "loading..."
		} ]
		restServices.getStageResults(competitionID, eventID,editionID,genderID,classID,phase1ID)
			.then(function(response) {
				$scope.results = response.data;
				showResults();
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
		restServices.getClassification(competitionID,eventID,editionID,genderID,classID,
				phase1ID,phaseClassificationID).then(
				function(response) {
					$scope.results = response.data;
					showResults();
				});
	}
	
	
	function showResults () {
		$scope.verFiltro = false;
		$scope.verCompetis = false;
		$scope.verResults = true;
	}
	
	function showCompetitions() {
		$scope.verCompetis = true;
		$scope.verResults = false;
		$scope.verFiltro = false;
	}
	
	$scope.showResults = function(){
		showResults();
	}
	
	$scope.showCompetitions = function(){
		showCompetitions();
	}
	
	$scope.showForm = function() {
		$scope.verFiltro = true;
		$scope.verCompetis = false;
		$scope.verResults = false;
	}
	
	
}]);
