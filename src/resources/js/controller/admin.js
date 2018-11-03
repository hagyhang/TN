var app = angular.module('myApp', []);
app.controller('myCtrl', function($scope, $http) {
	$scope.users=[];
	$scope.pages=[];

	$scope.signOut = ()=>{
		delete $cookies["token"]
	}
	$scope.deletePage = ($event)=>{
		let tag = $event.srcElement.attributes['tag'].value;
		console.log(tag);
		$http.delete("http://localhost:9200/index/titles/" + tag)
	    .then(function(response) {
	    });
		$http.delete("http://localhost:8080/page/delete?id=" + tag)
		.then(function(response) {
			refresh();
		});
	}
	$scope.addLinks = ()=>{
		let str = $scope.inputLinks;
		if (str != ""){
			$http.post("http://localhost:8080/page/add?links=" + str)
			.then(function(response) {
				// console.log(response.data);
			});
		}
	}
	$scope.refresh = ()=>{
		refresh();
	}
	refresh();
	
	function refresh(){
		$http.get("http://localhost:8080/user/users")
		.then(function(response) {
			let result = response.data;
			console.log(result);
			$scope.users = result;
		});
		$http.get("http://localhost:8080/page/pages")
		.then(function(response) {
			let result = response.data;
			console.log(result);
			$scope.pages = result;
		});
	}
	var webSocket = new WebSocket("ws://localhost:8080/ws");
    webSocket.onopen = function() {
      	webSocket.send("onlineUser.name");
	};
	  
	webSocket.onerror = function(err) {
	  	//alert("Error: " + err);
	};
	webSocket.onmessage = function (message) {
		alert("Message: " + message);
	};
	webSocket.onclose = function(message) {
		var webSocket = new WebSocket("ws://localhost:8080/Chat/sendMessage");
	};
});