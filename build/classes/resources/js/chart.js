/*-- options merchant --*/
var options = {
    animationEnabled: true,
    theme: "light2",
    title:{
        text: ""
    },
    axisX:{
        valueFormatString: "DD-MM-YYYY"
    },
    axisY: {
        title: "Tiền (VNĐ)",
        suffix: " Triệu",
        minimum: 30
    },
    toolTip:{
        shared:true
    },  
    legend:{
        cursor:"pointer",
        verticalAlign: "bottom",
        horizontalAlign: "left",
        dockInsidePlotArea: true,
        itemclick: toogleDataSeries
    },
    data: [{
        type: "line",
        showInLegend: true,
        name: "Doanh Thu",
        markerType: "square",
        xValueFormatString: "DD MMM, YYYY",
        color: "#F08080",
        yValueFormatString: "#,##0K",
        dataPoints: [
            { x: new Date(2017, 10, 1), y: 63 },
            { x: new Date(2017, 10, 2), y: 69 },
            { x: new Date(2017, 10, 3), y: 65 },
            { x: new Date(2017, 10, 4), y: 70 },
            { x: new Date(2017, 10, 5), y: 71 },
            { x: new Date(2017, 10, 6), y: 65 },
            { x: new Date(2017, 10, 7), y: 73 },
            { x: new Date(2017, 10, 8), y: 96 },
            { x: new Date(2017, 10, 9), y: 84 },
            { x: new Date(2017, 10, 10), y: 85 },
            { x: new Date(2017, 10, 11), y: 86 },
            { x: new Date(2017, 10, 12), y: 94 },
            { x: new Date(2017, 10, 13), y: 97 },
            { x: new Date(2017, 10, 14), y: 86 },
            { x: new Date(2017, 10, 15), y: 89 }
        ]
    }]
};
var options2 = {
    animationEnabled: true,
    theme: "light2",
    title:{
        text: ""
    },
    axisX:{
    },
    axisY: {
        title: "Tiền (VNĐ)",
        suffix: " Triệu",
        minimum: 30
    },
    toolTip:{
        shared:true
    },  
    legend:{
        cursor:"pointer",
        verticalAlign: "bottom",
        horizontalAlign: "left",
        dockInsidePlotArea: true,
        itemclick: toogleDataSeries
    },
    data: [{
        type: "line",
        showInLegend: true,
        name: "Doanh Thu",
        markerType: "square",
        xValueFormatString: "DD MMM, YYYY",
        color: "#F08080",
        yValueFormatString: "#,##0K",
        dataPoints: [
            { x: 10, y: 63, label: "Tháng 1" },
            { x: 20, y: 69, label: "Tháng 2" },
            { x: 30, y: 65, label: "Tháng 3" },
            { x: 40, y: 70, label: "Tháng 4" },
            { x: 50, y: 71, label: "Tháng 5" },
            { x: 60, y: 65, label: "Tháng 6" },
            { x: 70, y: 73, label: "Tháng 7" },
            { x: 80, y: 96, label: "Tháng 8" },
            { x: 90, y: 84, label: "Tháng 9" },
            { x: 100, y: 85, label: "Tháng 10" },
            { x: 110, y: 86, label: "Tháng 11" },
            { x: 120, y: 94, label: "Tháng 12" },
        ]
    }]

};
$("#chartContainer").CanvasJSChart(options);
$("#chartContainer2").CanvasJSChart(options2);
function toogleDataSeries(e){
    if (typeof(e.dataSeries.visible) === "undefined" || e.dataSeries.visible) {
        e.dataSeries.visible = false;
    } else{
        e.dataSeries.visible = true;
    }
    e.chart.render();
}
/*-- end options merchant --*/