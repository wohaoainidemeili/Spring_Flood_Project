<%--
  Created by IntelliJ IDEA.
  User: Yuan
  Date: 2017/2/11
  Time: 16:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <!--<link rel="stylesheet" href="resources/jquery-ui-1.12.1.custom/jquery-ui.css">
  <script src="resources/jquery-ui-1.12.1.custom/external/jquery/jquery.js"></script>
  <script src="resources/jquery-ui-1.12.1.custom/jquery-ui.js"></script>-->
 <!-- <link href="resources/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet"/>
  <script src="resources/bootstrap-3.3.7-dist/css/bootstrap-theme.min.css"></script>
  <script src="resources/jquery-2.1.1/jquery.min.js"></script>
  <script src="resources/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
  <link rel="stylesheet" href="resources/jquery-ui-1.12.1.custom/jquery-ui.css">
  <script src="resources/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
  <script src="resources/jquery-ui-1.12.1.custom/external/jquery/jquery.js"></script>
  <script src="resources/jquery-ui-1.12.1.custom/jquery-ui.js"></script>

  <script src="resources/jquery.steps-1.1.0/modernizr-2.6.2.min.js"></script>
  <script src="resources/jquery.steps-1.1.0/jquery.steps.js"></script>
  <script src="resources/jquery-2.1.1/jquery.validate.min.js"></script>
  <script src="resources/chart-2.4.0/Chart.js"></script>-->
    <link href="${pageContext.request.contextPath}/resources/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet"/>
    <script src="${pageContext.request.contextPath}/resources/jquery-2.1.1/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/highchart-5.0.7/highcharts.js"></script>
    <title></title>
</head>
<body>
<script>
    //highcarts success
//    Highcharts.chart('container', {
//        xAxis: {
//            type: 'datetime',
//            tickInterval: 10 * 24 * 36e5,
//            labels: {
//                format: '{value: %y-%m-%d %H:%M}',
//                align: 'right',
//                rotation: -30
//            }
//        },
//
//        series: [{
//            name:'eventTest',
//            data: [{x:1420070400000,y:29.9,className:'diagnosis'}, {x:1433116800000,y:71.5,className:'prepare'}, {x:1435708800000,y:110,className:'response'}, {x:1441065600000,y:129.2,className:'recovery'}]}],
//        tooltip: {
//            shared: true,
//            useHTML: true,
//            headerFormat: '<small>{point.key}</small><table>',
//            pointFormat: '<tr><td style="color: {series.color}">{series.name}: </td>' +
//            '<td style="text-align: right"><b>{point.y}</b></td><td style="text-align: right"><b>{point.className}</b></td></tr>',
//            footerFormat: '</table>',
//            valueDecimals: 2
//        },
//    });
  $(function(){
      var title={text:'洪涝事件探测'};
      var series;
      $.ajax({
          url:"/sad",
          type:"get",
          async:false,
          success:function(data){

              series=eval("("+data+")");
          }
      })
      var colors = Highcharts.getOptions().colors;

      var subtitle={text:'text'};
      var xAxis={type:'datetime',
          tickInterval: 10*24 * 36e5,
          labels: {
          format: '{value: %Y-%m-%d %H:%M:%S }',
          align: 'right',
          rotation: -30
      }
      };
      var series=[{
          type: 'areaspline',
         // data:sensorInfo,
           data: [29.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6, 148.5, 216.4, 194.1, 95.6, 54.4],
          pointStart: Date.UTC(2015, 0),
          pointIntervalUnit: 'month',
          zoneAxis: 'x',
          zones: [{
              value: Date.UTC(2015, 5),
              color: colors[2]
          }, {
              value: Date.UTC(2015, 6),
              color: colors[5]
          }, {
              value: Date.UTC(2015, 8),
              color: colors[2]
          }]
      }];

      var json={};
      json.title=title;
      json.subtitle=subtitle;
      json.xAxis=xAxis;
      json.series=series;
      $("#container").highcharts(json);
    //Get the context of the canvas element we want to select
//      $.getJSON("sad",function(data){
//          for(var i=0;i<data.length;i++){
//              var j=0;
//          }
//      })
//      var chart= Highcharts.chart('container', {
//          title: {
//              text: 'Zones on X axis'
//          },
//          subtitle: {
//              text: 'Colors signify periods of increase and decrease'
//          },
//          xAxis: {
//              type: 'datetime'
//          },
//          series: [{
//              type: 'areaspline',
//              data: [29.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6, 148.5, 216.4, 194.1, 95.6, 54.4],
//              pointStart: Date.UTC(2015, 0),
//              pointIntervalUnit: 'month',
//              zoneAxis: 'x',
//              zones: [{
//                  value: Date.UTC(2015, 5),
//                  color: colors[2]
//              }, {
//                  value: Date.UTC(2015, 6),
//                  color: colors[5]
//              }, {
//                  value: Date.UTC(2015, 8),
//                  color: colors[2]
//              }, {
//                  color: colors[5]
//              }]
//          }]
//      });
     // chart.series[0].setData(dataInfo);

//    var lineChartData = {
//      labels : ["January","February","March","April","May","June","July"],
//      datasets : [
//        {
//          fillColor : "rgba(220,220,220,0.5)",
//          strokeColor : "rgba(220,220,220,1)",
//          pointColor : "rgba(220,220,220,1)",
//          pointStrokeColor : "#fff",
//          data : [65,59,90,81,56,55,40]
//        },
//        {
//          fillColor : "rgba(151,187,205,0.5)",
//          strokeColor : "rgba(151,187,205,1)",
//          pointColor : "rgba(151,187,205,1)",
//          pointStrokeColor : "#fff",
//          data : [28,48,40,19,96,27,100]
//        }
//      ]
//
//    }
//
//    var myLine = new Chart(document.getElementById("myChart").getContext("2d")).Line(lineChartData);
  });
</script>

<button id="spinner">sadasd</button>>
<div id="container" style="min-width:800px;height:400px"></div>
<fieldset>
  <h2>12</h2>
</fieldset>
</body>
</html>
