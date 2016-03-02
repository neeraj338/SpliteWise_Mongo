var appAddAmt = angular.module('addJob', []);
appAddAmt.controller('AddJobCntrl', ['$scope','$http','$location','$cookies', function($scope, $http, $location, $cookies){
	//pre fil the login user data.
	var loggedInUser = $cookies.getObject("loggedInUser");
	$scope.name = loggedInUser.name;
	$scope.email = loggedInUser.email;
	console.log(loggedInUser);
	$http.get('/getJobHistoryData').success(function(data, status, headers, config){
		
  		$scope.sort={
  			column:'completedTime', // set the default sort type
  			descending:true
  		};
		$scope.jobHistory = data;
	});
	$scope.addJob = function(){
		var criteria = {
			name : $scope.name,
			category : $scope.option,
			email :  $scope.email
		};

		$http.post('/addJob', criteria).success(function(data, status, headers, config){
			//$location.path('/Dashboard');
			$scope.email = "";
			$scope.option = "";
			$scope.message = " Job Added Successfully."
		});
	};
	$scope.deleteJobHistory = function(id){
			console.log("delete history method claa "+id);
			var index = -1;
			for (var i = 0; i < $scope.jobHistory.length; i++) {
				var historyData = $scope.jobHistory[i];
				if(historyData.id == id){
					index = i;
				}
			};
			if(index != -1){
				$http.get("/deleteJobHistory/"+id).success(function(data, status, header, config){
					$scope.jobHistory.splice(index, 1);
					console.log($scope.jobHistory);
				});
			}
	};

}]);