var app = angular.module("mainApp", ["ngRoute"]);

function ConfigToken(){
	if(window.localStorage.getItem("FOOD_TOKEN") == null) return null;
	return {
		headers : {
			Token: window.localStorage.getItem("FOOD_TOKEN")
		}
	}
}
app.controller("loginCtrl", ($scope, $location, $http)=>{
	// let baseUrl = 'http://localhost:5000';
	let baseUrl = 'https://hagyhang.herokuapp.com';
	$scope.email = "";
	$scope.password = "";
	$http.defaults.headers.post["Content-Type"] = "application/x-www-form-urlencoded";
	let token = ConfigToken();
	if (token != null){
		window.location = baseUrl + "/dashboard"
	}
	$scope.SignUp = ()=>{
		$location.path('/sign-up');
	}
	$scope.submit = ()=>{
		var user = {
			name : $scope.email, //'phananh123qqq@gmail.com'
			pass : $scope.password //'123123qqq'
		}
		$http.post(baseUrl + "/login?name=" + $scope.email + "&" + "pass=" + $scope.password, user, ConfigToken()).then((res)=>{
			var data = res.data;
			console.log(data)
			if (data == true)
			{
				window.location = baseUrl + "/dashboard"
			}
		})
	}
})