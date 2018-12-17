var app = angular.module("mainApp", ["ngRoute"]);
var currentUser;

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
app.controller("dashCtrl", ($scope, $http)=>{
	$scope.points;
	$scope.bins=[];
	$scope.bin;
	$scope.index="";
	$scope.listId=".";
	$scope.boxStyle;
	let map, markers=[], endPoints=[], waypointsIndex=[];
	let start, lon, lat, marker, isStartSet = false, isEndSet = false, listId=[], endPointId=-1;
	let maxX = -1000, maxY = -1000, minX = 1000, minY = 1000;
	let baseUrl = 'http://localhost:5000';
	// let baseUrl = 'https://hagyhang.herokuapp.com';
	let greedIcon = '/images/green_bin_26x26.png';
	let redIcon = '/images/red_bin_26x26.png';
	let greedIconChoosed = '/images/green_bin_choosed_26x26.png';
	let redIconChoosed = '/images/red_bin_choosed_26x26.png';
	let startIcon = '/images/start_26x26.jpg';
	let endIcon = '/images/endpoint_56x40.png';
	let endIconChoosed = '/images/endpoint_choosed_56x40.png';
	setTimeout(function(){
		let center = {lat: 10.794103, lng: 106.6979763}
		map = new google.maps.Map(document.getElementById('map'), {
		  center: center,
		  zoom: 15
		});
		map.addListener('rightclick', function(event) {
			lon = event.latLng.lng();
			lat = event.latLng.lat();
		});
		marker = new google.maps.Marker({
		  	position: center,
		  	map: null,
		  	icon : startIcon,
		  	title: 'Start Point'
		})
		$http.get("/path").then((res)=>{
			$scope.bins = res.data.bins;
			$scope.points = res.data.points;
			for (let i=0; i<$scope.bins .length; i++){
				let bin = $scope.bins [i];				
				let marker = new google.maps.Marker({
			      position: {lat: bin.lat, lng: bin.lon},
			      map: map,
			      icon : greedIcon,
			      title: i + ""
			    })
			    if(bin.status != true){
			    	marker.icon = redIcon;
			    	let point = new google.maps.LatLng(bin.lat, bin.lon);
			    	// console.log ("lon: " + bin.lon + " - lat: " + bin.lat);
				    // wayPoints.push({
				    //     location: point,
				    //     stopover: true
				    // });
				    waypointsIndex.push(i);
			    }
			    markers.push(marker);
			    maxX = maxX > bin.lat ? maxX : bin.lat;
			    maxY = maxY > bin.lon ? maxY : bin.lon;
			    minX = minX < bin.lat ? minX : bin.lat;
			    minY = minY < bin.lon ? minY : bin.lon;
			    
			    marker.addListener('click', function() {
			    	markerIndex = this.title;
			    	let isAdd = true;
			    	for (let i=0; i<listId.length; i++){
			    		if (listId[i] == markerIndex){
			    			isAdd = false;
			    			listId.splice(i, 1);
			    			if ($scope.bins[markerIndex].status == true)
			    				this.setIcon(greedIcon);
			    			else 
			    				this.setIcon(redIcon);
			    			break;
			    		}
			    	}
			    	if (isAdd == true){
			    		if ($scope.bins[markerIndex].status == true)
			    			this.setIcon(greedIconChoosed);
			    		else 
			    			this.setIcon(redIconChoosed);
						listId.push(markerIndex);
			    	}
					updateListId();
				});
			}
			for (let i=0; i<$scope.points.length; i++){
				let p = $scope.points[i];				
				let marker = new google.maps.Marker({
			      position: {lat: p.lat, lng: p.lon},
			      map: map,
			      icon : endIcon,
			      title: p.name,
			      index : i
			    });
			    marker.addListener('click', function() {
			    	markerIndex = this.index;
			    	if (markerIndex == endPointId){
			    		this.setIcon(endIcon);
			    		endPointId = -1;
			    	} else {
			    		this.setIcon(endIconChoosed);
			    		if (endPointId != -1)
			    			endPoints[endPointId].setIcon(endIcon);
			    		endPointId = markerIndex;
			    	}
				});
			    endPoints.push(marker);
			    maxX = maxX > p.lat ? maxX : p.lat;
			    maxY = maxY > p.lon ? maxY : p.lon;
			    minX = minX < p.lat ? minX : p.lat;
			    minY = minY < p.lon ? minY : p.lon;
			}
			center = new google.maps.LatLng((maxX + minX)/2, (maxY + minY)/2);
		    map.panTo(center);
			// console.log(wayPoints);
		})
	 	// console.log(markers);
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
	function updateListId(){
		let str = "";
		for (let i=0; i<listId.length; i++){
			if (i==0)
				str += $scope.bins[listId[i]].id;
			else 
				str += "; " + $scope.bins[listId[i]].id;
		}
		$scope.listId = str;
	}
	$scope.allFull = ()=>{
		let list = waypointsIndex.slice();
		listId = list;
		for (let i=0; i<$scope.bins.length; i++){
			if ($scope.bins[i].status == true)
				markers[i].setIcon(greedIcon);
			else 
				markers[i].setIcon(redIcon);
		}
		for (let i=0; i<listId.length; i++){
			if ($scope.bins[listId[i]].status == true)
				markers[listId[i]].setIcon(greedIconChoosed);
			else 
				markers[listId[i]].setIcon(redIconChoosed);
		}
		updateListId();
	}
	// $scope.pointSelected = ()=>{
	// 	console.log($scope.index);
	// 	if ($scope.index != ""){
	// 		point = $scope.points[$scope.index];
	// 		end = new google.maps.LatLng(point.lat, point.lon);
	// 		isEndSet = true;
	// 	}	
	// }
	let directionsDisplay;
	$scope.findPath = ()=>{
		if (listId.length==0 || !isStartSet || endPointId == -1){
			alert("Choose Waypoints, Start Point and End Point by click on map!")
			return;
		}
		let wayPoints=[];
		for (let i=0; i<listId.length; i++){
			let bin = $scope.bins[listId[i]];
			let point = new google.maps.LatLng(bin.lat, bin.lon);
		    wayPoints.push({
		        location: point,
		        stopover: true
		    });
		}
		let temp = $scope.points[endPointId];
		let end = new google.maps.LatLng(temp.lat, temp.lon);

		if (directionsDisplay != null){
			directionsDisplay.setMap(null);
		}
		let directionsService = new google.maps.DirectionsService;
        directionsDisplay = new google.maps.DirectionsRenderer;
        // console.log(wayPoints);
        directionsDisplay.setMap(map); 
        directionsService.route({
          origin: start,
          destination: end,
          waypoints: wayPoints,
          optimizeWaypoints: true,
          avoidHighways: true,
          travelMode: 'WALKING'
        }, function(response, status) {
          if (status === 'OK') {
            directionsDisplay.setDirections(response);
            // console.log(response);
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
	$scope.sendMail = ()=>{
		if (listId.length==0 || !isStartSet || endPointId == -1){
			alert("Choose Waypoints, Start Point and End Point by click on map!")
			return;
		}
		let data = [];
		for (let i=0; i<listId.length; i++){
			let bin = $scope.bins[listId[i]];
			data.push(bin.id);
		}
		$http.post("/path", data).then((res)=>{

		});
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
		$scope.boxStyle  = {
			visibility: "collapse"
		}
		marker.setMap(null);
		marker = new google.maps.Marker({
		  	position: {lat: lat, lng: lon},
		  	map: map,
		  	icon : startIcon,
		  	title: 'point'
		})
		marker.setMap(map);
		start = new google.maps.LatLng(lat, lon);
		isStartSet = true;
	}
})