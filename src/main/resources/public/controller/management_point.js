var app = angular.module("mainApp", ["ngRoute"]);
function ConfigToken(){
	if(window.localStorage.getItem("FOOD_TOKEN") == null) return null;
	return {
		headers : {
			Token: window.localStorage.getItem("FOOD_TOKEN")
		}
	}
}
app.directive('ngRightClick', function($parse) {
    return function(scope, element, attrs) {
        var fn = $parse(attrs.ngRightClick);
        element.bind('contextmenu', function(event) {
            scope.$apply(function() {
                event.preventDefault();
                fn(scope, {$event:event});
            });
        });
    };
});
app.controller("mainCtrl", ($scope, $location, $http)=>{
	$scope.dashboardHref = "/dashboasrd";
	$scope.points;
	$scope.point = {
		lon: null,
		lat: null,
		id: null
	};
	$scope.boxStyle;
	let lon, lat, map, marker, isMark = false, isAddMode = true;
	let baseUrl = 'http://localhost:5000';
	// let baseUrl = 'https://hagyhang.herokuapp.com';
	let greedIcon = '/images/endpoint_56x40.png';
	$http.defaults.headers.post["Content-Type"] = "application/x-www-form-urlencoded";
	let token = ConfigToken();
	if (token != null){
		window.location = "/management"
	}
	loadPoint()
	$scope.delete = (id)=>{
		var r = confirm("Confirm delete this item!");
		if (r == true) {
			$http.delete("/point?" + "id=" + id, ConfigToken()).then((res)=>{
				loadPoint();
			})
		}
	}
	$scope.update= (point)=>{
		isAddMode = false;
		$scope.point.id = point.id;
		$scope.point.lon = point.lon;
		$scope.point.lat = point.lat;
		$scope.point.name = point.name;
		let center = {lat: point.lat, lng: point.lon};
		map = new google.maps.Map(document.getElementById('map'), {
		  center: center,
		  zoom: 17
		});
		marker = new google.maps.Marker({
		  	position: center,
		  	map: map,
		  	icon : greedIcon,
		  	title: 'point'
		})
		map.addListener('rightclick', function(event) {
			lon = event.latLng.lng();
			lat = event.latLng.lat();
		});
	}
	$scope.add = ()=>{
		isAddMode = true;
		let center = {lat: 10.794103, lng: 106.6979763}
		map = new google.maps.Map(document.getElementById('map'), {
		  center: center,
		  zoom: 17
		});
		marker = new google.maps.Marker({
		  	position: center,
		  	map: null,
		  	icon : greedIcon,
		  	title: 'point'
		})
		map.addListener('rightclick', function(event) {
			lon = event.latLng.lng();
			lat = event.latLng.lat();
		});
	}
	$scope.OK = ()=>{
		if (isAddMode){
			if (isMark){
				$http.post("/point?lon=" + $scope.point.lon + "&lat=" + $scope.point.lat + "&name=" + $scope.point.name, ConfigToken()).then((res)=>{
					console.log(res.data)
					loadPoint()
				})
			} else {
				alert("Add fail, missing location!")
			}
		} else {
		    $http.put("/point?lon=" + $scope.point.lon + "&lat=" + $scope.point.lat  + "&id=" + $scope.point.id + "&name=" + $scope.point.name, ConfigToken()).then((res)=>{
				console.log($scope.point.id)
				console.log(res.data)
				loadPoint()
			})
		}
	}
	$scope.rightClick = (e)=>{	
		$scope.boxStyle  = {
			visibility: "visible",
			left: e.pageX + "px",
			top: e.pageY + "px"
		};
	}
	$scope.bodyClick = ()=>{
		$scope.boxStyle  = {
			visibility: "collapse"
		}
	}
	$scope.boxClick = ()=>{
		$scope.point.lon = lon;
		$scope.point.lat = lat;
		$scope.boxStyle  = {
			visibility: "collapse"
		}
		marker.setMap(null);
		marker = new google.maps.Marker({
		  	position: {lat: lat, lng: lon},
		  	map: map,
		  	icon : greedIcon,
		  	title: 'point'
		})
		marker.setMap(map);
		isMark = true;
	}
	$scope.closePopup = ()=>{
		$scope.point = {
			lon: null,
			lat: null,
			id: null
		};
	}
	function loadPoint(){
		$http.get("/point", ConfigToken()).then((res)=>{
			$scope.points = res.data;
			console.log($scope.points)
		});
	}
	$scope.logout = ()=>{
		$http.post("/logout").then((res)=>{});
	}
})