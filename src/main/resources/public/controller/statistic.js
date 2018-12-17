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
	        data: [{ 
	        	y: 43934,
	        	name: "1h"
	        },
	        { 
	        	y: 57177,
	        	name: "2h"
	        },
	        { 
	        	y: 69658,
	        	name: "3h"
	        }]
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
	setInterval(updateChart, 1000);
	
	function updateChart(){
		console.log("???");
		$http.get(baseUrl + "/chart_points").then((res)=>{
			chart.series[0].setData(res.data.data)
			console.log(res.data.data);
		});
	}
})