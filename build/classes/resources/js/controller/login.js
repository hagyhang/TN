var app = angular.module('myApp', []);
app.controller('myCtrl', function($scope, $http, $window) {
	//sign in variable
	$scope.username="";
	$scope.password="";
	$scope.remember=false;
	$scope.alert = false;

	//sign up variable
	$scope.su={};
	$scope.su.fullName="";
	$scope.su.loginName="";
	$scope.su.password="";

	$scope.login = ()=>{
		// alert($scope.username + " " + $scope.password + " " + $scope.remember);
		$http.post("http://localhost:8080/login?username=" + $scope.username + "&password=" + $scope.password)
	    .then(function(response) {
	    	let result = response.data;
	    	if (result == null){
	    		$scope.alert = true;
	    	}
	    	else{
	    		if (result.loginName == $scope.username)
	    			$window.location.href = "http://localhost:8080/admin";
	    	}
	    	
	    });
	}
	$scope.loginWithFB = ()=>{
		alert($scope.username + " " + $scope.password + " " + $scope.remember);
	}
	$scope.signUp = ()=>{
		$http.post("http://localhost:8080/user/add", $scope.su)
	    .then(function(response) {
	    	$window.location.href = "http://localhost:8080/login";
	    		
	    });
	}
});
var showSignUp = ()=>{
	$('#signupbox').show();
	$('#loginbox').hide();
}
var showSignIn = ()=>{
	$('#signupbox').hide();
	$('#loginbox').show();
}