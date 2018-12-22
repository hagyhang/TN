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
	$http.defaults.headers.post["Content-Type"] = "application/json";
	let token = ConfigToken();
	if (token != null){
		window.location = "http://localhost:5000/management"
	}
	let baseUrl = 'http://localhost:5000';
	var chart = Highcharts.chart('chart', {
		chart: {
		    animation: false
		},
	    title: {
	        text: 'Bin Map Status'
	    },
	    subtitle: {
	        text: 'Source: hagyhang'
	    },
	    yAxis: {
	    	min: 0,
	    	tickInterval: 1,
	        title: {
	            text: 'Number of Full Bin'
	        }
	    },
	    xAxis: {
	    	tickPositions: [0, 10, 60, 119],
	    	type: "category"
	    },
	    
	    legend: {
	        layout: 'vertical',
	        align: 'right',
	        verticalAlign: 'middle'
	    },
	    series: [{
	        name: 'Full Bins',
	        data: []
	    }],
	    plotOptions: {
	        series: {
	            allowPointSelect: true
	        }
	    },
	    responsive: {
	        rules: [{
	            condition: {
	                maxWidth: 500
	            },
	            chartOptions: {
	                legend: {
	                    layout: 'horizontal',
	                    align: 'center',
	                    verticalAlign: 'bottom'
	                }
	            }
	        }]
	    }
	});
	var chartScore = Highcharts.chart('chartScore', {
		chart: {
		    animation: false,
		    type: 'column'
		},
	    title: {
	        text: 'Employee Score'
	    },
	    subtitle: {
	        text: 'Source: hagyhang'
	    },
	    yAxis: {
	    	min: 0,
	    	tickInterval: 1,
	        title: {
	            text: 'Score'
	        }
	    },
	    xAxis: {
	    	// tickPositions: [],
	    	type: "category"
	    	// crosshair: true
	    },
	    
	    legend: {
	        layout: 'vertical',
	        align: 'right',
	        verticalAlign: 'middle'
	    },
	    series: [{
	        name: 'Score',
	        data: []
	    }],
	    plotOptions: {
	        column: {
	            pointPadding: 0.2,
	            borderWidth: 0
	        }
	    },
	    responsive: {
	        rules: [{
	            condition: {
	                maxWidth: 500
	            },
	            chartOptions: {
	                legend: {
	                    layout: 'horizontal',
	                    align: 'center',
	                    verticalAlign: 'bottom'
	                }
	            }
	        }]
	    }
	});
	setInterval(updateChart, 1000);
	setInterval(updateChartScore, 1000);
	
	function updateChart(){
		$http.get("/chart_points").then((res)=>{
			chart.series[0].setData(res.data.data)
			// console.log(res.data.data);
		});
	}
	function updateChartScore(){
		$http.get("/chart_score").then((res)=>{
			chartScore.series[0].setData(res.data)
			console.log(res.data);
		});
	}
	$scope.logout = ()=>{
		$http.post("/logout").then((res)=>{});
	}
})