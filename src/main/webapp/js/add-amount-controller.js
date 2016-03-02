var appAddAmt = angular.module('appAddAmt', ['ngCookies']);
appAddAmt.controller('AddAmountCntrl', ['$scope','$http','$location','$cookies', function($scope, $http, $location, $cookies){
	//pre fil the login user data.
	var loggedInUser = $cookies.getObject("loggedInUser");
	$scope.name = loggedInUser.name;
	$scope.email = loggedInUser.email;
	console.log(loggedInUser);
	$scope.updateAmount = function(){
		var criteria = {
			name : $scope.name,
			email : $scope.email,
			amountSpent :  $scope.amountSpent,
			mobile : $scope.mobile,
			expenseComment : [{comment : $scope.comment, date: new Date(), amount: $scope.amountSpent}]
		};
		
		$http.post('/expeseAdd', criteria).success(function(data, status, headers, config){
			$location.path('/Dashboard');
		});
	}
}]);