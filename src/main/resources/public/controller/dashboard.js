var app = angular.module("mainApp", ["ngRoute"]);
var currentUser;
var backLocation="/";
// app.config(($routeProvider)=>{
// 	$routeProvider
// 	.when("/", {
// 		templateUrl : "login.html"
// 	})
// 	.when("/sign-up", {
// 		templateUrl : "sign-up.html"
// 	})
// 	// .when("/dashboard", {
// 	// 	templateUrl : "dashboard.html"
// 	// })
// 	// .when("/management", {
// 	// 	templateUrl : "management.html"
// 	// })
// 	// .when("/statistic", {
// 	// 	templateUrl : "statistic.html"
// 	// })
// 	.otherwise({
// 		redirectTo: "/"
// 	});
// });


function ConfigToken(){
	if(window.localStorage.getItem("FOOD_TOKEN") == null) return null;
	return {
		headers : {
			Token: window.localStorage.getItem("FOOD_TOKEN")
		}
	}
}
function initMap() {

}
app.controller("dashCtrl", ($scope, $http)=>{
	let map, markers, wayPoints = [], start, end;
	// let baseUrl = 'http://localhost:5000';
	let baseUrl = 'https://hagyhang.herokuapp.com';
	let greedIcon = baseUrl + '/images/green_bin_26x26.png';
	let redIcon = baseUrl + '/images/red_bin_26x26.png';
	setTimeout(function(){
		let center = {lat: 10.794103, lng: 106.6979763}
		markers=[];
		wayPoints=[];
		map = new google.maps.Map(document.getElementById('map'), {
		  center: center,
		  zoom: 17
		});
		$http.get(baseUrl + "/bins").then((res)=>{
			let bins = res.data;
			start = new google.maps.LatLng(bins[0].lat, bins[0].lon);
			end = new google.maps.LatLng(bins[bins.length-1].lat, bins[bins.length-1].lon);
			for (let i=0; i<bins.length; i++){
				let bin = bins[i];				
				let marker = new google.maps.Marker({
			      position: {lat: bin.lat, lng: bin.lon},
			      map: map,
			      icon : greedIcon,
			      title: 'Bin'
			    })
			    if(bin.status != true)
			    	marker.icon = redIcon;
			    markers.push(marker);
			    let point = new google.maps.LatLng(bin.lat, bin.lon);
			    wayPoints.push({
			        location: point,
			        stopover: true
			    });
			}
			console.log("???/");
			console.log(wayPoints);
		})
	 	
	  //   setInterval(function(){
	  //   	$http.get("http://localhost:5000/bins").then((res)=>{
			// 	let bins = res.data;
			// 	// console.log(bins);
			// 	for (let i=0; i<bins.length; i++){
			// 		let bin = bins[i];
			// 		if(bin.status != true)
			// 	    	markers[i].icon = redIcon;
			// 	    else
			// 	    	markers[i].icon = greedIcon;
			// 	    markers[i].icon = greedIcon;

			// 	}
			// 	for (var i=0; i<markers.length; i++) {
			//         markers[i].setMap(map);
			//     }
			// 	// console.log(markers);
			// })
	  //   }, 2000);
	}, 1000);
	$scope.findPath = ()=>{
		let directionsService = new google.maps.DirectionsService;
        let directionsDisplay = new google.maps.DirectionsRenderer;
        console.log(wayPoints);
        directionsDisplay.setMap(map); 
        directionsService.route({
          origin: start,
          destination: end,
          waypoints: wayPoints,
          optimizeWaypoints: true,
          travelMode: 'DRIVING'
        }, function(response, status) {
          if (status === 'OK') {
            directionsDisplay.setDirections(response);
            var route = response.routes[0];
            var summaryPanel = document.getElementById('directions-panel');
            summaryPanel.innerHTML = '';
            // For each route, display summary information.
            for (var i = 0; i < route.legs.length; i++) {
              var routeSegment = i + 1;
              summaryPanel.innerHTML += '<b>Route Segment: ' + routeSegment +
                  '</b><br>';
              summaryPanel.innerHTML += route.legs[i].start_address + ' to ';
              summaryPanel.innerHTML += route.legs[i].end_address + '<br>';
              summaryPanel.innerHTML += route.legs[i].distance.text + '<br><br>';
            }
          } else {
            window.alert('Directions request failed due to ' + status);
          }
        });
	}
})

// app.controller("signUpCtrl", ($scope, $location, $http)=>{
// 	$scope.submit = ()=>{
// 		var user={
// 			email : $scope.email,
// 			password : $scope.password,
// 			fullName : $scope.fullName
// 		};
		
// 		$http.post("https://cookbook-server.herokuapp.com/users/register", user).then((res)=>{
// 			var data = res.data;
// 			if (data == true)
// 			{
// 				console.log("Tạo thành công!");
// 				$location.path('/');
// 			}
// 		})
// 	}
// })

// app.controller("foodCtrl", ($scope, $location, $http)=>{
// 	$scope.currentPath = $location.$$path;
// 	$scope.isDel = "collapseDel";
// 	$http.get("https://cookbook-server.herokuapp.com" + $scope.currentPath + "/detail", ConfigToken()).then((res)=>{
// 		var data = res.data;
// 		if (data.success == true){
// 			$scope.food = data.results;
// 			if ($scope.food.createBy == currentUser){
// 				$scope.isDel = "visibleDel";
// 			}
// 		}
// 		else {
// 			$location.path("/");
// 		}
// 	})
// 	$scope.Logout = ()=>{
// 		window.localStorage.clear();
// 		$location.path("/");
// 	}
// 	$scope.AddFood = ()=>{
// 		$location.path("/add-food");
// 		backLocation = $scope.currentPath;
// 	}
// 	$scope.DelFood = ()=>{
// 		if (confirm("Bạn có chắc xóa bài viết này?")) {
// 			$http.post("https://cookbook-server.herokuapp.com" + $scope.currentPath + "/delete",{}, ConfigToken()).then((res)=>{
// 				var data = res.data;
// 				if (data.success == true){
// 					$scope.food = data.results;
// 					console.log(data);
// 					alert("Đã xóa");
// 					$location.path("/");
// 				}
// 			})
// 	    } 
// 	}
// })

// app.controller("addFoods", ($scope, $location, $http) => {
// 	$scope.Back = ()=>{
// 		$location.path(backLocation);
// 	}
// 	$scope.Logout = ()=>{
// 		window.localStorage.clear();
// 		$location.path("/");
// 	}
// 	$scope.uploadSubImage = function(e){
// 		var i = e.attributes['tag'].value;
// 		var f = document.getElementById('file'+i).files[0];
// 		var fd = new FormData();
// 		fd.append("file", f);
// 		// console.log("file" + f);
// 		$http({
// 	        url: "https://cookbook-server.herokuapp.com/image/",
// 	        method: 'POST',
// 	        data: fd,
// 	        headers: { 'Content-Type': undefined, Token: window.localStorage.getItem("FOOD_TOKEN")},
// 	    }).then((res)=>{
// 			var data = res.data;
// 			if (data.success == true)
// 			{
// 				let path = "https://cookbook-server.herokuapp.com/" + data.results.path;
// 				var img = angular.element(document.querySelector('#img'+i));
// 				img.attr('src', path);
// 				$scope.food.content[i].arrImage.push({image: path});
// 	        	console.log(angular.element(e));
// 	        	angular.element(e).val(null);
// 			}
// 		});
//     };
// 	$scope.uploadImage = function() {
// 	    var f = document.getElementById('file').files[0];
// 	    // console.log(f);
//         var fd = new FormData();
// 			fd.append("file", f);

// 		$http({
// 	        url: "https://cookbook-server.herokuapp.com/image/",
// 	        method: 'POST',
// 	        data: fd,
// 	        headers: { 'Content-Type': undefined, Token: window.localStorage.getItem("FOOD_TOKEN")},
// 	    }).then((res)=>{
// 			var data = res.data;
// 			if (data.success == true)
// 			{
// 				let path = "https://cookbook-server.herokuapp.com/" + data.results.path;
// 				$scope.food.image = path;
// 				var i = angular.element(document.querySelector('#img'));
// 				i.attr('src', path);
// 				i.attr('class', 'food-image center-block');
// 				console.log(path);
// 			}
// 		});
// 	}

// 	$scope.add = function(){
// 		var materials = $scope.materials.split(";")
// 		if ($scope.materials != "" && $scope.food.name != "" && $scope.food.decriptions != "" && $scope.food.content.length > 0)
// 		{
// 			materials.forEach((m)=>{
// 				var ss = m.split(":");
// 				var temp = {
// 					material: ss[0],
// 					amount: ss[1]
// 				}
// 				$scope.food.materials.push(temp);
// 				console.log($scope.food.name);
// 				// console.log($scope.food.materials); //amazing !!!!!
// 			})
// 			var selector = document.getElementById("selector");
// 			$scope.food.categoryId = selector.options[ selector.selectedIndex ].value;
// 			console.log(selector.options[selector.selectedIndex ].value);
// 			console.log($scope.food);
// 			$http.post("https://cookbook-server.herokuapp.com/food/" + $scope.food.categoryId + "/create", $scope.food, ConfigToken()).then((res)=>{
// 				var data = res.data;
// 				if (data.success == true){
// 					console.log(data.results);
// 					alert("Thêm thành công!");
// 					$location.path('/dashboard');
// 				}
// 			})
// 		}
// 		else alert ("Bạn phải nhập đủ thông tin có dấu *");
// 	}
// 	$scope.delStep = function(i){
// 		console.log($scope.food.content);
// 		$scope.food.content.splice(i, 1);
// 	}
// 	$scope.addStep = function(i){
// 		var emptyStep = {
// 			"step": "",
// 			"arrImage": []
// 		}
// 		$scope.food.content.push(emptyStep);
// 		console.log($scope.food.content);
// 	}
// 	$http.get("https://cookbook-server.herokuapp.com/category", ConfigToken()).then((res)=>{
// 		var data = res.data;
// 		if (data.success == true){
// 			$scope.categories = data.results;
// 		}
// 	})
// 	$scope.food={};
// 	$scope.food.favourite=[];//
// 	$scope.food.name="";
// 	$scope.food.categoryId=[];//
// 	$scope.food.materials=[];
// 	$scope.materials="";
// 	$scope.food.content = [];
// })

// app.directive('customOnChange', function() {
//   return {
//     restrict: 'A',
//     link: function (scope, element, attrs) {
//       var onChangeFunc = scope.$eval(attrs.customOnChange);
//       element.bind('change', onChangeFunc);
//     }
//   };
// });
