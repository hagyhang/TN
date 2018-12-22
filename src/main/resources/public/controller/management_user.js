var app = angular.module("mainApp", ["ngRoute"]);
function ConfigToken(){
	if(window.localStorage.getItem("FOOD_TOKEN") == null) return null;
	return {
		headers : {
			Token: window.localStorage.getItem("FOOD_TOKEN")
		}
	}
}
app.controller("mainCtrl", ($scope, $location, $http)=>{
	$scope.users;
	$scope.user = {
		id: null,
		name: null,
		pass: null,
		type: null,
		email: null,
		address: null
	};
	let baseUrl = 'http://localhost:5000';
	// let baseUrl = 'https://hagyhang.herokuapp.com';
	let greedIcon = '/images/green_bin_26x26.png';
	$http.defaults.headers.post["Content-Type"] = "application/x-www-form-urlencoded";
	let token = ConfigToken();
	if (token != null){
		window.location = "/management"
	}
	load()
	$scope.delete = (id)=>{
		var r = confirm("Confirm delete this item!");
		if (r == true) {
			$http.delete("/users?" + "id=" + id, ConfigToken()).then((res)=>{
				load();
			})
		}
	}
	$scope.update = (user)=>{
		isAddMode = false;
		$scope.user.name = user.name;
		$scope.user.email = user.email;
		$scope.user.type = user.type;
		$scope.user.id = user.id;
		$scope.user.address = user.address;
		$scope.user.pass = user.pass;
	}
	$scope.add = ()=>{
		isAddMode = true;
	}
	$scope.OK = ()=>{
		$http.post( "/users?id=" + $scope.user.id + "&pass=" + $scope.user.pass + "&name=" + $scope.user.name + "&address=" + $scope.user.address + "&type=" + $scope.user.type + "&email=" + $scope.user.email, ConfigToken()).then((res)=>{
			if (res.data == true){
				alert("Success!");
				load();
			} else{
				alert("User already existed!!")
			}
		})
	}
	$scope.closePopup = ()=>{
		$scope.user = {
			id: null,
			name: null,
			pass: null,
			type: null,
			email: null,
			address: null
		};
	}
	function load(){
		$http.get("/users", ConfigToken()).then((res)=>{
			$scope.users = res.data;
			console.log($scope.users)
		});
	}
	$scope.logout = ()=>{
		$http.post("/logout").then((res)=>{});
	}
})