/**
 * Angular JS for competitions.html
 */

var app = angular.module('calendarApp', ['cyclingResultsServices'])
.controller('calendarCtrl', ['$scope', '$filter', '$http', '$window', 'contracts', 'restServices',
   function($scope, $filter, $http, $window, contracts,restServices) {
	$scope.results = [ {
		initdate : "-",
		finishdate: "-",
		name:"-",
		country: "-",
		category: "-",
		eventClass: "-"		
	} ]

	var d = new Date();
	var initDate = $filter('date')(d, "yyyyMMdd");
	d.setMonth(d.getMonth() + 1);
	var finishDate = $filter('date')(d, "yyyyMMdd");

	$scope.filter = {
			initdate : initDate,
			finishdate: finishDate,
			name:"-",
			country: "-",
			category: "-",
			eventClass: "-"		
		}
	var filter = $scope.filter;
	
	$scope.getCalendar = function(){
		$scope.results = [ {
			initdate : "-",
			finishdate: "-",
			name:"-",
			country: "-",
			category: "-",
			eventClass: "-"		
		} ];
		restServices.getCalendar(filter.initdate, filter.finishdate, filter.name, filter.country, filter.category, filter.eventClass)
		.then(function(response){
			$scope.results = response.data;
		});
	}
	
	
}]);
