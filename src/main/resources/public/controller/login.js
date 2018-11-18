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
	$scope.email = "";
	$scope.password = "";
	$http.defaults.headers.post["Content-Type"] = "application/x-www-form-urlencoded";
	let token = ConfigToken();
	if (token != null){
		window.location = "http://localhost:5000/dashboard"
	}
	$scope.SignUp = ()=>{
		$location.path('/sign-up');
	}
	$scope.submit = ()=>{
		var user = {
			name : $scope.email, //'phananh123qqq@gmail.com'
			pass : $scope.password //'123123qqq'
		}
		$http.post("http://localhost:5000/login?name=" + $scope.email + "&" + "pass=" + $scope.password, user, ConfigToken()).then((res)=>{
			var data = res.data;
			console.log(data)
			if (data == true)
			{
				window.location = "http://localhost:5000/dashboard"
			}
		})
	}
})