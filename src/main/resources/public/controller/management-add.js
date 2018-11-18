var app = angular.module("mainApp", ["ngRoute"]);
function ConfigToken(){
	if(window.localStorage.getItem("FOOD_TOKEN") == null) return null;
	return {
		headers : {
			Token: window.localStorage.getItem("FOOD_TOKEN")
		}
	}
}
function initMap(){
	let center = {lat: 10.794103, lng: 106.6979763}
	let map = new google.maps.Map(document.getElementById('map'), {
	  center: center,
	  zoom: 17
	});
}
app.controller("mainCtrl", ($scope, $location, $http)=>{
	$scope.dashboardHref = "/dashboasrd";
	$scope.bins;
	$http.defaults.headers.post["Content-Type"] = "application/x-www-form-urlencoded";
	let token = ConfigToken();
	if (token != null){
		window.location = "http://localhost:5000/management"
	}
	$http.get("http://localhost:5000/bins", ConfigToken()).then((res)=>{
		$scope.bins = res.data;
		console.log($scope.bins)
	})
	$scope.deleteBin = (id)=>{
		id = 123;
		alert(id);

		$http.delete("http://localhost:5000/bins?" + "id=" + id, ConfigToken()).then((res)=>{
			$http.get("http://localhost:5000/bins", ConfigToken()).then((res)=>{
				$scope.bins = res.data;
				console.log($scope.bins)
			})
		})
	}
	$scope.addBin = ()=>{
		
	}
})