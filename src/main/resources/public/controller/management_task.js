var app = angular.module("mainApp", ["ngRoute"]);
app.controller("mainCtrl", ($scope, $location, $http)=>{
	$http.defaults.headers.post["Content-Type"] = "application/json";

	$scope.tasks;
	updateTasks();

	$scope.accept = (userId)=>{
		$http.delete("/task?userId=" + userId + "&isAccept=true").then((res)=>{
			if (res.data == true){
				alert("Success!");
				updateTasks();
			} else {
				alert("Fail!");
			}
		});
	}
	$scope.deny = (userId)=>{
		$http.delete("/task?userId=" + userId + "&isAccept=false").then((res)=>{
			if (res.data == true){
				alert("Success!");
				updateTasks();
			} else {
				alert("Fail!");
			}
		});
	}
	$scope.logout = ()=>{
		$http.post("/logout").then((res)=>{});
	}
	function updateTasks(){
		$http.get("/task").then((res)=>{
			$scope.tasks = res.data;
		});
	}
})