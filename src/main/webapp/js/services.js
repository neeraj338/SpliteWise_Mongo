angular.module('appUserCotext', []).
service('UserContextService', ["$http", function($http){
	var loggedInUser = {
		email : "",
		name : ""
	};

	return{
		updateAndGetloggedInUser : function(user){
			loggedInUser.email = user.email;
			loggedInUser.name = user.firstName +" "+user.lastName;
			console.log("logged in User is : "+ loggedInUser.name);
			return loggedInUser;
		}
	};
	return{
		getTotalExpendetureByUser : function(){
			var userExpence = [];
			$http.get('/userExpence').success(function(data, status){
				return userExpence;
			});
		}
	};
	
	
}]);
	