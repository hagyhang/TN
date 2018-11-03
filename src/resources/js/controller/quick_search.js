var app = angular.module('myApp', []);
app.controller('myCtrl', function($scope, $http) {
	$scope.isContentShow = "hidden";
	$scope.listSearch=[];
	$scope.ids=[];
	$scope.count = 0;
	$scope.pages = [];
	$scope.text;
	$scope.itemSelected = (s)=>{
		alert(s);
		$scope.text = s;
	}
    $scope.Find = ()=>{
    	$scope.pages = [];
    	let obj = {ids : $scope.ids};
    	console.log(obj);
    	$http.post("http://localhost:8080/quick-search?", obj)
	    .then(function(response) {
	    	let pages = response.data;
	    	console.log(pages);
	    	pages.forEach((p)=>{
	    		p.description = p.description.substring(0, 200) + "...";
	    	})
	    	$scope.pages = pages;
	    });	
    };
    $scope.textChange = ()=>{
    	let encodeText = encodeURI($scope.text)
    	// console.log("encode text: " + encodeText);
    	$http.get("http://localhost:9200/index/titles/_search?q=" + encodeText)
	    .then(function(response) {
	    	let data = response.data;
	    	if (data.timed_out == true || data.hits.total == 0){
	    		console.log("not found!");
	    		$scope.isContentShow = "hidden";
	    		// $http.put("http://localhost:8080/quick-search?key=" + encodeText)
			    // .then(function(response) {
			    // 	let obj = {title: $scope.text, isSearch: true};
			    // 	$http.post("http://localhost:9200/index/titles", obj)
			    // 	.then((res)=>{
			    // 		console.log("isSearch change");
			    // 	})
			    // });	
	    	}
	    	else{
	    		$scope.listSearch=[];
	    		$scope.ids=[];
	    		let tempList = data.hits.hits;
	    		console.log(tempList);
	    		tempList.forEach((h)=>{
	    			if (h._source.isSearch != true){
	    				let temp = h._source.title;
		    			console.log(temp);
		    			if (temp.length > 40) temp = temp.substring(0, 40) + "...";
		    			if ($scope.title !== "trash"){
		    				$scope.listSearch.push(temp);
		    				$scope.ids.push(h._source.pageID);
		    			}
	    			}
	    		});
	    		// $scope.listSearch.forEach((s)=>{
	    		// 	console.log(s);
	    		// });
	    		$scope.isContentShow = "visible";
	    	}
	    });
    }
    $scope.focusOut = ()=>{
    	$scope.isContentShow = "hidden";
    }
});	