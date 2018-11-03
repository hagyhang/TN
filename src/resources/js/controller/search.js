var app = angular.module('myApp', []);
app.controller('myCtrl', function($scope, $http) {
	$scope.bien = "123";
	$scope.isContentShow = "hidden";
    $scope.Find = ()=>{
    	// alert("a");
	$scope.isContentShow = "visible";
	    $http.post("http://localhost:8080/search?link=" + $scope.link)
	    .then(function(response) {
	    	$scope.page = response.data;
	    	if ($scope.page.title == null){
	    		alert ("Not Found");
	    		$scope.isContentShow = "hidden";
	    	}
	    	else
	        console.log($scope.page);
	    });
    };
});