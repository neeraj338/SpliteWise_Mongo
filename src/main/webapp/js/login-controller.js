//Log In User
var loginModule = angular.module("loginModule", ['appUserCotext','ngCookies']);

loginModule.controller("LoginController",["$scope","$http",'$cookies','UserContextService', function($scope, $http, $cookies, UserContextService) {
  
  $scope.isErr = false;
  $scope.email = "neeraj338@gmail.com";
  //$watch of email and password field
  $scope.$watch('email', function(newValue, oldValue, scope) {
    $scope.isErr = false;
    if(angular.isUndefined(newValue)){
      $scope.isErr = true;
      $scope.message = "Please Enter Valid Email."
    }
  });

  $scope.submitLogIn = function(item, event) {
    console.log("--> Submitting form");
    var dataObject = {
       email : $scope.email,
       password  : $scope.password
    };

    var responsePromise = $http.post("/login", dataObject, {});
    responsePromise.success(function(dataFromServer, status, headers, config) {
       console.log('Log is success ... ');
       var loggedInUser =  UserContextService.updateAndGetloggedInUser(dataFromServer);
       $cookies.putObject("loggedInUser",loggedInUser);
       window.location = "/goto/splitWise";
    });
     responsePromise.error(function(data, status, headers, config) {
       alert("Submitting form failed .. !");
    });
  }

}]);