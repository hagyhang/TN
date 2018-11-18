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
	$scope.bins;
	$scope.bin = {
		lon: null,
		lat: null,
		status: null,
		id: null
	};
	$scope.boxStyle;
	let lon, lat, map, marker, isMark = false, isAddMode = true;
	// let baseUrl = 'http://localhost:5000';
	let baseUrl = 'https://hagyhang.herokuapp.com';
	let greedIcon = baseUrl + '/images/green_bin_26x26.png';
	$http.defaults.headers.post["Content-Type"] = "application/x-www-form-urlencoded";
	let token = ConfigToken();
	if (token != null){
		window.location = baseUrl + "/management"
	}
	loadBin()
	$scope.deleteBin = (id)=>{
		var r = confirm("Confirm delete this item!");
		if (r == true) {
			$http.delete(baseUrl + "/bins?" + "id=" + id, ConfigToken()).then((res)=>{
				loadBin();
			})
		}
	}
	$scope.updateBin = (bin)=>{
		isAddMode = false;
		$scope.bin.lon = bin.lon;
		$scope.bin.lat = bin.lat;
		$scope.bin.status = bin.status;
		$scope.bin.id = bin.id;
		let center = {lat: bin.lat, lng: bin.lon};
		map = new google.maps.Map(document.getElementById('map'), {
		  center: center,
		  zoom: 17
		});
		marker = new google.maps.Marker({
		  	position: center,
		  	map: map,
		  	icon : greedIcon,
		  	title: 'Bin'
		})
		map.addListener('rightclick', function(event) {
			lon = event.latLng.lng();
			lat = event.latLng.lat();
		});
		console.log($scope.bin.status)
	}
	$scope.addBin = ()=>{
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
		  	title: 'Bin'
		})
		map.addListener('rightclick', function(event) {
			lon = event.latLng.lng();
			lat = event.latLng.lat();
		});
	}
	$scope.addBinOK = ()=>{
		if (isAddMode){
			if (isMark){
				$http.post(baseUrl + "/bins?lon=" + $scope.bin.lon + "&lat=" + $scope.bin.lat + "&status=" + $scope.bin.status, ConfigToken()).then((res)=>{
					console.log(res.data)
					loadBin()
				})
			} else {
				alert("Add fail, missing location!")
			}
		} else {
		    $http.put(baseUrl + "/bins?lon=" + $scope.bin.lon + "&lat=" + $scope.bin.lat + "&status=" + $scope.bin.status + "&id=" + $scope.bin.id, ConfigToken()).then((res)=>{
				console.log($scope.bin.id)
				console.log(res.data)
				loadBin()
			})
		}
	}
	$scope.rightClick = (e)=>{	
		$scope.boxStyle  = {
			visibility: "visible",
			left: e.x + "px",
			top: e.y + "px"
		};
	}
	$scope.bodyClick = ()=>{
		$scope.boxStyle  = {
			visibility: "collapse"
		}
	}
	$scope.boxClick = ()=>{
		$scope.bin.lon = lon;
		$scope.bin.lat = lat;
		$scope.boxStyle  = {
			visibility: "collapse"
		}
		marker.setMap(null);
		marker = new google.maps.Marker({
		  	position: {lat: lat, lng: lon},
		  	map: map,
		  	icon : greedIcon,
		  	title: 'Bin'
		})
		marker.setMap(map);
		isMark = true;
	}
	$scope.closePopup = ()=>{
		$scope.bin = {
			lon: null,
			lat: null,
			status: null,
			id: null
		};
	}
	function loadBin(){
		$http.get("baseUrl + /bins", ConfigToken()).then((res)=>{
			$scope.bins = res.data;
			console.log($scope.bins)
		});
	}
})