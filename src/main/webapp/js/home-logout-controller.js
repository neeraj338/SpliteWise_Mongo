var  dashBoard = angular.module('homeLogOut', []);
dashBoard.controller('HomeLogOutController', ['$scope','$http','$location','$cookies', function($scope,$http,$location,$cookies){
	$scope.home = function(){
		$location.path('/Dashboard');
	};
	$scope.logOut = function(){
		$cookies.remove("loggedInUser");
		window.location = "/";
	}

}]);