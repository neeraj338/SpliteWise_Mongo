

var compareTo = function() {
    return {
        require: "ngModel",
        scope: {
            otherModelValue: "=compareTo"
        },
        link: function(scope, element, attributes, ngModel) {
             
            ngModel.$validators.compareTo = function(modelValue) {
                var isValid = (modelValue == scope.otherModelValue);
                console.log("Is Cinfirm Password matches .. "+isValid);
                //scope.isValid = isValid;
                return isValid;
            };
 
            scope.$watch("otherModelValue", function() {
                ngModel.$validate();
            });
        }
    };
};

var newUserRegister = angular.module("newUserRegister", []);
newUserRegister.directive("compareTo", compareTo);
newUserRegister.controller("RegisterUserController",["$scope","$http", function($scope, $http) {

  $scope.registerUser = function(item, event) {
    console.log("--> Submitting New User Registration.");
    var dataObject = {
       firstName : $scope.newUser.firstName,
       lastName  : $scope.newUser.lastName,
       email  : $scope.newUser.email,
       password  : $scope.newUser.password,
       confirmPassword  : $scope.newUser.confirmPassword
    };

    var responsePromise = $http.post("/newUser", dataObject, {});
    responsePromise.success(function(dataFromServer, status, headers, config) {
       console.log(dataFromServer.title);
       window.location = "/";
    });
     responsePromise.error(function(data, status, headers, config) {
       alert("Submitting form failed, User Already Present!");
    });
  }

}]);