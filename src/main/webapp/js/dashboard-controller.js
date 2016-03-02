var  dashBoard = angular.module('dashBoard', []);
dashBoard.controller('DashBoardChartController', ['$scope','$http', function($scope,$http){
	//$scope.objDash = {};
	$scope.options = {width: 500, height: 300, 'bar': 'aaa'};
            $scope.data = [1, 2, 3, 4];
            
            $scope.hovered = function(d){
                $scope.barValue = d;
                $scope.$apply();
            };
            $scope.barValue = 'None';

     function loadUserExpenceData(path){
    	$http.get(path).success(function(data, status){
				$scope.userExpence = [];
				for (var i = 0; i < data.length; i++) {
					var userData = {
						userName : "",
						emaail : "",
						totalAmount : ""
					};
					var user = data[i];
					userData.userName = user.firstName + " " +user.lastName;
					userData.email = user.email;
					userData.totalAmount = user.amountSpent;
					$scope.userExpence.push(userData);
				};
			});
    }
    //fetch total Expence current month data by user on page load
   	loadUserExpenceData('/userExpence/false');

   	$scope.getPreviousMonthData = function(){
   		var url = "/userExpence/true";
   		console.log("loading Previous month data ..");
   		loadUserExpenceData(url);
   	}
   	$scope.getCurrentMonthdata = function(){
   		var url = "/userExpence/false";

   		console.log("loading current month data ..");
   		loadUserExpenceData(url);
   	}
   	$scope.sort = {
   		column : 'totalAmount',
   		descending : true
   	};
}]);